package de.trireme.apps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShowPrimesFromFile extends JPanel {

	private boolean firstGen = true;

	private String[][] matrixToShow;

	private static final long serialVersionUID = 1290794342643541524L;

	public ShowPrimesFromFile(String[][] matrix) {
		this.matrixToShow = matrix;
	}

	public void paint(Graphics g) {

		int vertLength = matrixToShow.length;
		int horizontalLength = matrixToShow[0].length;

		int cellRectWidth = 20;
		int cellRectHeight = 20;

		int rectWidth = vertLength * cellRectWidth;
		int rectHeigth = horizontalLength * cellRectHeight;

		matrixToShow = new String[vertLength][horizontalLength];
		
		paintNextGeneration(matrixToShow, g, rectWidth, rectHeigth);

	}

	private void paintNextGeneration(String[][] valMatrix, Graphics g, int width, int height) {
		g.setColor(Color.BLACK);
		g.fillRect(100, 100, height, width);

		int smallWidth = width / valMatrix.length;
		int smallHeight = height / valMatrix[0].length;

		for (int x = 0; x < valMatrix.length; x++) {
			for (int y = 0; y < valMatrix[x].length; y++) {

				if (valMatrix[x][y].equals("1")) {
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
		int size = 64;

		PrimesFileReader pfr = new PrimesFileReader("primes1.txt", size);
		List<Integer> primeValues = pfr.getValues();

		String[][] matrix = getMatrix(primeValues, size);

		ShowPrimesFromFile spff = new ShowPrimesFromFile(matrix);

		JFrame frame = new JFrame();
		frame.setSize(800, 800);
		frame.getContentPane().add(spff);
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

		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);
				frame.repaint();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}
	}

	private static String[][] getMatrix(List<Integer> values, int size) {

		String[][] sValues = new String[size][size];

		for (int x = 1; x <= size; x++) {
			for (int y = 1; y <= size; y++) {
				sValues[x-1][y-1] = values.get(x * y).toString();
			}
		}

		return sValues;
	}

}

class PrimesFileReader {

	private List<Integer> primeValues;
	private String fileName;
	private int primeValuesCount;

	public PrimesFileReader(String fileName, int count) {
		this.fileName = fileName;
		this.primeValuesCount = count * count;
		init();
	}

	public void init() {
		InputStream is = getFileFromResourceAsStream(this.fileName);
		List<String> lines = getLinesFromFile(is);

		int count = 0;

		primeValues = new ArrayList<>();

		for (String line : lines) {
			if (count > 2 && count < primeValuesCount) {
				String[] values = line.replace("  ", " ").replace("  ", " ").replace("  ", " ").replace("  ", " ").trim().split(" ");
				ArrayList<Integer> list = new ArrayList<>();
				for (String val : values) {
					Integer i = Integer.parseInt(val);
					list.add(i);
				}
				primeValues.addAll(list);
			}
			count++;
		}
	}

	public List<Integer> getValues() {
		return primeValues;
	}

	// print input stream
	private List<String> getLinesFromFile(InputStream is) {

		List<String> lines = new ArrayList<>();

		try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(streamReader)) {

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);

				lines.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;

	}

	// get a file from the resources folder
	// works everywhere, IDEA, unit test and JAR file.
	private InputStream getFileFromResourceAsStream(String fileName) {

		// The class loader that loaded the class
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}

	}

}
