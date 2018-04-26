
public class Evaluation {
	public static int opening_length = 20;
	
	public static double knight_and_bishop_development = 0.1;
	public static double castle = 0.11;
	public static double rook_open_file = 0.04;
	public static double doubled_pawns = -0.06;
	public static double pawn_height = 0.02;
	
	public static double knight_multiplier = 0.1;
	
	public static double[][] white_knight_positions = {
			{-1,-0.5,-0.5,-0.5,-0.5,-0.5,-0.5,-1},
			{-0.5,-0.5,-0.5,-0.5,-0.5,-0.5,-0.5,-0.5},
			{-0.5,-0.4,-0.3,-0.3,-0.3,-0.3,-0.4,-0.5},
			{-0.5,-0.4,-0.3,-0.3,-0.3,-0.3,-0.4,-0.5},
			{-0.5,-0.4, 0 , 0 , 0 , 0 ,-0.4,-0.5},
			{-0.3,-0.1, 1 , 0 , 0 , 1 ,-0.1,-0.3},
			{-0.3,-0.1, 0 , 0.5 , 0.5 , 0 ,-0.1,-0.3},
			{-1,-0.5,-0.5,-0.5,-0.5,-0.5,-0.5,-1}
	};
	public static double[][] black_knight_positions = {
			{-1,-0.5,-0.5,-0.5,-0.5,-0.5,-0.5,-1},
			{-0.3,-0.1, 0 , 0.5 , 0.5 , 0 ,-0.1,-0.3},
			{-0.3,-0.1, 1 , 0 , 0 , 1 ,-0.1,-0.3},
			{-0.5,-0.4, 0 , 0 , 0 , 0 ,-0.4,-0.5},
			{-0.5,-0.4,-0.3,-0.3,-0.3,-0.3,-0.4,-0.5},
			{-0.5,-0.4,-0.3,-0.3,-0.3,-0.3,-0.4,-0.5},
			{-0.5,-0.5,-0.5,-0.5,-0.5,-0.5,-0.5,-0.5},
			{-1,-0.5,-0.5,-0.5,-0.5,-0.5,-0.5,-1}
	};
}
