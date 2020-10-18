package game.PacCPE;
import java.util.List;
import java.util.Random;
public class GhostsCoach {
	Random random =new Random();
	public int[] decide(GameData data) {
		int[] dirs = new int[4];
		for (int i=0; i<4; i++){
			List<Integer> list = data.getPossibleDirs(data.ghostInfos[i].pos);
			list.remove(Integer.valueOf(MoverInfo.REV[data.ghostInfos[i].curDir]));
			dirs[i] = list.get(random.nextInt(list.size()));
		}

		return dirs;
		}

	}
	

