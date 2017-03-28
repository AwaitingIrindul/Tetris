package Model.ModelPuzzle;

import Model.ModelBoard.Board.Board;
import Model.ModelBoard.Direction;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelBoard.Position.Position;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Irindul on 15/03/2017.
 * Puzzle game contains rule for this game
 */
public class Puzzle {

    private Board board;
    private Piece goal;
    public static int height = 6;
    public static int width = 6;
    private List<Piece> pieces;
    private Piece selected;
    private static Position winningPosition = new Position(2, 5);
    private PuzzleObserver listener;
    private int currentLevel = 1;

    public Puzzle(PuzzleObserver listener) {
        this.board = new Board(height, width);
        /*goal = PuzzlePieceFactory.get(PuzzlePieces.TwoH);
        goal.setPosition(new Position(2, 0));*/
        this.listener = listener;

        pieces = new ArrayList<>();
        /*pieces.add(PuzzlePieceFactory.get(PuzzlePieces.ThreeV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.ThreeV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.TwoV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.TwoV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.TwoV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.TwoH));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.TwoH));


        pieces.get(0).setPosition(new Position(1, 3));
        pieces.get(1).setPosition(new Position(1, 4));
        pieces.get(2).setPosition(new Position(1, 2));
        pieces.get(3).setPosition(new Position(3, 2));
        pieces.get(4).setPosition(new Position(4, 1));
        pieces.get(5).setPosition(new Position(3, 0));
        pieces.get(6).setPosition(new Position(5, 2));


        pieces.forEach(piece -> board.addPiece(piece));
        board.addPiece(getGoal());

        board.toFile("ressources/init/puzzle2.json");
*/
        

        File f = new File("ressources/saves/puzzle1.json");
        if(f.exists() && !f.isDirectory()) {

            board = Board.fromFile("ressources/saves/puzzle1.json");

        } else {
            board = Board.fromFile("ressources/init/puzzle1.json");
        }

        pieces.addAll(board.getPieces());
        goal = board.getPieceAt(new Position(2, 0));
    }

    public void addPiece(Piece piece){
        pieces.add(piece);
        board.addPiece(piece);
    }

    public void setSelected(Piece piece){
        selected = piece;
    }

    public void moveSelected(Direction direction){
        if (selected != null) {
            boolean ok = false;
            switch (selected.getOrientation()){

                case HORIZONTAL:
                    if(direction.equals(Direction.LEFT) || direction.equals(Direction.RIGHT))
                        ok = true;
                    break;
                case VERTICAL:
                    if(direction.equals(Direction.UP) || direction.equals(Direction.DOWN))
                        ok = true;
                    break;
                default:
                    ok = false;
            }
            if(ok)
                board.movePiece(direction, selected);
            checkFinish();
        }
    }

    private void checkFinish() {
        boolean finnish = false;

        for(Position pos : goal.getPositions()){
            if(pos.equals(winningPosition)){
                finnish =true;
                break;
            }
        }

        if(finnish){
            listener.onFinish();
        }
    }

    public Piece getGoal(){
        return goal;
    }

    public List<Piece> getPieces(){
        return pieces.stream()
                .filter(piece -> !piece.equals(goal))
                .collect(Collectors.toList());
    }

    public void reset() {
        File file = new File("ressources/saves/puzzle1.json");
        if(file.delete())
           System.out.println("Successfully deleted file");

            

        pieces =new ArrayList<>();
        board = Board.fromFile("ressources/init/puzzle1.json");

        pieces.addAll(board.getPieces());
        goal = board.getPieceAt(new Position(2, 0));

    }

    public void quit() {
        String sb = "ressources/saves/puzzle" +
                currentLevel +
                ".json";

        board.toFile(sb);
    }

    public void next() {
        currentLevel++;
        String sb = "ressources/init/puzzle" +
                currentLevel +
                ".json";

        File f = new File(sb);

        if(f.exists() && !f.isDirectory()) {
            pieces =new ArrayList<>();
            board = Board.fromFile(sb);
            pieces.addAll(board.getPieces());
            goal = board.getPieceAt(new Position(2, 0));

        } else {
            System.err.println("No more levels created");
            currentLevel--;
        }

    }

    public void highscore(String pseudo, int score){
        File f = new File("ressources/saves/highscore.txt");
        if( f.exists() && !f.isDirectory()){

            try (Stream<String> stream = Files.lines(Paths.get("ressources/saves/highscore.txt"))) {

                int high = stream.map(string -> {
                    StringBuilder sb = new StringBuilder(string);
                    return sb;
                }).map(sb -> {
                    CharacterIterator it = new StringCharacterIterator(sb.toString());
                    StringBuilder nSb = new StringBuilder();
                    while(it.next() != ':');
                    it.next();

                    while(it.getIndex() != it.getEndIndex()){
                        nSb.append(it.current());
                        it.next();
                    }

                    return nSb;

                }).mapToInt(sb -> Integer.parseInt(sb.toString().trim()))
                        .min().getAsInt();

                if(score < high){
                    listener.onNewBestScore();
                    try {
                        String save = pseudo + " : " + score;
                        Files.write(Paths.get("ressources/saves/highscore.txt"), save.getBytes(), StandardOpenOption.APPEND);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                if(f.createNewFile()){
                    BufferedWriter out = new BufferedWriter(new FileWriter("ressources/saves/highscore.txt"));
                    String s = pseudo + " : "+ score;
                    out.write(s);
                    out.close();
                    listener.onNewBestScore();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
