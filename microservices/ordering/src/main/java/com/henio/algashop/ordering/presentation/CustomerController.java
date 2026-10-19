package com.henio.algashop.ordering.presentation;

import com.henio.algashop.ordering.application.customer.management.CustomerInput;
import com.henio.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.henio.algashop.ordering.application.customer.query.CustomerFilter;
import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.application.customer.query.CustomerQueryService;
import com.henio.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManagementApplicationService customerManagementApplicationService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOutput create(@RequestBody @Valid CustomerInput input, HttpServletResponse httpServletResponse) {
        CustomerId customerId = customerManagementApplicationService.create(input);

        UriComponentsBuilder uriComponentsBuilder = MvcUriComponentsBuilder.fromMethodCall(
                MvcUriComponentsBuilder.on(CustomerController.class).findById(customerId.toString())
        );

        httpServletResponse.addHeader("Location", uriComponentsBuilder.toUriString());

        return customerQueryService.findById(customerId.toString());
    }

    @GetMapping
    public PageModel<CustomerSummaryOutput> findAll(CustomerFilter filter) {
        return PageModel.of(customerQueryService.filter(filter));
    }

    @GetMapping("/{customerId}")
    public CustomerOutput findById(@PathVariable String customerId) {
        return customerQueryService.findById(customerId);
    }
}
