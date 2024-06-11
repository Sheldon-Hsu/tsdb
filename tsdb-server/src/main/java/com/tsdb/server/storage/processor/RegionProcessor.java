package com.tsdb.server.storage.processor;

import com.tsdb.server.exception.WriteProcessException;
import com.tsdb.server.exception.query.OutOfTTLException;
import com.tsdb.server.plan.physics.InsertRowPlan;
import com.tsdb.server.storage.StorageEngine;
import com.tsdb.server.storage.TsFileManager;
import com.tsdb.tsfile.exception.write.DiskSpaceInsufficientException;
import com.tsdb.tsfile.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RegionProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RegionProcessor.class);
    private TreeMap<Long, TsFileProcessor> tsFileProcessorMap = new TreeMap<>();
    private Map<Long, Long> lastUpdate = new ConcurrentHashMap<>();
    private TsFileManager tsFileManager = TsFileManager.getInstance();

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    /**
     * cache last data each tags, for last query
     */
    private Map<Integer, InsertRowPlan> lastRowCache;

    public RegionProcessor() {

        lastRowCache = new ConcurrentHashMap<>();
    }

    /**
     * data ttl,check whether the data time is within the TTL time range.
     * Invalid data is not written
     */
    private long dataTTL = Long.MAX_VALUE;

    public void insert(InsertRowPlan insertRowPlan) throws WriteProcessException, IOException, DiskSpaceInsufficientException {
        if (!isAlive(insertRowPlan.getTime())) {
            throw new OutOfTTLException(insertRowPlan.getTime(), (TimeUtils.currentTime() - dataTTL));
        }
        try {
            writeLock();
            long timeRangId = StorageEngine.getTimePartition(insertRowPlan.getTime());
            TsFileProcessor tsFileProcessor = getOrCreateTsFileProcessor(timeRangId);
            tsFileProcessor.insert(insertRowPlan);
            lastRowCache.put(insertRowPlan.getId(), insertRowPlan);
        } finally {
            writeUnLock();
        }
    }

    private TsFileProcessor getOrCreateTsFileProcessor(long timeRangeId)
            throws IOException, DiskSpaceInsufficientException {

        TsFileProcessor res = tsFileProcessorMap.get(timeRangeId);
        if (null == res) {
            // build new processor, memory control module will control the number
            logger.info("create new tsfile process");
            res = new TsFileProcessor();
            tsFileProcessorMap.put(timeRangeId, res);
            tsFileManager.add(timeRangeId, res.getTsFileResource());
        }

        return res;
    }


    private void writeLock() {
        rwLock.writeLock().lock();
        logger.info("region process write lock.");
    }

    private void writeUnLock() {
        rwLock.writeLock().unlock();
        logger.info("region process write unlock.");
    }

    /**
     * @return whether the given time falls in ttl
     */
    private boolean isAlive(long time) {
        return dataTTL == Long.MAX_VALUE || (TimeUtils.currentTime() - time) <= dataTTL;
    }
}
