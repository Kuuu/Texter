import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.ComponentOrientation;
import java.nio.file.*;
import java.util.*;
import java.io.IOException;
import java.io.File;

public class Texter {

	JFrame frame;
	JTextArea field;
	JScrollPane scroll;
	JFileChooser fileChooser;
	JMenuItem[] fontItems;
	JMenuItem[] sizeButtons;
	
	JButton quitButton;
	JButton maximizeButton;
	JButton minimizeButton;
	
	int currentSize;
	String currentFont;
	Color currentColor;
	
	String[] fonts = {"Arial", "Calibri", "Comic Sans MS", "Times New Roman", "Andy"};
	int[] sizes = {11, 14, 18, 24, 36, 48, 72};

	
	
	//----------------------------------------------------------  MAIN --------------------------------------------------------------
	public static void main(String[] args) {
		Texter app = new Texter();
		app.setVariablesAndUI();
		app.buildGUI();
	}
	//----------------------------------------------------------  END OF MAIN --------------------------------------------------------------
	
	
	public void setVariablesAndUI() {
		currentSize = 24;
		currentFont = "Times New Roman";
		currentColor = new Color(82, 207, 98);
		
		/*UIManager.put("TabbedPane.background", currentColor);
		UIManager.put("TabbedPane.selected", currentColor);*/
		
		UIManager.put("MenuBar.border", currentColor);
		UIManager.put("MenuBar.selectionBackground", currentColor);
		UIManager.put("MenuBar.shadow", currentColor);
		
		UIManager.put("MenuItem.background", currentColor);
		UIManager.put("MenuItem.selectionBackground", currentColor);
		UIManager.put("MenuItem.border", BorderFactory.createLineBorder(currentColor, 1));
		
		UIManager.put("Menu.background", currentColor);
		UIManager.put("Menu.selectionBackground", currentColor);
		UIManager.put("Menu.border", BorderFactory.createLineBorder(currentColor, 1));
		
		UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(currentColor, 1));
		UIManager.put("PopupMenu.background", currentColor);
		UIManager.put("PopupMenu.selectionBackground", currentColor);
		
		
		UIManager.put("Button.select", currentColor);
	}
	
	//----------------------------------------------------------  BUILD GUI --------------------------------------------------------------
	
	public void buildGUI() {
		fileChooser = new JFileChooser();
	
		frame = new TexterWindow();
		
		MyMenuBar menuBar = new MyMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem newItem = new JMenuItem("New");
		newItem.addActionListener(new NewListener());
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new SaveListener());

		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new OpenListener());

		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new QuitListener());	
		
		
		
		fileMenu.add(newItem);
		fileMenu.add(saveItem);
		fileMenu.add(openItem);
		fileMenu.add(quitItem);
		
		menuBar.add(fileMenu);
		
		JMenu editorMenu = new JMenu("Editor");
		
		JMenu fontMenu = new JMenu("Font");
		fontItems = new JMenuItem[fonts.length];
		for (int i = 0; i < fonts.length; i++) {
			fontItems[i] = new JMenuItem(fonts[i]);
			fontItems[i].addActionListener(new FontListener(fonts[i]));
			fontMenu.add(fontItems[i]);
		}
		editorMenu.add(fontMenu);
		
		JMenu sizeMenu = new JMenu("Size");
		sizeButtons = new JMenuItem[sizes.length];
		for (int i = 0; i < sizes.length; i++) {
			sizeButtons[i] = new JMenuItem(Integer.toString(sizes[i]));
			sizeButtons[i].addActionListener(new SizeListener(sizes[i]));
			sizeMenu.add(sizeButtons[i]);
		}
		editorMenu.add(sizeMenu);
		
		menuBar.add(editorMenu);
		
		ImageIcon quitIcon = new ImageIcon("resources/quitButton.png");
		quitButton = new MenuButton(quitIcon);
		quitButton.addActionListener(new QuitListener());	
		
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(quitButton);
		
		
		
	
		field = new JTextArea();
		field.setFont(new Font(currentFont, Font.PLAIN, currentSize));
		scroll = new JScrollPane(field);
		
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.CENTER, scroll);
		
		
		frame.setVisible(true);
	}
	//----------------------------------------------------------  END OF BUILD GUI --------------------------------------------------------------
	
	
	
	
	//----------------------------------------------------------  UTIL METHODS --------------------------------------------------------------
	public void saveFile(Path path, List<String> text) {
		try {
		
			Files.write(path, text);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openFile(Path path) {
		try {
				
			List<String> text = Files.readAllLines(path);
			clear();
			for (String line : text) {
				field.append(line);
				field.append("\n");
			}
				
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	public void clear() {
		field.setText("");
	}
	
	public void quit() {
		for (float opacity = 1.0f; opacity > 0.05f; opacity -= 0.05f) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {e.printStackTrace();}
			frame.setOpacity(opacity);
		}
		System.exit(0);
	}
	
	//----------------------------------------------------------  END OF UTIL METHODS --------------------------------------------------------------
	
	
	
	
	
	//----------------------------------------------------------  BUTTON LISTENERS --------------------------------------------------------------
	
	class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			clear();
		}
	}
	
	class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			fileChooser.showSaveDialog(frame);
			
			Path path = fileChooser.getSelectedFile().toPath();
			ArrayList<String> text = new ArrayList<String>(Arrays.asList(field.getText().split("\n")));
			
			saveFile(path, text);
		}
	}
	
	class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			fileChooser.showOpenDialog(frame);
			
			Path path = fileChooser.getSelectedFile().toPath();
			openFile(path);
		}
	}
	
	class QuitListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			quit();
		}
	}
	
	class FontListener implements ActionListener {
		String name;
		
		public FontListener(String n) {
			name = n;
		}
	
		public void actionPerformed(ActionEvent a) {
			currentFont = name;
			Font font = new Font(currentFont, Font.PLAIN, currentSize);
			field.setFont(font);
		}
	}
	
	class SizeListener implements ActionListener {
		int size;
		
		public SizeListener(int s) {
			size = s;
		}
	
		public void actionPerformed(ActionEvent a) {
			currentSize = size;
			Font font = new Font(currentFont, Font.PLAIN, currentSize);
			field.setFont(font);
		}
	}
	
	//----------------------------------------------------------  END OF BUTTON LISTENERS --------------------------------------------------------------
	
	
	
	
	//----------------------------------------------------------  MENU CLASS --------------------------------------------------------------
	
	class MyMenuBar extends JMenuBar {
		int posX;
		int posY;
		int sizeX = frame.getWidth();
		int sizeY = 45;

		public MyMenuBar() {
			setPreferredSize(new Dimension(sizeX, sizeY));
			
			addMouseListener(new MouseAdapter() {	
				public void mousePressed(MouseEvent e)
				{
					posX=e.getX();
					posY=e.getY();
				}
			});
		
			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {	
					Point location = frame.getLocation();
					frame.setLocation(location.x+(e.getX()-posX), location.y+(e.getY()-posY));
				}
			});
		}
		
		public void setColor(Color color) {
			currentColor=color;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(currentColor);
			g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

		}
	}
	
	//----------------------------------------------------------  END OF MENU CLASS --------------------------------------------------------------
	
	
	
	
	//----------------------------------------------------------  MENU BUTTON CLASS --------------------------------------------------------------
	class MenuButton extends JButton {

        private Color hoverBackgroundColor;
        private Color pressedBackgroundColor;

		public MenuButton(ImageIcon icon) {
			super(icon);
			setBorder(null);
			setBackground(currentColor);
			this.pressedBackgroundColor = currentColor;
			this.hoverBackgroundColor = currentColor;
		}

        @Override
        protected void paintComponent(Graphics g) {
            /*if (getModel().isPressed()) {
                g.setColor(pressedBackgroundColor);
            } else if (getModel().isRollover()) {
                g.setColor(hoverBackgroundColor);
            } else {
                g.setColor(getBackground());
            }*/
			g.setColor(currentColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }

        @Override
        public void setContentAreaFilled(boolean b) {
        }

        public Color getHoverBackgroundColor() {
            return hoverBackgroundColor;
        }

        public void setHoverBackgroundColor(Color hoverBackgroundColor) {
            this.hoverBackgroundColor = hoverBackgroundColor;
        }

        public Color getPressedBackgroundColor() {
            return pressedBackgroundColor;
        }

        public void setPressedBackgroundColor(Color pressedBackgroundColor) {
            this.pressedBackgroundColor = pressedBackgroundColor;
        }
    }
	
	//----------------------------------------------------------  END OF MENU BUTTON CLASS --------------------------------------------------------------
}