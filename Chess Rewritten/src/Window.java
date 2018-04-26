import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 * @author Itai Rivkin-Fish
 * @version 9/12/17
 * 
 * Entire window class code borrowed(and possibly modified) from Zachary Berenger using his tutorial https://www.codingmadesimple.com/wizards-intermediate-java-course/ 
 */
public class Window{
	
	public Window(int width, int height, String title,Game game) {
		 try {
		     UIManager.setLookAndFeel(
		             UIManager.getSystemLookAndFeelClassName());
		     UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//"javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		
		JFrame frame = new JFrame(title);
		
		Dimension size = new Dimension(width,height);
		Dimension maxSize = new Dimension(width*2,height*2);
		frame.setPreferredSize(size);
		frame.setMaximumSize(maxSize);
		frame.setMinimumSize(size);
		PromotePieceDropdown dropDown = new PromotePieceDropdown(frame,width/2,10,100,50);
		SwitchSidesButton showSides = new SwitchSidesButton(frame,0,0,200,80);
		
		showSides.getRadioPanel().add(dropDown.getPieceList());
		
		frame.add(game);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		
		//frame.setIconImage(new Pawn(null,null,Side.Black).image);
	}
	
	
}
