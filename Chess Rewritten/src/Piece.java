
public enum Piece {
	empty(0,'\t'),
	whitePawn(1,'\u2659'), blackPawn(-1,'\u265F'),
	whiteKnight(3,'\u2658'), blackKnight(-3,'\u265E'),
	whiteBishop(3,'\u2657'), blackBishop(-3,'\u265D'),
	whiteRook(5,'\u2656'), blackRook(-5,'\u265C'),
	whiteQueen(9,'\u2655'), blackQueen(-9,'\u265B'),
	whiteKing(999,'\u2654'), blackKing(-999,'\u265A');
	
	private final int value;
	
	private final char display;
	
	
	Piece(int value, char display){
		this.value = value;
		this.display = display;
	}

	public int getValue() {
		return value;
	}
	
	/**
	 * 
	 * @return Side of piece. 1 if white, -1 if black
	 */
	public int getSide() {
		return (int) Math.signum(getValue());
	}



	public char getDisplay() {
		return display;
	}

	
}
