package org.example;


public class User {
    private int user_id;
    private String username;
    private String email;
    private String user_role;
    private String message;

    public int getUser_id(){
        return this.user_id;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }
    public String getEmail(){
        return this.email;
    }

    public void setEmail(String Email){
        this.email = Email;

    }
    public String getUsername(){

        return this.username;
    }

    public void setUsername(String Username){

        this.username = Username;
    }

    public String getUserRole(){

        return this.user_role;
    }

    public void setUserRole(String user_role){

        this.user_role = user_role;
    }


}
