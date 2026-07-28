package com.henio.algashop.ordering.domain.exception;

public class CustomerMessages {

    public static final String EMAIL_IS_INVALID = "Email is invalid";

    public static final String EMAIL_IS_REQUIRED = "Email is required";

    public static final String EMAIL_CANNOT_BE_BLANK = "Email name cannot be blank";

    public static final String FULL_NAME_IS_REQUIRED = "Full name is required";

    public static final String FULL_NAME_CANNOT_BE_BLANK = "Full name cannot be blank";

    public static final String BIRTH_DATE_MUST_BE_IN_PAST = "Birth date must be in the past";

    public static final String PHONE_IS_REQUIRED = "Phone is required";

    public static final String PHONE_CANNOT_BE_BLANK = "Phone name cannot be blank";

    public static final String DOCUMENT_IS_REQUIRED = "Document is required";

    public static final String DOCUMENT_CANNOT_BE_BLANK = "Document name cannot be blank";

    public static final String ERROR_CUSTOMER_ARCHIVED = "Customer is archived it cannot be changed";

    public static final String LOYALTY_POINTS_MUST_BE_GREATER_THAN_ZERO = "Loyalty points must be greater than zero";

    private CustomerMessages() {
    }

}
