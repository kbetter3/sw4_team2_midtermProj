package sw4.team2.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class RecepieForm extends JDialog{
	//poi쓰기 위한 변수
	static XSSFRow row;
	static XSSFCell cell;
	//이미지
	private ImageIcon icon1 = new ImageIcon("img/reci.png");
	//패널구간
	private JPanel MainPanel = new JPanel();
	//툴바 + 라벨 1개
	private JToolBar toolBar = new JToolBar();
	private JLabel Recipe = new JLabel("");
	//텍스트 표시용 텍스트에어리어
	private JTextArea display = new JTextArea(20,30);
	private JScrollPane Jsp = new JScrollPane(display);
	
	public RecepieForm(Frame f, boolean modal){
		super(f, modal);
		
		display();
		event();
		menu();
		this.setTitle("필기노트!");
		this.setSize(500, 500);
		this.setLocationByPlatform(true);
		this.setResizable(false);
		this.setVisible(true);
	}
	public void display() {
		this.setContentPane(MainPanel);
		NotePad();
		ToolBar();
		try {
			Note();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void menu() {
		
	}
	public void event() {
	
	}
	public void ToolBar() {
		MainPanel.add(toolBar,BorderLayout.NORTH);
		toolBar.setBackground(new Color(255, 228, 225));
		Recipe.setIcon(icon1);
		toolBar.add(Recipe);
	}
	public void NotePad() {
		MainPanel.setLayout(new BorderLayout());
		MainPanel.add(Jsp);
		display.setEditable(false);
	}
	
	public void Note() throws IOException {
		//display.setText("");
		String value ="";
		int cnt=0;
		List<String> notepad = new ArrayList(); 
		FileInputStream fis = new FileInputStream(new File("files","CocktailRecepieDB.xlsx"));
		XSSFWorkbook note = new XSSFWorkbook(fis);
		int rowindex=0;
		int columindex=0;
		for(int i=0;i<39;i++) {
			XSSFSheet sheet = note.getSheetAt(i);
			int rows=sheet.getPhysicalNumberOfRows();
			for(rowindex=0;rowindex<rows;rowindex++) {
				XSSFRow row = sheet.getRow(rowindex);
				if(row!=null) {
					XSSFCell cell = row.getCell(columindex);
					value=cell.getStringCellValue();
					display.append(value+"\n");
				}
			}
			display.append("\n");
		}
		//테두리 적용
		Border line = BorderFactory.createLineBorder(Color.pink, 3);
		Border title = BorderFactory.createTitledBorder(line,"Cocktail Recipe");
		display.setBorder(title);
	}
}