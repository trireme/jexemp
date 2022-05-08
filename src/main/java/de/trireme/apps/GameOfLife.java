package de.trireme.apps;
import java.util.Random;

public class GameOfLife {

//    Any live cell with fewer than two live neighbours dies, as if by underpopulation.
//    Any live cell with two or three live neighbours lives on to the next generation.
//    Any live cell with more than three live neighbours dies, as if by overpopulation.
//    Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
//Test
//    These rules, which compare the behavior of the automaton to real life, can be condensed into the following:
//
//    Any live cell with two or three neighbors survives.
//    Any dead cell with three live neighbors becomes a live cell.
//    All other live cells die in the next generation. Similarly, all other dead cells stay dead.

	public static void main(String[] args) {

		int vertLength = 20;
		int horizontalLength = 20;

		String[][] matrix4GameOfLife = new String[vertLength][horizontalLength];

		buildRandomMatrix4GameOfLife(matrix4GameOfLife);
//		showMatrix4GameOfLife(matrix4GameOfLife);
		showMatrix4GameOfLifeReadable(matrix4GameOfLife);
//		printNeigbours(matrix4GameOfLife);

		for (int i = 0; i < 5000; i++) {
			matrix4GameOfLife = nextGeneration(matrix4GameOfLife);
			System.out.println("");
//			showMatrix4GameOfLife(matrix4GameOfLife);
			System.out.println("");
			showMatrix4GameOfLifeReadable(matrix4GameOfLife);
			//	printNeigbours(matrix4GameOfLife);
		}

	}

	private static void printNeigbours(String[][] matrix4GameOfLife) {
		for (int x = 0; x < matrix4GameOfLife.length; x++) {
			for (int y = 0; y < matrix4GameOfLife[x].length; y++) {
				int numberOfNeighbours = getNumberOfNeighbours(matrix4GameOfLife, x, y);
				System.out.print("Val " + matrix4GameOfLife[x][y] + " {x,y}:{" + x + "," + y + "} NoN : "
						+ numberOfNeighbours + " - ");
			}
			System.out.println("");
		}
	}

	public static String[][] nextGeneration(String[][] matrix4GameOfLife) {

		String[][] matrix4GameOfLifeNext = new String[matrix4GameOfLife.length][matrix4GameOfLife[0].length];

		for (int x = 0; x < matrix4GameOfLife.length; x++) {
			for (int y = 0; y < matrix4GameOfLife[x].length; y++) {
				int numberOfNeighbours = getNumberOfNeighbours(matrix4GameOfLife, x, y);

				if (numberOfNeighbours < 2 || numberOfNeighbours > 3) {
					matrix4GameOfLifeNext[x][y] = "0";
				} else {
					matrix4GameOfLifeNext[x][y] = "1";
				}
			}
		}

		return matrix4GameOfLifeNext;
	}

	public static void buildRandomMatrix4GameOfLife(String[][] matrix4GameOfLife) {
		Random ran = new Random();
		for (String[] vert : matrix4GameOfLife) {
			for (int i = 0; i < vert.length; i++) {
				int nxt = ran.nextInt(2);
				vert[i] = Integer.toString(nxt);
			}
		}
	}

	public static void showMatrix4GameOfLife(String[][] matrix4GameOfLife) {
		for (String[] vert : matrix4GameOfLife) {
			for (int i = 0; i < vert.length; i++) {
				System.out.print(vert[i]);
			}
			System.out.println("");
		}
	}

	public static void showMatrix4GameOfLifeReadable(String[][] matrix4GameOfLife) {
		for (String[] vert : matrix4GameOfLife) {
			for (int i = 0; i < vert.length; i++) {

				if (vert[i].equals("1")) {
					System.out.print("* ");
				} else {
					System.out.print("- ");
				}
			}
			System.out.println("");
		}
	}

	// Logische Schritte:
	// 1.Ebene
	// Wenn eine zelle weniger als 2 um sich herum hat
	// (man muss nur die Werte der Zellen um sich herum zusammenandieren)
	// stribt sie
	// hat sie 2 oder 3 Nachbar lebt sie

	// hat sie mehr als 3 Nachbar stirbt sie

	// Eine leere Zelle mit 2 oder 3 lebenden Nachbarzellen wird lebendig

	// 2. Ebene
	// Jede Zelle mit Nachbar < 2 und > 3 stirbt
	// jede Zelle mit Nachbar > 1 und < 4 lebt
	// jede Zelle mit Nachbar > 1 und < 4 wird geboren

	// 3.Ebene
	// Jede Zelle mit Nachbar < 2 oder > 3 stirbt ( war sie 0 bleibt sie 0,
	// ansonsten wird sie 0)
	// jede Zelle mit Nachbar > 1 oder < 4 lebt (war sie 1 bleibt sie 1, ansonsten
	// wird sie 1).

	// Return anzahl der Nachbarn
	private static int getNumberOfNeighbours(String[][] matrix4GameOfLife, int row, int column) {
		int numberOfNeibouhrRowAbove = 0;
		int triple[] = { -1, 0, 1 };
		for (int r : triple) {
			for (int c : triple) {
				boolean currentCell = r == 0 && c == 0;
				if (!currentCell) {
					numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row + r, column + c);
				}
			}
		}

//		numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row - 1, column - 1);
//		numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row - 1, column);
//		numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row - 1, column + 1);
//
//		numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row, column - 1);
//		numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row, column + 1);
//
//		numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row + 1, column - 1);
//		numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row + 1, column);
//		numberOfNeibouhrRowAbove += getValueFromCell(matrix4GameOfLife, row + 1, column + 1);

		return numberOfNeibouhrRowAbove;
	}

	public static int getValueFromCell(String[][] matrix4GameOfLife, int row, int column) {
		int valueOfCell = 0;
		if (row >= 0 && row <= matrix4GameOfLife.length - 1 && column >= 0
				&& column <= matrix4GameOfLife[row].length - 1) {
			valueOfCell = Integer.parseInt(matrix4GameOfLife[row][column]);
		}
		return valueOfCell;

	}

}
