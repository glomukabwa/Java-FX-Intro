package com.example.introtofx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private Label lb_welcome;
    @FXML
    private Label lb_favchan;
    @FXML
    private Button btn_logout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "sign-up.fxml","Log In", null,null);
            }
        });
    }
    //I don't know why this method is not inside the initialize method but that's how he's done it
    public void setUserInformation(String username, String favChannel){
        lb_welcome.setText("Welcome " + username + "!");
        lb_favchan.setText("Your favourite channel is " + favChannel + "!");
    }
}
