package Model.ModelTetris.Player;

import Model.ModelTetris.Tetris;

import java.util.Random;

/**
 * Created by Irindul on 20/02/2017.
 */
public class Evaluator {

    private double a;
    private double b;
    private double c;
    private double d;

    private double score;

    public Evaluator(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        score = 0;
    }

    public Evaluator(Evaluator other){
        this(other.a, other.b, other.c, other.d);
    }

    public double evaluate(Tetris t){
        return a*t.sumHeight() + b* t.rowsToSweep() + c*t.holes() + d*t.bumpiness();
    }

    public static Evaluator getRandomEvaluator(){
        double min = -1;
        double max = 1;

        Random rd = new Random();

        double a = min + (rd.nextDouble() * (max - min));
        double b = min + (rd.nextDouble() * (max - min));
        double c = min + (rd.nextDouble() * (max - min));
        double d = min + (rd.nextDouble() * (max - min));

        return new Evaluator(a, b, c, d);


    }

    public void operatorTime(double score){

        a *= score;
        b *= score;
        c *= score;
        d *= score;
    }

    public void operatorAdd(Evaluator other){
        this.a += other.a;
        this.b += other.b;
        this.c += other.c;
        this.d += other.d;

    }


    public void setScore(double score) {
        this.score = score;
    }

    public void normalization(){
        double norme = norme();

        a /= norme;
        b /= norme;
        c /= norme;
        d /= norme;
    }

    public double norme(){
        return Math.sqrt( a*a  + b*b + c*c + d*d );
    }

    public void mutation(){
        Random rd = new Random();
        int component = rd.nextInt(4);

        double adj = -0.2 + (rd.nextDouble() * (0.4));
        switch (component){
            case 0:
                a += adj;
                break;
            case 1:
                b += adj;
                break;
            case 2:
                c += adj;
                break;
            case 3:
                d += adj;
                break;
            default:
                break;
        }

    }

    public  void display(){
        System.out.println(a +", " + b + ", " + c + ", " + d);
        


    }
    public static Evaluator crossover(Evaluator p1, Evaluator p2){
        Evaluator child = new Evaluator(p1);
        Evaluator child2 = new Evaluator(p2);


        child.operatorTime(p1.score + 1);
        child2.operatorTime(p2.score + 1);

        child.operatorAdd(child2);
        //child.operatorTime(1/(p1.score + p2.score));

        return child;
    }
}
