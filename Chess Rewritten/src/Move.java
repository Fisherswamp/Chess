
public class Move {
	private PieceData piece;
	private PieceData capture;
	private PieceData rook;
	private PieceData promote;
	private Position fromPos,toPos,rookFromPos,rookToPos,epPos;
	private boolean pieceHasMoved;
	private boolean isCastle;
	private boolean isPromotion = false;
	private boolean isEPCapture = false;
	private boolean pawnIsEPCapturable;
	
	public Move(PieceData piece, PieceData capture, Position fromPos, Position toPos) {
		this.piece = piece;
		this.capture = capture;
		this.fromPos = fromPos;
		this.toPos = toPos;
		this.setPieceHasMoved(piece.hasMoved());
		setCastle(false);
		if(Math.abs(piece.getPiece().getValue()) == 1) {//if is pawn
			pawnIsEPCapturable = piece.isEpCapturable();
		}
	}
	public Move(PieceData piece, Position fromPos, Position toPos,PieceData rook,Position rookFromPos,Position rookToPos) {
		this(piece, Board.EMPTY_PIECE, fromPos, toPos);
		setCastle(true);
		this.rook = rook;
		this.setRookFromPos(rookFromPos);
		this.setRookToPos(rookToPos);
	}
	public Move(PieceData piece, PieceData capture, Position fromPos, Position toPos,PieceData promote) {
		this(piece, capture, fromPos, toPos);
		setPromotion(true);
		this.promote = promote;
	}
	public Move(PieceData piece, PieceData capture, Position fromPos, Position toPos, Position epCapturePos) {
		this(piece, capture, fromPos, toPos);
		setEPCapture(true);
		setEpPos(epCapturePos);		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capture == null) ? 0 : capture.hashCode());
		result = prime * result + ((epPos == null) ? 0 : epPos.hashCode());
		result = prime * result + ((fromPos == null) ? 0 : fromPos.hashCode());
		result = prime * result + (isCastle ? 1231 : 1237);
		result = prime * result + (isEPCapture ? 1231 : 1237);
		result = prime * result + (isPromotion ? 1231 : 1237);
		result = prime * result + (pawnIsEPCapturable ? 1231 : 1237);
		result = prime * result + ((piece == null) ? 0 : piece.hashCode());
		result = prime * result + (pieceHasMoved ? 1231 : 1237);
		result = prime * result + ((promote == null) ? 0 : promote.hashCode());
		result = prime * result + ((rook == null) ? 0 : rook.hashCode());
		result = prime * result + ((rookFromPos == null) ? 0 : rookFromPos.hashCode());
		result = prime * result + ((rookToPos == null) ? 0 : rookToPos.hashCode());
		result = prime * result + ((toPos == null) ? 0 : toPos.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (capture == null) {
			if (other.capture != null)
				return false;
		} else if (!capture.equals(other.capture))
			return false;
		if (epPos == null) {
			if (other.epPos != null)
				return false;
		} else if (!epPos.equals(other.epPos))
			return false;
		if (fromPos == null) {
			if (other.fromPos != null)
				return false;
		} else if (!fromPos.equals(other.fromPos))
			return false;
		if (isCastle != other.isCastle)
			return false;
		if (isEPCapture != other.isEPCapture)
			return false;
		if (isPromotion != other.isPromotion)
			return false;
		if (pawnIsEPCapturable != other.pawnIsEPCapturable)
			return false;
		if (piece == null) {
			if (other.piece != null)
				return false;
		} else if (!piece.equals(other.piece))
			return false;
		if (pieceHasMoved != other.pieceHasMoved)
			return false;
		if (promote == null) {
			if (other.promote != null)
				return false;
		} else if (!promote.equals(other.promote))
			return false;
		if (rook == null) {
			if (other.rook != null)
				return false;
		} else if (!rook.equals(other.rook))
			return false;
		if (rookFromPos == null) {
			if (other.rookFromPos != null)
				return false;
		} else if (!rookFromPos.equals(other.rookFromPos))
			return false;
		if (rookToPos == null) {
			if (other.rookToPos != null)
				return false;
		} else if (!rookToPos.equals(other.rookToPos))
			return false;
		if (toPos == null) {
			if (other.toPos != null)
				return false;
		} else if (!toPos.equals(other.toPos))
			return false;
		return true;
	}
	/**
	 * Converts a move to PGN notation-ish (dont have checks or piece differentiation when on same row/col yet)
	 */
	public String toString() {
		String pieceString;
		if(piece.getPiece() == Piece.whiteKnight || piece.getPiece() == Piece.blackKnight) {
			pieceString = "N";
		}else if(piece.getPiece() == Piece.whitePawn || piece.getPiece() == Piece.blackPawn){
			if(isPromotion()) {	
				return toPos.toString() + "=" + promote.getPiece().toString().charAt(5);
			}else {
				pieceString = "";
			}
		}else {
			pieceString = piece.getPiece().toString().charAt(5) + "";
		}
		
		if(isCastle) {
			if(rookFromPos.x > fromPos.x) {
				return "O-O";
			}else {
				return "O-O-O";
			}
		}
		if(capture.getPiece() == Piece.empty) {
			return pieceString + toPos.toString(); 
		}else {
			if(piece.getPiece() == Piece.whitePawn || piece.getPiece() == Piece.blackPawn)
			pieceString = fromPos.toString().charAt(0) + "";
			return pieceString + "x" + toPos.toString();
		}
	}
	
	public PieceData getPiece() {
		return piece;
	}

	public PieceData getCapture() {
		return capture;
	}

	public Position getFromPos() {
		return fromPos;
	}

	public Position getToPos() {
		return toPos;
	}

	public void setPiece(PieceData piece) {
		this.piece = piece;
	}

	public void setCapture(PieceData capture) {
		this.capture = capture;
	}

	public void setFromPos(Position fromPos) {
		this.fromPos = fromPos;
	}

	public void setToPos(Position toPos) {
		this.toPos = toPos;
	}

	public boolean isPieceHasMoved() {
		return pieceHasMoved;
	}

	public void setPieceHasMoved(boolean pieceHasMoved) {
		this.pieceHasMoved = pieceHasMoved;
	}
	public Position getRookFromPos() {
		return rookFromPos;
	}
	public void setRookFromPos(Position rookFromPos) {
		this.rookFromPos = rookFromPos;
	}
	public boolean isCastle() {
		return isCastle;
	}
	public void setCastle(boolean isCastle) {
		this.isCastle = isCastle;
	}
	public Position getRookToPos() {
		return rookToPos;
	}
	public void setRookToPos(Position rookToPos) {
		this.rookToPos = rookToPos;
	}
	public PieceData getRook() {
		return rook;
	}
	public void setRook(PieceData rook) {
		this.rook = rook;
	}
	public boolean isPromotion() {
		return isPromotion;
	}
	public void setPromotion(boolean isPromotion) {
		this.isPromotion = isPromotion;
	}
	public PieceData getPromote() {
		return promote;
	}
	public void setPromote(PieceData promote) {
		this.promote = promote;
	}
	public boolean isEPCapture() {
		return isEPCapture;
	}
	public void setEPCapture(boolean isEPCapture) {
		this.isEPCapture = isEPCapture;
	}
	public Position getEpPos() {
		return epPos;
	}
	public void setEpPos(Position epPos) {
		this.epPos = epPos;
	}
	public boolean isPawnIsEPCapturable() {
		return pawnIsEPCapturable;
	}
	public void setPawnIsEPCapturable(boolean pawnIsEPCapturable) {
		this.pawnIsEPCapturable = pawnIsEPCapturable;
	}
	
}
