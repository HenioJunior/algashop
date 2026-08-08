package com.henio.algashop.ordering.domain.model.validation;

import java.util.regex.Pattern;

public final class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private EmailValidator() {
        throw new UnsupportedOperationException(
                "Esta classe não pode ser instanciada"
        );
    }

    public static boolean isValid(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return EMAIL_PATTERN
                .matcher(email.trim())
                .matches();
    }
}
