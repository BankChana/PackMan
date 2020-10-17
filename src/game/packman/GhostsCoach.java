package game.packman;
import java.util.Random;
public class GhostsCoach {
	Random random =new Random();
	public int[] decide(GameData data) {
		return new int[]{
				random.nextInt(4),
				random.nextInt(4),
				random.nextInt(4),
				random.nextInt(4)
		};

	}
	
}
