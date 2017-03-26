package Model.ModelBoard.Position;

import java.io.Serializable;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * Created by Irindul on 09/02/2017.
 * Class position with 2 coordinates
 */
public class Position implements Serializable{

    private int x;
    private int y;

    public Position(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    private void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    private void setY(int y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj == this || obj instanceof Position && this.equals((Position) obj));

    }

    @Override
    public String toString() {
        String position = "{ \"x\": \"" + x +"\", \"y\": \"" + y +"\"}";
        return position.replaceAll("\\s+", "");
    }


    public static Position fromJson(String json){
        CharacterIterator iterator = new StringCharacterIterator(json);
        iterator.next();
        char c;

        //we remove {"x"
        while(iterator.next() != ':');
        
        iterator.next();
        c = iterator.next();
        //c = x value
        
        StringBuilder sb = new StringBuilder();
        while(c != '\"'){
            sb.append(c);
            c = iterator.next();
        }
        //sb contain integer value of x

        int x = Integer.parseInt(sb.toString());

        //We remove ","y"
        while(iterator.next() != ':');

        iterator.next();
        c = iterator.next();
        //c = y value

        sb = new StringBuilder();
        while(c != '\"'){
            sb.append(c);
            c = iterator.next();
        }

        int y = Integer.parseInt(sb.toString());


        return new Position(x, y);



    }

    private boolean equals(Position pos) {
        return (pos.x == this.x && pos.y == this.y);
    }
}
