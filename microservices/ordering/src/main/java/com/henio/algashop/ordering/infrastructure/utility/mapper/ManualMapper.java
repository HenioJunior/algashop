package com.henio.algashop.ordering.infrastructure.utility.mapper;

import com.henio.algashop.ordering.application.commons.AddressData;
import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.application.order.query.*;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartItemOutput;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.henio.algashop.ordering.application.utility.Mapper;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.order.*;
import com.henio.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartItemPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntity;
import io.hypersistence.tsid.TSID;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

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

            case ShoppingCartPersistenceEntity shoppingCart
                    when destinationType == ShoppingCartOutput.class ->
                    toShoppingCartOutput(shoppingCart);

            case ShoppingCartItemPersistenceEntity item
                    when destinationType == ShoppingCartItemOutput.class ->
                    toShoppingCartItemOutput(item);

            case CustomerPersistenceEntity customer
                    when destinationType == CustomerMinimalOutput.class ->
                    toCustomerMinimalOutput(customer);

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

    private CustomerMinimalOutput toCustomerMinimalOutput(
            CustomerPersistenceEntity customer) {

        CustomerMinimalOutput output = new CustomerMinimalOutput();

        output.setId(customer.getId());
        output.setFirstName(customer.getFirstName());
        output.setLastName(customer.getLastName());
        output.setEmail(customer.getEmail());
        output.setDocument(customer.getDocument());
        output.setPhone(customer.getPhone());

        return output;
    }

    private OrderDetailOutput toOrderDetailOutput(
            OrderPersistenceEntity order) {

        OrderDetailOutput output = new OrderDetailOutput();

        output.setId(toTsidString(order.getId()));
        output.setTotalAmount(order.getTotalAmount());
        output.setTotalItems(order.getTotalItems());

        output.setPlacedAt(order.getPlacedAt());
        output.setPaidAt(order.getPaidAt());
        output.setCanceledAt(order.getCanceledAt());
        output.setReadyAt(order.getReadyAt());

        output.setStatus(order.getStatus());
        output.setPaymentMethod(order.getPaymentMethod());

        output.setCustomer(
                order.getCustomer() == null
                        ? null
                        : toCustomerMinimalOutput(order.getCustomer())
        );

        output.setBilling(toBillingOutput(order.getBilling()));
        output.setShipping(toShippingOutput(order.getShipping()));

        output.setItems(
                order.getItems() == null
                        ? List.of()
                        : order.getItems().stream()
                        .map(this::toOrderItemDetailOutput)
                        .toList()
        );

        return output;
    }

    private static ShippingData toShippingOutput(ShippingEmbeddable shipping) {
        if (shipping == null) {
            return null;
        }

        ShippingData output = new ShippingData();

        output.setCost(shipping.getCost());
        output.setExpectedDate(shipping.getExpectedDate());
        output.setAddress(toAddressOutput(shipping.getAddress()));
        output.setRecipient(toRecipientOutput(shipping.getRecipient()));

        return output;
    }

    private static BillingData toBillingOutput(BillingEmbeddable billing) {
        if (billing == null) {
            return null;
        }

        BillingData output = new BillingData();

        output.setFirstName(billing.getFirstName());
        output.setLastName(billing.getLastName());
        output.setDocument(billing.getDocument());
        output.setEmail(billing.getEmail());
        output.setPhone(billing.getPhone());
        output.setAddress(toAddressOutput(billing.getAddress()));

        return output;
    }

    private static AddressData toAddressOutput(AddressEmbeddable address) {
        if (address == null) {
            return null;
        }

        AddressData output = new AddressData();

        output.setStreet(address.getStreet());
        output.setNumber(address.getNumber());
        output.setComplement(address.getComplement());
        output.setNeighborhood(address.getNeighborhood());
        output.setCity(address.getCity());
        output.setState(address.getState());
        output.setZipCode(address.getZipCode());

        return output;
    }

    private static RecipientData toRecipientOutput(RecipientEmbeddable recipient) {
        if (recipient == null) {
            return null;
        }

        RecipientData output = new RecipientData();

        output.setFirstName(recipient.getFirstName());
        output.setLastName(recipient.getLastName());
        output.setDocument(recipient.getDocument());
        output.setPhone(recipient.getPhone());

        return output;
    }

    private OrderItemDetailOutput toOrderItemDetailOutput(
            OrderItemPersistenceEntity item
    ) {
        OrderItemDetailOutput output = new OrderItemDetailOutput();

        output.setId(toTsidString(item.getId()));
        output.setOrderId(toTsidString(item.getOrderId()));
        output.setProductId(item.getProductId().toString());
        output.setProductName(item.getProductName());
        output.setPrice(item.getPrice());
        output.setQuantity(item.getQuantity());
        output.setTotalAmount(item.getTotalAmount());

        return output;
    }

    private ShoppingCartOutput toShoppingCartOutput(
            ShoppingCartPersistenceEntity entity
    ) {
        ShoppingCartOutput output = new ShoppingCartOutput();
        output.setId(entity.getId());
        output.setCustomerId(entity.getCustomerId());
        output.setTotalItems(entity.getTotalItems());
        output.setTotalAmount(entity.getTotalAmount());
        output.setItems(toShoppingCartItemOutputList(entity.getItems()));
        return output;
    }

    private List<ShoppingCartItemOutput> toShoppingCartItemOutputList(Set<ShoppingCartItemPersistenceEntity> items) {
        return items.stream()
                .map(this::toShoppingCartItemOutput)
                .toList();
    }

    private ShoppingCartItemOutput toShoppingCartItemOutput(ShoppingCartItemPersistenceEntity item) {
        ShoppingCartItemOutput output = new ShoppingCartItemOutput();
        output.setId(item.getId());
        output.setProductId(item.getProductId());
        output.setName(item.getProductName());
        output.setPrice(item.getPrice());
        output.setQuantity(item.getQuantity());
        output.setTotalAmount(item.getTotalAmount());
        output.setAvailable(item.isAvailable());
        return output;
    }


    private String toTsidString(Long value) {
        return value == null
                ? null
                : new TSID(value).toString();
    }
}
