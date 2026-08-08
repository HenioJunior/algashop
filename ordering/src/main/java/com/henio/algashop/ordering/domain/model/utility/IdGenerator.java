package com.henio.algashop.ordering.domain.model.utility;

import io.hypersistence.tsid.TSID;

public class IdGenerator {

    public static final TSID.Factory tsidFactory = TSID.Factory.INSTANCE;

    private IdGenerator() {
    }

    /* Setar em Produção
     * TSID_NODE
     * TSID_NODE_COUNT
     */

    public static TSID generateTSID() {
        return tsidFactory.generate();
    }
}
