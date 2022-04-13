package com.example.ssdi_final;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class clientController {

    @FXML Button sendButton, exitButton;
    @FXML TextField messageText, username;
    @FXML Label userNameLabel;

    String userId = "";
    public void setUser(String userId){
        this.userId = userId;
    }

    private PrintWriter outputToServer;

    public void handle(ActionEvent actionEvent) {
        System.out.println("button clicked :D");
        // outputToServer.println("From Client: " + messageText.getText());
    }

    public void initialize(){

        // name is null if not in this
        Platform.runLater(() -> {
            userNameLabel.setText(userId);
            System.out.println("Client Controller Started");
            System.out.println("name from login: " + this.userId);

            try {
                Socket socket = new Socket("localhost", 8000);
                // wrap raw output stream in print writer to send text to server
                outputToServer = new PrintWriter(socket.getOutputStream(), true);
                // buffered reader is revers of print writer
            } catch (IOException error) {
                System.out.println("CLIENT ISSUE");
                System.out.println(error);
            }

            // Exit
            sendButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    System.out.println("send clicked");
                    outputToServer.println(userId + ": " + messageText.getText());
                }
            });

        });
    }

}

