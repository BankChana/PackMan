package game.packman;

import java.util.ArrayList;
import java.util.List;


public class GameData {

	int mazeNo;
	List<Position> pills;
	List<Position> powerPills;
	public MoverInfo packman;
	public GhostInfo[] ghostInfos = new GhostInfo[4];
	public int score;
	
	Maze[] mazes;
	
	public GameData() {
		mazes = new Maze[4];
		// load mazes information
		for (int m=0; m<4; m++) {
			mazes[m] = new Maze(m);
		}
//		mazeNo = 0;
		setMaze(mazeNo);
	}
	
	private void setMaze(int m) {
		packman = new MoverInfo(mazes[m].packmanPos);
		for (int g=0; g<4; g++) {
			ghostInfos[g] = new GhostInfo(mazes[m].ghostPos);
		}
		pills = (List<Position>)(mazes[m].pills.clone());
		powerPills = (List<Position>)(mazes[m].powerPills.clone());
	}

	public void movePackMan(int reqDir) {
		if (move(reqDir, packman)) {
			packman.curDir = reqDir;
		} else {
			move(packman.curDir, packman);
		}
		
	}
	
	final int LEFT=0, UP=1, RIGHT=2, DOWN=4;
	
	private boolean move(int reqDir, MoverInfo info) {
		// current position of packman is (row, column)
		int row = info.pos.row;
		int column = info.pos.column;
		int rows = mazes[mazeNo].rows;
		int columns = mazes[mazeNo].columns;
		switch (reqDir) {
		case LEFT: // 37
			if (column > 0 && mazes[mazeNo].charAt(row, column-1) != '0') {
				info.pos.column -= 1;
				return true;
			} 
			if (column == 0 && mazes[mazeNo].charAt(row, columns-1) == '1' ) {
//				row = row;
				info.pos.column = columns-1;
				return true;
			}
			break;
		case UP:   // 38
			if (row > 0 && mazes[mazeNo].charAt(row-1, column) != '0') {
				info.pos.row -= 1;
				return true;
			}
			break;
		case RIGHT: // 39
			if (column < columns-1 && mazes[mazeNo].charAt(row, column+1) != '0') {
				info.pos.column += 1;
				return true;
			}
			break;
		case DOWN:  // 40
			if (row < rows-1 && mazes[mazeNo].charAt(row+1, column) != '0') {
				info.pos.row += 1;
				return true;
			}
			break;
		}
		return false;
	}
	public void update() {
		// TODO Auto-generated method stub
		
	}
	public void moveGhosts(int[] decide) {
		// TODO Auto-generated method stub
		
	}
	public int getWidth() {
		return mazes[mazeNo].width;
	}
	public int getHeight() {
		return mazes[mazeNo].height;
	}
}
