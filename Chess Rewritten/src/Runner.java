import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
		Board board = new Board();
		Game game = new Game(800, 800, board);
		PieceData[][] bArray = board.getBoard();
		Position pos;
		Scanner input = new Scanner(System.in);
		
		String move;
		boolean fail = false;
		Settings.side = 1;
		while(board.getStatus(board.getWorkingBoard()) == GameResult.ongoing) {
			
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			if(Settings.showSwitch) {
				Settings.drawSide = Settings.side;
			}
//			if(Settings.side == -1) {
//				Move bestMove = board.minmaxRoot(4, false, Settings.side);
//				board.move(bestMove, true);
//				Settings.side *= -1;
//				continue;
//			}
			if(Settings.side == 1){
				Move bestMove = board.minmaxRoot(4, true, Settings.side);
				board.move(bestMove, true);
				Settings.side *= -1;
				Settings.selected = null;
				continue;
			}
/*
			ArrayList<Move> moves = board.findLegalMoves(Settings.side);
			
			
			move = input.next();
			input.nextLine();
			fail = true;
			if(move.startsWith("list")) {
				String str = "";
				for(Move m : moves) {
					str += m.toString() + ", ";
				}
				System.out.println(str);
			}else if(move.startsWith("undo")) {
				board.unMove();
				board.unMove();
			}
			
			else
			for(Move m : moves) {
				
				move = move.trim();
				if(move.equals(m.toString().trim())) {
					board.move(m,true);
					fail = false;
					break;
				}
				
			}
			if(fail) {
				System.out.println(move + " is not a valid move");
			}else {
				//System.out.println(board);
				Settings.side *= -1;
			}
*/			
		}
		System.out.print("The game has ended in " + board.getStatus().toString());
		

	}
	/**
	 * 
	 * @param n Number to round
	 * @param places Number of decimal places to round, must be > 0
	 * @return n rounded to places decimal places
	 */
	public static double round(double n, int places) {
		double n0 = ((int)((n*Math.pow(10.0, places)) + 0.5))/(Math.pow(10.0, places));
		return n0;
	}

}
