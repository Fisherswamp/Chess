
public enum GameResult {
	ongoing(0),
	white_win(1),
	black_win(-1),
	draw_fifty_move_rule(2),
	draw_threefold_repetition(3),
	draw_stalemate(4);
	
	public int value;
	
	GameResult(int value){
		this.value = value;
	}
}
