
public class Score implements Comparable<Score>{
	
	private int scoreAmount;
	private String name;
	
	public Score(int score, String n) {
		scoreAmount = score;
		name = n;
	}
	public int getScoreAmount() {
		return scoreAmount;
	}
	public void setScoreAmount(int scoreAmount) {
		this.scoreAmount = scoreAmount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int compareTo(Score o) {
		// TODO Auto-generated method stub
		return scoreAmount-o.getScoreAmount();
	}
	
}
