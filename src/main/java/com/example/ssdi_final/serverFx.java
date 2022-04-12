package com.example.ssdi_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class serverFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("server.fxml"));

        URL url = new File("src/main/java/com/example/ssdi_final/server.fxml").toURI().toURL();
        System.out.println(getClass().getResource("server.fxml")); // <- returns null, so we do ^
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        System.out.println("sever up");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
