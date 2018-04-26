import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

public class SwitchSidesButton implements ActionListener{
	
	private int width,height,x,y;
	private JToolBar radioPanel;
	
	public SwitchSidesButton(JFrame frame,int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		
		ButtonGroup buttons = new ButtonGroup();
		
		JRadioButton button_white = new JRadioButton("Show White");
		JRadioButton button_black = new JRadioButton("Show Black");
		JRadioButton button_relative = new JRadioButton("Show Current Turn");
		
		button_white.setActionCommand("1");
		button_black.setActionCommand("-1");
		button_relative.setActionCommand("0");
		
		buttons.add(button_white);
		buttons.add(button_black);
		buttons.add(button_relative);
		
		button_white.setSelected(true);
		
		button_white.addActionListener(this);
		button_black.addActionListener(this);
		button_relative.addActionListener(this);
		
		//JPanel radioPanel = new JPanel(new GridLayout(0,1));
		radioPanel = new JToolBar("View");
		radioPanel.add(button_white);
		radioPanel.add(button_black);
		radioPanel.add(button_relative);
		
		//radioPanel.setBounds(x, y, width, height);
		
		
		
		frame.add(radioPanel,BorderLayout.PAGE_START);
		
		
	}
	
	public JToolBar getRadioPanel() {
		return radioPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int side = Integer.parseInt(e.getActionCommand());
		if(side == 0) {
			Settings.showSwitch = true;
		}else {
			Settings.showSwitch = false;
			Settings.drawSide = side;
		}
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

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
