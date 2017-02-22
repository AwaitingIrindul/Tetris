package ModelBoard.Pieces;

import ModelBoard.Board.Grid;
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

    public Block(Block b){
        this.width = b.width;
        this.height = b.height;
        this.pos = new Position[height][width];
        this.position = b.position;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.pos[i][j] = new Position(b.pos[i][j]);
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

    public boolean checkMovement(Direction d, Grid g){

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Position tmp = d.getNewPosition(getPosition(i, j));
                int x = tmp.getX();
                int y = tmp.getY();
                if(g.isInRange(x, y)){
                    if(!g.isEmpty(x, y)){
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
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


    public boolean checkRotation(Grid g, Position origin) {
        Position current;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                current = getPosition(i, j);

                    /*
                        We calculate the new points according to a rotation of (-PI/2)
                        The new coordinates are computed with this formula :
                        newX  = originX + (currentX - originX) * cos(-pi/2) - (currentY - originY) * sin(-pi/2)
                        newY = originY + (currentX - originX) * sin(-pi/2) - (currentY - originY) * cos(-pi/2)

                        As we know sin(-pi/2) = 1 and cos(-pi/2)  = 0
                        So we simplify  and get the formula below
                     */
                int newx = origin.getX() + (current.getY() - origin.getY());
                int newy = origin.getY() - current.getX() + origin.getX();

                if(g.isInRange(newx, newy)){
                    if(!g.isEmpty(newx, newy)){
                        return false;
                    }
                } else {
                    return  false;
                }
            }
        }

        return true;

    }

    public void rotateClockWise(Position origin) {
        Position current;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                current = getPosition(i, j);

                    /*
                        We calculate the new points according to a rotation of (-PI/2)
                        The new coordinates are computed with this formula :
                        newX  = originX + (currentX - originX) * cos(-pi/2) - (currentY - originY) * sin(-pi/2)
                        newY = originY + (currentX - originX) * sin(-pi/2) - (currentY - originY) * cos(-pi/2)

                        As we know sin(-pi/2) = 1 and cos(-pi/2)  = 0
                        So we simplify  and get the formula below
                     */
                int newx = origin.getX() + (current.getY() - origin.getY());
                int newy = origin.getY() - current.getX() + origin.getX();

                pos[i][j].setXY(newx, newy);

            }
        }
    }
}
