# JavaFX Tic-Tac-Toe Game

### Contributors
- **Alex Parsons** 
- **Dan Hallanger** 

### Course Information
- **Course**: CIS 296: Java Programming
- **Professor**: John Baugh
- **Date**: December 10, 2023

---

## Project Overview

This project is a JavaFX-based multiplayer Tic-Tac-Toe game that allows two clients to connect and play against each other. The game is structured around a server-client architecture where moves are transmitted between players through a server.

### Files Included
- **Proj4_Client.java**: Standard JavaFX launcher for the GUI and loads the `Proj4_ClientUI.fxml` for the game interface.
- **Proj4_ClientUI.fxml**: FXML document used to design the game interface.
- **Proj4_ClientUIController.java**: Controls the user interface logic, handles move submissions, and receives and processes messages from the server.
- **server.java**: Server-side logic responsible for managing client connections, processing moves, and handling game state updates.

---

## How It Works

### Client-Side Logic

#### Proj4_ClientUIController.java

- **`sendMoveHandler`**: Detects which button (representing a move) is clicked by the user. It sends the button ID and player symbol (`X` or `O`) to the server.
- **`receiveMessages`**: Listens for messages from the server. These messages can indicate game status updates (such as move results, winner, or draw).
- **`processServerMessage`**: Processes incoming messages from the server. It checks if the message indicates a winner or a draw. If so, it updates the game status label accordingly. Otherwise, it updates the UI buttons with the correct player symbol.
- **`disableAllButtons`**: Disables all game buttons once a winner is declared to prevent further moves after the game ends.

### Server-Side Logic

#### server.java

- **Client Connection**: The server allows two clients to connect using `localhost` and a specified port number. It informs both clients when the game is ready to start.
- **Game State Management**: The server keeps track of player moves using a multidimensional array to store the game board state.
  
#### Key Methods:

- **`GameClientHandler`**: Adapted from a chat example, this class manages client connections and communicates with clients during the game.
- **`processMove`**: Parses moves sent by the clients, updates the game board, and broadcasts the updated game state to all players.
- **`checkForWinner`**: Checks the game board for a winning condition by examining rows, columns, and diagonals.
- **`broadcastDraw/Winner/Move`**: Sends messages to clients to update the game status, indicating a draw, a winner, or a player move.

---

## Game Screenshots

### Client Interfaces
1. **Clients running and connected to each other**  
![Screenshot of two clients running and connected](https://github.com/user-attachments/assets/305384b0-f180-4d2d-8d08-8999887dadb1)

2. **Client X wins the game**  
![Client X wins](https://github.com/user-attachments/assets/f141d7e8-185d-4153-82a7-ff83ac592d56)

3. **Game ends in a draw**  
![Game ends in a draw](https://github.com/user-attachments/assets/24a48780-85e8-44df-89f9-81e30b471ed3)

---

## How to Run the Project

1. **Clone the Repository**: Clone this project to your local machine using Git.
   ```bash
   git clone https://github.com/HallaDan/CIS-296-Project-4.git
   ```
2. **Set Up JavaFX**: Ensure JavaFX is properly set up in your environment. Refer to the [official JavaFX setup guide](https://openjfx.io/openjfx-docs/) if needed.
3. **Run the Server**: Start the `server.java` file. It will wait for two clients to connect.
4. **Run the Clients**: Launch two instances of `Proj4_Client.java`. These clients will connect to the server and begin the game.
5. **Play**: Players take turns clicking buttons in the GUI to place their `X` or `O` and win by aligning three symbols in a row, column, or diagonal.

---

## Key Features

- **Multiplayer Game**: Two clients can connect to the same server and play against each other.
- **Real-Time Updates**: Moves are broadcast to both clients immediately, ensuring game synchronization.
- **Game Status Tracking**: The server keeps track of the game board and checks for winners or a draw.
- **Simple and Interactive GUI**: The game uses JavaFX for a user-friendly, interactive interface.

---

## Team Contributions

- **Alex Parsons**: Developed the game logic, addressed FXML issues, and contributed to server-client messaging.
- **Dan Hallanger**: Worked on tie game logic, initial setup of project files, and server-client implementation.
