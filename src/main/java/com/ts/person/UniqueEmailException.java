package com.ts.person;

public class UniqueEmailException extends RuntimeException {
    private static final String errorMessage = "The email address %s is already registered to a user, " +
            "please use a different email address";

    public UniqueEmailException(String email) {
        super(String.format(errorMessage, email));
    }
}
