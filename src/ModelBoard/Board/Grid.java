package ModelBoard.Board;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Grid {
    private int height;
    private int width;

    private Tile[][] tiles;

    public Grid(int height, int weight) {
        this.height = height;
        this.width = weight;

        tiles = new Tile[height][weight];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public void throwOutOfBound(){
        throw new IndexOutOfBoundsException("Please make sure i and j are in the range of the grid");
    }

    public boolean isEmpty(int i, int j){
        if(isInRange(i, j))
            return tiles[i][j].isEmpty();
        else
            throwOutOfBound();

        return false;
    }

    public boolean isInRange(int i, int j){
        if( i < 0 || j < 0 || i >= height || j >= width)
            return false;
        else
            return true;
    }

    public void placeOnTile(int i, int j){
        if(isInRange(i, j)){
            tiles[i][j].setEmpty(false);
        } else
            throwOutOfBound();
    }

    public void removeFromTile(int i, int j){
        if(isInRange(i, j)){
            tiles[i][j].setEmpty(true);
        } else
            throwOutOfBound();
    }
}
