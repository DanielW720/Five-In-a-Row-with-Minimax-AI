# Five In a Row with Minimax AI

In this project I have written a program for playing Gomoku, aka. Five in a row, with the computer as your opponent. 
The AI opponent uses the popular Minimax algorithm in order to find the best possible move.

The biggest challenges of this project was 

1. To think of a way to evaluate the score of a specific move on a specific game position/grid
2. To implement the evaluation function from (1)
3. To understand how the underlying data structure of the whole program must be built.

The Minimax algorithm uses a tree in order to recursively find the best possible move, given that its opponent is 
playing at their absolute best. Hence, each move is a node is the tree, and each node has a number of children, 
implicitly pointing to each one of its children (using indexing), and so on. 

To read more about the logic of the evaluation function, see explanation in ScoreEvaluation.java.
