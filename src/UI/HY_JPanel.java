package UI;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class HY_JPanel extends JPanel{
	String strBgPicPath;
	
	public HY_JPanel(String str) {
		this.strBgPicPath = str;
	}	
	
	protected void paintComponent(Graphics g) {
		ImageIcon icon = new ImageIcon(this.strBgPicPath);
		Image img = icon.getImage();
		g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());		
	}
}
