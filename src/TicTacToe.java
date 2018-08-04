import java.util.HashMap;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created by Sacha on 3/08/2018.
 */
public class TicTacToe {

//    1 for the player move, 2 for the agent move
    public int[] board;

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        // Create a reader to read player moves
        Scanner reader = new Scanner(System.in);

        // Play the game
        while (true) {
            // Get the player move
            System.out.println("Enter a box location: ");
            int i = reader.nextInt();

            // Make the player move
            if (game.player_move(i)) {
                break;
            }

            // Make the agent move
            if (game.make_move()) {
                break;
            }
        }

        reader.close();
    }

    /**
     * Create a new game of Tic Tac Toe to player against a rational agent with 1 turn of foresight.
     *
     */
    public TicTacToe() {
        // Create the board
        board = new int[9];

        // Display an init message
        System.out.println("Welcome to Tic Tac Toe.");
        System.out.println("To make a move enter the number corresponding to the board location:");
        System.out.println("0 1 2");
        System.out.println("3 4 5");
        System.out.println("6 7 8");
    }

    /**
     * Checks if the game is over and which player won.
     *
     * @param board The board the game is being played on.
     * @Return true if a player has won, else false.
     */
    public static boolean check_for_win(int[] board) {
        int[][] toCheck = new int[8][3];

        // Add the rows
        for (int i = 0; i < 3; i++) {
            toCheck[i][0] = i * 3 + 0;
            toCheck[i][1] = i * 3 + 1;
            toCheck[i][2] = i * 3 + 2;
        }

        // Add the columns
        for (int i = 0; i < 3; i++) {
            toCheck[3 + i][0] = i + 0;
            toCheck[3 + i][1] = i + 3;
            toCheck[3 + i][2] = i + 6;
        }

        // Add the diagonals
        toCheck[6][0] = 0;
        toCheck[6][1] = 4;
        toCheck[6][2] = 8;

        toCheck[7][0] = 2;
        toCheck[7][1] = 4;
        toCheck[7][2] = 6;

        for (int[] values : toCheck) {
            int oneCount = 0;
            int twoCount = 0;

            for (int i = 0; i < 3; i++) {
                if (board[values[i]] == 1) {
                    oneCount++;
                } else if (board[values[i]] == 2) {
                    twoCount++;
                }
            }


            if (oneCount == 3) {
                System.out.println("Player 1 wins.");

                return true;
            } else if (twoCount == 3) {
                System.out.println("Player 2 wins.");

                return true;
            }
        }

        return false;
    }

    /**
     * Computes the next move for the agent to make. This agent only looks forward to the next move made by the
     * player and no further.
     *
     * @return True on game over, False otherwise.*
     */
    public boolean make_move() {
        // Search the space using the following reward function
        // Win = 100
        // Loss = -100
        // Step = -1

        // Store  position -> cost
        HashMap<Integer, Integer> costs = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            if (board[i] != 0) {
                continue;
            }

            // Check if this is a valid location to place
            int[] tempBoard = board.clone();
            tempBoard[i] = 1;

//            System.out.println(Arrays.toString(tempBoard));

            costs.putIfAbsent(i, 0);

            // Check if i'll win
            if (check_for_win(tempBoard) == true) {
                costs.put(i, 100);
                continue;
            }

            // Check all opponents moves
            for (int j = 0; j < 9; j++) {
                if (tempBoard[j] != 0) {
                    continue;
                }

                int[] innerTemp = tempBoard.clone();
                innerTemp[j] = 2;

//                System.out.println("second move:");
//                System.out.println(Arrays.toString(innerTemp));

                // Check for opponents win
                if (check_for_win(innerTemp)) {

                    costs.put(i, costs.get(i) - 100);
                }
            }
        }

        // Find the highest reward move
        int maxKey = 0;
        int maxValue = -1000;
        for (Integer key : costs.keySet()) {
            if (costs.get(key) > maxValue) {
                maxValue = costs.get(key);
                maxKey = key;
            }
        }

        // make the highest reward move
        System.out.println("Agent move at: " + maxKey + " with reward: " + maxValue);
        board[maxKey] = 1;

        // Display the board
        display_board();

        return check_for_win(board);
    }

    /**
     * Displays the current board state.
     *
     */
    public void display_board() {
        // Display the board
        for (int i = 0; i < 9; i++) {
            System.out.print(board[i]);
            System.out.print(" ");

            if ((i + 1) % 3 == 0 && i > 0) {
                System.out.print("\r\n");
            }
        }
        System.out.print("\r\n");
    }

    /**
     * Prompots the player to make a move and checks for a win.
     *
     * @param i The location the players wishes to move at.
     * @return True on game over, False otherwise.
     */
    public boolean player_move(int i) {
        board[i] = 2;

        return check_for_win(board);
    }
}