/**
 * Do not run this program in Command prompt as it doesn't support ANSI coloring.
 */

public class Main {
    public static void main(String[] args) {
        int GRID_SIZE = 6;
        int MINIMAX_DEPTH = 3;

        Grid grid = new Grid(GRID_SIZE, MINIMAX_DEPTH);
        System.out.println("\nNew game created\n");
        grid.printGrid(grid.getPly(), true);

        boolean fiveInARow = false;
        while (!fiveInARow) {
            System.out.println("Current ply: " + grid.getPly());
            grid.newPlayerMove();
            grid.printGrid(grid.getPly()-1, false); // ply has increased by 1
            fiveInARow = grid.fiveInARow(grid.getPly() - 1);
            if (fiveInARow) {
                System.out.println("You won!!!");
                break;
            }
            else if (grid.gameIsATie(grid.getPly()-1)) {
                System.out.println("Game is tied");
                break;
            }
            System.out.println("Current ply: " + grid.getPly());
            grid.newComputerMove();
            grid.printGrid(grid.getPly()-1, false);
            fiveInARow = grid.fiveInARow(grid.getPly() - 1); // ply has increased by 1
            if (fiveInARow) System.out.println("Computer won...");
            else if (grid.gameIsATie(grid.getPly()-1)) {
                System.out.println("Game is tied");
                break;
            }
        }

//         Testing
//        System.out.println("TESTING FIVE IN A ROW");
//        int GRID_SIZE = Test.FiveInARow.size;
//        int MINIMAX_DEPTH = 1;
//
//        Grid grid = new Grid(GRID_SIZE, MINIMAX_DEPTH);
//        // Minimax depth set to 1. This requires that previous grid exists.
//        grid.setPly(Test.FiveInARow.previousPly);
//        grid.setGrid(Test.FiveInARow.gridPreviousPly);
//
//        grid.setGrid(Test.FiveInARow.gridCurrentPly); // X makes a move
//        grid.setPly(Test.FiveInARow.currentPly); // Ply increases after a move
//        grid.printGrid(grid.getPly()-1, false); // Ply has increased
//
//        System.out.println("Current ply: " + grid.getPly());
//        grid.newComputerMove(); // Computer makes a move
//        grid.printGrid(grid.getPly()-1, false); // Ply has increased
//
//        System.out.println("\nTESTING FOUR IN A ROW");
//        GRID_SIZE = Test.FourInARow.size;
//        MINIMAX_DEPTH = 1;
//
//        Grid grid2 = new Grid(GRID_SIZE, MINIMAX_DEPTH);
//        // Minimax depth set to 1. This requires that previous grid exists.
//        grid2.setPly(Test.FourInARow.previousPly);
//        grid2.setGrid(Test.FourInARow.gridPreviousPly);
//
//        grid2.setGrid(Test.FourInARow.gridCurrentPly); // X makes a move
//        grid2.setPly(Test.FourInARow.currentPly); // Ply increases after a move
//        grid2.printGrid(grid2.getPly()-1, false); // Ply has increased
//
//        System.out.println("Current ply: " + grid2.getPly());
//        grid2.newComputerMove(); // Computer makes a move
//        grid2.printGrid(grid2.getPly()-1, false); // Ply has increased
//
//        System.out.println("\nTESTING DoNotBuildTowardsLeft");
//        Test.DoNotBuildTowardsLeft.explanation();
//        int GRID_SIZE = Test.DoNotBuildTowardsLeft.size;
//        int MINIMAX_DEPTH = 1;
//
//        Grid grid3 = new Grid(GRID_SIZE, MINIMAX_DEPTH);
//        // Minimax depth set to 1. This requires that previous grid exists.
//        grid3.setPly(Test.DoNotBuildTowardsLeft.previousPly);
//        grid3.setGrid(Test.DoNotBuildTowardsLeft.gridPreviousPly);
//
//        grid3.setGrid(Test.DoNotBuildTowardsLeft.gridCurrentPly); // X makes a move
//        grid3.setPly(Test.DoNotBuildTowardsLeft.currentPly); // Ply increases after a move
//        grid3.printGrid(grid3.getPly()-1, false); // Ply has increased
//
//        System.out.println("Current ply: " + grid3.getPly());
//        grid3.newComputerMove(); // Computer makes a move
//        grid3.printGrid(grid3.getPly()-1, false); // Ply has increased
//
//        System.out.println("\nTESTING FiveInARowDiagonal");
//        Test.FiveInARowDiagonal.explanation();
//        int GRID_SIZE = Test.FiveInARowDiagonal.size;
//        int MINIMAX_DEPTH = 1;
//
//        Grid grid4 = new Grid(GRID_SIZE, MINIMAX_DEPTH);
//        // Minimax depth set to 1. This requires that previous grid exists.
//        grid4.setPly(Test.FiveInARowDiagonal.previousPly);
//        grid4.setGrid(Test.FiveInARowDiagonal.gridPreviousPly);
//
//        grid4.setGrid(Test.FiveInARowDiagonal.gridCurrentPly); // X makes a move
//        grid4.setPly(Test.FiveInARowDiagonal.currentPly); // Ply increases after a move
//        grid4.printGrid(grid4.getPly()-1, false); // Ply has increased
//
//        System.out.println("Current ply: " + grid4.getPly());
//        grid4.newComputerMove(); // Computer makes a move
//        grid4.printGrid(grid4.getPly()-1, false); // Ply has increased
//
//        System.out.println("\nTESTING GameIsATie");
//        Test.GameIsATie.explanation();
//        int GRID_SIZE = Test.GameIsATie.size;
//        int MINIMAX_DEPTH = 1;
//        Grid grid5= new Grid(GRID_SIZE, MINIMAX_DEPTH);
//        grid5.setPly(Test.GameIsATie.currentPly);
//        grid5.setGrid(Test.GameIsATie.gridCurrentPly);
//        grid5.printGrid(grid5.getPly(), false); // Ply has increased
//        System.out.print("Checking if game is a tie...\ngameIsATie(grid5.getPly()): ");
//        System.out.print(grid5.gameIsATie(grid5.getGrid(grid5.getPly())));
//        System.out.println("\nTESTING PreventFiveInARow");
//        Test.PreventFiveInARow2.explanation();
//        int GRID_SIZE = Test.PreventFiveInARow2.size;
//        int MINIMAX_DEPTH = 2;
//
//        Grid grid6 = new Grid(GRID_SIZE, MINIMAX_DEPTH);
//        // Minimax requires at least 1 existing grid, i.e., ply >= 1.
//        grid6.setPly(Test.PreventFiveInARow2.previousPly);
//        grid6.setGrid(Test.PreventFiveInARow2.gridPreviousPly);
//
//        grid6.setGrid(Test.PreventFiveInARow2.gridCurrentPly); // X makes a move
//        grid6.setPly(Test.PreventFiveInARow2.currentPly); // Ply increases after a move
//        grid6.printGrid(grid6.getPly()-1, false); // Ply has increased
//
//        System.out.println("Current ply: " + grid6.getPly());
//        grid6.newComputerMove(); // Computer makes a move
//        grid6.printGrid(grid6.getPly()-1, false); // Ply has increased
    }
}
