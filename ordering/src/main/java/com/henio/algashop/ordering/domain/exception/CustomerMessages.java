package com.henio.algashop.ordering.domain.exception;

public class CustomerMessages {

    public static final String EMAIL_IS_INVALID =
            "Email is invalid";

    public static final String EMAIL_IS_REQUIRED =
            "Email is required";

    public static final String FULL_NAME_IS_REQUIRED =
            "Full name is required";

    public static final String FULL_NAME_CANNOT_BE_BLANK =
            "Full name cannot be blank";

    public static final String BIRTH_DATE_MUST_BE_IN_PAST =
            "Birth date must be in the past";

    private CustomerMessages() {
    }

}
