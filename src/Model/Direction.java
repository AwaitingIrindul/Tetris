package Model;

import Model.Position.Position;
import javafx.geometry.Pos;

/**
 * Created by Irindul on 09/02/2017.
 */
public enum Direction {
    /**
     * Direction will help indicate a futur position
     * They each refer to a certain position on an orthonormal grid.
     * We will set the origin (0, 0) on the top left corner.
     */
    UP {
        @Override
        public Position getNewPosition(Position pos) {
            return new Position(pos.getX() - 1, pos.getY());
        }
    }, /** refer to minus one in the X axes */

    LEFT {
                @Override
                public Position getNewPosition(Position pos) {
                    return new Position(pos.getX(), pos.getY() - 1);
                }
            }, /** refer to minus one in the Y axes */

    DOWN {
                @Override
                public Position getNewPosition(Position pos) {
                    return new Position(pos.getX() + 1, pos.getY());
                }
            }, /** refer to plus one ine the X axes */

    RIGHT {
                @Override
                public Position getNewPosition(Position pos) {
                    return new Position(pos.getX(), pos.getY() + 1);
                }
            }; /** refer to plus one in the Y axes*/


    public abstract Position getNewPosition(Position pos);

}
