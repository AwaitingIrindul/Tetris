package ModelBoard.Pieces;

import ModelBoard.Direction;
import ModelBoard.Position.Position;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Block {
    private int width;
    private int height;

    private Position position;


    private Position[][] pos;
    public Block(int height, int width) {
        this.width = width;
        this.height = height;
        pos = new Position[height][width];
        position = new Position(0, 0);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pos[i][j] = new Position(i, j);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {

        int x = position.getX() - this.position.getX();
        int y = position.getY() - this.position.getY();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pos[i][j].setX(pos[i][j].getX() + x);
                pos[i][j].setY(pos[i][j].getY() + y);
            }
        }

        this.position = position;
    }

    public void setPosition(int i, int j, Position position){
        pos[i][j].setXY(position.getX(), position.getY());
    }

    public Position[][] getPos() {
        return pos;
    }

    public Position getPosition(int i, int j){
        return pos[i][j];
    }

    public void move(Direction d){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = d.getNewPosition(pos[i][j]).getX();
                int y = d.getNewPosition(pos[i][j]).getY();
                pos[i][j].setXY(x, y);
            }
        }
    }

    public boolean isInBlock(Position position){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(pos[i][j].equals(position)){
                    return true;
                }
            }
        }

        return false;
    }


}
