import java.awt.Color;
import java.awt.Font;

public class Settings {
	static Color lightColor = new Color(240,217,181);//Color.white;//(255-55,222-55,173-55);
	static Color darkColor = new Color(181,136,99);//new Color(160,160,160);//(160,82,45);//(139,69,19);
	static int squareSize = 64;
	static final int boardSize = 8;
	static int xOffset = 86;
	static int yOffset = 86;

	static int drawSide = 1;
	static int side = 1;
	static boolean showSwitch = false;
	
	static Position selectedCoords = null;
	static PieceData selected = null;
	
	static boolean whiteKingInCheck = false;
	static boolean blackKingInCheck = false;
	
	static double castleEvaluationVal = 0.5;
	static double movedPieceEvaluationVal = 0.045;
	
	static boolean imageOverAscii = true;
	
	static String promotePiece = "Queen";
	
	static Font mainFont = new Font("Monospaced",Font.PLAIN,60);
	static Font smallFont = new Font("Monospaced",Font.PLAIN,20);
	static boolean render = true;
	
	static boolean sort = true;//Whether or not to sort the move list based on captures and piece value
//	static Game game;
	static long moveTime = 5*1000000000L;
	
}
	