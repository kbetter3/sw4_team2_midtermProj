package sw4.team2.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ItemButton extends JButton {
	private ImageIcon bgIcon;
	private BufferedImage normalImg, pressedImg, bgImg;
	private MouseListener listener;
	
	public ItemButton(ImageIcon icon, int width, int height) {
		super(icon);
		
		this.bgIcon = icon;
		
		try {
			this.bgImg = this.normalImg = ImageIO.read(new File("img/normalBtn.png"));
			this.pressedImg = ImageIO.read(new File("img/pressedBtn.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.setSize(width, height);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorder(null);
		
		this.setIcon(ImageResizer.fit(this, bgIcon));
		
		event();
		this.repaint();
	}	

	private void event() {
		listener = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				bgImg = pressedImg;
				ItemButton.this.repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				bgImg = normalImg;
				ItemButton.this.repaint();
			}
		};
		this.addMouseListener(listener);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(bgImg, 0, 0, ItemButton.this.getWidth(), ItemButton.this.getHeight(), ItemButton.this);
		g.drawImage(ImageResizer.fitImage(ItemButton.this, bgIcon), 0, 0, ItemButton.this);
	}
	
	public static ItemButton emptyBtn() {
//		ItemButton btn = new ItemButton(new ImageIcon("img/emptyBtn.png"), 74, 74);
		ItemButton btn = new ItemButton(new ImageIcon("img/GREEN CREAMED MINT.png"), 74, 74);
		
		btn.setEnabled(false);
		btn.removeMouseListener(btn.listener);
		return btn;
	}
}