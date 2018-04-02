package sw4.team2.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class BasicButton extends JButton {
	private boolean isSelect = false;
	private ImageIcon bgIcon;
	private BufferedImage normalImg, pressedImg, bgImg;
	
	public BasicButton(ImageIcon icon) {
		super(icon);
		
		this.bgIcon = icon;
		
		try {
			this.bgImg = this.normalImg = ImageIO.read(new File("img/normalBtn.png"));
			this.pressedImg = ImageIO.read(new File("img/pressedBtn.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.setSize(100, 100);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorder(null);
		
		this.setIcon(ImageResizer.fit(this, bgIcon));
		
		event();
	}	

	private void event() {
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				bgImg = pressedImg;
				BasicButton.this.repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				bgImg = normalImg;
				BasicButton.this.repaint();
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isSelect) {
					bgImg = pressedImg;
				} else {
					bgImg = normalImg;
				}
				BasicButton.this.repaint();
				isSelect = !isSelect;
				System.out.println("k");
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(bgImg, 0, 0, BasicButton.this.getWidth(), BasicButton.this.getHeight(), BasicButton.this);
		g.drawImage(ImageResizer.fitImage(BasicButton.this, bgIcon), 0, 0, BasicButton.this);
	}
}
