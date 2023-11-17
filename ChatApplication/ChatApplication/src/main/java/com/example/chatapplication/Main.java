// Main.java
package com.example.chatapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static int serverPort;
    private static int clientPort;

    public static int getServerPort() {
        return serverPort;
    }

    public static int getClientPort() {
        return clientPort;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Assign different ports for the server and client
        serverPort = 6001; // Port for the server
        clientPort = 6002; // Port for the client

        // Load Client.fxml
        FXMLLoader clientLoader = new FXMLLoader(getClass().getResource("Client.fxml"));
        Parent clientRoot = clientLoader.load();
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(clientRoot, 600, 400));
        primaryStage.show();

        // Pass the port to the controller
        ClientController clientController = clientLoader.getController();
        clientController.setPort(clientPort);

        // Load Server.fxml
        FXMLLoader serverLoader = new FXMLLoader(getClass().getResource("Server.fxml"));
        Parent serverRoot = serverLoader.load();
        Stage serverStage = new Stage();
        serverStage.setTitle("Server");
        serverStage.setScene(new Scene(serverRoot, 600, 400));
        serverStage.show();

        // Pass the port to the controller
        ServerController serverController = serverLoader.getController();
        serverController.setPort(serverPort);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
