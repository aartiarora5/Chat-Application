package com.example.chatapplication;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.chatapplication.Main.getClientPort;
import static com.example.chatapplication.Main.getServerPort;

public class ClientController {

    @FXML
    private TextField messageField;

    @FXML
    private VBox messageContainer;

    @FXML
    private ScrollPane scrollPane;

    private int port;

    // Setter for the port
    public void setPort(int port) {
        this.port = port;
        startClient();
    }

    // Initialize method
    public void initialize() {
        // Bind scrollPane to messageContainer for automatic scrolling
        scrollPane.vvalueProperty().bind(messageContainer.heightProperty());
        messageContainer.setPadding(new Insets(0, 17, 0, 0)); // Set the desired left padding value
    }

    // Start the client thread
    private void startClient() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(getClientPort());

                while (true) {
                    Socket socket = serverSocket.accept();
                    DataInputStream din = new DataInputStream(socket.getInputStream());
                    String message = din.readUTF();
                    socket.close();

                    messageContainer.setAlignment(Pos.BOTTOM_CENTER);

                    Platform.runLater(() -> {
                        // Create a new VBox to hold the message
                        VBox newVBox = new VBox();
                        newVBox.setPrefHeight(10);
                        newVBox.setPrefWidth(200);

                        VBox timeStamp = new VBox();
                        timeStamp.setPrefHeight(10);
                        timeStamp.setPrefWidth(200);

                        // Create a label to display the received message
                        Label receivedMessage = new Label("Server: " + message);

                        receivedMessage.setStyle(
                                "-fx-background-color: #C5E3BF; " +
                                        "-fx-padding: 5px; " +
                                        "-fx-background-insets: 0 0 -1 0;" +
                                        "-fx-background-radius: 5px;"
                        );

                        // Set the alignment of the VBox (optional)
                        newVBox.setAlignment(Pos.BOTTOM_LEFT);

                        // Add the Label to the VBox
                        newVBox.getChildren().add(receivedMessage);

                        // Time
                        LocalDateTime currentTime = LocalDateTime.now();

                        // Format the current time as a string
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                        String formattedTime = currentTime.format(formatter);

                        // Create a label to display the timestamp
                        Label timeLabel = new Label(formattedTime);
                        timeLabel.setStyle("-fx-font-size: 10"); // Set the font size to 10 points

                        // Set the alignment of the VBox (optional)
                        timeStamp.setAlignment(Pos.BOTTOM_LEFT);

                        // Add the Label to the VBox
                        timeStamp.getChildren().add(timeLabel);

                        // Add the new VBox to the message container
                        messageContainer.getChildren().add(newVBox);

                        // Add the new VBox to the message container
                        messageContainer.getChildren().add(timeStamp);

                        messageContainer.setPadding(new Insets(10, 17, 10, 10)); // Set the desired left padding value
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Send a message
    @FXML
    private void sendMessage() {
        String message = messageField.getText();
        messageField.clear();

        try {
            Socket socket = new Socket("localhost", getServerPort());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(message);
            dout.flush();
            dout.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the alignment of the message container
        messageContainer.setAlignment(Pos.BOTTOM_CENTER);

        // Create a new VBox to hold the sent message
        VBox newVBox = new VBox();

        // Set the height and width of the new VBox
        newVBox.setPrefHeight(10);
        newVBox.setPrefWidth(200);

        // Set the height and width of the new VBox
        newVBox.setPrefHeight(10);
        newVBox.setPrefWidth(200);

        VBox timeStamp = new VBox();

        // Set the height and width of the new VBox
        timeStamp.setPrefHeight(10);
        timeStamp.setPrefWidth(200);

        // Create a new Label to hold the sent message
        Label sentMessage = new Label("You: " + message);

        // Apply styling to the sent message Label
        sentMessage.setStyle(
                "-fx-background-color: #1DA1F2; " +
                        "-fx-padding: 5px; " +
                        "-fx-background-insets: 0 0 -1 0;" +
                        "-fx-background-radius: 5px;"
        );

        // Set the alignment of the VBox (optional)
        newVBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Add the sent message Label to the new VBox
        newVBox.getChildren().add(sentMessage);

        //Time
        LocalDateTime currentTime = LocalDateTime.now();

        // Format the current time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = currentTime.format(formatter);

        // Create a Label to display the timestamp
        Label timeLabel = new Label(formattedTime);
        timeLabel.setStyle("-fx-font-size: 10;"); // Set the font size to 10 points

        // Set the alignment of the timestamp VBox (optional)
        timeStamp.setAlignment(Pos.BOTTOM_RIGHT);

        // Add the timestamp Label to the timestamp VBox
        timeStamp.getChildren().add(timeLabel);

        // Add the new VBox holding the sent message to the message container
        messageContainer.getChildren().add(newVBox);

        // Add the timestamp VBox to the message container
        messageContainer.getChildren().add(timeStamp);
    }
}
