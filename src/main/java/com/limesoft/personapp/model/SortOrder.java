package com.limesoft.personapp.model;

public enum SortOrder {
    ASC, DESC;

    public static SortOrder fromString(String order) {
        return switch (order.toLowerCase()) {
            case "asc" -> ASC;
            case "desc" -> DESC;
            default -> throw new IllegalArgumentException("Unknown sort order: " + order);
        };
    }
}
