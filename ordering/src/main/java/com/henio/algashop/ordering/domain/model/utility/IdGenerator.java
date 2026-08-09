package com.henio.algashop.ordering.domain.model.utility;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import io.hypersistence.tsid.TSID;

import java.util.UUID;

public class IdGenerator {

    public static final TSID.Factory tsidFactory = TSID.Factory.INSTANCE;

    private static final TimeBasedEpochRandomGenerator timeBasedEpochRandomGenerator
            = Generators.timeBasedEpochRandomGenerator();

    private IdGenerator() {
    }

    /* Setar em Produção
     * TSID_NODE
     * TSID_NODE_COUNT
     */

    public static TSID generateTSID() {
        return tsidFactory.generate();
    }

    public static UUID generateTimeBasedUUID() {
        return timeBasedEpochRandomGenerator.generate();
    }
}
