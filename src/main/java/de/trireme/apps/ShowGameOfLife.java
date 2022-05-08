package de.trireme.apps;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShowGameOfLife extends JPanel {

	private boolean firstGen = true;
	private String[][] matrix4GameOfLife = {{"0","1","1","1","1","0","1","0","0","0"},{"0","1","0","1","0","1","0","1","0","1"},{"0","0","1","0","0","1","1","1","0","0"},{"0","0","1","1","1","0","0","0","0","0"},{"0","0","0","0","0","0","1","0","1","1"},{"1","0","1","1","1","0","1","1","1","1"},{"0","0","0","0","1","0","0","1","0","1"},{"0","0","0","0","0","1","0","0","0","0"},{"0","1","0","1","0","0","0","1","1","0"},{"1","0","0","1","1","1","1","0","1","0"}};

//	private String[][] matrix4GameOfLife;

	private static final long serialVersionUID = 1290794342643541524L;

	public void paint(Graphics g) {
		
		int vertLength = 25;
		int horizontalLength = 25;
		
		int cellRectWidth = 20;
		int cellRectHeight = 20;
		
		int rectWidth = vertLength * cellRectWidth;
		int rectHeigth = horizontalLength * cellRectHeight;

		if (firstGen) {
			matrix4GameOfLife = new String[vertLength][horizontalLength];
			//matrix4GameOfLife = GameOfLife.buildRandomMatrix4GameOfLife(matrix4GameOfLife);
			GameOfLife.buildRandomMatrix4GameOfLife(matrix4GameOfLife);

			firstGen = false;
			paintNextGeneration(matrix4GameOfLife, g, rectWidth, rectHeigth);
		} else {
			matrix4GameOfLife = GameOfLife.nextGeneration(matrix4GameOfLife);
			paintNextGeneration(matrix4GameOfLife, g, rectWidth, rectHeigth);
		}

	}

	private void paintNextGeneration(String[][] matrix4GameOfLife, Graphics g, int width, int height) {
		g.setColor(Color.BLACK);
		g.fillRect(100, 100,  height, width);

		int smallWidth = width / matrix4GameOfLife.length;
		int smallHeight = height / matrix4GameOfLife[0].length;

		for (int x = 0; x < matrix4GameOfLife.length; x++) {
			for (int y = 0; y < matrix4GameOfLife[x].length; y++) {

				if (matrix4GameOfLife[x][y].equals("1")) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.YELLOW);
				}

				if (y == 0 && x == 0) {
					g.fillRect(100 + y + 1, 100 + x + 1, smallWidth - 1, smallHeight - 1);
				} else if (y == 0) {
					g.fillRect(100 + y + 1, 100 + x * smallHeight + 1, smallWidth - 1, smallHeight - 1);
				} else if (x == 0) {
					g.fillRect(100 + y * smallWidth + 1, 100 + x + 1, smallWidth - 1, smallHeight - 1);
				} else {
					g.fillRect(100 + y * smallWidth + 1, 100 + x * smallHeight + 1, smallWidth - 1, smallHeight - 1);
				}
			}
		}

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(800, 800);
		frame.getContentPane().add(new ShowGameOfLife());
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		MouseListener listen = new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				frame.repaint();
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}
		};
		frame.addMouseListener(listen);
		
		for( int i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);
				frame.repaint();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		}
	}
}
