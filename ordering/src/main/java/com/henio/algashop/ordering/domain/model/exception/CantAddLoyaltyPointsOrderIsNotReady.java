package com.henio.algashop.ordering.domain.model.exception;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_ORDER_IS_NOT_READY_YET;

public class CantAddLoyaltyPointsOrderIsNotReady extends DomainException{

    public CantAddLoyaltyPointsOrderIsNotReady() {
        super(ERROR_ORDER_IS_NOT_READY_YET);
    }
}
