import ModelBoard.Board.Board;
import ModelBoard.Direction;
import ModelBoard.Pieces.Piece;
import ModelBoard.Position.Position;
import ModelTetris.TetrisBlocks;
import ModelTetris.TetrisPieceFactory;
import View.Menu;
import View.PuzzleGame;
import View.TetrisGame;

import java.io.*;
import java.nio.file.NoSuchFileException;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Main {

    public static void main(String[] args) throws IOException {


        //javafx.application.Application.launch(TetrisGame.class);
        javafx.application.Application.launch(Menu.class);

    }
}
