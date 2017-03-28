import Model.ModelBoard.Direction;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelPuzzle.PuzzlePieceFactory;
import Model.ModelPuzzle.PuzzlePieces;
import View.Menu;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by Irindul on 09/02/2017.
 * Launches the application
 */
public class Main {

    public static void main(String[] args) throws IOException {


        //System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("file.txt"))));
        javafx.application.Application.launch(Menu.class);
                
    }
}
