import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * @author Itai Rivkin-Fish
 * @version 9/12/17
 * 
 *  This class does all of the game and GUI handling
 */ 
public class Game extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	static long gameStartTime;
	private boolean isRunning = false;
	private Thread thread;
	
	private final int xSize,ySize;

	private Board board;
	
	/**
	 * 
	 * @param xSize - Screen size X
	 * @param ySize - Screen size Y
	 * @param game - Ches game to access
	 */
	public Game(int xSize, int ySize, Board b) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.board = b;
		
		new Window(xSize,ySize,"Chess",this);
		
		gameStartTime = System.nanoTime();
		//Mouse listener to handle mouse clicks
		addMouseListener(
			new MouseAdapter() { 
	          public void mousePressed(MouseEvent me) { 
	            handleMouse(me); 
	          } 
			}
		); 
		
		start();
	}
	/**
	 * 
	 * @param me - mouse event to handle
	 * this function checks mouse clicks and deals with them
	 */
	protected void handleMouse(MouseEvent me) {
		
		if(me.getButton() == 1) {//Left Click
			int mouseX = me.getX();
			int mouseY = me.getY();
			
			Position clickedOn = coordsToPosition(mouseX,mouseY,Settings.drawSide);
			
			//System.out.println(board.getPiece(clickedOn).getPiece() + " X: " + clickedOn.x + ", Y: " + clickedOn.y);
			if(clickedOn == null) {
				System.out.println("Null position clicked on");
				return;
			}
			
			PieceData selectedPiece = board.getWorkingPiece(clickedOn);
			
			if(selectedPiece != null) {
				
				if(selectedPiece != Board.EMPTY_PIECE && selectedPiece.getPiece().getSide() == Settings.side) {
					Settings.selected = selectedPiece;
					Settings.selectedCoords = clickedOn;
				}else {
					ArrayList<Move> moves = board.findLegalMoves(Settings.side);//,board.getWorkingBoard());
					System.out.println(moves.size() + " moves found");
					for(Move m : moves) {
						if(m.getPiece() == Settings.selected &&  m.getToPos().equals(clickedOn)) {	
							Settings.selected = null;
							board.move(m, true);
							Settings.side *= -1;
							if(Settings.showSwitch) {
								Settings.drawSide = Settings.side;
							}
							break;
						}
					}
					
					//Settings.selected = null;
				}
			}
			
		}else if(me.getButton() == 3) {//right click
			int mouseX = me.getX();
			int mouseY = me.getY();
			
			System.out.println(Settings.side);
		}
		
	}
	/**
	 * 
	 * @param x - x coord on screen
	 * @param y - y coord on screen
	 * @return Position object of where the mouse clicked
	 */
	public Position coordsToPosition(int x, int y, int side) {
		//if within board
		if(x > Settings.xOffset && y > Settings.yOffset && x < Settings.xOffset + (Settings.boardSize*Settings.squareSize) && y < Settings.yOffset + (Settings.boardSize*Settings.squareSize)) {
		
			int trueX = (x-Settings.xOffset)/Settings.squareSize;
			int trueY = Settings.boardSize - ((y-Settings.yOffset)/Settings.squareSize);
			
			if(side == -1) {
				return new Position((Settings.boardSize-1)-trueX,trueY-1);
			}else {
				return new Position(trueX,(Settings.boardSize+1)-trueY-1);
			}
		}
		//if not within board return null
		return null;
	}
	/**
	 * Starts a thread and sets isRunning to true
	 */
	private void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	/**
	 * Stops a thread and sets isRunning to false
	 */
	private void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Code that runs constantly- calling the render and tick functions
	 * Borrowed from Zachary Berenger who copied it from Notch who copied it from who knows?
	 */
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double ticksPerSecond = 30.0;
		double nanoSecondsPerTick = Math.pow(10, 9)/ticksPerSecond;
		double ticksToRender = 0;
		long fpsTimer = System.currentTimeMillis();
		int frames = 0;
		while(isRunning) {
			long currentTime = System.nanoTime();
			ticksToRender += (currentTime - lastTime)/nanoSecondsPerTick;
			lastTime = currentTime;
			while(ticksToRender >= 1) {
//				render();
				tick();
				ticksToRender--;
			}
			render();
			
			frames++;
			if(System.currentTimeMillis() - fpsTimer > 1000) {
				//System.out.println(frames);
				fpsTimer += 1000;
				frames = 0;
			}
		}
		stop();
	}
	
	public void tick() {
		
	}
	/**
	 * 
	 * @param g - graphics object
	 * @param toDraw - string to draw
	 * @param drawX - x position of draw
	 * @param drawY - y position of draw
	 * @param outlineColor - color to outline
	 */
	public void drawStringOutline(Graphics g,String toDraw,int drawX, int drawY, Color outlineColor) {
		Color savedColor = g.getColor();
		
		g.setColor(outlineColor);
		
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				g.drawString(toDraw, drawX + i, drawY + j);
			}
		}
		
		g.setColor(savedColor);
		
		g.drawString(toDraw, drawX, drawY);
		
	}
	/**
	 * overload of previous function
	 * @param g - graphics object
	 * @param toDraw - string to draw
	 * @param drawX - x position of draw
	 * @param drawY - y position of draw
	 * @param outlineColor - color to outline
	 * @param outlineWidth - Width of outline
	 */
	public void drawStringOutline(Graphics g,String toDraw,int drawX, int drawY, Color outlineColor, int outlineWidth) {
		Color savedColor = g.getColor();
		
		g.setColor(outlineColor);
		
		for(int i = -outlineWidth; i <= outlineWidth; i++) {
			for(int j = -outlineWidth; j <= outlineWidth; j++) {
				g.drawString(toDraw, drawX + i, drawY + j);
			}
		}
		
		g.setColor(savedColor);
		
		g.drawString(toDraw, drawX, drawY);
		
	}
	
	/**
	 * Draws everything that needs drawing
	 */
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		/***BEGIN DRAW***/
		//Clear draw
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, xSize, ySize);
		//draw background
		drawBoard(g);
		drawCoords(g);
		//draw selected piece moves
		drawSelectionMoves(g);
		//draw if king is in check
		drawKingCheck(g);
		//draw the pieces
		drawPieces(g);
		
		/***END DRAW***/
		g.dispose();
		bs.show();
	}
	private void drawPieces(Graphics g) {
		g.setFont(Settings.mainFont);
		for(int y = 0; y < board.getWorkingBoard().length; y++) {
			for(int x = 0; x < board.getWorkingBoard()[y].length; x++) {
				Position pos = new Position(x,y);
				int[] position = pos.toPixelCoords();
				PieceData p = board.getWorkingBoard()[y][x];
				g.drawString(p.getPiece().getDisplay() + "", position[0], position[1]);
			}
		}
	}
	private void drawKingCheck(Graphics g) {
//		if(Settings.whiteKingInCheck) {
//			int[] drawCoords = board.findKing(Side.White).position.toPixelCoords();
//			g.setColor(Color.RED);
//			g.fillRect(drawCoords[0], drawCoords[1], Settings.squareSize, Settings.squareSize);
//			g.setColor(Color.BLACK);
//			g.drawRect(drawCoords[0], drawCoords[1], Settings.squareSize, Settings.squareSize);
//		}
//		if(Settings.blackKingInCheck) {
//			int[] drawCoords = board.findKing(Side.Black).position.toPixelCoords();
//			g.setColor(Color.RED);
//			g.fillRect(drawCoords[0], drawCoords[1], Settings.squareSize, Settings.squareSize);
//			g.setColor(Color.BLACK);
//			g.drawRect(drawCoords[0], drawCoords[1], Settings.squareSize, Settings.squareSize);
//		}
	}
	
	private void drawSelectionMoves(Graphics g) {
		if(Settings.selected != null) {
			int[] drawCoords =  Settings.selectedCoords.toPixelCoords();
			g.setColor(Color.BLUE);
			g.fillRect(drawCoords[0], drawCoords[1]-Settings.squareSize, Settings.squareSize, Settings.squareSize);
			g.setColor(Color.BLACK);
			g.drawRect(drawCoords[0], drawCoords[1]-Settings.squareSize, Settings.squareSize, Settings.squareSize);
			
			ArrayList<Move> moves = board.findLegalMoves(Settings.side,board.getWorkingBoard());
			for(Move m : moves) {
				if(m.getPiece() == Settings.selected) {
					drawCoords = m.getToPos().toPixelCoords();
					g.setColor(Color.GREEN);
					g.fillRect(drawCoords[0], drawCoords[1]-Settings.squareSize, Settings.squareSize, Settings.squareSize);
					g.setColor(Color.BLACK);
					g.drawRect(drawCoords[0], drawCoords[1]-Settings.squareSize, Settings.squareSize, Settings.squareSize);
					
				}
			}
			
		}
		
	}
	public void drawBoard(Graphics g) {
		g.setColor(Settings.lightColor);
		g.fillRect(Settings.xOffset, Settings.yOffset, Settings.squareSize * Settings.boardSize, Settings.squareSize * Settings.boardSize);
		g.setColor(Settings.darkColor);
		for(int y = 0; y < Settings.boardSize; y++) {
			for(int x = (y%2 == 0 ? 1 : 0); x < Settings.boardSize; x+=2) {
				int squareX = Settings.xOffset + x*Settings.squareSize;
				int squareY = Settings.yOffset + y*Settings.squareSize;
				g.fillRect(squareX,squareY, Settings.squareSize, Settings.squareSize);
			}
		}
		g.setColor(Color.BLACK);
		g.drawRect(Settings.xOffset, Settings.yOffset, Settings.squareSize * Settings.boardSize, Settings.squareSize * Settings.boardSize);
	}
	public void drawCoords(Graphics g) {
		g.setFont(Settings.smallFont);
		g.setColor(Color.BLACK);
		int drawOffset = Settings.squareSize/2;
		int textOffset = g.getFont().getSize()/4;
		if(Settings.drawSide == 1) {
			for(char i = 'A'; i < 'A' + Settings.boardSize; i++) {
				g.drawString(i + "",drawOffset - textOffset + Settings.xOffset +  (i - 'A')*Settings.squareSize, drawOffset + Settings.yOffset + (Settings.squareSize * Settings.boardSize));
			}
			for(int i = 0; i < Settings.boardSize; i++) {
				g.drawString((i+1) + "", drawOffset - textOffset + Settings.xOffset + Settings.squareSize * Settings.boardSize,drawOffset + Settings.xOffset + (Settings.boardSize-i-1)*Settings.squareSize);
			}
		}else {
			for(char i = 'A'; i < 'A' + Settings.boardSize; i++) {
				g.drawString(i + "",drawOffset + textOffset + Settings.xOffset +  (Settings.boardSize - (i - 'A') -1)*Settings.squareSize, drawOffset + Settings.yOffset + (Settings.squareSize * Settings.boardSize));
			}
			for(int i = 0; i < Settings.boardSize; i++) {
				g.drawString((i+1) + "", drawOffset + textOffset + Settings.xOffset + Settings.squareSize * Settings.boardSize,drawOffset + Settings.xOffset + (i)*Settings.squareSize);
			}
		}
	}
	
}
	