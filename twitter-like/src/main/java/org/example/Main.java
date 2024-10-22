package org.example;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hello, World!");
        DatabaseObject databaseObject = new DatabaseObject();
        System.out.print(databaseObject);
        databaseObject.getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean visiting = true;
        while(visiting){
            System.out.println("What are you going to be doing today? 1: Login 2:Register");
            int response = scanner.nextInt();
            if (response == 1){
                databaseObject.Login();
                visiting = false;
            }
            if (response == 2){
                databaseObject.Register();
                visiting = false;
            }

            visiting = false;

        }


        }
    }
