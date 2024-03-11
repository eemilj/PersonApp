package com.limesoft.personapp.model;

public enum Filter {
    FIRST_NAME, LAST_NAME, AGE, GENDER;

    public static Filter fromString(String field) {
        return switch (field.toLowerCase()) {
            case "firstname" -> FIRST_NAME;
            case "lastname" -> LAST_NAME;
            case "age" -> AGE;
            case "gender" -> GENDER;
            default -> throw new IllegalStateException("Unexpected value: " + field.toLowerCase());
        };
    }

    public String toValue() {
        return switch (this) {
            case FIRST_NAME -> "firstName";
            case LAST_NAME -> "lastName";
            case AGE -> "age";
            case GENDER -> "gender";
        };
    }
}
