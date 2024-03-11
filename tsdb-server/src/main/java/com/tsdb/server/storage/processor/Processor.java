package com.tsdb.server.storage.processor;

import com.tsdb.server.exception.WriteProcessException;
import com.tsdb.server.plan.physics.InsertRowPlan;

public interface Processor {
    public void insert(InsertRowPlan insertRowPlan) throws WriteProcessException;
}
