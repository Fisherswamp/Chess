import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

	
public class Board2 {
	static final PieceData EMPTY_PIECE = new PieceData(Piece.empty);
	
	private PieceData[][] board;
	private PieceData[][] workingBoard;
	
	private ArrayList<PieceData[][]> boardHistory;
	private ArrayList<Integer> boardHistoryMarker;
	
	//private int moveNumber;
	//private int Game = 0;// 1 if white win, -1 if black win, 0 if live, 2 if draw
	
	private ArrayDeque<Move> moveHistory;
	
	private ArrayDeque<Move> undoneMoves;
	//private ArrayList<Move> moveList;
	
	public Board2() {
		PieceData[][] nb = {
				{new PieceData(Piece.blackRook),new PieceData(Piece.blackKnight),new PieceData(Piece.blackBishop),new PieceData(Piece.blackQueen),new PieceData(Piece.blackKing),new PieceData(Piece.blackBishop),new PieceData(Piece.blackKnight),new PieceData(Piece.blackRook)},
				{new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn)},
				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
				{new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn)},
				{new PieceData(Piece.whiteRook),new PieceData(Piece.whiteKnight),new PieceData(Piece.whiteBishop),new PieceData(Piece.whiteQueen),new PieceData(Piece.whiteKing),new PieceData(Piece.whiteBishop),new PieceData(Piece.whiteKnight),new PieceData(Piece.whiteRook)}
		};
//		PieceData[][] nb2 = {
//				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,new PieceData(Piece.blackKing),EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
//				{new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn),new PieceData(Piece.blackPawn)},
//				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
//				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
//				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
//				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE},
//				{new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn),new PieceData(Piece.whitePawn)},
//				{EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE,new PieceData(Piece.whiteKing),EMPTY_PIECE,EMPTY_PIECE,EMPTY_PIECE}
//		};
		board = nb;
		boardHistory = new ArrayList<PieceData[][]>();
		
		boardHistory.add(copyBoard());
		setWorkingBoard(copyBoard());
		
		boardHistoryMarker = new ArrayList<>();
		boardHistoryMarker.add(0);
		
		//moveNumber = 0;
		moveHistory = new ArrayDeque<>();
		undoneMoves = new ArrayDeque<>();
	}
	
	/**
	 * 
	 * @param m Move to make
	 * @param isOfficial Whether or not this is an official move to add to the move history 
	 * Does a move
	 */
	public void move(Move m, boolean isOfficial) {
		Position toPos = m.getToPos();
		Position fromPos = m.getFromPos();
		PieceData p = m.getPiece();
		
		p.setHasMoved(true);

		board[toPos.y][toPos.x] = p;
		board[fromPos.y][fromPos.x] = EMPTY_PIECE;
		if(m.isEPCapture()) {
			Position epPos = m.getEpPos();
			board[epPos.y][epPos.x] = p;
			board[toPos.y][toPos.x] = EMPTY_PIECE;
		}
		
		if(m.isCastle()) {
			Position rookToPos = m.getRookToPos();
			Position rookFromPos = m.getRookFromPos();
			board[rookToPos.y][rookToPos.x] = m.getRook();
			board[rookFromPos.y][rookFromPos.x] = EMPTY_PIECE;
			
			p.setHasCastled(true);
		}else if(m.isPromotion()) {
			board[toPos.y][toPos.x] = m.getPromote();
		}
		
		//new move has been done, set all piece en passant catpurable to false
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				PieceData pawn = getPiece(new Position(i,j));
				if(pawn.getPiece().getValue() == p.getPiece().getSide()) {//if pawn is a pawn and on the same side as the current piece being moved
					pawn.setEpCapturable(false);
				}
			}
		}
		//set current piece's EP capturability to true
		if(p.getPiece() == Piece.whitePawn || p.getPiece() == Piece.blackPawn) {
			if(Math.abs(toPos.y - fromPos.y) == 2) {
				p.setEpCapturable(true);
			}
		}
		
		if(isOfficial) {
			moveHistory.push(m);
			undoneMoves.clear();
			setWorkingBoard(copyBoard());
				
		}
		
		boardHistory.add(copyBoard());
		//if a piece has been captured or a pawn has been moved, add to board history marker
		if(m.getCapture() != EMPTY_PIECE || m.getPiece().isPawn()) {
			boardHistoryMarker.add(moveHistory.size()-1);
		}
		
	}
	/**
	 * Undoes the last move
	 */
	public void unMove() {
		unMove(moveHistory.peek(),true);
	}
	/**
	 * 
	 * @param m Move to unmake
	 * @param isOfficial Whether or not this is an official unmove to remove from movehistory
	 * Undoes a move
	 */
	private void unMove(Move m, boolean isOfficial) {
		Position toPos = m.getToPos();
		Position fromPos = m.getFromPos();
		PieceData p = m.getPiece();
		PieceData c = m.getCapture();
		
		p.setHasMoved(m.isPieceHasMoved());
		if(m.isEPCapture()) {
			Position epPos = m.getEpPos();
			
			board[epPos.y][epPos.x] = EMPTY_PIECE;
			board[fromPos.y][fromPos.x] = p;
			board[toPos.y][toPos.x] = c;
			
		}else {
			board[toPos.y][toPos.x] = c;
			board[fromPos.y][fromPos.x] = p;
			if(m.isCastle()) {
				Position rookToPos = m.getRookToPos();
				Position rookFromPos = m.getRookFromPos();
				PieceData r = m.getRook();
				r.setHasMoved(false);
				p.setHasCastled(false);
				board[rookToPos.y][rookToPos.x] = EMPTY_PIECE;
				board[rookFromPos.y][rookFromPos.x] = r;
			}
		}
		
		//set current piece's EP capturability to old value (before move)
		if(p.getPiece() == Piece.whitePawn || p.getPiece() == Piece.blackPawn) {
			p.setEpCapturable(m.isPawnIsEPCapturable());
		}
		
		if(isOfficial) {
			moveHistory.pop();
			undoneMoves.push(m);
			setWorkingBoard(copyBoard());
		}
		
		
		if(boardHistory.size() > 0 ) {
			
			boardHistory.remove(boardHistory.size()-1);
			
			if(boardHistoryMarker.size() > 1 && boardHistoryMarker.get(boardHistoryMarker.size()-1) == moveHistory.size()-1) {//if the last value put in the board history marker is the current move history size
				boardHistoryMarker.remove(boardHistoryMarker.size()-1);
			}
		}
	}
	/**
	 * Redoes an undone move
	 */
	public void reMove() {
		if(!undoneMoves.isEmpty()) {
			Move m = undoneMoves.pop();
			move(m,true);
		}
	}
	/**
	 * Finds all moves for a piece as if it moved like a pawn
	 * @param pos Position of piece to check
	 * @return ArrayList of possible moves
	 */
	public ArrayList<Move> findPawnMoves(Position pos){
		ArrayList<Move> moveList = new ArrayList<>();
		boolean promotion = getPiece(pos).isPawn();
		PieceData p = getPiece(pos);
		// 1 if white, -1 if black
		int side = p.getPiece().getSide();
		
		Position newPos;
		for(int i = 1; i <=2; i++) {//do for one move and for two
			if(i == 2 && p.hasMoved()) {//can't double move if pawn has already moved 
				break;
			}
			newPos = new Position(pos.x,pos.y - (i*side));
			if(newPos.y >= 0 && newPos.y < board.length && getPiece(newPos) == EMPTY_PIECE) {
				if(promotion && (side == 1 && pos.y ==1 || side == -1 && pos.y == 6)) {
					if(side == 1) {
						moveList.add(new Move(p,EMPTY_PIECE,pos,newPos,new PieceData(Piece.whiteBishop)));
						moveList.add(new Move(p,EMPTY_PIECE,pos,newPos,new PieceData(Piece.whiteKnight)));
						moveList.add(new Move(p,EMPTY_PIECE,pos,newPos,new PieceData(Piece.whiteRook)));
						moveList.add(new Move(p,EMPTY_PIECE,pos,newPos,new PieceData(Piece.whiteQueen)));
					}else {
						moveList.add(new Move(p,EMPTY_PIECE,pos,newPos,new PieceData(Piece.blackBishop)));
						moveList.add(new Move(p,EMPTY_PIECE,pos,newPos,new PieceData(Piece.blackKnight)));
						moveList.add(new Move(p,EMPTY_PIECE,pos,newPos,new PieceData(Piece.blackRook)));
						moveList.add(new Move(p,EMPTY_PIECE,pos,newPos,new PieceData(Piece.blackQueen)));
					}
				}else {
					moveList.add(new Move(p,EMPTY_PIECE,pos,newPos));
				}
			}else {
				break;
			}
		}
		if(pos.x > 0) {
			Position takePos = new Position(pos.x-1,pos.y-side);
			if(takePos.y >= 0 && takePos.y < board.length) {
				PieceData take = getPiece(takePos);
				if(take.getPiece().getSide() == -1*side) {//if take's side is the opposite of my side
					if(promotion && (side == 1 && pos.y ==1 || side == -1 && pos.y == 6)) {//if promotion
						if(side == 1) {
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.whiteBishop)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.whiteKnight)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.whiteRook)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.whiteQueen)));
						}else {
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.blackBishop)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.blackKnight)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.blackRook)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.blackQueen)));
						}
					}else {
						moveList.add(new Move(p,take,pos,takePos));
					}
				}
			}
		}
		if(pos.x < Settings.boardSize-1) {
			Position takePos = new Position(pos.x+1,pos.y-side);
			if(takePos.y > 0 && takePos.y < board.length) {
				PieceData take = getPiece(takePos);
				if(takePos.y > 0 && takePos.y < board.length && take.getPiece().getSide() == -1*side) {//if take's side is the opposite of my side
					if(promotion && (side == 1 && pos.y ==1 || side == -1 && pos.y == 6)) {//if promotion
						if(side == 1) {
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.whiteBishop)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.whiteKnight)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.whiteRook)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.whiteQueen)));
						}else {
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.blackBishop)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.blackKnight)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.blackRook)));
							moveList.add(new Move(p,take,pos,takePos,new PieceData(Piece.blackQueen)));
						}
					}else {
						moveList.add(new Move(p,take,pos,takePos));
					}
				}
			}
		}
		
		//En Passant
		if(promotion) {//only check for en passant if actually pawn
			for(int i = -1; i <= 1; i++) {//do this for left and right
				Position checkPos = new Position(pos.x+i,pos.y);
				if(checkPos.x > 0 && checkPos.x < Settings.boardSize-1) {
					PieceData take = getPiece(checkPos);
					if(take.getPiece().getValue() == -1*side) {//if is pawn of other team
						if(take.isEpCapturable()) {
							moveList.add(new Move(p,take,pos,checkPos,new Position(checkPos.x,checkPos.y-side)));
						}
					}
				}
			}
		}
		
		
		
		return moveList;
	}
	/**
	 * Finds all moves for a piece as if it moved like a knight
	 * @param pos Position of piece to check
	 * @return ArrayList of possible moves
	 */
	public ArrayList<Move> findKnightMoves(Position pos){
		ArrayList<Move> moveList = new ArrayList<>();
		PieceData p = getPiece(pos);
		int side = p.getPiece().getSide();
		/*
		 * Key: 'O' are legal knight moves. 
		 * 		'.' are empty spaces
		 * 		'X' are squares being used to check 
		 * . O . O .
		 * O X . X O
		 * . . N . .
		 * O X . X O
		 * . O . O .
		 */
		for(int deltaX = -1; deltaX <= 1; deltaX+=2) {
			for(int deltaY = -1; deltaY <= 1; deltaY+=2) {
				int[] X_Coords = {pos.x+deltaX,pos.y+deltaY};
				//if outside board, then not a legal move
				if(X_Coords[0] < 0 || X_Coords[0] >= board.length || X_Coords[1] < 0 || X_Coords[1] >= board[X_Coords[0]].length) {
					continue;
				}
				int[] O_Coords = {X_Coords[0] + deltaX,X_Coords[1]};
				//X coord with delta X check
				if(!(O_Coords[0] < 0 || O_Coords[0] >= board.length || O_Coords[1] < 0 || O_Coords[1] >= board[O_Coords[0]].length)) {
					Position checkPos = new Position(O_Coords[0],O_Coords[1]);
					PieceData checkPiece = getPiece(checkPos);
					if(checkPiece.getPiece().getSide() != side) {
						moveList.add(new Move(p,checkPiece,pos,checkPos));
					}
				}
				//X coord with delta Y check
				O_Coords[0] = X_Coords[0];
				O_Coords[1] = X_Coords[1] + deltaY;
				if(!(O_Coords[0] < 0 || O_Coords[0] >= board.length || O_Coords[1] < 0 || O_Coords[1] >= board[O_Coords[0]].length)) {
					Position checkPos = new Position(O_Coords[0],O_Coords[1]);
					PieceData checkPiece = getPiece(checkPos);
					if(checkPiece.getPiece().getSide() != side) {
						moveList.add(new Move(p,checkPiece,pos,checkPos));
					}
				}
				
			}
		}
	
		
		return moveList;
	}
	/**
	 * Finds all moves for a piece as if it moved like a bishop
	 * @param pos Position of piece to check
	 * @return ArrayList of possible moves
	 */
	public ArrayList<Move> findBishopMoves(Position pos){
		ArrayList<Move> moveList = new ArrayList<>();
		
		PieceData p = getPiece(pos);
		int side = p.getPiece().getSide();
		
		for(int xDelta = -1; xDelta <= 1; xDelta+=2) {
			for(int yDelta = -1; yDelta <= 1; yDelta+=2) {
				
				
				for(int i = 1; i < Settings.boardSize; i++) {
					int xCheck = pos.x+i*xDelta;
					int yCheck = pos.y+i*yDelta;
					if(xCheck < 0 || xCheck >= board.length || yCheck < 0 || yCheck >= board[xCheck].length) {
						break;
					}
					Position checkPos = new Position(xCheck,yCheck);
					PieceData checkPiece = getPiece(checkPos);
					if(checkPiece.getPiece() != EMPTY_PIECE.getPiece()) {//if theres a piece in the path
						//add piece to move list if on other side, but can no longer move in this direction
						if(checkPiece.getPiece().getSide() != side) {
							moveList.add(new Move(p,checkPiece,pos,checkPos));
						}
						break;
					}
					moveList.add(new Move(p,checkPiece,pos,checkPos));
				}
				
			}
		}
	
		return moveList;
	}
	/**
	 * Finds all moves for a piece as if it moved like a rook
	 * @param pos Position of piece to find moves for
	 * @return returns an ArrayList of moves for that piece
	 */
	public ArrayList<Move> findRookMoves(Position pos){
		ArrayList<Move> moveList = new ArrayList<>();
		
		PieceData p = getPiece(pos);
		int side = p.getPiece().getSide();
		
		for(int xDelta = -1; xDelta <= 1; xDelta++) {
			for(int yDelta = -1; yDelta <= 1; yDelta++) {
				
				if((xDelta != 0 && yDelta != 0) || xDelta == yDelta) {//if trying to move in a diagonal, then DONT 
					continue;
				}
				for(int i = 1; i < Settings.boardSize; i++) {
					int xCheck = pos.x+i*xDelta;
					int yCheck = pos.y+i*yDelta;
					if(xCheck < 0 || xCheck >= board.length || yCheck < 0 || yCheck >= board[xCheck].length) {
						break;
					}
					Position checkPos = new Position(xCheck,yCheck);
					PieceData checkPiece = getPiece(checkPos);
					if(checkPiece != EMPTY_PIECE) {//if theres a piece in the path
						//add piece to move list if on other side, but can no longer move in this direction
						if(checkPiece.getPiece().getSide() != side) {
							moveList.add(new Move(p,checkPiece,pos,checkPos));
						}
						break;
					}
					moveList.add(new Move(p,checkPiece,pos,checkPos));
				}
				
			}
		}
		
		return moveList;
	}
	/**
	 * Finds all moves for a piece as if it moved like a king
	 * @param p Piece to find moves for
	 * @return returns an arraylist of moves for that piece
	 */
	public ArrayList<Move> findKingMoves(Position pos){
		ArrayList<Move> moveList = new ArrayList<>();
		
		PieceData p = getPiece(pos);
		int side = p.getPiece().getSide();
	
		for(int deltaX = -1; deltaX <= 1; deltaX++) {
			for(int deltaY = -1; deltaY <=1; deltaY++) {
				if(deltaX == 0 && deltaY == 0) {//gotta move somewhere
					continue;
				}
				int xCheck = pos.x + deltaX;
				int yCheck = pos.y + deltaY;
				
				if(xCheck < 0 || xCheck >= board.length || yCheck < 0 || yCheck >= board[xCheck].length) {
					continue;
				}
				Position checkPos = new Position(xCheck,yCheck);
				PieceData checkPiece = getPiece(checkPos);
		
				if(checkPiece.getPiece().getSide() != side) {
					moveList.add(new Move(p,checkPiece,pos,checkPos));
				}
				
				
			}
		}
		if(!p.hasMoved()) {//if king has not moved, check for castling
			Position[] rookChecks = {new Position(0,pos.y),new Position(7,pos.y)};
			for(int rk = 0; rk < 2; rk++) {
				PieceData rook = getPiece(rookChecks[rk]);
				if(rook != EMPTY_PIECE && rook.getPiece().getValue()*rook.getPiece().getSide() == 5) {//if piece is rook
					if(!rook.hasMoved()) {//if rook has not moved
						int rookCoord = rookChecks[rk].x;
						int delta = (pos.x > rookCoord ? 1 : -1);
						boolean canCastle = true;
						for(int emptyChkIndex = rookCoord + delta;emptyChkIndex != pos.x;emptyChkIndex += delta) {
							if(getPiece(new Position(emptyChkIndex,pos.y)) != EMPTY_PIECE) {
								canCastle = false;
								break;
							}
						}
						if(canCastle) {
							moveList.add(new Move(p,pos,new Position(pos.x - 2*delta,pos.y),rook,rookChecks[rk],new Position(pos.x - delta,pos.y)));
							//moveList.add(new CastleMove(p,new Position((char) (p.position.letterCoord - 2*delta),p.position.numCoord),rookChecks[rk]));
						}
					}
				}
			}
		}
		
		return moveList;
	}

	/**
	 * @param pos Position of piece to check
	 * @return whether or not the king is in check, or whether the piece selected can be taken by any other piece
	 */
	public boolean isKingInCheck(Position pos) {
		PieceData p = getPiece(pos);
		
		ArrayList<Move> checkMoves;
		
		checkMoves = findBishopMoves(pos);
		for(Move m : checkMoves) {
			if(m.getCapture().isBishop() || m.getCapture().isQueen()) {
				return true;
			}
		}
		checkMoves = findRookMoves(pos);
		for(Move m : checkMoves) {
			if(m.getCapture().isRook() || m.getCapture().isQueen()) {
				return true;
			}
		}
		checkMoves = findKnightMoves(pos);
		for(Move m : checkMoves) {
			if(m.getCapture().isKnight()) {
				return true;
			}
		}
		checkMoves = findPawnMoves(pos);
		for(Move m : checkMoves) {
			if(m.getCapture().isPawn()) {
				return true;
			}
		}
		checkMoves = findKingMoves(pos);
		for(Move m : checkMoves) {
			if(m.getCapture().isKing()) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public ArrayList<Move> findLegalMoves(int side){
		return findLegalMoves(side,board);
	}
	/**
	 * Find all legal moves for a side. 1 = white, -1 = black
	 * @return All legal moves for a side
	 */
	private ArrayList<Move> findLegalMoves(int side,PieceData[][] board){
		ArrayList<Move> moveList = new ArrayList<>();
		PieceData p;
		Position pos;
		Position kingPos = new Position(-1,-1);
		//look through every position on the board
		for(int y = 0; y < board.length; y++) {
			for(int x = 0; x < board[y].length; x++) {
				pos = new Position(x,y);
				p = getPiece(pos);
				if(p.getPiece().getSide() == side) {
					if(p.isPawn()){
						moveList.addAll(findPawnMoves(pos));
					}else if(p.isRook()) {
						moveList.addAll(findRookMoves(pos));
					}else if(p.isBishop()) {
						moveList.addAll(findBishopMoves(pos));
					}else if(p.isKnight()) {
						moveList.addAll(findKnightMoves(pos));
					}else if(p.isQueen()) {
						moveList.addAll(findRookMoves(pos));
						moveList.addAll(findBishopMoves(pos));
					}else if(p.isKing()) {
						moveList.addAll(findKingMoves(pos));
						kingPos = pos;
					}
				}
			}
		}
		//return moveList;
		
		//Legal moves to return
		ArrayList<Move> legalMoves = new ArrayList<>();
		//now check if each move is legal
		boolean legal;
		Position newKingPos;
		
		for(Move m : moveList) {
			 move(m,false);
			 
			 if(m.getPiece().isKing()) {
				 newKingPos = m.getToPos();
			 }else {
				 newKingPos = kingPos;
			 }
			 legal = !isKingInCheck(newKingPos);
			 
			 if(m.isCastle()) {//if a castle, also check if the rook can be taken, since that's where the king passes through, and king cant castle through check
				 legal = legal && !isKingInCheck(m.getRookToPos());
			 }
			 
			 unMove(m,false);
			 
			 if(m.isCastle()) {//if castle, also check if king is in check, since king cant castle out of check
				 legal = legal && !isKingInCheck(kingPos);
			 }
			 
			 if(legal) {
				 legalMoves.add(m);
			 }else {
				 //System.out.println(m + " is not legal");
			 }
		}
		
		
		//sort legal moves based on piece being captured, and then piece moving
		if(Settings.sort) {
			Collections.sort(legalMoves, new Comparator<Move>() {
			    @Override
			    public int compare(Move lhs, Move rhs) {
			        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
			    	int lhsCapture = Math.abs(lhs.getCapture().getPiece().getValue());
			        int rhsCapture = Math.abs(rhs.getCapture().getPiece().getValue());
			        int lhsPiece = Math.abs(lhs.getPiece().getPiece().getValue());
			        int rhsPiece = Math.abs(rhs.getPiece().getPiece().getValue());
			        
			    	return lhsCapture > rhsCapture ? -1 : (lhsCapture < rhsCapture) ? 1 : lhsPiece > rhsPiece ? -1 : lhsPiece < rhsPiece ? 1 : 0;
			    }
			});
		}
		return legalMoves;
		
	}
	/**
	 * 
	 * @return A numerical evaluation of the position. > 0 favors white, < 0 favors black
	 */
	public double evaluatePosition() {
		double total = 0;
		int side ;
		PieceData p;
		Position pos;
		for(int y = 0; y < board.length; y++) {
			for(int x = 0; x < board[y].length; x++) {
				pos = new Position(x,y);
				p = getPiece(pos);
				side = p.getPiece().getSide();
				
				if(p.isKnight() && moveHistory.size() < Evaluation.opening_length) {
					double[][] values = (side == 1 ? Evaluation.white_knight_positions : Evaluation.black_knight_positions);
					total += Evaluation.knight_multiplier*values[pos.y][pos.x]*side;
				}
				
				if((p.isBishop() || p.isKnight()) && p.hasMoved()) {
					total += Evaluation.knight_and_bishop_development*side;
				}else if(p.isKing() && p.hasCastled()) {
					total += Evaluation.castle*side;
				}else if(p.isRook()) {
					Piece[] pawns = {Piece.whitePawn,Piece.blackPawn};
					if(numPiecesInColumn(pawns,x) == 0) {
						total += Evaluation.rook_open_file*side;
					}
				}else if(p.isPawn()) {
					Piece[] me = {p.getPiece()};
					if(numPiecesInColumn(me,x) > 1) {
						total += Evaluation.doubled_pawns*side*0.5;//half as bad, since both pawns take away
					}
					
					int goal = (side == 1 ? 0 : 7);
					int distance = Math.abs(goal-y);
					
					total += Evaluation.pawn_height*(6-distance)*side;
				}
				
				
				total += p.getPiece().getValue();
			}
		}
		
		return total;
	}
	
	public GameResult getStatus() {
		long start = System.nanoTime();
		GameResult status = getStatus(boardHistory,moveHistory);
		long end = System.nanoTime();
		System.out.println((end-start)/1000000000.0 + " seconds to get status");
		return status;
	}
	
	/**
	 * update the result value 
	 * @return status of the game
	 */
	public GameResult getStatus(ArrayList<PieceData[][]> boardHistory, ArrayDeque<Move> moveHistory) {
		GameResult result = GameResult.ongoing;;
		
		if(boardHistory.size() - boardHistoryMarker.get(boardHistoryMarker.size()-1) >= 100) {
			result = GameResult.draw_fifty_move_rule;//game ends in a draw (50 move rule)
		}else if(isThreefoldRepetitionDraw(boardHistory)) {
			result = GameResult.draw_threefold_repetition;//game ends in a draw (3 fold repetition rule)
		}else {
			int side = moveHistory.size()%2 == 0 ? 1 : -1;
			ArrayList<Move> moveList = findLegalMoves(side);
			if(moveList.isEmpty()) {//if there are zero moves remaining
				Piece king = (side == 1 ? Piece.whiteKing : Piece.blackKing);
				if(isKingInCheck(findPiecePosition(king))) {
					result = side == 1 ? GameResult.black_win : GameResult.white_win;//
				}else {
					result = GameResult.draw_stalemate;//draw due to stalemate
				}
			}
		}
		
		return result;
	}
	
	public boolean isThreefoldRepetitionDraw(ArrayList<PieceData[][]> boardHistory) {
		if(boardHistory.size() <= 2) {
			return false;
		}
		int numSamePos = 1;
		for(int i = boardHistoryMarker.get(boardHistoryMarker.size()-1); i < boardHistory.size()-1 && i >= 0; i++) {
			if(boardEquals(boardHistory.get(i), boardHistory.get(boardHistory.size()-1))) {
				numSamePos++;
				
				if(numSamePos >= 3) {
					return true;
				}
			}	
		}
		
		
		
		
		return false;
	}
	/**
	 * 
	 * @param board1 First board to check
	 * @param board2 Second board to check
	 * @return If the two boards have the same piece enums at the same positions, and the same possible moves
	 */
	public boolean boardEquals(PieceData[][] board1, PieceData[][] board2) {
		for(int i = -1; i <= 1; i += 2) {
			ArrayList<Move> moves1 = findLegalMoves(i,board1);
			ArrayList<Move> moves2 = findLegalMoves(i,board2);
			
			if(!moves1.equals(moves2)) {
				return false;
			}
		}
		
		for(int y = 0; y < board1.length; y++) {
			for(int x = 0; x < board1[y].length; x++) {
				if(board1[y][x].getPiece() != board2[y][x].getPiece()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param piecesToFind An array of pieces to count/search for
	 * @param column Column to check
	 * @return number of pieces in a column
	 */
	private int numPiecesInColumn(Piece[] piecesToFind, int column) {
		
		int num = 0;
		
		Position pos;
		Piece p;
		
		for(int y = 0; y < board.length; y++) {
			 pos = new Position(column,y);
			 p = getPiece(pos).getPiece();
			 if(p == Piece.empty) {
				 continue;
			 }
			 for(int i = 0; i < piecesToFind.length; i++) {
				 if(p == piecesToFind[i]) {
					 num++;
					 break;
				 }
			 }
		}
		return num;
	}

	
	public Move minmaxRoot(int depth, boolean isMaximizing, int side) {
		long timeStart = System.nanoTime();
		
		ArrayList<Move> moveList = findLegalMoves(side);
		
		
		double bestMove = -999999*side;
		Move bestMoveFound;
		
		
		ArrayList<Move> bestMoveList = new ArrayList<>();
		
		for(Move m : moveList) {
			move(m,false);
			double value = minmax(depth-1,-999999,999999, !isMaximizing,side*-1);
			unMove(m,false);
			if(value == bestMove) {
				bestMoveList.add(m);
			}else {
				if(isMaximizing) {
					if(value > bestMove) {
						bestMove = value;
						//bestMoveFound = m;
						
						bestMoveList.clear();
						bestMoveList.add(m);
					}
				}else {
					if(value < bestMove) {
						bestMove = value;
						//bestMoveFound = m;
						
						bestMoveList.clear();
						bestMoveList.add(m);
					}
				}
			}
		}
		if(bestMoveList.isEmpty()) {
			Piece king = (isMaximizing ? Piece.whiteKing : Piece.blackKing);
			if(isKingInCheck(findPiecePosition(king))) {
				System.out.println(getPiece(findPiecePosition(king)).getPiece().getDisplay() + " has been checkmated");
			}else {
				System.out.println(getPiece(findPiecePosition(king)).getPiece().getDisplay() + " has been stalemated");
			}
			try {
				throw new NoMovesRemainingException();
			} catch (NoMovesRemainingException e) {
				e.printStackTrace();
			}
		}
		bestMoveFound = bestMoveList.get((int)(Math.random()*bestMoveList.size()));
		System.out.println("Best move is " + bestMoveFound + " with a value of " + Runner.round(bestMove,3) + " chosen from " + bestMoveList.size() + " different possible move(s). This took " + Runner.round((System.nanoTime() - timeStart)/1000000000.0,4) + " seconds to find.");
		return bestMoveFound;
	}
	
	private double minmax(int depth, double alpha, double beta, boolean isMaximizing, int side) {
		
		GameResult status = getStatus();
		if(status != GameResult.ongoing) {
			if(Math.abs(status.value) == 1) {
				return 9999*status.value*depth;
			}else {
				return 0;
			}
		}
		
		if(depth == 0) {
			return evaluatePosition();
		}
		
		ArrayList<Move> moveList = findLegalMoves(side);
/*
		if(moveList.isEmpty()) {	
			Piece king = (isMaximizing ? Piece.whiteKing : Piece.blackKing);
			if(isKingInCheck(findPiecePosition(king))) {
				return -9999*side*depth;
				
			}else {
				return 0;//Stalemate is tie
			}
			
		}
*/	
		if(isMaximizing) {
			double bestMove = -99999;
			for(Move m : moveList) {
				move(m,false);
				bestMove = Math.max(bestMove, minmax(depth-1,alpha,beta,!isMaximizing,side*-1));
				unMove(m,false);
				alpha = Math.max(alpha, bestMove);
				if(beta <= alpha) {
					//System.out.println("Best move at depth " + depth + " is " + bestMove);
					return bestMove;
				}
			}
			
			//System.out.println("Best move at depth " + depth + " is " + bestMove);
			return bestMove;
		}else {
			double bestMove = 99999;
			for(Move m : moveList) {
				move(m,false);
				bestMove = Math.min(bestMove, minmax(depth-1,alpha,beta,!isMaximizing,side*-1));
				unMove(m,false);
				beta = Math.min(beta, bestMove);
				if(beta <= alpha) {
					//System.out.println("Best move at depth " + depth + " is " + bestMove);
					return bestMove;
				}
			}
			//System.out.println("Best move at depth " + depth + " is " + bestMove);
			return bestMove;
		}
		
	}
	
	/**
	 * 
	 * @param p Piece to find
	 * @return Position of first instance of piece found
	 */
	private Position findPiecePosition(Piece p) {
		for(int y = 0; y < board.length; y++) {
			for(int x = 0; x < board[y].length; x++) {
				Position pos = new Position(x,y);
				if(getPiece(pos).getPiece() == p) {
					return pos;
				}
			}
		}
		return null;
	}
	
	public PieceData getPiece(Position p) {
		return board[p.y][p.x];
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int y = 0; y < board.length; y++) {
			for(int x = 0; x < board[y].length; x++) {
				if(board[y][x].getPiece() != Piece.empty) {
					str.append(board[y][x].getPiece().getDisplay() + "\t");
				}else {
					//str.append(".\t");
					str.append((char)('\u25A0' + ((x + 1 + y%2)%2)) + "\t");
				}
				
			}
			str.append("\n");
		}
		return str.toString();
	}

	public PieceData[][] getBoard() {
		return board;
	}

	public void setBoard(PieceData[][] board) {
		this.board = board;
	}

	public PieceData[][] getWorkingBoard() {
		return workingBoard;
	}

	public void setWorkingBoard(PieceData[][] workingBoard) {
		this.workingBoard = workingBoard;
	}


	public PieceData[][] copyBoard(){
		PieceData[][] newBoard = new PieceData[board.length][board[0].length];
		for(int y = 0; y < board.length; y++) {
			for(int x = 0; x < board.length; x++) {
				newBoard[y][x] = board[y][x].copy();
			}
		}
		return newBoard;
	}

}
