package ModelBoard.Pieces;

import ModelBoard.Position.Position;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Block {
    private int width;
    private int height;

    private Position position;
    public Block(int width, int height) {
        this.width = width;
        this.height = height;
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
        this.position = position;
    }
}
