package models;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ScoreList {

	private ArrayList<int[]> scoreList;
	private int coins;

	public ScoreList() {
		scoreList = new ArrayList<int[]>();
	}

	@JsonIgnore
	public int[] getBestScore() {
		scoreList.sort((x, y) -> {
			return Double.compare(((y[0] * 3600) + (y[1] * 60) + y[2] + (y[3] / 100.0)),
					((x[0] * 3600) + (x[1] * 60) + x[2] + (x[3] / 100.0)));
		});

		try {
			return scoreList.get(0);
		} catch (Exception e) {
			return new int[] { 0, 0, 0, 0 };
		}

	}

	public void addScore(int[] score) {
		scoreList.add(score);
	}

	public void addCoins(int newCoins) {
		coins += newCoins;
	}

	public int getCoins() {
		return coins;
	}
}
