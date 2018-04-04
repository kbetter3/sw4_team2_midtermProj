package sw4.team2.client;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import sw4.team2.common.Recipe;

public class RecipeButton extends JButton {
	private ImageIcon bgIcon;
	private BufferedImage normalImg, pressedImg, bgImg;
	private MouseListener listenerPress;
	private int type;
	private Recipe recipe;
	
	public RecipeButton(ImageIcon icon, int width, int height, int type, Recipe recipe) {
		super(icon);
		
		this.type = type;
		this.bgIcon = icon;
		this.recipe = recipe;
		
		
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
		this.setEnabled(false);
		
		this.setIcon(ImageResizer.fit(this, bgIcon));
		
		event();
		this.repaint();
	}	

	private void event() {
		listenerPress = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				bgImg = pressedImg;
				RecipeButton.this.repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				bgImg = normalImg;
				RecipeButton.this.repaint();
			}
		};
		this.addMouseListener(listenerPress);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(bgImg, 0, 0, RecipeButton.this.getWidth(), RecipeButton.this.getHeight(), RecipeButton.this);
		g.drawImage(ImageResizer.fitImage(RecipeButton.this, bgIcon), 0, 0, RecipeButton.this);
	}
	
	public int getType() {
		return this.type;
	}
	
	public Recipe getRecipe() {
		return this.recipe;
	}
	
	public static RecipeButton emptyBtn() {
		RecipeButton btn = new RecipeButton(new ImageIcon("img/emptyBtn.png"), 74, 74, -1, null);
		
		btn.setEnabled(false);
		btn.setBorder(null);
		btn.removeMouseListener(btn.listenerPress);
		return btn;
	}
}
