package com.example.introtofx;

//This is a Utility Class
//A utility class in Java is a class that contains just static methods
//It cannot be instantiated.
//It contains a bunch of related methods
//It is also known as a helper class.
//Some common examples are StringUtils or CollectionUtils

//Here we are gonna do the signing up, logging in

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.*;


public class DBUtils{

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String favChannel){
        //For the parameters, ActionEvent is the event that happens when this method is called. Here, we'll call the setUserInformation() method to change the labels in the welcome page
        //fxmlFile is the file that is supposed to be displayed depending on what happens eg logging in or logging out
        //title is the title of whatever fxml file will be displayed at the moment so in case it's Log In, the title of that scene will be Log In {like super("title) in Java Swing}
        //username and favChannel will be the respective values retrieved from the database depending on the user that has signed in

        Parent root = null;
        //A parent node is a node that has children
        //For example, if the top most pane is the HBox Pane in the page and it contains other panes and elements such as labels, buttons etc, it is the parent node
        //Normally, we like to declare the parent node like u'll see below once we use it cz whatever u apply on the parent node affects all its children so it makes it easy to handle the fxml file

        if(username != null && favChannel != null){//This just means that if a user has entered the data required to log in
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                //Here we are loading the fxml file we want to display
                //DBUtils.class gets the class that DBUtils is in which I know doesn't make sense but u'll understand in a few
                //getResources(fxmlFile) method checks the resource folder for an fxml file that we will specify within the brackets
                //Now, the reason we have to get the class is so that we know the package that the class is in so that we can look for the resource folder in the specific package.
                //So in this case, we get DBUtils class and we find out the package it is in then now getResource has an easy time looking for the specific fxml file
                //I've seen in meta that you can do : FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml")) .
                //In the above line, getClass() will get the class that the method is currently being typed in which is nice cz just in case you change the class name, you don't have to edit it here.
                //Then for getResource(), I wanted to show that u can write the String directly inside. U don't have to pass it as a variable but in this case we need to do that so that we can call this method in different controllers. Check in LoggedIn controller to understand

                root = loader.load();
                //This is where the actual loading happens. The load method parses the fxml file and returns the parent node. You've heard of parsing before. For example parsing a string to an int will convert that string to an integer.
                //Parsing in this context means reading the fxml file to understand the structure, creating a graph of the JavaFX objects(the different nodes, respective controller and scene) and then returning the parent object of the graph which in this case is the top most pane

                //Now, this part of code is for when the user has set their username and favChannel so if they have, we assign those values to setUserInformation()
                //Plz not that this is not displaying. This is just setting the values. You'll see the displaying after the if...else...
                LoggedInController logIn = loader.getController();
                logIn.setUserInformation(username, favChannel);

            }catch(IOException e){//PLz note the type of exception here. It is the exception that can occur bcz of error of input of data
                e.printStackTrace();
            }

        }else{//This is for just in case the user hasn't entered any data and they click that blue button at the bottom. In this case, they'll just be switching between sign up and log in
            try{
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));//This is the short version of the methods above. Instead of creating an instance of FXMLLoader then using load(), u're just doing this directly
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        //Displaying the respective page:
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //Ok so here, first you need to know what a stage is
        //A stage is a window. A window is a rectangular area on your computer that displays content from a specific application.(JFrame in Swing)
        // Types of windows include a Stage which is a main application window, a PopUpWindow used for pop-up controls, Dialog window which is used to interact with a user eg alerting them or giving them information about the system
        //There can only be one stage but it can have multiple scenes. Each page is a scene for example here, the sign up page is a scene, the welcome page is another. So this method we are creating here called changeScene() is for switching between the pages
        //Now, back to the method I've written:
        //Here, we are double casting. Casting means telling the compiler to treat sth as a certain data type eg int Num = (int) ("213") will mean, treat 213 as a number. In this example, my casting is also doing parsing but even if I had written int Num = (int) (213) it would still be okay. Casting is just a way of ensuring sth is treated exactly the way its supposed to be treated
        //In this case, b4 I explain the (Stage) casting, I'll explain the methods
        //First, we get the event which we passed as a parameter of changeScene() in the beginning of this method. It is an object of ActionEvent which is found when we use setOnAction on a button. So we are telling the compiler to get the source of the action meaning find the button that has been clicked
        //Then getScene() finds the Scene that the button belongs to then getWindow() returns the window that the scene belongs to. You'll see y we need to get the window in the next line
        //Now that we have the info we need, we use (Stage) to cast so that we can specify that the window that has been returned by the getWindow() is a Stage window not a PopUp or a Dialog. After this, we can now use the instance of the stage we have created to access methods in the Stage class as u'll see in the next line
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600,400));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username, String password, String favChannel){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        //I can't make the variables below private cz this class methods are going to be accessed by other classes
        //When I try to make them static, I also get an error so idk y but I'm trying to follow what he does so lemmi just leave them as String
        String url = "jdbc:mysql://localhost:3306/fxintro";
        String user = "root";
        String pass = "L@ur3nceangelina";

        try{
            connection = DriverManager.getConnection(url, user, pass);
            psCheckUserExists = connection.prepareStatement("SELECT *  FROM users WHERE username = ?");
            psCheckUserExists.setString(1,username);
            resultSet = psCheckUserExists.executeQuery();

            if(resultSet.isBeforeFirst()){//isBeforeFirst() is a method used to check if the result set is empty. It returns false if it is empty
                System.out.println("User already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);//A Message Dialog window in fx
                alert.setContentText("You cannot use this username");
                alert.show();
            }else{
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, favChannel) VALUES (?, ?, ?)");
                psInsert.setString(1,username);
                psInsert.setString(2,password);
                psInsert.setString(3,favChannel);
                psInsert.executeUpdate();

                changeScene(event,"welcome.fxml","Welcome!",username,favChannel);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            //Here we are gonna close the db connection. This is for safety purposes since not closing it could lead to memory leakage
            //Eg. IF we had 10 users who each have a connection and 3 of them log out but we don't close the connection then those connections remain in use and no other user can use them. This is wastage

            //The order of closing is resultSet then prepared statements then connection
            if(resultSet != null){//Tbh I don't know why we check if it's not null and why if so, is when we close it. I think that it means that if there was a resulSet meaning a legit user actually logged in, and now they are logging out, we close the connection
                try{//We put the try...catch... to cater for just in case it is null cz if we don't, if its null, it'll bring an error
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null){//Ok I think I get it. This will only be null if a user doesn't exist and you can't close a connection that didn't exist so we're making sure that we only close a connection that existed
                try{
                    psCheckUserExists.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null){
                try{
                    psInsert.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logInUser(ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //I can't make the variables below private cz this class methods are going to be accessed by other classes
        //When I try to make them static, I also get an error so idk y but I'm trying to follow what he does so lemmi just leave them as String
        String url = "jdbc:mysql://localhost:3306/fxintro";
        String user = "root";
        String pass = "L@ur3nceangelina";

        try {
            connection = DriverManager.getConnection(url, user, pass);
            preparedStatement = connection.prepareStatement("SELECT password, favChannel FROM users WHERE username = ?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            }else{//Comparing the password in the database with the password provided by the user while attempting to log in
                while (resultSet.next()){//It's ok to put it in a loop since it'll only  return one row so the code inside this will only affect that
                    String retrievedPassword = resultSet.getString("password");//We're using getString cz the data of this column is a string but if it was an int we would have used getInt()
                    String retrievedChannel = resultSet.getString("favChannel");

                    if(retrievedPassword.equals(password)){
                        changeScene(event,"welcome.fxml","Welcome",username,retrievedChannel);
                    }else{
                        System.out.println("Passwords did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Password is incorrect");
                        alert.show();
                    }
                }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{//Remember u always have to close
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}



