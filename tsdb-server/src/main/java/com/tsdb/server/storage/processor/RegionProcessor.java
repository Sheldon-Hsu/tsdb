package com.tsdb.server.storage.processor;

import com.tsdb.server.plan.physics.InsertRowPlan;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RegionProcessor {
    private TreeMap<Long,Processor> tsFileProcessorMap = new TreeMap<>();
    private Map<Long,Long> lastUpdate = new HashMap<>();

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private Map<Integer, InsertRowPlan> lastRowCache ;



}
