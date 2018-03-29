package sw4.team2.client;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class BasicButton extends JButton {
	private ImageIcon normalIcon, pressedIcon;
	
	public BasicButton(ImageIcon normalIcon, ImageIcon pressedIcon) {
		super();
		
		this.normalIcon = normalIcon;
		this.pressedIcon = pressedIcon;
		
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorder(null);
		
		this.setIcon(normalIcon);
		
		event();
	}

	private void event() {
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				BasicButton.this.setIcon(pressedIcon);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				BasicButton.this.setIcon(normalIcon);
			}
		});
	}
	
	private ImageIcon fit(Component c, ImageIcon icon) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		BufferedImage resize = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = resize.getGraphics();
		g.drawImage(icon.getImage(), 0, 0, width, height, null);
		
		return new ImageIcon(resize);
	}
}
