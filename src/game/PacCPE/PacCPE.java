package game.PacCPE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import org.game.engine.Game;
import org.game.engine.GameApplication;
import org.game.engine.SpriteSheet;

public class PacCPE extends Game {

	public static void main(String[] args) {
		GameApplication.start(new PacCPE());
	}

	int reqDir, frame;
	GameData data;
	SpriteSheet drawer;
	GhostsCoach ghostsCoach;

	public PacCPE() {
		data = new GameData();
		drawer = new SpriteSheet("images/packman_sheet.png", "images/packman_sheet.info");
		ghostsCoach = new GhostsCoach();

		title = "PacCPE";
		width = data.getWidth()+15;
		height = data.getHeight()+50;
		delay = 10;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (37 <= key && key <= 40) {
			reqDir = key-37;
		}
	}
	@Override
	public void update() {
		if(!data.dead) {
			frame++;
			data.movePackMan(reqDir);
			if(frame%2 == 0) {
				data.moveGhosts(ghostsCoach.decide(data));
			}
			data.update();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// clear the canvas
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		// draw maze
		drawer.draw(g, "mazes", data.mazeNo, 0, 0, false);
		// draw pills
		for (Position pill : data.pills) {
			drawer.draw(g, "pill", 0, pill.column * 2, pill.row * 2, true);
		}
		// draw powerpills
		for (Position powerPill : data.powerPills) {
			drawer.draw(g, "powerpills", 0, powerPill.column * 2, powerPill.row * 2, true);
		}

		// draw pacman
		MoverInfo packman = data.paccpe;
		drawer.draw(g, "packmans", packman.curDir, frame % 3, packman.pos.column * 2, packman.pos.row * 2, true);

		// draw ghosts
		for (int i = 0; i < data.ghostInfos.length; i++) {
			GhostInfo ginfo = data.ghostInfos[i];
			// draw ghost i
			if(ginfo.edibleCountDown == 0) {
				drawer.draw(g, "ghosts", i, ginfo.curDir + frame % 2, ginfo.pos.column * 2, ginfo.pos.row * 2, true);
			}else {
				drawer.draw(g, "edibleghosts",  frame % 2, ginfo.pos.column * 2,ginfo.pos.row * 2, true);
			}
		}

		// draw scores
		drawer.draw(g, "score", 0, 10, 510, false);
		String score = "" + data.score;
		for (int i = 0; i < score.length(); i++) {
			char c = score.charAt(score.length() - 1 - i);
			drawer.draw(g, "digits", c - '0', width - i * 20 - 40, 510, false);
		}
		// draw game over
		if (data.dead) {
			g.setColor(new Color(100, 100, 100, 200));
			g.fillRect(0,0,width,height);
			drawer.draw(g,"over",0,width/2, height/2-50,true);
		}
	}
}