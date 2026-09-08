package com.henio.algashop.ordering.infrastructure.fake;

import com.henio.algashop.ordering.domain.model.service.ShippingCostService;
import com.henio.algashop.ordering.domain.model.valueobject.Money;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@ConditionalOnProperty(
        name = "algashop.integrations.shipping.provider",
        havingValue = "LOCAL",
        matchIfMissing = true
)
public class ShippingCostServiceImpl implements ShippingCostService {

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        return new CalculationResult(new Money("20"), LocalDate.now().plusDays(5));
    }
}
