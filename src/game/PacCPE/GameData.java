package game.PacCPE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameData {

	int mazeNo;
	CopyOnWriteArrayList<Position> pills;
	CopyOnWriteArrayList<Position> powerPills;
	public MoverInfo paccpe;
	public GhostInfo[] ghostInfos = new GhostInfo[4];
	public int score;
	int speedGhost = 6 ;
	int delay = 10 ; // game speed
	Maze[] mazes;
	boolean dead = false;
	boolean win = false ;

	public GameData() {
		mazes = new Maze[4];
		// load mazes information
		for (int m=0; m<4; m++) {
			mazes[m] = new Maze(m);
		}
		setMaze(mazeNo);
	}

	private void setMaze(int m) {
		paccpe = new MoverInfo(mazes[m].packmanPos);
		for (int g=0; g<4; g++) {
			ghostInfos[g] = new GhostInfo(mazes[m].ghostPos);
		}
		pills = new  CopyOnWriteArrayList((List<Position>)(mazes[m].pills.clone()));
		powerPills =new  CopyOnWriteArrayList((List<Position>)(mazes[m].powerPills.clone()));
	}

	public void movePackMan(int reqDir) {
		if (move(reqDir, paccpe)) {
			paccpe.curDir = reqDir;
		} else {
			move(paccpe.curDir, paccpe);
		}

	}


	private  int wrap(int value,int incre,int max){
		return (value+max+incre)%max;
	}

	private boolean move(int reqDir, MoverInfo info) {
		// current position of pacman is (row, column)
		int row = info.pos.row;
		int column = info.pos.column;
		int rows = mazes[mazeNo].rows;
		int columns = mazes[mazeNo].columns;
		int nrow = wrap(row, MoverInfo.DROW[reqDir],rows); // Movement
		int ncol = wrap(column,MoverInfo.DCOL[reqDir],columns);// Movement
		if(mazes[mazeNo].charAt(nrow,ncol) != '0'){
			info.pos.row = nrow;
			info.pos.column  = ncol ;
			return true;
		}
		return false ;

	}
	public void update() {// eat Dot
		//System.out.println(ghostInfos[0].reqDir+" "+ghostInfos[0].curDir);
		//System.out.println(paccpe.pos.row+" "+paccpe.pos.column);
		//System.out.println(ghostInfos[1].pos.row +" "+ghostInfos[1].pos.column);
		//System.out.println(ghostInfos[2].pos);
		//System.out.println(ghostInfos[3].pos);
		if(pills.contains(paccpe.pos)){
			pills.remove(paccpe.pos);
			score += 5;
		}else if(powerPills.contains(paccpe.pos)){
			powerPills.remove(paccpe.pos);
			score += 100;
			if (mazeNo == 0){
			for (GhostInfo g:ghostInfos){
					g.edibleCountDown=500;
				}
			}
			if(mazeNo == 1){
				for (GhostInfo g:ghostInfos){
					g.freezeGhost=400;
				}
			}
			if(mazeNo == 2){
				for(GhostInfo g:ghostInfos){
					g.slowGhost=400;
				}
			}
			if(mazeNo == 3){
				for(GhostInfo g:ghostInfos){
					g.speedPacman =400;
				}
			}
		}
		for (GhostInfo g:ghostInfos) {
			if (mazeNo == 0) {
				if (g.edibleCountDown > 0) {
					if (touching(g.pos, paccpe.pos)) {
						//eat power pills
						score += 100;
						g.curDir = g.reqDir = MoverInfo.UP;
						g.pos.row = mazes[mazeNo].ghostPos.row;
						g.pos.column = mazes[mazeNo].ghostPos.column;
					}
					g.edibleCountDown--;
				} else {
					//PacDead();
				}
			}
			if (mazeNo == 1) {
				if (g.freezeGhost > 0) {
					speedGhost = 10000;
					g.freezeGhost--;
				} else {
					//PacDead();
					speedGhost = 5;
				}
			}
			if (mazeNo == 2) {
				if (g.slowGhost > 0) {
					speedGhost = 20;
					g.slowGhost--;
					//PacDead();
				} else {
					//PacDead();
					speedGhost = 4;
				}
			}
			if (mazeNo == 3) {
				if (g.speedPacman > 300) {
					speedGhost = 8;
					g.speedPacman--;
					//PacDead();
				} else if (g.speedPacman > 200) {
					speedGhost = 7;
					g.speedPacman--;
					//PacDead();
				} else if (g.speedPacman > 100) {
					speedGhost = 6;
					g.speedPacman--;
					//PacDead();
				} else if (g.speedPacman > 0) {
					speedGhost = 5;
					g.speedPacman--;
					//PacDead();
				} else {
					//PacDead();
					speedGhost = 4;
				}
			}
		}

		if(pills.isEmpty()&& powerPills.isEmpty()){ // if clear next maze
			mazeNo++;
			if(mazeNo<4){
				setMaze(mazeNo);

			}
		}

	}

	private boolean touching(Position a, Position b) {
		return Math.abs(a.row-b.row)+Math.abs(a.column-b.column) <3 ;
	}

	public void moveGhosts(int[] reqDirs) {

		for (int i=0;i<4 ;i++){
			GhostInfo info = ghostInfos[i];
			info.reqDir = reqDirs[i];
			if(move(info.reqDir,info)){
				info.curDir = info.reqDir;
			}else {
				move(info.curDir,info);
			}
		}

	}
	public int getWidth() {
		return mazes[mazeNo].width;
	}
	public int getHeight() {
		return mazes[mazeNo].height;
	}

	public List<Integer> getPossibleDirs(Position pos){
		List<Integer> list = new ArrayList<Integer>();
		for (int d=0;d<4;d++) {
			Position npos = getNextPositoinInDir(pos, d);
			if (mazes[mazeNo].charAt(npos.row, npos.column) != '0') {
				list.add(d);
			}
		}
		return list ;
	}

	public Position getNextPositoinInDir(Position pos,int d){
		int nrow = wrap(pos.row, MoverInfo.DROW[d],mazes[mazeNo].rows);
		int ncol = wrap(pos.column,MoverInfo.DCOL[d],mazes[mazeNo].columns);
		return  new Position(nrow,ncol);
	}

	public void PacDead() {
		for(GhostInfo g:ghostInfos) {
			if (touching(g.pos, paccpe.pos)) {
				dead = true;
			}
		}
	}
}