package com.example.ssdi_final;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ClientLoginController extends Application {
    @FXML
    TextField username;
    @FXML
    Button logInButton;



    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/main/java/com/example/ssdi_final/clientLogin.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Client Login");
        primaryStage.setScene(new Scene(root, 700, 475));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void initialize(){

        logInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // get a handle to the stage

                String inputted_username = (username.getText());
                System.out.println(inputted_username);
                Stage stage = (Stage) logInButton.getScene().getWindow();

                stage.close();

                try {

                    URL url = new File("src/main/java/com/example/ssdi_final/client.fxml").toURI().toURL();
                    FXMLLoader fxmlLoader = new FXMLLoader(url);
                    Parent root1 = (Parent) fxmlLoader.load();

                    // sending the username that was inputted to the clientController
                    clientController controller = fxmlLoader.<clientController>getController();
                    controller.setUser(inputted_username);


                    stage = new Stage();
                    stage.setTitle("Chat Server");
                    stage.setScene(new Scene(root1));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
