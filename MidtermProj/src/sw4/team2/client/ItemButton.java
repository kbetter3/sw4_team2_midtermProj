package sw4.team2.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ItemButton extends JButton {
	private boolean isBig = false;
	private ImageIcon[] normalImage, pressedImage;
	
	public ItemButton(ImageIcon[] normalImage, ImageIcon[] pressedImage) {
		super();
	
		this.normalImage = normalImage;
		this.pressedImage = pressedImage;
		
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorder(null);
		
		this.setIcon(normalImage[0]);
		
		event();
	}
	
	public boolean getIsBig() {
		return isBig;
	}
	
	public ImageIcon[] getNormalImage() {
		return normalImage;
	}

	public ImageIcon[] getPressedImage() {
		return pressedImage;
	}
	
	private void event() {
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				/*
				if (isBig)
					ItemButton.this.setIcon(pressedImage[4]);
				else
					ItemButton.this.setIcon(pressedImage[0]);
				*/
				
				ItemButton.this.setIcon(fit(ItemButton.this, pressedImage[0]));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				/*
				if (isBig)
					ItemButton.this.setIcon(normalImage[4]);
				else
					ItemButton.this.setIcon(normalImage[0]);
				*/
				
				ItemButton.this.setIcon(fit(ItemButton.this, normalImage[0]));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				BtnThread bt;
				bt = new BtnThread(ItemButton.this, ItemButton.this.getNormalImage(), ItemButton.this.getIsBig());
				bt.setDaemon(true);
				bt.start();
				
				isBig = !isBig;
			}
		});
	}
	
	class BtnThread extends Thread {
		private ItemButton btn;
		private ImageIcon[] normalIcon;
		private boolean isBig;
		
		public BtnThread(ItemButton btn, ImageIcon[] normalIcon, boolean isBig) {
			this.btn = btn;
			this.normalIcon = normalIcon;
			this.isBig = isBig;
		}
		
		@Override
		public void run() {
			for (int i = 1; i < 5; i++) {
				if (isBig)
					small(i);
				else
					big(i);
				
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void small(int i) {
			btn.setSize(btn.getWidth() - 4, btn.getHeight() - 4);
//			btn.setIcon(normalIcon[4 - i]);
			btn.setPreferredSize(new Dimension(btn.getPreferredSize().width - 4, btn.getPreferredSize().height - 4));
			btn.setIcon(fit(btn, normalIcon[0]));
		}
		
		private void big(int i) {
			btn.setSize(btn.getWidth() + 4, btn.getHeight() + 4);
//			btn.setIcon(normalIcon[i]);
			btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 4, btn.getPreferredSize().height + 4));
			btn.setIcon(fit(btn, normalIcon[0]));
		}
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
