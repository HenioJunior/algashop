package com.henio.algashop.ordering.infrastructure.fake;

import com.henio.algashop.ordering.domain.model.service.ShippingCostService;
import com.henio.algashop.ordering.domain.model.valueobject.Money;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ShippingCostServiceImpl implements ShippingCostService {

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        return new CalculationResult(new Money("20"), LocalDate.now().plusDays(5));
    }
}
