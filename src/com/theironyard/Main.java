package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static final int PAGE = 20;
    static final String Persons = "people.txt";
    static public ArrayList<Person> people = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        readFile();

        Spark.get(
                "/",
                (request, response) -> {
                    int offset = 0;
                    String offsetST = request.queryParams("offset");
                    if (offsetST != null){
                        offset = Integer.valueOf(offsetST);
                    }
                    ArrayList offsetList = new ArrayList<> (people.subList(offset, offset+PAGE));
                    HashMap m = new HashMap();
                    m.put("offsetP", offset - PAGE);
                    m.put("offsetN", offset + PAGE);
                    m.put("showP", offset > 0);
                    m.put("showN", offset + PAGE < people.size());
                    m.put("people",offsetList);
                    return new ModelAndView(m,"home.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                (request, response) -> {
                    int id = Integer.valueOf(request.queryParams("id"));
                    Person p = people.get(id -1);
                    return new ModelAndView(p,"person.html");
                },
                new MustacheTemplateEngine()
        );
    }
    public static ArrayList<Person> readFile() throws FileNotFoundException {
        File f = new File("people.txt");
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(",");
            Person person = new Person(Integer.valueOf(columns[0]), columns[1], columns[2], columns[3], columns[4], columns[5]);
            people.add(person);
        }
        return people;
    }
}