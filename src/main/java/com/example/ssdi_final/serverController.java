package com.example.ssdi_final;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

// import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class serverController{

    String textString = "";
    List<clientHandler> clients = new ArrayList<>();


    @FXML
    TextArea messageBox;
    @FXML
    Button Savelog;

    public void handle(ActionEvent actionEvent) {
        System.exit(0);
    }

    /*public void savelogs(ActionEvent actionEvent) {
        System.out.println( "Save Log clicked");
        String filename = "server_log_" + LocalDate.now(ZoneId.of("America/Toronto")).toString() + ".txt";
        try {
            PrintWriter outputStream = new PrintWriter(filename);
            outputStream.println(messageBox.getText());
            System.out.println(messageBox.getText());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public class clientHandler implements Runnable {
        private final Socket clientSock;
        private String textString;

        clientHandler(Socket socket){
            this.clientSock = socket;
        }

        public void run() {
            BufferedReader inStream = null;
            try {
                inStream = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));

                String message;
                while ((message = inStream.readLine()) != null){
                    System.out.println( "Sent from client: " + message);
                    messageBox.setText( messageBox.getText() + "\n"+ message);

                    // send message to all connected clients
                    for (int i = 0; i < clients.size(); i++){
                        PrintWriter outputToClient = new PrintWriter(clients.get(i).clientSock.getOutputStream(), true);
                        outputToClient.println(message);
                    }

                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            finally {
                try {
                    inStream.close();
                    clientSock.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


    // waiting for clients joining needs to be in own thread otherwise the thread gets
    // blocked and the java fx server thing doesnt appear. b/c while loop goes forever
    Thread waitForClientThread = new Thread(){
        public void run(){
            ServerSocket serve = null;
            try {
                serve = new ServerSocket(8000); //0 -> lets your OS select a port; port > 1024
                serve.setReuseAddress(true);
                System.out.println("Starting server...");
                System.out.println("Waiting for client connection...");


                while (true) {
                    Socket sock = serve.accept();
                    System.out.println("Client is connected " + sock.getInetAddress().getHostAddress()); //this will display the host address of client
                    clientHandler client = new clientHandler(sock);

                    // add the client to the list of clients
                    clients.add(client);


                    new Thread(client).start();
                }



            }
            catch(IOException e){
                e.printStackTrace();
            }
            try {
                serve.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    };


    public void initialize(){
            Savelog.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    System.out.println("Save Log clicked");
                    String filename = "server_log_" + LocalDate.now(ZoneId.of("America/Toronto")).toString() + ".txt";
                    try {
                        PrintWriter outputStream = new PrintWriter(filename);
                        outputStream.println(messageBox.getText());
                        System.out.println(messageBox.getText());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        waitForClientThread.start();



    }
}
