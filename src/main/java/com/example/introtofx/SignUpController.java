package com.example.introtofx;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    //Initializable is an interface used for controller initialization
    //Method to override is initialize()
    //initialize() is called after all @FXML annotated members have been injected.
    //In simple words, initialize() method inside the Initializable interface is what allows u to work with the FXML components

    //@FXML is how you call the fxml components created
    //For every component u are initializing, you have to type @FXML
    //No, you cannot put them in the same line like we did in swing so eg private Button btn_signup,btn_login will bring an error when compiling
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_signup;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private RadioButton rb_wittcode;
    @FXML
    private RadioButton rb_sbelse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //We need to group the radio buttons to ensure that someone can only select one at a time:
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_wittcode.setToggleGroup(toggleGroup);
        rb_sbelse.setToggleGroup(toggleGroup);

        //Setting the default radio button meaning the button that will be selected even b4 the user does their selection:
        rb_wittcode.setSelected(true);

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Getting the fav Channel selected through the radio button
                String toggleName = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    //In the above if condition we are checking if the textfields are have been filled.
                    //We use trim() which is a method that removes spaces to prevent an occurrence where a user has just entered
                    //spaces cz isEmpty() won't know if the spaces are not actual words so might mark the username or password as set.
                    //Therefore note that trim() comes before isEmpty()
                    DBUtils.signUpUser(actionEvent, tf_username.getText(), tf_password.getText(), toggleName);
                }else{
                    System.out.println("Please fill in all the information required");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all the information to sign in!");
                    alert.show();
                }
            }
        });

        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent,"sign-up.fxml","Log In", null,null);
            }
        });
    }
}
