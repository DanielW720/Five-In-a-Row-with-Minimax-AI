import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Grid {
    private final int files; // x
    private final int ranks; // y
    private class GamePos {
        char[][] grid;
        public GamePos(int size) {
            this.grid = new char[size][size];
        }
    }
    private GamePos[] mGamePos;
    private class Move {
        /**
         * One-way tree structure; parents knows the index of its children, but no child knows the index
         * of its parent.
         */
        int[][] child;
        int numOfChildren;
        int selectedChild; // Selected by Minimax
        int tmpSelectChild;
        public Move(int maxNumOfChildren) {
            this.numOfChildren = 0;
            this.child = new int[maxNumOfChildren][2];
        }
    }
    private Move[] mMove;
    private int ply;
    private final int maxPly;
    private final int minimaxDepth;
    Scanner scanner;
    Random rand;

    public Grid(int size, int minimaxDepth) {
        this.files = size;
        this.ranks = size;
        maxPly = size * size; // Cannot be more moves than there are squares
        this.minimaxDepth = minimaxDepth;
        mGamePos = new GamePos[maxPly]; // Each obj in mGamePos is only declared, not initialized yet
        mMove = new Move[maxPly]; // Each obj in mMove is only declared, not initialized yet
        ply = 0;
        scanner = new Scanner(System.in);
        rand = new Random();
        // Initialize the first grid
        mGamePos[ply] = new GamePos(size); // ply = 0
        for (int y=0; y<ranks; y++) {
            for (int x=0; x<files; x++) {
                mGamePos[ply].grid[y][x] = '*';
            }
        }
    }

    // ----- Getters and setters (for testing purposes) -----

    public void setPly(int ply) {
        this.ply = ply;
    }

    public int getPly() {
        return ply;
    }

    public void setGrid(char[][] grid) {
        mGamePos[ply] = new GamePos(grid.length);
        mGamePos[ply].grid = grid;
    }

    public char[][] getGrid(int mPly) {
        return mGamePos[mPly].grid;
    }

    // ---------- Game mechanics ----------

    /**
     * Copy the game position at mGamePos[mPly] to newGamePos and return newGamePos.
     * @return a new GamePos object, identical to mGamePos[mPly].
     */
    private GamePos copyGamePos(int mPly) {
        GamePos newGamePos = new GamePos(files);
        for (int y=0; y<ranks; y++)
            System.arraycopy(mGamePos[mPly].grid[y], 0, newGamePos.grid[y], 0, files);
        return newGamePos;
    }

    /**
     * Create a new move based on coordinates. Uses ply to find out
     * which player is making the move.
     * @param x the coordinate for the horizontal axis.
     * @param y the coordinate for the vertical axis.
     */
    private void updateGrid(int mPly, int x, int y) {
        if (mPly > 0) {
            // Initialize new grid and copy current game position
            mGamePos[mPly] = copyGamePos(mPly - 1);
        }
        // Update mGamePos[ply].grid with the new move
        mGamePos[mPly].grid[y][x] = (mPly % 2 == 0) ? 'X' : 'O';
    }

    private boolean validMove(int x, int y) {
        if (x < 0 || x > files || y < 0 || y > ranks) {
            return false;
        } else if (ply == 0) {
            return true;
        } else {
            return mGamePos[ply - 1].grid[y][x] == '*';
        }
    }

    /**
     * Let human player make a move.
     */
    public void newPlayerMove() {
        boolean keepReadingInput = true;
        System.out.println("Your turn!");
        int x, y;
        while (keepReadingInput) {
            try {
                System.out.println("Enter x-coordinate:");
                x = scanner.nextInt();
                scanner.nextLine();
                if (x < 0 || x >= files) {
                    System.out.println("x coordinate is out of range");
                    continue;
                }
                System.out.println("Enter y-coordinate:");
                y = scanner.nextInt();
                scanner.nextLine();
                if (y < 0 || y >= ranks) {
                    System.out.println("y coordinate is out of range");
                    continue;
                }
                if (validMove(x, y)) {
                    System.out.println("\nYou played (" + x + ", " + y + ")");
                    updateGrid(ply, x, y);
                    ply++;
                    keepReadingInput = false;
                } else {
                    System.out.println("Invalid move, try again.");
                }
            }
            catch (Exception e) {
                System.out.println("Invalid input, try again.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Let computer make a move based only on randomness.
     */
    public void newRandomComputerMove() {
        int x, y;
        boolean keepAsking = true;
        while (keepAsking) {
            x = rand.nextInt(files);
            y = rand.nextInt(ranks);
            if (validMove(x, y)) {
                updateGrid(ply, x, y);
                ply++;
                System.out.println("Computer played (" + x + ", " + y + ")");
                keepAsking = false;
            }
        }
    }

    //------------- MINIMAX -------------

    /**
     * Let computer make a move. This method uses the Minimax algorithm
     * to find the optimal move for the computer player.
     */
    public void newComputerMove() {
        float minimax = minimax(ply, minimaxDepth, 'O');
        int childIdx = mMove[ply].selectedChild;
        int x = mMove[ply].child[childIdx][0];
        int y = mMove[ply].child[childIdx][1];
        System.out.println("Computer selected child " + childIdx + " with coordinates: (" + x + ", " + y + ")");
        System.out.println(String.format("Minimax score: %.8f\n", minimax));
        updateGrid(ply, x, y);
        ply++;
    }

    /**
     * Finds the best move. {@code minimax()} uses the DFS-algorithm to search the tree and
     * will update the data members of mMove[mPly] in order for other methods to access the
     * optimal child (i.e., the optimal move) at mPly.
     * @param mPly the move that is to be made.
     * @param depth maximum depth of the decision tree.
     * @return the score of the optimal child.
     */
    private float minimax(int mPly, int depth, char player) {
        if (depth == 0 || fiveInARow(mPly - 1)) {
            // Static evaluation of leaf nodes
            int currentIdx = mMove[mPly-1].tmpSelectChild;
            int x = mMove[mPly-1].child[currentIdx][0];
            int y = mMove[mPly-1].child[currentIdx][1];
            // Update player; base case (depth = 0) always evaluate score
            // for current player, so it has to be changed back
            char playerUpdated = ((mPly-1) % 2 == 0) ? 'X' : 'O';
            return ScoreEvaluation.goalFunction(mGamePos[mPly-1].grid, playerUpdated, x, y);
        }
        // If there's already an object at mGamePos[mPly] or mMove[mPly], it will be reset
        mGamePos[mPly] = copyGamePos(mPly-1); // Instantiate game position at mPly
        mMove[mPly] = new Move(files*ranks); // Instantiate move at mPly
        findAllChildren(mPly); // Find all children at mPly
//        findAllChildren(mGamePos[mPly].grid, mPly); // Alternative child-finding function
        if (player == 'X') {
            // Human player tries to maximize the score
            float maxEval = -Float.MAX_VALUE;
            int currentBestChildIdx = 0;
            for (int i=0; i<mMove[mPly].numOfChildren; i++) {
                mMove[mPly].tmpSelectChild = i; // So base case (depth = 0) have knowledge of the current child's index
                // Update grid (not including previous child)
                updateGrid(mPly, mMove[mPly].child[i][0], mMove[mPly].child[i][1]);
                float eval = minimax(mPly + 1, depth-1, 'O'); // Go deeper into tree
                if (eval > maxEval) {
                    maxEval = eval;
                    currentBestChildIdx = i;
                }
            }
            mMove[mPly].selectedChild = currentBestChildIdx;
            return maxEval;
        }
        else {
            // Computer player tries to minimize the score
            float minEval = Float.MAX_VALUE;
            int currentBestChildIdx = 0;
            for (int i=0; i<mMove[mPly].numOfChildren; i++) {
                mMove[mPly].tmpSelectChild = i; // So base case (depth = 0) have knowledge of the current child's index
                // Update grid (not including previous child)
                updateGrid(mPly, mMove[mPly].child[i][0], mMove[mPly].child[i][1]);
                float eval = minimax(mPly + 1, depth-1, 'X'); // Go deeper into tree
                if (eval < minEval) {
                    minEval = eval;
                    currentBestChildIdx = i;
                }
            }
            mMove[mPly].selectedChild = currentBestChildIdx;
            return minEval;
        }
    }

    /**
     * Find and add all children at ply mPly.
     * @param mPly the ply at which we wish to find all children.
     */
    private void findAllChildren(int mPly) {
        for (int y=0; y<ranks; y++) {
            for (int x=0; x<files; x++) {
                if (mGamePos[mPly].grid[y][x] == '*') {
                    // Each empty square is a valid move and should be as a child of mMove[mPly]
                    mMove[mPly].child[mMove[mPly].numOfChildren][0] = x;
                    mMove[mPly].child[mMove[mPly].numOfChildren][1] = y;
                    mMove[mPly].numOfChildren++;
                }
            }
        }
    }

    /**
     * Find and add all children at ply mPly, where a child is an empty square adjacent to a non-empty square.
     * This method is an alternative to findAllChildren(int mPly).
     * @param mPly the ply at which we wish to find all children.
     */
    private void findAllChildren(char[][] grid, int mPly) {
        for (int y=0; y<ranks; y++) {
            for (int x=0; x<files; x++) {
                // Each empty square that is adjacent to a non-empty square is a valid move and
                // should be as a child of mMove[mPly]
                if (grid[y][x] == '*' && (neighborVertically(grid, x, y) ||
                                neighborHorizontally(grid, x, y) ||
                                neighborDiagonally(grid, x, y))) {
                    mMove[mPly].child[mMove[mPly].numOfChildren][0] = x;
                    mMove[mPly].child[mMove[mPly].numOfChildren][1] = y;
                    mMove[mPly].numOfChildren++;
                }
            }
        }
    }

    private boolean neighborVertically(char[][] grid, int x, int y) {
        int size = grid[x].length;
        return ((y+1 < size && grid[y+1][x] != '*') || (y > 0 && grid[y-1][x] != '*'));
    }

    private boolean neighborHorizontally(char[][] grid, int x, int y) {
        int size = grid[x].length;
        return ((x+1 < size && grid[y][x+1] != '*') || (x > 0 && grid[y][x-1] != '*'));
    }

    private boolean neighborDiagonally(char[][] grid, int x, int y) {
        int size = grid[x].length;
        if (y > 0 && x+1 < size && grid[y-1][x+1] != '*') return true;
        else if (y+1 < size && x+1 < size && grid[y+1][x+1] != '*') return true;
        else if (y+1 < size && x > 0 && grid[y+1][x-1] != '*') return true;
        else return (y > 0 && x > 0 && grid[y-1][x-1] != '*');
    }

    // ---------- Check game state ----------

    public boolean fiveInARow(int mPly) {
        char player = (mPly % 2 == 0) ? 'X' : 'O';
        for (int y=0; y<ranks; y++) {
            for (int x=0; x<files; x++) {
                if (mGamePos[mPly].grid[y][x] == player) {
                    if (checkVertical(player, mPly, x, y) ||
                            checkHorizontal(player, mPly, x, y) ||
                            checkDiagonal(player, mPly, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkVertical(char player, int mPly, int x, int y) {
        int count = 1;
        // Check upwards
        for (int i=y+1; i<ranks; i++) {
            if (mGamePos[mPly].grid[i][x] == player) count++;
            else break;
        }
        // Check downwards
        for (int i=y-1; i>=0; i--) {
            if (mGamePos[mPly].grid[i][x] == player) count++;
            else break;
        }
        return count >= 5;
    }

    private boolean checkHorizontal(char player, int mPly, int x, int y) {
        int count = 1;
        // Check right
        for (int i=x+1; i<files; i++) {
            if (mGamePos[mPly].grid[y][i] == player) count++;
            else break;
        }
        // Check left
        for (int i=x-1; i>=0; i--) {
            if (mGamePos[mPly].grid[y][i] == player) count++;
            else break;
        }
        return count >= 5;
    }

    private boolean checkDiagonal(char player, int mPly, int x, int y) {
        int countA = 1, countB = 1;
        // Check right, upwards
        int x1 = x+1, y1 = y-1;
        while (x1 < files && y1 >= 0) {
            if (mGamePos[mPly].grid[y1][x1] == player) {
                countA++;
                x1++;
                y1--;
            }
            else break;
        }
        // Check right, downwards
        x1 = x+1; y1 = y+1;
        while (x1 < files && y1 < ranks) {
            if (mGamePos[mPly].grid[y1][x1] == player) {
                countB++;
                x1++;
                y1++;
            }
            else break;
        }
        // Check left, downwards
        x1 = x-1; y1 = y+1;
        while (x1 >= 0 && y1 < ranks) {
            if (mGamePos[mPly].grid[y1][x1] == player) {
                countA++;
                x1--;
                y1++;
            }
            else break;
        }
        // Check left, upwards
        x1 = x-1; y1 = y-1;
        while (x1 >= 0 && y1 > 0) {
            if (mGamePos[mPly].grid[y1][x1] == player) {
                countB++;
                x1--;
                y1--;
            }
            else break;
        }
        return countA >= 5 || countB >= 5;
    }

    public boolean gameIsATie(int mPly) {
        return gameIsATie(mGamePos[mPly].grid);
    }

    public boolean gameIsATie(char[][] grid) {
        if (ply == maxPly) return true;
        char X = 'X', O = 'O', empty = '*';
        for (int y=0; y<ranks; y++) {
            for (int x=0; x<files; x++) {
                if (grid[y][x] == empty) {
                    // Check if win is possible in any direction
                    if (possibleVertical(grid, x, y, X) ||
                            possibleVertical(grid, x, y, O) ||
                            possibleHorizontal(grid, x, y, X) ||
                            possibleHorizontal(grid, x, y, O) ||
                            possibleDiagonal(grid, x, y, X) ||
                            possibleDiagonal(grid, x, y, O))
                        return false;
                    }
                }
            }
        return true;
    }

    private boolean possibleVertical(char[][] grid, int x, int y, char opponent) {
        int count = 1;
        // Check upwards
        for (int i=y+1; i<ranks; i++) {
            if (grid[i][x] != opponent) count++;
            else break;
        }
        // Check downwards
        for (int i=y-1; i>=0; i--) {
            if (grid[i][x] != opponent) count++;
            else break;
        }
        return count >= 5;
    }

    private boolean possibleHorizontal(char[][] grid, int x, int y, char opponent) {
        int count = 1;
        // Check right
        for (int i=x+1; i<files; i++) {
            if (grid[y][i] != opponent) count++;
            else break;
        }
        // Check left
        for (int i=x-1; i>=0; i--) {
            if (grid[y][i] != opponent) count++;
            else break;
        }
        return count >= 5;
    }

    private boolean possibleDiagonal(char[][] grid, int x, int y, char opponent) {
        int countA = 1, countB = 1;
        // Check right, upwards
        int x1 = x+1, y1 = y-1;
        while (x1 < files && y1 >= 0) {
            if (grid[y1][x1] != opponent) {
                countA++;
                x1++;
                y1--;
            }
            else break;
        }
        // Check right, downwards
        x1 = x+1; y1 = y+1;
        while (x1 < files && y1 < ranks) {
            if (grid[y1][x1] != opponent) {
                countB++;
                x1++;
                y1++;
            }
            else break;
        }
        // Check left, downwards
        x1 = x-1; y1 = y+1;
        while (x1 >= 0 && y1 < ranks) {
            if (grid[y1][x1] != opponent) {
                countA++;
                x1--;
                y1++;
            }
            else break;
        }
        // Check left, upwards
        x1 = x-1; y1 = y-1;
        while (x1 >= 0 && y1 > 0) {
            if (grid[y1][x1] != opponent) {
                countB++;
                x1--;
                y1--;
            }
            else break;
        }
        return countA >= 5 || countB >= 5;
    }

    // ---------- Print methods ----------

    /**
     * Print grid at mPly.
     * @param mPly the index of the game position that is to be displayed.
     */
    public void printGrid(int mPly, boolean initialPrint) {
        if (initialPrint) System.out.println("Printing grid at ply 0:\n");
        else {
            System.out.println("Printing grid after ply " + mPly + ":\n");
        }
        System.out.print("   ");
        for (int x=0; x<files; x++) System.out.print(Color.ANSI_YELLOW + x + " " + Color.ANSI_RESET);
        System.out.println();
        for (int y=0; y<ranks; y++) {
            if (y<10) System.out.print(Color.ANSI_YELLOW + y + "  " + Color.ANSI_RESET);
            else System.out.print(Color.ANSI_YELLOW + y + " " + Color.ANSI_RESET);
            for (int x=0; x<files; x++) {
                char currentSquare = mGamePos[mPly].grid[y][x];
                if (x>=10) {
                    if (currentSquare == 'O')
                        System.out.print(Color.ANSI_CYAN + " " + currentSquare + " " + Color.ANSI_RESET);
                    else if (currentSquare == 'X')
                        System.out.print(Color.ANSI_MAGENTA + " " + currentSquare + " " + Color.ANSI_RESET);
                    else
                        System.out.print(" " + currentSquare + " ");
                } else {
                    if (currentSquare == 'O')
                        System.out.print(Color.ANSI_CYAN + currentSquare + " " + Color.ANSI_RESET);
                    else if (currentSquare == 'X')
                        System.out.print(Color.ANSI_MAGENTA + currentSquare + " " + Color.ANSI_RESET);
                    else
                        System.out.print(currentSquare + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Print all grids up to the current ply.
     */
    public void printAllPlys() {
        for (int i=0; i<ply; i++) {
            printGrid(i, false);
        }
    }

    /**
     * Print all children of mMove[mPly].
     * @param mPly the index of the specified move.
     */
    private void printChildren(int mPly) {
        System.out.println("The children of mMove[" + mPly + "] are:");
        for (int i=0; i<mMove[mPly].numOfChildren; i++) {
            System.out.println(Arrays.toString(mMove[mPly].child[i]));
        }
        System.out.println();
    }
}
