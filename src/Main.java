import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Main {

    static final int SEMESTER_MAX = 1000;
    static final int VALID = 1;
    static final int INVALID = 0;
    static final char VALID_CELL = '▮';
    static final char INVALID_CELL = '▯';
    static final char SPACE = ' ';



    public static Scanner scanner;  // Note: Do not change this line.

    public static void initMatrix(int[][] board, int m, int n) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = INVALID;
            }
        }
    }

    /**
     * @param xy an empty array (len=2) which used to save the numbers from the input
     * @param input the input string that was received from the user
     * The function reads from a string in a format of - "m X n" or "x, y" the integers (x,y / m,n)
     */
    public static void getNumFromScanner(int[] xy, String input) {

        String sX = "", sY = "" ;
        int i = 0;
        while(input.charAt(i) != SPACE && input.charAt(i) != ',')
        {
            sX = sX + input.charAt(i);
            i++;
        }
        while(input.charAt(i) == SPACE || input.charAt(i) == ',' || input.charAt(i) == 'X') i++;
        while(i < input.length()){
            sY = sY + input.charAt(i);
            i++;
        }
        xy[0] = Integer.parseInt(sX);
        xy[1] = Integer.parseInt(sY);
    }



    public static void printBoard(int[][] board, int m, int n) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == INVALID) {
                    System.out.print(INVALID_CELL);
                } else {
                    System.out.print(VALID_CELL);
                }
            }
            System.out.println();
        }
    }


    /**
     *
     * @param row the row number which is checked if it's out of bounds.
     * @param col the column number which is checked if it's out of bounds.
     * @param m the number of rows in the matrix.
     * @param n the number of columns in the matrix.
     * @return true if the position is in the bounds of a matrix[m][n] false otherwise.
     */
    public static boolean inBounds(int row, int col, int m, int n) {
        return ((row >= 0) && (col >= 0) && (col < n) && (row < m));
    }
    /** The function updates the validation of a student in board[row][col]*/
    public static void updateStudentStatus(int[][] board, int row, int col) {
        if (board[row][col] == INVALID) {
            board[row][col] = VALID;
        } else {
            board[row][col] = INVALID;
        }
    }

    /**
     *
     * @param board the game board
     * @param m the number of rows in board
     * @param n the number of columns in board
     * xy - an integers array[2] used to retract the locations(integers) from the input(string)
     * The function initialize the board according to the president's input
     * gets from the user (The president) a series of locations(strings) for the valid students until "Yokra" is entered.
     * The function updates the board accordingly.
     */
    public static void initStudents(int[][] board, int m, int n) {
        int row, col;
        int [] xy = new int[2];
        System.out.println("Dear president, please enter the cell’s indexes.");
        String cellsIndexes = scanner.nextLine();

        while (!cellsIndexes.equals("Yokra")) {
            getNumFromScanner(xy,cellsIndexes);
            row = xy[0];
            col = xy[1];

            if (inBounds(row, col, m, n)) {
                updateStudentStatus(board, row, col);
                System.out.println("Dear president, please enter the cell’s indexes.");
            } else {
                System.out.println("The cell is not within the board’s boundaries, enter a new cell.");
            }

            cellsIndexes = scanner.nextLine();
        }
    }

    public static int semesterChanges(int[][] board, int[][] validFriends, int m, int n) {
        int numOfChanges = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if ((board[i][j] == VALID) && ((validFriends[i][j] <= 1) || (validFriends[i][j] >= 4))) {
                    board[i][j] = INVALID;
                    numOfChanges++;
                }
                if ((board[i][j] == INVALID) && ((validFriends[i][j] == 3))) {
                    board[i][j] = VALID;
                    numOfChanges++;
                }
            }
        }
        return numOfChanges;
    }


    /**
     *
     * @param board The game board
     * @param validFriends A matrix[m][n] used to save for each student in board the number of valid friends he has.
     * @param m the number of rows in board
     * @param n the number of columns in board
     * @return the number of valid students on the board.
     */
    public static int countValidFriends(int[][] board, int[][] validFriends, int m, int n) {

        int[][] locations = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        int curRow, curCol, validStudentsNum = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                validFriends[i][j] = 0;
                if (board[i][j] == VALID) validStudentsNum++;
                for (int k = 0; k < locations.length; k++) {
                    curCol = j + locations[k][1];
                    curRow = i + locations[k][0];
                    if (inBounds(curRow, curCol, m, n)) {
                        validFriends[i][j] += board[curRow][curCol];
                    }
                }
            }
        }
        return validStudentsNum;
    }

    /**
     * @param board the game board
     * @param m the number of rows in board
     * @param n the number of columns in board
     * @param validStudentsNum the number of valid students on the board.
     * @param semester the number of the current semester.
     *  The function prints the current status of the game - the semester number, the board, number of valid students.
     */
    public static void printStatus(int[][] board,int m, int n, int validStudentsNum, int semester) {
        System.out.println("Semester number " + semester + ":");
        printBoard(board, m, n);
        System.out.println("Number of students: " + validStudentsNum);
        System.out.println();

    }

    /**
     * @param board the game board
     * @param m the number of rows in board
     * @param n the number of columns in board
     * @param semester the number of the current semester.
     * @param validStudentsNum the number of valid students on the board.
     * @param changes the number of changes in the current semester
     * The function checks if the game is still on by these rules:
     * haven't reached the semester number limit.
     * there were changes in the past semester.
     * there's at least one valid students on board.
     * @return the function returns true if the game can continue false otherwise
     */
    public static boolean isGameContinues(int[][] board , int m, int n, int semester, int validStudentsNum, int changes) {
        if (changes == 0) {
            System.out.println("The students have stabilized.");
            return false;
        }
        printStatus(board,m,n,validStudentsNum,semester);
        if (semester == SEMESTER_MAX) {
            System.out.println("The semesters limitation is over.");
            return false;
        }
        if (validStudentsNum == 0) {
            System.out.println("There are no more students.");
            return false;
        }
        return true;
    }

    /**
     * The main function of the game.
     */
    public static void theStudentsGame() {
        int m, n, validStudentsNum, semester = 1, changes = 1;
        int[] mn = new int[2];
        System.out.println("Dear president, please enter the board’s size.");
        String sizeOfBoard = scanner.nextLine();
        getNumFromScanner(mn, sizeOfBoard);
        m = mn[0];
        n = mn[1];
        int[][] board = new int[m][n];
        initMatrix(board, m, n);
        initStudents(board, m, n);

        int[][] validFriends = new int[m][n];
        validStudentsNum = countValidFriends(board, validFriends, m, n);

        while (isGameContinues(board, m, n, semester, validStudentsNum, changes)) {
            changes = semesterChanges(board, validFriends, m, n);
            semester++;
            validStudentsNum = countValidFriends(board, validFriends, m, n);
        }
    }

    public static void main(String[] args) throws IOException {
        String path = args[0];
        //System.out.println(path);
        scanner = new Scanner(new File(path));
        int numberOfGames = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= numberOfGames; i++) {
            System.out.println("Game number " + i + " starts.");
            theStudentsGame();
            System.out.println("Game number " + i + " ended.");
            System.out.println("-----------------------------------------------");
        }
        System.out.println("All games are ended.");
    }
}


