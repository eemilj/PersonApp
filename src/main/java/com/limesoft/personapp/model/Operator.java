package com.limesoft.personapp.model;

public enum Operator {
    EQUALS, GREATER, GREATER_EQUALS, LESSER, LESSER_EQUALS;

    public static Operator fromString(String operator) {
        return switch (operator) {
            case "=" -> EQUALS;
            case ">" -> GREATER;
            case ">=" -> GREATER_EQUALS;
            case "<" -> LESSER;
            case "<=" -> LESSER_EQUALS;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }


    // Helper function to see if an operator is valid for a specific filter
    public static boolean validOperator(Operator operator, Filter filter) {
        if (filter == Filter.FIRST_NAME || filter == Filter.LAST_NAME || filter == Filter.GENDER) {
            return operator == Operator.EQUALS;
        } else {
            return filter == Filter.AGE;
        }
    }

}
