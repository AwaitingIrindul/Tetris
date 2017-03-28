package Model.ModelBoard.Pieces;

import Model.ModelBoard.Direction;
import Model.ModelBoard.Orientation;
import Model.ModelBoard.Position.Position;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Irindul on 09/02/2017.
 * Class piece contains piece definition.
 * This class handle self-movement
 */
public class Piece{

    private Position position;
    private int height, width;
    private boolean positions[][];
    private boolean hasBeenChanged = false;
    private Orientation orientation;

    public Piece(int height, int width) {
        position = new Position(0, 0);
        this.height = height;
        this.width = width;
        positions = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                positions[i][j] = false;
            }
        }
    }

    public Piece(String[][] scheme, Position startingPosition, Orientation orientation){
        this(scheme.length, scheme[0].length);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                positions[i][j] = scheme[i][j].equals("1");
            }
        }
        this.orientation = orientation;
        position = startingPosition;

    }

    public Piece(Piece b){
        this.position = b.position;
        height = b.height;
        width = b.width;
        positions = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(b.positions[i], 0, positions[i], 0, width);
        }
        this.orientation = b.getOrientation();
    }


    public void removePosition(Position pos){
        positions[pos.getX() - position.getX()][pos.getY() - position.getY()] = false;
        if(!onlyFalse())
            setHasChanged(true);
    }


    private boolean[][] rotate(){
        int M = positions.length;
        int N = positions[0].length;
        boolean rotated[][] = new boolean[N][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                rotated[j][M-1-i] = positions[i][j];
            }
        }
        return rotated;
    }

    public void rotateClockWise(){

        positions = rotate();
        height = positions.length;
        width = positions[0].length;
    }

    public void move(Direction d){
        position = d.getNewPosition(position);
    }

    public List<Position> getPositions(){
        List<Position> positionsList = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(positions[i][j])
                    positionsList.add(new Position(position.getX()+i, position.getY()+j));
            }
        }

        return positionsList;
    }

    public List<Position> getRotations(){
        List<Position> rotationList = new ArrayList<>();
        boolean[][] rotated = rotate();
        for (int i = 0; i < rotated.length; i++) {
            for (int j = 0; j < rotated[0].length; j++) {
                if(rotated[i][j])
                    rotationList.add(new Position(position.getX()+i, position.getY()+j));
            }
        }

        return rotationList;
    }


    public void resolveHoles() {
        setHasChanged(false);
        for (int i = 0; i < height; i++) {
            applyGravity();
        }
    }

    private void applyGravity(){
        for (int r = height-2; r >= 0; r--) {
            for (int c = 0; c < width; c++) {
                if(positions[r][c] && !positions[r+1][c]){
                    positions[r][c] = false;
                    positions[r+1][c] = true;
                }
            }
        }
    }

    public boolean onlyFalse(){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(positions[i][j])
                    return false;
            }
        }
        return true;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public boolean hasBeenChanged(){
        return hasBeenChanged;
    }

    private void setHasChanged(boolean changed){
        hasBeenChanged = changed;
    }

    public Orientation getOrientation(){
        return orientation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\"");
        sb.append("position\":");
        sb.append(position.toString());
        sb.append(", ");
        sb.append("\"height\": \"");
        sb.append(height);
        sb.append("\", ");
        sb.append("\"width\": \"");
        sb.append(width);
        sb.append("\", ");
        sb.append("\"positions\": [");
        for (int i = 0; i < height; i++) {
            sb.append("[");
            for (int j = 0; j < width; j++) {
                sb.append("{");
                sb.append("\"position\":\"");
                sb.append(positions[i][j]);
                sb.append("\"}");
                if(j + 1 < width)
                    sb.append(",");
            }
            sb.append("]");
            if(i + 1 < height)
                sb.append(", ");
        }
        sb.append("]");
        sb.append(",\"orientation\": \"");
        sb.append(orientation);
        sb.append("\"");
        sb.append("}");
        return sb.toString().replaceAll("\\s+", "");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(obj == null)
            return this == null;
        if(this == null)
            return false;

        if(obj instanceof Piece){
            Piece other = (Piece) obj;
            if (this.height == other.height) {
                if(this.width == other.width){
                    boolean ok = true;
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            if (positions[i][j] != positions[i][j])
                                ok = false;
                        }
                    }
                    return (super.equals(obj) || (ok && position.equals(other.position) && orientation.equals(other.orientation)));

                }
            }
        }
        return super.equals(obj);
    }

    /*@Override
    public int hashCode() {
        int hash = position.hashCode();
        hash += Integer.hashCode(height);
        hash += Integer.hashCode(width);
        hash += orientation.hashCode();
        return  Objects.hash();
       // return Integer.hashCode(hash);
    }*/

    public static Piece fromJson(String json){
        CharacterIterator iterator = new StringCharacterIterator(json);
        char c;
        //Removing {"position"
        while(iterator.next() != ':');

        StringBuilder positionJson = new StringBuilder();
        //We extract the json position
        while(( c =iterator.next()) != '}'){
            positionJson.append(c);
        }
        positionJson.append(c);
        Position position = Position.fromJson(positionJson.toString());

        //Removing ,"height"
        while(iterator.next() != ':');
        
        StringBuilder sb = new StringBuilder();
        
        //Removing :
        iterator.next();
                
        while ( (c = iterator.next()) != '\"'){
            sb.append(c);
        }

        int height = Integer.parseInt(sb.toString());
        

        //Removing ","width"
        while(iterator.next() != ':');
        iterator.next();
        sb = new StringBuilder();
        while ( (c = iterator.next()) != '\"'){
            sb.append(c);
        }

        int width = Integer.parseInt(sb.toString());
        
        //Removing ","position":
        while(iterator.next() != '[');

        //Removing [
        iterator.next();


        boolean[][] positions = new boolean[height][width];

       //TO REPEAT
        for (int i = 0; i < height; i++) {
            
            //Removing [
            //iterator.next();
            for (int j = 0; j < width; j++) {
                
                while(iterator.next() != ':');
                iterator.next();
                sb = new StringBuilder();
                while((c = iterator.next() )!= '\"'){
                    sb.append(c);
                }
                switch (sb.toString()){
                    case "true":
                        positions[i][j] = true;
                        break;
                    case "false":
                        positions[i][j] = false;
                        break;
                    default:
                        break;

                }
            }
        }


        while(iterator.next() != ':');
        iterator.next();
        sb = new StringBuilder();
        while((c = iterator.next()) != '\"'){
            sb.append(c);
        }
        Orientation orientation;
        switch (sb.toString()){
            case "HORIZONTAL":
                orientation= Orientation.HORIZONTAL;
                break;
            case "VERTICAL":
                orientation = Orientation.VERTICAL;
                break;
            default:
                orientation = Orientation.HORIZONTAL;
                break;
        }
            
        Piece piece = new Piece(height, width);
        piece.positions = positions;
        piece.position = position;
        piece.orientation = orientation;
        
        return piece;
    }


}
