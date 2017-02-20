package ModelTetris.Player;

import ModelTetris.Tetris;

/**
 * Created by Irindul on 20/02/2017.
 */
public class Evaluator {

    private double a;
    private double b;
    private double c;
    private double d;



    public Evaluator(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public double evaluate(Tetris t){
        return a * t.sumHeight() + b * t.rowsToSweep() * c * t.holes() + d * t.bumpiness();
    }
}
