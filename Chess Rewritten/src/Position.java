
public class Position {
	public int x,y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return (char)('a' + x) + "" + (Settings.boardSize - y) + " (" + x + "," + y + ")";
	}
	
	public int[] toPixelCoords() {
		int startX,startY,incX,incY,pixelX,pixelY;
		
		if(Settings.drawSide == -1) {
			startX = Settings.xOffset;
			startY = Settings.yOffset+(Settings.boardSize-1)*Settings.squareSize;
			incX = 1;
			incY = -1;
		}else {
			startX = Settings.xOffset+(Settings.boardSize-1)*Settings.squareSize;
			startY = Settings.yOffset;
			incX = -1;
			incY = 1;
		}
		pixelX = startX + (Settings.boardSize-(x+1))*incX*Settings.squareSize;
		pixelY = startY + (y+Settings.drawSide)*incY*Settings.squareSize;
		
		int[] returnVal = {pixelX,pixelY};
		return returnVal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
