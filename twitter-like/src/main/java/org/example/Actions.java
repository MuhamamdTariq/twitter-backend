package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class Actions {
    DatabaseObject Object1 = new DatabaseObject();

    public List<String> getSubscribers(int user_id, int response) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        List<String> subscribers = new ArrayList<>();
        if (response == 1) {
            try {
                connection = Object1.getConnection();
                String query = "SELECT u1.username AS SUBSCRIBER_USERNAME, " +
                        "u2.username AS SUBSCRIBED_TO_USERNAME " +
                        "FROM TWITTER_SUBSCRIBERS ts " +
                        "JOIN TWITTER_USERS u1 ON ts.user_id = u1.user_id " +
                        "JOIN TWITTER_USERS u2 ON ts.subscribed_to_user_id = u2.user_id " +
                        "WHERE u1.user_id = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, user_id);
                set = statement.executeQuery();
                while (set.next()) {
                    subscribers.add(set.getString("SUBSCRIBED_TO_USERNAME"));
                }
                if (!set.next()) {
                    System.out.println("You have not subscribed to anyone!");
                }

                return subscribers;

            } catch (SQLException e) {
                throw new RuntimeException("Error : " + e.getMessage());
            } finally {
                if (set != null) {
                    set.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } else if (response == 2) {
            try {
                connection = Object1.getConnection();
                String query = "SELECT u1.username AS SUBSCRIBER_USERNAME, " +
                        "u2.username AS SUBSCRIBED_TO_USER_NAME " +
                        "FROM TWITTER_SUBSCRIBERS ts " +
                        "JOIN TWITTER_USERS u1 ON ts.user_id = u1.user_id " +
                        "JOIN TWITTER_USERS u2 ON ts.subscribed_to_user_id = u2.user_id " +
                        "WHERE u2.user_id = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, user_id);
                set = statement.executeQuery();

                while (set.next()) {
                    subscribers.add(set.getString("subscriber_username"));
                }
                if (!set.next()) {
                    System.out.println("Sorry error retrieving records or you haven't subscribed to anyone");
                }
                return subscribers;

            } catch (SQLException e) {
                throw new RuntimeException("error" + e.getMessage());
            } finally {
                if (set != null) {
                    set.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }

        }
        return  subscribers;
    }



       public void getMessages(int user_id) throws SQLException {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet set = null;
            try {
                connection = Object1.getConnection();
                String query = "SELECT " +
                        "  tu.username AS sender_username, " +
                        "  tm.user_messages AS messages, " +
                        "  su.username AS subscribed_to_username " +
                        "FROM  " +
                        "  TWITTER_MESSAGES tm " +
                        "  JOIN TWITTER_USERS tu ON tm.user_id = tu.user_id " +
                        "  JOIN TWITTER_USERS su ON tm.subscribed_to_user_id = su.user_id " +
                        "  WHERE tm.user_id = ? ";
                statement = connection.prepareStatement(query);
                statement.setInt(1, user_id);
                set = statement.executeQuery();
                while(set.next()){
                        System.out.println("You sent a message :" + set.getString("messages" ) + "  to:" + set.getString("subscribed_to_username"));

                }

            }
            catch (SQLException e){
                    throw new RuntimeException("This is the error : " + e.getMessage());
            }
            finally {
                    if (set != null){
                            set.close();
                    }
                    if (statement != null){
                            statement.close();
                    }
                    if (connection != null){
                            connection.close();
                    }
            }
        }

        public HashMap<Integer, String> subScribedTo(int user_id) throws Exception {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet set = null;
            HashMap<Integer, String> Subscriptions = new HashMap<>();

            try {
                connection = Object1.getConnection();
                String query = "SELECT ts.user_id, ts.subscribed_to_user_id, tu.username " +
                        "FROM TWITTER_SUBSCRIBERS ts " +
                        "JOIN TWITTER_USERS tu ON ts.subscribed_to_user_id = tu.user_id " +
                        "WHERE ts.user_id = ?";

                statement = connection.prepareStatement(query);
                statement.setInt(1, user_id);
                set = statement.executeQuery();
                while(set.next()){
                Subscriptions.put(set.getInt("subscribed_to_user_id"), set.getString("username"));
                }

                return Subscriptions;


            } catch (SQLException e){
                throw new Exception(e.getMessage());
            }

            finally {
                if (set != null){
                    set.close();
                }
                if (statement != null){
                    statement.close();
                }
                if (connection != null){
                    connection.close();
                }
            }


        }
        public static Integer getKeyForValue(Map<Integer, String> map, String value){
            for (Map.Entry<Integer, String> entry : map.entrySet()){
                if(entry.getValue().equals(value)){
                    System.out.println(entry.getKey());
                    return entry.getKey();

                }
            }
            return null;
        }


        public int checkSubscription(int user_id, int subscriber_id) throws SQLException {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet set = null;

            try {
                connection = Object1.getConnection();
                String query = "SELECT COUNT(*) FROM TWITTER_SUBSCRIBERS WHERE USER_ID = ? AND SUBSCRIBED_TO_USER_ID = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, user_id);
                statement.setInt(2, subscriber_id);
                set = statement.executeQuery();

                int rowCount = 0;
                if (set.next()) {
                    rowCount = set.getInt(1);

                }
                else {
                    System.out.println("Sorry no rows");
                }
                return rowCount;



            }

            catch (SQLException e){
                throw new RuntimeException("Error" + e.getMessage());
            }

            finally {
                if (set != null){
                    set.close();
                }
                if (statement != null){
                    statement.close();
                }

                if (connection != null){
                    connection.close();
                }
            }


        }
        public void subscribe(int user_id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Who do you want to subscribe from below");
        HashMap<Integer, String> creators = Object1.contentCreators();
        for (Map.Entry<Integer, String> entry : creators.entrySet()){
            System.out.println(entry.getValue());

        }
        boolean flag = true;
            while (flag){
                String recipient = scanner.nextLine();
                if (creators.containsValue(recipient)){
                    try{
                        Integer subscriber_id = getKeyForValue(creators, recipient);
                        if (checkSubscription(user_id, subscriber_id) >= 1){
                            System.out.println("You have already subscribed");
                            break;

                        }

                        else {
                            connection = Object1.getConnection();
                            String check = "INSERT INTO TWITTER_SUBSCRIBERS(user_id, subscribed_to_user_id) VALUES(?, ?)";
                            statement = connection.prepareStatement(check);
                            statement.setInt(1, user_id);
                            statement.setInt(2, subscriber_id);
                            int rows = statement.executeUpdate();
                            if (rows > 0){
                                System.out.println("Successfully subscribed");
                            }

                            else {
                                System.out.println("sorry we couldn't subscribe you to : " + recipient);
                            }

                        }
                        flag = false;
                    }

                catch (SQLException e){
                    throw new RuntimeException(e.getMessage());
                }
                    finally{


                        if (statement != null){
                            statement.close();
                        }
                        if (connection != null){
                            connection.close();
                        }
                    }

            }
        }

    }

    public void sendMessage(int user_id) throws Exception {
        Connection connection = null;
        PreparedStatement checking = null;
        ResultSet set = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Who do you want to send a message to!");
        HashMap<Integer, String> creators = subScribedTo(user_id);

        for (Map.Entry<Integer, String> entry: creators.entrySet()){
            System.out.println(entry.getValue());
        }
        boolean y = true;
        while(y){
            String recipient = scanner.nextLine();
            if(creators.containsValue(recipient)){
                Integer content_id = getKeyForValue(creators, recipient);
                int subscribed_to = content_id;
                System.out.println("Please write your message");
                String message = scanner.nextLine();
                try{
                    connection = Object1.getConnection();
                    String sql = "INSERT INTO TWITTER_MESSAGES(USER_ID, USER_MESSAGES, SUBSCRIBED_TO_USER_ID) VALUES (?, ?, ?)";
                    checking = connection.prepareStatement(sql);
                    checking.setInt(1, user_id);
                    checking.setString(2, message);
                    checking.setInt(3, subscribed_to);
                    int rows = checking.executeUpdate();
                    if (rows > 0){
                        System.out.println("User has successfully been messaged");
                        y = false;
                    }
                    else {
                        System.out.println("sorry couldn't message the user");
                        y = false;
                    }

                }

                catch (SQLException e){
                    throw new RuntimeException(e.getMessage());
                }
                finally {
                    if (checking != null){
                        checking.close();
                    }

                    if (connection != null){
                        connection.close();
                    }
                }
            }
            else {
                System.out.println("Sorry");
            }
        }




    }



 }


