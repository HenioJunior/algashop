package com.henio.algashop.ordering.domain.model.exception;

public class CustomerMessages {

    public static final String EMAIL_IS_INVALID = "Email is invalid";

    public static final String EMAIL_IS_REQUIRED = "Email is required";

    public static final String EMAIL_CANNOT_BE_BLANK = "Email name cannot be blank";

    public static final String FULL_NAME_IS_REQUIRED = "Full name is required";

    public static final String FIRST_NAME_IS_REQUIRED = "First name is required";

    public static final String LAST_NAME_IS_REQUIRED = "Last name is required";

    public static final String FULL_NAME_CANNOT_BE_BLANK = "Full name cannot be blank";

    public static final String FIRST_NAME_AND_LAST_NAME_CANNOT_BE_BLANK
            = "First name and Last name cannot be blank";

    public static final String BIRTH_DATE_MUST_BE_IN_PAST = "Birth date must be in the past";

    public static final String BIRTH_DATE_IS_REQUIRED = "Birthdate is required";

    public static final String PHONE_NUMBER_IS_REQUIRED = "Phone number is required";

    public static final String PHONE_NUMBER_CANNOT_BE_BLANK = "Phone number cannot be blank";

    public static final String DOCUMENT_IS_REQUIRED = "Document is required";

    public static final String DOCUMENT_CANNOT_BE_BLANK = "Document name cannot be blank";

    public static final String ERROR_CUSTOMER_ARCHIVED
            = "Customer is archived it cannot be changed";

    public static final String LOYALTY_POINTS_CANNOT_BE_NEGATIVE
            = "Loyalty points cannot be negative";

    public static final String LOYALTY_POINTS_TO_ADD_MUST_BE_GREATER_THAN_ZERO
            = "Loyalty points to add must be greater than zero";

    public static final String FIELD_CANNOT_BE_BLANK = "Field cannot be blank";

    public static final String FIELD_IS_REQUIRED = "Field is required";

    public static final String ERROR_ORDER_STATUS_CANNOT_BE_CHANGED
            = "Order %s cannot be changed from %s to %s";

    public static final String ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST
            = "Order %s expected delivery date cannot be in the past";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS
            = "Order %s cannot be placed, it has no items";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_SHIPPING_INFO
            = "Order %s cannot be placed, it has no shipping info";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_BILLING_INFO
            = "Order %s cannot be placed, it has no billing info";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_PAYMENT_METHOD
            = "Order %s cannot be placed, it has no payment method";

    public static final String ERROR_ORDER_DOES_NOT_CONTAIN_ITEM
            = "Order %s does not contain item %s";

    public static final String ERROR_ORDER_NOT_FOUND
            = "Order %s cannot be found!";

    public static final String ERROR_PRODUCT_IS_OUT_OF_STOCK
            = "Product %s is out of stock";

    public static final String ERROR_ORDER_CANNOT_BE_EDITED = "Order %s with status %s cannot be edited";

    public static final String ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT
            = "Shopping Cart %s cannot be updated, incompatible product %s";

    public static final String ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM = "Shopping Cart %s does not contain item %s";

    public static final String ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT =
            "Shopping Cart %s does not contain product %s";

    public static final String ERROR_CUSTOMER_AND_ORDER_MUST_BELONG_TO_THE_SAME_CUSTOMER =
            "Customer and order must belong to the same customer";

    public static final String ERROR_ORDER_IS_NOT_READY_YET =
            "Order is not ready yet";

    public static final String ERROR_EMAIL_ALREADY_REGISTERED =
            "Email already registered";

    public static final String ERROR_SHOPPING_CART_IS_NOT_VALID =
            "Shopping cart is not valid";

    private CustomerMessages() {
    }

}
