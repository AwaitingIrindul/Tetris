package ModelBoard.Position;

import java.util.Objects;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        setX(x);
        setY(y);
    }

    public Position(Position pos){
        this(pos.getX(), pos.getY());
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void setXY(int x, int y){
        setX(x);
        setY(y);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        return obj instanceof Position && this.equals((Position) obj);

    }

    private boolean equals(Position pos) {
        return (pos.x == this.x && pos.y == this.y);
    }
}
