package com.example.ssdi_final.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

// import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.scene.control.TextArea;

public class serverController{

    String textString = "";

    @FXML
    TextArea messageBox;

    public void handle(ActionEvent actionEvent) {
        System.exit(0);
    }

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
        waitForClientThread.start();
    }
}
