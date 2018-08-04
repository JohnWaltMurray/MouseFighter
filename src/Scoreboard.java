import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Scoreboard {
	String filename = "";
	ArrayList<Score> scoreList;
	public Scoreboard(String fn) {
		filename = fn;
		scoreList = new ArrayList<Score>();
		
		FileReader reader;
		try {
			reader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String read = "xd";
			for(int i = 0; i < 20; i++) {
				try {
					read = bufferedReader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!(read == null)) {
					String[] splitString = read.split(" ");
					scoreList.add(new Score(Integer.parseInt(splitString[1]), splitString[0]));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(scoreList);
		Collections.reverse(scoreList);
		
	}
	
	public ArrayList<Score> getScoreboard() {
		return scoreList;
	}
}
