package com.limesoft.personapp.controller;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.limesoft.personapp.model.Filter;
import com.limesoft.personapp.model.Operator;
import com.limesoft.personapp.model.Person;
import com.limesoft.personapp.model.SortOrder;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class PersonController {
    public List<Person> createpersonList(String filePath) throws IOException {
        Gson gson = new Gson();
        List<Person> personList = new ArrayList<>();
        JsonReader reader = new JsonReader(new FileReader(filePath));

        reader.beginArray();

        while (reader.hasNext()) {
            JsonElement element = JsonParser.parseReader(reader);
            // Check if JSON schema contains any missing or invalid attributes (skip these objects)
            String invalidAttribute = findMissingOrInvalidAttribute(element);
            if (invalidAttribute.isEmpty()) {
                Person person = gson.fromJson(element, Person.class);
                personList.add(person);
            } else {
                System.out.println("Warn: " + invalidAttribute + " invalid in object, skipping object");
            }
        }

        reader.endArray();

        return personList;
    }

    public void interactivelyFilterAndSortPersons(List<Person> personList) {
        Scanner scanner = new Scanner(System.in);

        Filter filter;
        Operator operator;
        String value;
        SortOrder sortOrder;

        System.out.println("Enter the field to filter by (firstName, lastName, age, gender):");
        while (true) {
            // Try to create filter from user input, if invalid, prompt again
            try {
                filter = Filter.fromString(scanner.nextLine());
            } catch (IllegalStateException e) {
                System.out.println("Field needs to be one of: firstName, lastName, age, gender:");
                continue;
            }
            break;
        }

        System.out.println("Enter the comparison operator (=, >=, <=, >, <) for age, = for others:");
        while (true) {
            try {
                // Try to create operator from user input, if invalid, prompt again
                operator = Operator.fromString(scanner.nextLine());
            } catch (IllegalStateException e) {
                System.out.println("Comparison operator needs to be (=, >=, <=, >, <) for age, = for others:");
                continue;
            }
            // Check if operator is valid for specified filter
            if (!Operator.validOperator(operator, filter)) {
                System.out.println("Comparison operator needs to be (=, >=, <=, >, <) for age, = for others:");
                continue;
            }
            break;
        }

        System.out.println("Enter the value to compare against:");
        while (true) {
            value = scanner.nextLine();
            // If we are filtering on age, we need an integer input, prompt until valid
            if (filter == Filter.AGE) {
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    System.out.println("Value needs to be an integer when filtering for age: ");
                    continue;
                }
            }
            break;
        }

        System.out.println("Enter sort order (asc, desc):");
        while (true) {
            //Try to create SortOrder from user input, if invalid, prompt again
            try {
                sortOrder = SortOrder.fromString(scanner.nextLine());
            } catch (IllegalStateException e) {
                System.out.println("Sort order needs to be one of (asc, desc):");
                continue;
            }
            break;
        }

        // Filter and sort based on user input, print each entry that matches in our list of Person's
        filterAndSort(personList, filter, operator, value, sortOrder).forEach(System.out::println);

    }

    private List<Person> filterAndSort(List<Person> personList, Filter filter, Operator operator, String value, SortOrder sortOrder) {
        return personList.stream()
                .filter(person -> matchesCriteria(person, filter, operator, value))
                .sorted(getComparator(filter, sortOrder))
                .collect(Collectors.toList());
    }

    private boolean matchesCriteria(Person person, Filter filter, Operator operator, String value) {
        try {
            // Get field we need to compare from Person, by comparing to our filter value
            Field field = Person.class.getDeclaredField(filter.toValue());
            field.setAccessible(true);

            // Do comparison based on filter and operator
            switch (filter) {
                case AGE:
                    int personAge = field.getInt(person);
                    int intValue = Integer.parseInt(value);
                    return switch (operator) {
                        case EQUALS -> personAge == intValue;
                        case GREATER_EQUALS -> personAge >= intValue;
                        case LESSER_EQUALS -> personAge <= intValue;
                        case GREATER -> personAge > intValue;
                        case LESSER -> personAge < intValue;
                    };
                case FIRST_NAME, LAST_NAME, GENDER:
                    String personValue = (String) field.get(person);
                    return personValue.equals(value);
                default:
                    return false;
            }
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            // Error accessing the field or parsing value
            return false;
        }
    }

    private static Comparator<Person> getComparator(Filter filter, SortOrder sortOrder) {

        // Sort based on given filter
        Comparator<Person> comparator = switch (filter) {
            case FIRST_NAME -> Comparator.comparing(Person::getFirstName);
            case LAST_NAME -> Comparator.comparing(Person::getLastName);
            case AGE -> Comparator.comparingInt(Person::getAge);
            case GENDER -> Comparator.comparing(Person::getGender);
        };

        // If the sort order is descending, reverse the comparator
        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private String findMissingOrInvalidAttribute(JsonElement element) {
        JsonObject obj = element.getAsJsonObject();

        // Check that JsonObject contains correct keys and has correct format
        if (!obj.has("firstName") || !isJsonString(obj.get("firstName"))) {
            return "firstName";
        }
        if (!obj.has("lastName") || !isJsonString(obj.get("lastName"))) {
            return "lastName";
        }
        if (!obj.has("age") || !isJsonInteger(obj.get("age"))) {
            return "age";
        }
        if (!obj.has("gender") || !isJsonString(obj.get("gender"))) {
            return "gender";
        }
        return "";
    }

    // Helper function to check if a JsonElement is a String
    private boolean isJsonString(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }

    // Helper function to check if a JsonElement is an int
    private boolean isJsonInteger(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            try {
                primitive.getAsInt();
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }


}
