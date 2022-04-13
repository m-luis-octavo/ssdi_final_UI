package com.example.ssdi_final;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class clientController {

    @FXML Button sendButton, exitButton;
    @FXML TextField messageText, username;
    @FXML Label userNameLabel;
    @FXML TextArea clientChatBox;

    String userId = "";
    public void setUser(String userId){
        this.userId = userId;
    }

    private PrintWriter outputToServer;

    public void handle(ActionEvent actionEvent) {
        System.out.println("button clicked :D");
        // outputToServer.println("From Client: " + messageText.getText());
    }

    Thread listenForMessages = new Thread() {
        public void run() {
            BufferedReader inStream = null;

            try {
                Socket server_message_sock = new Socket("localhost", 8000);
                inStream = new BufferedReader(new InputStreamReader(server_message_sock.getInputStream()));

                String message;
                while ((message = inStream.readLine()) != null) {
                    System.out.println("Sent from server: " + message);
                    clientChatBox.setText( clientChatBox.getText() + "\n"+ message);
                    //clientChatBox.setText( clientChatBox.getText() + "\n"+ message);
                    // messageBox.setText( messageBox.getText() + "\n"+ message);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };



    public void initialize(){

        listenForMessages.start();

        // name is null if not in this
        Platform.runLater(() -> {

            userNameLabel.setText(userId);
            System.out.println("Client Controller Started");
            System.out.println("name from login: " + userId);

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
    }}



