package com.henio.algashop.ordering.infrastructure.utility.mapper;

import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.application.order.query.OrderDetailOutput;
import com.henio.algashop.ordering.application.order.query.OrderItemDetailOutput;
import com.henio.algashop.ordering.application.utility.Mapper;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.infrastructure.persistence.order.OrderItemPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntity;
import io.hypersistence.tsid.TSID;
import org.springframework.stereotype.Component;

@Component
public class ManualMapper implements Mapper {

    @Override
    public <T> T convert(Object source, Class<T> destinationType) {
        if (source == null) {
            return null;
        }

        Object result = switch (source) {
            case Customer customer
                    when destinationType == CustomerOutput.class ->
                    toCustomerOutput(customer);

            case OrderPersistenceEntity order
                    when destinationType == OrderDetailOutput.class ->
                    toOrderDetailOutput(order);

            case OrderItemPersistenceEntity item
                    when destinationType == OrderItemDetailOutput.class ->
                    toOrderItemDetailOutput(item);

            default -> throw new IllegalArgumentException(
                    "Unsupported mapping: %s -> %s"
                            .formatted(
                                    source.getClass().getName(),
                                    destinationType.getName()
                            )
            );
        };

        return destinationType.cast(result);
    }

    private CustomerOutput toCustomerOutput(Customer customer) {
        CustomerOutput output = new CustomerOutput();

        output.setFirstName(
                customer.fullName() != null
                        ? customer.fullName().firstName()
                        : null
        );

        output.setLastName(
                customer.fullName() != null
                        ? customer.fullName().lastName()
                        : null
        );

        output.setBirthDate(
                customer.birthDate() != null
                        ? customer.birthDate().value()
                        : null
        );

        // demais propriedades...

        return output;
    }

    private OrderDetailOutput toOrderDetailOutput(
            OrderPersistenceEntity order
    ) {
        OrderDetailOutput output = new OrderDetailOutput();

        output.setId(toTsidString(order.getId()));
        output.setTotalAmount(order.getTotalAmount());

        // demais propriedades...

        return output;
    }

    private OrderItemDetailOutput toOrderItemDetailOutput(
            OrderItemPersistenceEntity item
    ) {
        OrderItemDetailOutput output = new OrderItemDetailOutput();

        output.setId(toTsidString(item.getId()));
        output.setOrderId(toTsidString(item.getOrderId()));

        // demais propriedades...

        return output;
    }

    private String toTsidString(Long value) {
        return value == null
                ? null
                : new TSID(value).toString();
    }
}
