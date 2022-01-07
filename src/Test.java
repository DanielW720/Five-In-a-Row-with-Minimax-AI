/**
 * This class is made in order for me to be able to play around with different scenarios and check that
 * the computer player makes the correct call. Assuming minimax depth is set to 1, each scenario needs
 * one grid for mPly and one grid for mPly-1. Ply always correspond to the number of 'X's and 'O's on the
 * grid.
 */

//static char[][] gridPlyZero = {
//        {'*', '*', '*', '*', '*', '*'},
//        {'*', '*', '*', '*', '*', '*'},
//        {'*', '*', '*', '*', '*', '*'},
//        {'*', '*', '*', '*', '*', '*'},
//        {'*', '*', '*', '*', '*', '*'},
//        {'*', '*', '*', '*', '*', '*'}
//        };

public class Test {

     // ---------- Tests for depth = 1 (Standard tests of the goal function) ----------

     static class FourInARow {

          static int size = 6;
          static int previousPly = 6;
          static int currentPly = 7;

          static char[][] gridPreviousPly = {
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', 'O', '*', 'O', 'O', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', 'X', 'X', '*', '*', '*'},
                  {'X', '*', '*', '*', '*', '*'}
          };

          static char[][] gridCurrentPly = {
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', 'O', '*', 'O', 'O', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', 'X', 'X', 'X', '*', '*'},
                  {'X', '*', '*', '*', '*', '*'}
          };
     }

     static class FiveInARow {

          static int size = 6;
          static int previousPly = 8;
          static int currentPly = 9;

          static char[][] gridPreviousPly = {
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', 'O', '*', 'O', 'O', 'O'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', 'X', 'X', 'X', '*', '*'},
                  {'X', '*', '*', '*', '*', '*'}
          };

          static char[][] gridCurrentPly = {
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', 'O', '*', 'O', 'O', 'O'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', 'X', 'X', 'X', 'X', '*'},
                  {'X', '*', '*', '*', '*', '*'}
          };
     }

     static class DoNotBuildTowardsLeft {

          static int size = 6;
          static int previousPly = 4;
          static int currentPly = 5;

          /*
          Without rule (6) (see ScoreEvaluation.java), 'O' would continue to build towards the left on row 2. This
          class tests rule (6).
           */

          static char[][] gridPreviousPly = {
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', 'O', 'O', 'X', '*'}, // Row #1. Unable to build five in a row towards left
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', 'X'}
          };

          static char[][] gridCurrentPly = {
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', 'O', 'O', 'X', '*'}, // Row #1. Unable to build five in a row towards left
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'},
                  {'*', '*', 'X', '*', '*', 'X'}
          };

          static void explanation() {
               System.out.println("\nTest is successful if player 'O' (computer) plays " +
                       "anything but (0, 1) or (1, 1).\nThis is a test of rule (6).\n");
          }
     }

     static class DoNotBuildTowardsLeft2 {

          static int size = 7;
          static int previousPly = 14;
          static int currentPly = 15;

          // Here there may not be a better play actually. Maybe there is, and if so, it might be found by setting
          // minimax depth > 1.

          static char[][] gridPreviousPly = {
                  {'*', 'O', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', 'X', '*', '*', '*'},
                  {'*', 'X', 'X', 'O', 'X', 'X', '*'},
                  {'*', 'O', 'O', 'O', 'X', '*', '*'}, // Row #1. Unable to build five in a row towards left
                  {'*', '*', '*', 'X', 'O', '*', '*'},
                  {'*', '*', 'O', '*', 'X', '*', '*'},
                  {'*', '*', '*', '*', '*', '*', '*'}
          };

          static char[][] gridCurrentPly = {
                  {'*', 'O', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', 'X', '*', '*', '*'},
                  {'*', 'X', 'X', 'O', 'X', 'X', '*'},
                  {'*', 'O', 'O', 'O', 'X', '*', '*'}, // Row #1. Unable to build five in a row towards left
                  {'*', '*', '*', 'X', 'O', '*', '*'},
                  {'*', '*', 'O', '*', 'X', '*', '*'},
                  {'*', '*', '*', '*', '*', '*', '*'}
          };

          static void explanation() {
               System.out.println("\nTest is successful if player 'O' (computer) plays " +
                       "anything but (0, 1) or (1, 1).\nThis is a test of rule (6).\n");
          }
     }

     static class FiveInARowDiagonal {

          static int size = 6;
          static int previousPly = 8;
          static int currentPly = 9;

          // Here there may not be a better play actually. Maybe there is, and if so, it might be found by setting
          // minimax depth > 1.

          static char[][] gridPreviousPly = {
                  {'*', '*', 'X', 'X', '*', '*'}, // (0, 0) should not receive maximumScore
                  {'*', 'O', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', '*', '*'}, // (2, 2) should receive maximumScore
                  {'*', '*', '*', 'O', '*', '*'},
                  {'*', '*', '*', '*', 'O', '*'},
                  {'*', 'X', 'X', '*', '*', 'O'}
          };

          static char[][] gridCurrentPly = {
                  {'*', '*', 'X', 'X', '*', '*'}, // (0, 0) should not receive maximumScore
                  {'*', 'O', '*', '*', '*', 'X'},
                  {'*', '*', '*', '*', '*', '*'}, // (2, 2) should receive maximumScore
                  {'*', '*', '*', 'O', '*', '*'},
                  {'*', '*', '*', '*', 'O', '*'},
                  {'*', 'X', 'X', '*', '*', 'O'}
          };

          static void explanation() {
               System.out.println("\nTesting if onlyPlayerSymbolsVisitedB (boolean) in ScoreEvaluation.java is\n" +
                       "successfully doing what it is supposed to. If (2, 2) is selected, test has passed. If \n" +
                       "(0, 0) (or anything other than (2, 2) is selected, test has failed.");
          }
     }

     static class GameIsATie {

          static int size = 6;
          static int currentPly = 23;

          static char[][] gridCurrentPly = {
                  {'X', 'X', '*', 'X', 'O', '*'},
                  {'*', 'O', 'X', 'X', '*', 'X'}, // 'X' makes a move
                  {'O', '*', 'O', 'O', 'X', 'O'},
                  {'X', '*', 'X', 'O', '*', 'O'},
                  {'*', 'O', '*', 'X', 'O', '*'},
                  {'*', 'X', '*', '*', 'O', 'X'}
          };

          static void explanation() {
               System.out.println("\nTesting if grid is win-able for any player. If gameIsATie() returns true" +
                       "\nfor this example, test has passed.\n");
          }
     }

     // ---------- Tests for depth >= 2 ----------

     static class PreventFiveInARow {

          static int size = 6;
          static int previousPly = 8;
          static int currentPly = 9;

          static char[][] gridPreviousPly = {
                  {'O', '*', '*', '*', '*', '*'}, // X can win here (5, 0)
                  {'*', '*', '*', '*', 'X', '*'},
                  {'*', '*', '*', 'X', 'O', '*'},
                  {'*', '*', 'X', '*', 'O', '*'},
                  {'*', 'X', '*', '*', '*', '*'},
                  {'O', '*', '*', '*', '*', '*'} // X can win at (0, 5)
          };

          static char[][] gridCurrentPly = {
                  {'O', '*', '*', '*', '*', '*'},
                  {'*', '*', '*', '*', 'X', '*'},
                  {'*', '*', '*', 'X', 'O', '*'},
                  {'*', '*', 'X', '*', 'O', '*'},
                  {'*', 'X', '*', '*', 'X', '*'}, // X makes dumb move
                  {'O', '*', '*', '*', '*', '*'}
          };

          static void explanation() {
               System.out.println("\nIf MINIMAX_DEPTH = 1, this test is no problem. But when depth is \n" +
                       "set to 2 or higher, what happens? \n" +
                       "Test is passed if 'O' tries to block 'X' on the diagonal.\n");
          }
     }

     static class PreventFiveInARow2 {

          static int size = 6;
          static int previousPly = 8;
          static int currentPly = 9;

          static char[][] gridPreviousPly = {
                  {'O', '*', '*', '*', '*', '*'}, // X can win here (5, 0)
                  {'*', '*', '*', '*', 'X', '*'},
                  {'*', '*', '*', 'X', 'O', '*'},
                  {'*', '*', 'X', '*', 'O', '*'},
                  {'*', 'X', '*', '*', '*', '*'},
                  {'O', '*', '*', '*', '*', '*'} // X can win at (0, 5)
          };

          static char[][] gridCurrentPly = {
                  {'O', '*', '*', '*', '*', '*'},
                  {'O', '*', '*', '*', 'X', '*'},
                  {'*', '*', '*', 'X', 'O', '*'},
                  {'*', '*', 'X', '*', 'O', '*'},
                  {'*', 'X', '*', '*', 'X', '*'}, // X makes dumb move
                  {'*', '*', '*', '*', '*', '*'}
          };

          static void explanation() {
               System.out.println("\nIf MINIMAX_DEPTH = 1, this test is no problem. But when depth is \n" +
                       "set to 2 or higher, what happens? \n" +
                       "Test is passed if 'O' tries to block 'X' on the diagonal.\n");
          }
     }
}
