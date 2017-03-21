package View.ViewBoard;

import ModelBoard.Pieces.Piece;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irindul on 16/02/2017.
 */
public class PieceView {

    private Color color;
    private Piece piece;
    private double tilesize;
    private int offset;

    private List<Rectangle> square;

    public PieceView(Color color, Piece block, double tilesize, int offset) {
        this.color = color;
        this.piece = block;
        this.tilesize = tilesize;
        this.offset = offset;
        square = new ArrayList<>();

        piece.getPositions().stream()
                .map(position -> {
                    Rectangle rect = new Rectangle(tilesize, tilesize, color);
                    rect.relocate(position.getY()*tilesize, position.getX()*tilesize);
                    return rect;
                })
                .forEach(rectangle -> square.add(rectangle));

    }



    public synchronized void update(){

        //square.forEach(rectangle -> square.remove(rectangle));
        square.clear();
        square = new ArrayList<>();

        piece.getPositions().stream()
                .map(position -> {
                    Rectangle rect = new Rectangle(tilesize, tilesize, color);
                    rect.relocate(position.getY()*tilesize, (position.getX()-offset)*tilesize);
                    rect.setStroke(Color.BLACK);
                    return rect;
                })
                .forEach(rectangle -> square.add(rectangle));
    }


    public void drawNext(GraphicsContext g){
        g.setFill(color);

        int minY = piece.getMinimumY();

        piece.getPositions().forEach(pos -> {
            int x, y;
            x = pos.getY();
            y = pos.getX();

            x-=minY;
            y+=1;
            g.fillRect(x*tilesize, y*tilesize, tilesize, tilesize);
        });

    }

    public List<Rectangle> getSquare() {
        return square;
    }

    // TODO: 15/03/2017 add offset in parameter (-2 for tetris, 0 for blocks ect..)
    public void draw(Color c, Color s, GraphicsContext g){
        g.setFill(c);
        g.setStroke(s);

        piece.getPositions().forEach(pos -> {
            int x, y;
            x = pos.getY();
            y = pos.getX(); //TODO Refactor with normal sense;
            y-=offset;
            g.strokeRect(x*tilesize, y*tilesize, tilesize, tilesize);
            g.fillRect(x*tilesize, y*tilesize, tilesize, tilesize);
        });

    }

    public Piece getPiece() {
        return piece;
    }

    public void undraw(GraphicsContext g) {

       piece.getPositions().forEach(pos -> {
           int x, y;
           x = pos.getY();
           y = pos.getX();

           y-=offset;

           g.clearRect(x*tilesize, y*tilesize, tilesize+0.5, tilesize+0.5);
       });
    }
}
