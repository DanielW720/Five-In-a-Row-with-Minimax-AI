/**
 * ---------- Score logic: ----------
 *
 * Here follows an explanation of how the score is evaluated for a move defined by mPly and coordinates (x, y).
 *
 * "Correct symbol" refers to 'X' if mPly is even, else 'O'.
 * "Incorrect symbol" refers to 'O' if mPly is even, else 'X'.
 * "Blank square" refers to the symbol '*'.
 * "Player" refers to the current player that is about to make a move.
 *
 * For each direction (vertical, horizontal and diagonal), friendlyNeighborScore = 2 is added to scorePlayer for each
 * correct consecutive symbol in a row (without any other symbol interfering with this row). Once a different
 * symbol, blank square or end of the row/column is reached, we have found the score for that
 * specific direction, and we can move on to the next direction. If the first symbol in a specific direction
 * is the opponents symbol, the same logic applies, except the added score is hostileNeighborScore = 0.5.
 *
 * There are many more factors or "strategic rules" to consider when quantifying a move. Here follows a list of
 * different cases that must be accounted for when deciding the score of a move. I have marked each rule in the
 * following list as (1), (2) and so on. In order to make the code a bit more readable, I have added comments such as
 * "Rule (i)" in the method verticalScore() to mark that the following piece of code handles rule (i). Since
 * it becomes easy to understand horizontalScore() and diagonalScore() once verticalScore() is understood, I leave these
 * comments out in horizontalScore() and diagonalScore().
 *
 *  LIST OF RULES:
 *
 * (1) If the move results in five correct symbols in a row, the move is a winning move, and
 * thus, the algorithm can return maximum score (the maximum score is arbitrary as long as no other score can be
 * larger; I set the maximum score to 1 000 000 000) and thereby terminate since there is no need to keep look for a
 * better move.
 *
 * (2) If the count of incorrect symbols in a given direction reaches 4, the move is critical to be made (unless we
 * find a winning move, that is) since the opponent will win on the next ply unless the current player blocks.
 * Hence, goalFunction() will add a score of 1 000 000 for this move. This "protection move" is the second-highest
 * score a move can reach, which reflects its importance rank (winning move being the highest ranking move, of course).
 * Contrary to a maximum scoring move, this move will not be returned instantly as there is still a chance of finding
 * a maximum scoring move.
 *
 * (3) If the count of incorrect symbols in a given direction
 * reaches 3 AND the very next symbol is a blank square, it is important to
 * make this move and so preventFourInARowScore = 1000 will be added to score.
 * Example ('O' marks the opponent):
 *              ... '*' '*' 'O' 'O' 'O' '*' '*' ...
 * Here, we see that unless 'X' marks one of the two adjacent '*', the opponent will have four in a row the very next
 * ply, and then 'X' won't be able to block the opponent from winning.
 *
 * (4) If a move is on the very end of the grid, this should be penalized. It's generally better to play mid-grid
 * since this allows the player to build on multiple directions, whereas if the player makes a move at the end of
 * the grid, the player cannot build further in that direction. A score of endOfGridScore = -0.5 is added to
 * the score of the move.
 *
 * (5) Let's say that player is 'O'. In a given direction (doesn't matter which one), six
 * adjacent squares looks like this:
 *                                ... '*' 'O' '*' 'O' 'O' '*' ...
 * Unless the game position fulfills rule (1) or rule (2), the best move for player is the third blank square '*'
 * from the left. This gives player four in a row, and opponent will not be able to stop player from winning in the
 * next two plies. Therefore, this move (the third '*' from the left) must be given extra high score, but less than
 * protectionScore. I will call this fourInARowScore, and I will set it to 100 000. The easiest way to implement this is
 * to add fourInARowScore each time player gets four in a row, but the example shows why it _can_ be worth so much.
 *
 * (6) A potential move should be penalized if it cannot result in five in a row. Example ("|" marks end of grid):
 *                      | '*' '*' 'X' 'X' 'O' ...
 * From what we can see in this example, there's no point for 'X' to continue building on the left since 'X' cannot
 * get five in row that way. Therefore, a move towards the left will be penalized by unableToBuildFiveScore = -2. On
 * the other hand, if a move can build towards five in a row, it is by ableToBuildFiveScore = 1.
 *
 */

public final class ScoreEvaluation {

    // The following scores are sorted from most important to the least important
    private final static float maximumScore = 1_000_000_000f; // Rule (1)
    private final static float protectionScore = 1_000_000f; // Rule (2)
    private final static float fourInARowScore = 100_000f; // Rule (5)
    private final static float preventFourInARowScore = 1000f; // Rule (3)
    private final static float unableToBuildFiveScore = -2f; // Rule (6)
    private final static float ableToBuildFiveScore = 1f; // Rule (6)
    private final static float friendlyNeighborScore = 2f; // See logic explanation
    private final static float hostileNeighborScore = 0.5f; // See logic explanation
    private final static float endOfGridScore = -0.5f; // Rule (4)

    /**
     * Calculate the score for a potential move given by (x, y).
     * @param grid the current game position.
     * @param player the player that is about to make a move.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @return the score of move (x, y) on game position grid by player.
     */
    public static float goalFunction(char[][] grid, char player, int x, int y) {
        int size = grid.length;
        float vertical = verticalScore(grid, player, size, x, y);
        float horizontal = horizontalScore(grid, player, size, x, y);
        float diagonal = diagonalScore(grid, player, size, x, y);
        return (player == 'X') ? (vertical+horizontal+diagonal) : -(vertical+horizontal+diagonal);
    }

    private static float verticalScore(char[][] grid, char player, int size, int x, int y) {
        char opponent = (player == 'X') ? 'O': 'X';
        int countPlayer = 1, countOpponent = 0, countPlayerAndBlanks = 1;
        boolean onlyPlayerSymbolsVisited;
        float score = 0f;
        // Check upwards, check for square == player
        onlyPlayerSymbolsVisited = true;
        for (int i=y+1; i<size; i++) {
            if (grid[i][x] != player) onlyPlayerSymbolsVisited = false;
            if (grid[i][x] != opponent) countPlayerAndBlanks++; // Rule (6), see end of method
            if (grid[i][x] == player && onlyPlayerSymbolsVisited) {
                countPlayer++;
                if (countPlayer == 5) return maximumScore; // Rule (1)
                if (i == size-1) score += endOfGridScore; // Rule (4)
                score += friendlyNeighborScore;
            } else if (grid[i][x] == opponent) break;
        }
        // Check upwards, check for square == opponent
        for (int i=y+1; i<size; i++) {
            if (countOpponent == 3 && grid[i][x] == '*') score += preventFourInARowScore; // Rule (3)
            if (grid[i][x] == opponent) {
                countOpponent++;
                if (countOpponent == 4) score += protectionScore; // Rule (2)
                if (i == size-1) score += endOfGridScore; // Rule (4)
                score += hostileNeighborScore;
            }
            else break;
        }
        // Check downwards, check for square == player
        onlyPlayerSymbolsVisited = true;
        for (int i=y-1; i>=0; i--) {
            if (grid[i][x] != player) onlyPlayerSymbolsVisited = false;
            if (grid[i][x] != opponent) countPlayerAndBlanks++;
            if (grid[i][x] == player && onlyPlayerSymbolsVisited) {
                countPlayer++;
                if (countPlayer == 5) return maximumScore;
                if (i == 0) score += endOfGridScore;
                score += friendlyNeighborScore;
            } else if (grid[i][x] == opponent) break;
        }
        // Check downwards, check for square == opponent
        for (int i=y-1; i>=0; i--) {
            if (countOpponent == 3 && grid[i][x] == '*') score += preventFourInARowScore;
            if (grid[i][x] == opponent) {
                countOpponent++;
                if (countOpponent == 4) score += protectionScore;
                if (i == 0) score += endOfGridScore;
                score += hostileNeighborScore;
            }
            else break;
        }
        if (countPlayerAndBlanks < 5) score += unableToBuildFiveScore; // Rule (6)
        else score += ableToBuildFiveScore;
        if (countPlayer == 4) score += fourInARowScore; // Rule (5)
        if (x == size-1 || x == 0) score += endOfGridScore;
        return score;
    }

    private static float horizontalScore(char[][] grid, char player, int size, int x, int y) {
        char opponent = (player == 'X') ? 'O': 'X';
        int countPlayer = 1, countOpponent = 0, countPlayerAndBlanks = 1;
        boolean onlyPlayerSymbolsVisited;
        float score = 0;
        // Check right, check for square == player
        onlyPlayerSymbolsVisited = true;
        for (int i=x+1; i<size; i++) {
            if (grid[y][i] != player) onlyPlayerSymbolsVisited = false;
            if (grid[y][i] != opponent) countPlayerAndBlanks++;
            if (grid[y][i] == player && onlyPlayerSymbolsVisited) {
                countPlayer++;
                if (countPlayer == 5) return maximumScore;
                if (i == size-1) score += endOfGridScore;
                score += friendlyNeighborScore;
            } else if (grid[y][i] == opponent) break;
        }
        // Check right, check for square == opponent
        for (int i=x+1; i<size; i++) {
            if (countOpponent == 3 && grid[y][i] == '*') score += preventFourInARowScore;
            if (grid[y][i] == opponent) {
                countOpponent++;
                if (countOpponent == 4) score += protectionScore;
                if (i == size-1) score += endOfGridScore;
                score += hostileNeighborScore;
            }
            else break;
        }
        // Check left, check for square == player
        onlyPlayerSymbolsVisited = true;
        for (int i=x-1; i>=0; i--) {
            if (grid[y][i] != player) onlyPlayerSymbolsVisited = false;
            if (grid[y][i] != opponent) countPlayerAndBlanks++;
            if (grid[y][i] == player && onlyPlayerSymbolsVisited) {
                countPlayer++;
                if (countPlayer == 5) return maximumScore;
                if (i == 0) score += endOfGridScore;
                score += friendlyNeighborScore;
            } else if (grid[y][i] == opponent) break;
        }
        // Check left, check for square == opponent
        for (int i=x-1; i>=0; i--) {
            if (countOpponent == 3 && grid[y][i] == '*') score += preventFourInARowScore;
            if (grid[y][i] == opponent) {
                countOpponent++;
                if (countOpponent == 4) score += protectionScore;
                if (i == 0) score += endOfGridScore;
                score += hostileNeighborScore;
            }
            else break;
        }
        if (countPlayerAndBlanks < 5) score += unableToBuildFiveScore;
        else score += ableToBuildFiveScore;
        if (countPlayer == 4) score += fourInARowScore;
        if (y == size-1 || y == 0) score += endOfGridScore;
        return score;
    }

    private static float diagonalScore(char[][] grid, char player, int size, int x, int y) {
        char opponent = (player == 'X') ? 'O': 'X';
        int countPlayerA = 1, countOpponentA = 0;
        int countPlayerB = 1, countOpponentB = 0;
        int countPlayerAndBlanksA = 1, countPlayerAndBlanksB = 1;
        boolean onlyPlayerSymbolsVisitedA, onlyPlayerSymbolsVisitedB;
        float score = 0;
        // Check right upwards, check for square == player
        onlyPlayerSymbolsVisitedA = true;
        int x1 = x+1, y1 = y-1;
        while (x1 < size && y1 >= 0) {
            if (grid[y1][x1] != player) onlyPlayerSymbolsVisitedA = false;
            if (grid[y1][x1] != opponent) countPlayerAndBlanksA++;
            if (grid[y1][x1] == player && onlyPlayerSymbolsVisitedA) {
                countPlayerA++; x1++; y1--;
                if (countPlayerA == 5) return maximumScore;
                if (x1 == size-1 || y == 0) score += endOfGridScore;
                score += friendlyNeighborScore;
            } else if (grid[y1][x1] == opponent) break;
            else {
                x1++; y1--;
            }
        }
        // Check right upwards, check for square == opponent
        x1 = x+1; y1 = y-1;
        while (x1 < size && y1 >= 0) {
            if (countOpponentA == 3 && grid[y1][x1] == '*') score += preventFourInARowScore;
            if (grid[y1][x1] == opponent) {
                countOpponentA++; x1++; y1--;
                if (countOpponentA == 4) score += protectionScore;
                if (x1 == size-1 || y == 0) score += endOfGridScore;
                score += hostileNeighborScore;
            }
            else break;
        }
        // Check right downwards, check for square == player
        onlyPlayerSymbolsVisitedB = true;
        x1 = x+1; y1 = y+1;
        while (x1 < size && y1 < size) {
            if (grid[y1][x1] != player) onlyPlayerSymbolsVisitedB = false;
            if (grid[y1][x1] != opponent) countPlayerAndBlanksB++;
            if (grid[y1][x1] == player && onlyPlayerSymbolsVisitedB) {
                countPlayerB++; x1++; y1++;
                if (countPlayerB == 5) return maximumScore;
                if (x1 == size-1 || y == size-1) score += endOfGridScore;
                score += friendlyNeighborScore;
            } else if (grid[y1][x1] == opponent) break;
            else {
                x1++; y1++;
            }
        }
        // Check right downwards, check for square == opponent
        x1 = x+1; y1 = y+1;
        while (x1 < size && y1 < size) {
            if (countOpponentB == 3 && grid[y1][x1] == '*') score += preventFourInARowScore;
            if (grid[y1][x1] == opponent) {
                countOpponentB++; x1++; y1++;
                if (countOpponentB == 4) score += protectionScore;
                if (x1 == size-1 || y == size-1) score += endOfGridScore;
                score += hostileNeighborScore;
            }
            else break;
        }
        // Check left downwards, check for square == player
        onlyPlayerSymbolsVisitedA = true;
        x1 = x-1; y1 = y+1;
        while (x1 >= 0 && y1 < size) {
            if (grid[y1][x1] != player) onlyPlayerSymbolsVisitedA = false;
            if (grid[y1][x1] != opponent) countPlayerAndBlanksA++;
            if (grid[y1][x1] == player && onlyPlayerSymbolsVisitedA) {
                countPlayerA++; x1--; y1++;
                if (countPlayerA == 5) return maximumScore;
                if (x1 == 0 || y == size-1) score += endOfGridScore;
                score += friendlyNeighborScore;
            } else if (grid[y1][x1] == opponent) break;
            else {
                x1--; y1++;
            }
        }
        // Check left downwards, check for square == opponent
        x1 = x-1; y1 = y+1;
        while (x1 >= 0 && y1 < size) {
            if (countOpponentA == 3 && grid[y1][x1] == '*') score += preventFourInARowScore;
            if (grid[y1][x1] == opponent) {
                countOpponentA++; x1--; y1++;
                if (countOpponentA == 4) score += protectionScore;
                if (x1 == 0 || y == size-1) score += endOfGridScore;
                score += hostileNeighborScore;
            }
            else break;
        }
        // Check left upwards, check for square == player
        onlyPlayerSymbolsVisitedB = true;
        x1 = x-1; y1 = y-1;
        while (x1 >= 0 && y1 > 0) {
            if (grid[y1][x1] != player) onlyPlayerSymbolsVisitedB = false;
            if (grid[y1][x1] != opponent) countPlayerAndBlanksB++;
            if (grid[y1][x1] == player && onlyPlayerSymbolsVisitedB) {
                countPlayerB++; x1--; y1--;
                if (countPlayerB == 5) return maximumScore;
                if (x1 == 0 || y == 0) score += endOfGridScore;
                score += friendlyNeighborScore;
            } else if (grid[y1][x1] == opponent) break;
            else {
                x1--; y1--;
            }
        }
        // Check left upwards, check for square == opponent
        x1 = x-1; y1 = y-1;
        while (x1 >= 0 && y1 > 0) {
            if (countOpponentB == 3 && grid[y1][x1] == '*') score += preventFourInARowScore;
            if (grid[y1][x1] == opponent) {
                countOpponentB++; x1--; y1--;
                if (countOpponentB == 4) score += protectionScore;
                if (x1 == 0 || y == 0) score += endOfGridScore;
                score += hostileNeighborScore;
            }
            else break;
        }
        if (countPlayerAndBlanksA < 5 || countPlayerAndBlanksB < 5) score += unableToBuildFiveScore;
        else score += ableToBuildFiveScore;
        if (countPlayerA == 4 || countPlayerB == 4) score += fourInARowScore;
        if (x == size-1 || x == 0) score += endOfGridScore;
        if (y == size-1 || y == 0) score += endOfGridScore;
        return score;
    }
}
