
public class PieceData {
	private boolean hasMoved;
	private boolean epCapturable;
	private final Piece piece;
	private boolean hasCastled;
	
	public boolean hasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public boolean isEpCapturable() {
		return epCapturable;
	}

	public void setEpCapturable(boolean epCapturable) {
		this.epCapturable = epCapturable;
	}

	public boolean hasCastled() {
		return hasCastled;
	}

	public void setHasCastled(boolean hasCastled) {
		this.hasCastled = hasCastled;
	}

	public Piece getPiece() {
		return piece;
	}

	public PieceData(Piece piece) {
		this.piece = piece;
		hasMoved = false;
		epCapturable = false;
		setHasCastled(false);
	}
	
	public boolean isPawn() {
		return piece == Piece.whitePawn || piece == Piece.blackPawn;
	}
	public boolean isKnight() {
		return piece == Piece.whiteKnight || piece == Piece.blackKnight;
	}
	public boolean isRook() {
		return piece == Piece.whiteRook || piece == Piece.blackRook;
	}
	public boolean isQueen() {
		return piece == Piece.whiteQueen || piece == Piece.blackQueen;
	}
	public boolean isBishop() {
		return piece == Piece.whiteBishop || piece == Piece.blackBishop;
	}
	public boolean isKing() {
		return piece == Piece.whiteKing || piece == Piece.blackKing;
	}
	
	public PieceData copy() {
		PieceData newPiece = new PieceData(getPiece());
		newPiece.epCapturable = epCapturable;
		newPiece.hasCastled = hasCastled;
		
		return newPiece;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (epCapturable ? 1231 : 1237);
		result = prime * result + (hasCastled ? 1231 : 1237);
		result = prime * result + (hasMoved ? 1231 : 1237);
		result = prime * result + ((piece == null) ? 0 : piece.hashCode());
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
		PieceData other = (PieceData) obj;
//		if (epCapturable != other.epCapturable)
//			return false;
//		if (hasCastled != other.hasCastled)
//			return false;
//		if (hasMoved != other.hasMoved)
//			return false;
		if (piece != other.piece)
			return false;
		return true;
	}
	
	public String toString() {
		return piece.toString();
	}


	
}
