package com.henio.algashop.ordering.infrastructure.beans;

import com.henio.algashop.ordering.domain.model.utility.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "com.henio.algashop.ordering.domain.model",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = DomainService.class
        )
)
public class DomainServiceScanConfig {
}
