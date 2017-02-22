package ModelBoard.Board;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Grid {
    private int height;
    private int width;

    private Tile[][] tiles;

    public Grid(int height, int width) {
        this.height = height;
        this.width = width;

        tiles = new Tile[height][width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public Grid(Grid g){
        this.height = g.height;
        this.width = g.width;
        this.tiles = new Tile[height][width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                tiles[i][j] = new Tile(g.tiles[i][j]);
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

    public boolean isEmptyRow(int i){
        for (int j = 0; j < width; j++) {
            if(!isEmpty(i, j)){
                return false;
            }
        }

        return true;

    }

    public int sweep(){
        int distance = 0;

        for (int i = height-1; i >= 0; i--) {
            if(isFullRow(i)){
                distance++;
                for (int j = 0; j < width; j++) {
                    removeFromTile(i, j);
                }
            } else if(distance > 0){
                for (int j = 0; j < width; j++) {
                    if(isEmpty(i, j)){
                        removeFromTile(i + distance, j);
                    } else {
                        placeOnTile(i + distance, j);
                    }

                    removeFromTile(i, j);
                }
            }
        }

        return distance;
    }

    public int rowsToSweep(){
        int rows = 0;
        for (int i = 0; i < height; i++) {
            if(isFullRow(i)){
                rows++;
            }
        }

        return rows;
    }




    public boolean isFullRow(int i){
        for (int j = 0; j < width; j++) {
            if(this.isEmpty(i, j)){
                return false;
            }
        }

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

    @Deprecated
    public void display(){
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if(this.isEmpty(i, j)){
                    System.out.print("0 ");
                } else {
                    System.out.print("1 ");

                }
            }
            System.out.println();
        }
        
        System.out.println();
        
    }
}
