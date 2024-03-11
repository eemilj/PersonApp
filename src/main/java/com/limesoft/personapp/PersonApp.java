package com.limesoft.personapp;

import com.limesoft.personapp.controller.PersonController;
import com.limesoft.personapp.model.Person;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter filename like 'java PersonApp.Main input.json'");
            System.exit(0);
        }

        PersonController personController = new PersonController();
        // Get filename from command line arg
        String filePath = args[0];
        List<Person> personList = new ArrayList<>();

        // Create List of Person's from given json file and
        // handle any exceptions
        try {
            personList = personController.createpersonList(filePath);
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " does not exist");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("JSON file " + filePath + " is not of correct format or schema");
            System.exit(0);
        }

        // Prompt user for filtering and sorting
        personController.interactivelyFilterAndSortPersons(personList);

    }
}
