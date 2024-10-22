package org.example;

import oracle.jdbc.proxy.annotation.Pre;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DatabaseObject {
    private String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/orclpdb";
    private String username = "ADB1";
    private String password = "12340987";
    User user1 = new User();


    // Establish the connection to the database
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Successful Connection");
        } catch (SQLException e) {
            System.out.println("Failed to establish a connection: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver class not found: " + e.getMessage());
        }
        return connection;
    }

    public HashMap<Integer, String> contentCreators() throws SQLException {
        HashMap<Integer, String> creatorsList = new HashMap<>();
        Connection connection = null;
        PreparedStatement creators = null;
        ResultSet creatorSet = null;

        try {
            connection = getConnection();
            String check = "SELECT * FROM TWITTER_USERS WHERE user_role = ?";
            creators = connection.prepareStatement(check);
            creators.setString(1, "content creator");
            creatorSet = creators.executeQuery();
            if (creatorSet == null){
                System.out.println("sorry no creators found");
            }
            else {
                while(creatorSet.next()){
                    creatorsList.put(creatorSet.getInt(("user_id")), creatorSet.getString("username"));

                }
            }

            return creatorsList;
        } catch (SQLException e){
            throw new RuntimeException("Error retrieving user" + e.getMessage());
        }
        finally{
            if (creators != null){
                creators.close();
            }
            if (creatorSet != null){
                creatorSet.close();
            }

            if (connection != null){
                connection.close();
            }
        }
    }

    public void Login() throws SQLException {
        Actions action = new Actions();
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        PreparedStatement Login = null;
        ResultSet result = null;
        System.out.println("Please enter your username");
        String username = scanner.nextLine();
        try {
            connection = getConnection();
            String check = "SELECT * FROM TWITTER_USERS WHERE username = ?";
            Login = connection.prepareStatement(check);
            Login.setString(1, username);
            result = Login.executeQuery();
            if (!result.next()){
                System.out.println("User Does not Exist please try again or Register");
                Login();
            }

            else {
                String Username = result.getString("username");
                String email = result.getString("email");
                int user_id = result.getInt("user_id");
                user1.setUsername(Username);
                user1.setEmail(email);
                user1.setUserRole("content subscriber");
                user1.setUser_id(user_id);
                System.out.println("Welcome to Twitter what would you like to do Today!");
                while(user1.getUser_id() != 0){
                    System.out.println("1: subscribe to someone, 2:send a message to someone, " +
                            "3: get a list of all your messages, 4:Get a list of your subscribers" +
                            "5: get a list of who you're subscribed to, 6: Logout");
                    int response = scanner.nextInt();
                    if (response == 1){
                        action.subscribe(user_id);
                    }

                    else if (response == 2){
                        action.sendMessage(user_id);
                    }

                    else if (response == 3){
                        action.getMessages(user_id);
                    }

                    else if (response == 4){
                        System.out.println("Do you want to get a list of you're subscribers? 2 : or who" +
                                "you're subscribed to : 1");
                        int response2 = scanner.nextInt();
                        if (response2 == 1){
                            for (String value : action.getSubscribers(user_id, response2)){
                                System.out.println(value);
                            }

                        }
                        else if (response2 == 2){
                            for (String value : action.getSubscribers(user_id, response2)){
                                System.out.println(value);
                            }
                        }
                        else {
                            System.out.println("Sorry that was not a valid response");
                            break;

                        }
                    }

                    else if(response == 6){
                        user1.setUser_id(0);
                    }

                }
                System.out.println("Thanks for using Twitter have a great day!");
                System.exit(0);

            }



    } catch (SQLException e){
        throw new RuntimeException();
    } catch (Exception e) {
        throw new RuntimeException(e);
    } finally {
        if(result != null){
            result.close();
        }
        if(Login != null){
            Login.close();
        }
        if(connection != null){
            connection.close();
        }
    }
}


// Register the user
    public void Register() throws SQLException {
        Connection connection = null;
        PreparedStatement checkUserStatement = null;
        PreparedStatement insertUserStatement = null;
        ResultSet resultSet = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome Do You want to Register? : 1 or No : 2");
        int response = scanner.nextInt();
        String username = "";
        String email = "";
        if (response == 1){
            System.out.println("Please enter your username");
            username = scanner.nextLine();
            System.out.println("Please enter your email");
            email = scanner.nextLine();
        }
        else if(response == 2){
            return;
        }


        try {
            // Establish a single connection
            connection = getConnection();

            // Check if the email already exists in the USERS table
            String checkUserSql = "SELECT * FROM TWITTER_USERS WHERE email = ?";
            checkUserStatement = connection.prepareStatement(checkUserSql);
            checkUserStatement.setString(1, email);
            resultSet = checkUserStatement.executeQuery();

            // If the user does not exist, insert a new user
            if (!resultSet.next()) {
                String insertUserSql = "INSERT INTO TWITTER_USERS (username, email) VALUES (?, ?)";
                insertUserStatement = connection.prepareStatement(insertUserSql);
                insertUserStatement.setString(1, username);
                insertUserStatement.setString(2, email);


                int rowsInserted = insertUserStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("User registered successfully");
                    System.out.println("Please continue to login!");


                } else {
                    System.out.println("Error registering user or user already register please login");
                }
            } else {
                System.out.println("User with this email already exists.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error registering user: " + e.getMessage(), e);
        } finally {
            // Close resources in the reverse order of their creation
            if (resultSet != null) {
                resultSet.close();
            }
            if (checkUserStatement != null) {
                checkUserStatement.close();
            }
            if (insertUserStatement != null) {
                insertUserStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }



    public User getUser(){
        return user1;

    }
}
