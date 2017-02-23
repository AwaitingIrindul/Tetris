# Tetris
A School project of a tetris using JavaFX.

## Artificial Intelligence

Click on the button Run AI to see the artificial intelligence play the game.

This AI will generate every available moves and pick the best according to four heuristics.  
To evaluate the grid, it takes in account the height of the grid (by summing the height of every column),  
the number of lines this move would clear, the number of holes (empty space covered by at least one block)  
and the difference of height of each columns.

To each of this paramters is associated a weight.  
So the final formula is :  
a * height + b * numberOfLines + c * holes + d * differenceHeight.

As we want to make the height, the holes and the diffrence of height small for the grid to be correct,  
we might except a, c, and d to be negative.

To find good values for a, b, c and d, different methods can be used. Personally, I used a genetic algorithm. 

### Genetic Algorithm 

The genetic algotrithm used is define with an operator crossover, an operator mutation and a selection.

#### Crossover
