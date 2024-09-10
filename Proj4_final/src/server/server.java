package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class server {
    private static final int PORT = 5454;
    private static final Set<GameClientHandler> clients = new HashSet<>();
    private static final String[][] gameBoard = new String[3][3];
    private static final String[] playerSymbols = {"X", "O"};
    private static int currentPlayerIndex = 0;
    private static int movesMade = 0; // keep track of moves to check for draw

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            initializeGameBoard();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                GameClientHandler clientHandler = new GameClientHandler(clientSocket, playerSymbols[currentPlayerIndex]);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                
                // notify clients the game is ready
                if(clients.size() == 2){
                    for (GameClientHandler client : clients) {
                        client.writer.println("Game is ready!");
                    }
                }

                currentPlayerIndex = (currentPlayerIndex + 1) % playerSymbols.length;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static void initializeGameBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoard[i][j] = " ";
            }
        }
    }

    private static class GameClientHandler implements Runnable {
        private final Socket clientSocket;
        private final String playerSymbol;
        private PrintWriter writer;

        public GameClientHandler(Socket clientSocket, String playerSymbol) {
            this.clientSocket = clientSocket;
            this.playerSymbol = playerSymbol;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Inform client of their symbol
                writer.println("You are player " + playerSymbol);

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    processMove(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(this);
                
            }
        }

        /*
        Take message from clients, 
        parse move and update gameboard, 
        broadcast move to all clients. 
        Check for winner, 
        if there is a winner broadcast winner or brodcast draw if it is a draw 
        */
        private void processMove(String inputLine) {
            String[] parts = inputLine.split(":");
            if (parts.length == 2) {
                String buttonId = parts[0];
                String symbol = parts[1];

                updateGameBoard(buttonId, symbol);
                broadcastMove(buttonId, symbol);

                String winner = checkForWinner();
                if (!winner.equals(" ")) {
                    broadcastWinner(winner);
                }
                // check if winner returns no winner, 
                // and check that all possible moves have been made
                else if(winner.equals(" ") && movesMade == 9){
                    broadcastDraw();
                }
            }
        }

        private void updateGameBoard(String buttonId, String symbol) {
            int row = Integer.parseInt(buttonId.substring(3, 4));
            int col = Integer.parseInt(buttonId.substring(4, 5));
            gameBoard[row][col] = symbol;
            movesMade++; //increment moves made upon each board update
        }

        private String checkForWinner() {
            for (int i = 0; i < 3; i++) {
                // Check each row for a win
                if (!gameBoard[i][0].equals(" ") && gameBoard[i][0].equals(gameBoard[i][1]) && gameBoard[i][1].equals(gameBoard[i][2]))
                    return gameBoard[i][0];
                // Check each column for a win
                if (!gameBoard[0][i].equals(" ") && gameBoard[0][i].equals(gameBoard[1][i]) && gameBoard[1][i].equals(gameBoard[2][i]))
                    return gameBoard[0][i];
            }
            // Check the first diagonal (top-left to bottom-right) for a win
            if (!gameBoard[0][0].equals(" ") && gameBoard[0][0].equals(gameBoard[1][1]) && gameBoard[1][1].equals(gameBoard[2][2]))
                return gameBoard[0][0];
            // Check the second diagonal (top-right to bottom-left) for a win
            if (!gameBoard[0][2].equals(" ") && gameBoard[0][2].equals(gameBoard[1][1]) && gameBoard[1][1].equals(gameBoard[2][0]))
                return gameBoard[0][2];

            return " "; // No winner yet
        }
        
        // send 'draw' message to server for broadcast
        private void broadcastDraw(){
            for (GameClientHandler client : clients) {
                client.writer.println("It's a draw!");
            }
        }
        
        private void broadcastWinner(String winner) {
            for (GameClientHandler client : clients) {
                client.writer.println("WINNER:" + winner);
            }            
        }

        private void broadcastMove(String buttonId, String symbol) {
            for (GameClientHandler client : clients) {
                client.writer.println(buttonId + ":" + symbol);
            }
        }
    }
}