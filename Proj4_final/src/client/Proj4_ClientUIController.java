package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Proj4_ClientUIController implements Initializable {

    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML
    private Label lblStatus;

    private String playerSymbol;
    private boolean myTurn = false;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final int PORT = 5454;
    private static final String IP = "localhost";
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            socket = new Socket(IP, PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            playerSymbol = reader.readLine().split(" ")[3]; // "You are player X/O"
            lblStatus.setText("You are: " + playerSymbol);
            myTurn = playerSymbol.equals("X"); // X starts the game

            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMoveHandler(ActionEvent event) {
        if (!myTurn) return;

        Button clickedButton = (Button) event.getSource();
        if (clickedButton.getText().equals("")) {
            clickedButton.setText(playerSymbol);
            writer.println(clickedButton.getId() + ":" + playerSymbol);
            myTurn = false;
            lblStatus.setText("Waiting for opponent...");
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                final String finalMessage = message; // Make a final copy of the message
            Platform.runLater(() -> processServerMessage(finalMessage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void processServerMessage(String message) {       
        if (message.startsWith("WINNER")) {
            //update the status label with the winner's name, extracted from the message
            lblStatus.setText("Winner: " + message.split(":")[1]);
            disableAllButtons();
        } 
        // Check if the message contains a colon, indicating a game move
        else if (message.contains(":")) {
            // Split the message into parts using the colon as a delimiter
            String[] parts = message.split(":");
             // The first part is the ID of the button to update
            String buttonId = parts[0];
             // The second part is the symbol (X or O) to set on the button
            String symbol = parts[1];
            // Retrieve the button by its ID
            Button buttonToUpdate = getButtonById(buttonId);
            if (buttonToUpdate != null) {
                // Update the text of the button with the symbol
                buttonToUpdate.setText(symbol);
                 // Determine if it's the player's turn by checking if the symbol is not the player's symbol
                myTurn = !symbol.equals(playerSymbol); // It's my turn if the last move wasn't mine
            }
        }
        // if any other message, i.e "It's a draw!", set that message
        else{
            lblStatus.setText(message);
        }
    }

    private Button getButtonById(String id) {
        return switch (id) {
            case "btn00" -> btn00;
            case "btn01" -> btn01;
            case "btn02" -> btn02;
            case "btn10" -> btn10;
            case "btn11" -> btn11;
            case "btn12" -> btn12;
            case "btn20" -> btn20;
            case "btn21" -> btn21;
            case "btn22" -> btn22;
            default -> null;
        };
    }

    private void disableAllButtons() {
        btn00.setDisable(true);
        btn01.setDisable(true);
        btn02.setDisable(true);
        btn10.setDisable(true);
        btn11.setDisable(true);
        btn12.setDisable(true);
        btn20.setDisable(true);
        btn21.setDisable(true);
        btn22.setDisable(true);
    }
}