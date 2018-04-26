import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class PromotePieceDropdown implements ActionListener{
	private int width,height,x,y;
	private JComboBox pieceList;
	public PromotePieceDropdown(JFrame frame,int x, int y, int width, int height) {
		this.setWidth(width);
		this.height = height;
		this.x = x;
		this.y = y;
		
		String[] pieceString = {"Queen","Rook","Bishop","Knight"};
		pieceList = new JComboBox(pieceString);
		pieceList.setBounds(x, y, width, height);
		pieceList.setFont(Settings.smallFont);
		pieceList.setSelectedIndex(0);
		pieceList.addActionListener(this);
		pieceList.setMaximumSize(new Dimension(width,height));
		frame.add(pieceList);
	}
	
	 public JComboBox getPieceList() {
		return pieceList;
	}

	public void actionPerformed(ActionEvent e) {
	        JComboBox cb = (JComboBox)e.getSource();
	        String pieceName = (String)cb.getSelectedItem();
	        Settings.promotePiece = pieceName;
	    }

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
