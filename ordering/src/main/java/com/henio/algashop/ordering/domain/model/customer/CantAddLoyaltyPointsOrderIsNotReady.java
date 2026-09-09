package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_ORDER_IS_NOT_READY_YET;

public class CantAddLoyaltyPointsOrderIsNotReady extends DomainException {

    public CantAddLoyaltyPointsOrderIsNotReady() {
        super(ERROR_ORDER_IS_NOT_READY_YET);
    }
}
