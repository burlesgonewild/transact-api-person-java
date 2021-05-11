package com.ts.person;

import java.util.List;

public class ErrorResponse {
    private List<ErrorDetails> errors;

    public List<ErrorDetails> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDetails> errors) {
        this.errors = errors;
    }

    public static class ErrorDetails {
        private String fieldName;
        private String message;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
