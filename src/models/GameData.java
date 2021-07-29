package models;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class GameData {
	private int actualSkin;
	private ArrayList<Integer> purchasedSkins;
	private ArrayList<int[]> scoreList;
	private int coins;

	public GameData() {
		scoreList = new ArrayList<int[]>();
		purchasedSkins = new ArrayList<>();
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

	public void addSkin(int newSkin) {
		purchasedSkins.add(newSkin);
	}

	public void addCoins(int newCoins) {
		coins += newCoins;
	}

	public int getCoins() {
		return coins;
	}

	public int getActualSkin() {
		return actualSkin;
	}

	public ArrayList<Integer> getPurchasedSkins() {
		return purchasedSkins;
	}

	public void changeSkin(int skin) {
		actualSkin = skin;
	}
}
