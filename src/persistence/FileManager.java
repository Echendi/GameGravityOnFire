package persistence;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.Chronometer;
import models.Collider;
import models.Game;
import models.Player;
import models.GameData;
import models.Platform;

public class FileManager {
	public static final String GAME_PATH = "data/gameSaved.json";
	public static final String DATA_PATH = "data/gameData.json";

	private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			return (m.getDeclaringClass() != Game.class && m.getDeclaringClass() != Player.class
					&& m.getDeclaringClass() != Collider.class && m.getDeclaringClass() != Chronometer.class)
					|| super.hasIgnoreMarker(m);
		}
	}

	public static GameData loadGameData() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			GameData data = mapper.readValue(new File(DATA_PATH), GameData.class);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveGameData(GameData data) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_PATH), data);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveGame(Game game) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(GAME_PATH), game);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static Game loadGame() {
//		try {
//			final ObjectMapper mapper = new ObjectMapper();
//			mapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());
//			Game game = mapper.readValue(new File(GAME_PATH), Game.class);
//			game.setData(loadGameData());
//			return game;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public static Game loadGame() {
		try {
			String text = new String(Files.readAllBytes(Paths.get(GAME_PATH)), StandardCharsets.UTF_8);
			JsonParser parser = new JsonParser();
			JsonObject object = parser.parse(text).getAsJsonObject();
			Game game = getGame(object);
			return game;
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return new Game(loadGameData());
	}

	private static Game getGame(JsonObject object) {

		int maxPlatforms = object.get("maxPlatforms").getAsInt();
		boolean isOver = object.get("isOver").getAsBoolean();
		int[] objectCollisionIndex = loadArrayIndexCollision(object);
		Player player = loadPlayer(object);
		ArrayList<Platform> platforms = loadPlatforms("platforms", object);
		ArrayList<Platform> floor = loadPlatforms("floor", object);
		ArrayList<Platform> ceilling = loadPlatforms("ceilling", object);
		Chronometer chronometer = loadChronometer(object);
		int velocity = object.get("velocity").getAsInt();
		boolean execute = object.get("execute").getAsBoolean();

		Game game = new Game(maxPlatforms, isOver, objectCollisionIndex, player, platforms, floor, ceilling,
				chronometer, velocity, execute);
		game.setData(loadGameData());

		return game;
	}

	private static int[] loadArrayIndexCollision(JsonObject object) {
		int[] objectCollisionIndex = new int[6];
		JsonArray arrayCollisions = object.get("objectCollisionIndex").getAsJsonArray();
		for (int i = 0; i < objectCollisionIndex.length; i++) {
			objectCollisionIndex[i] = arrayCollisions.get(i).getAsInt();
		}
		return objectCollisionIndex;
	}

	private static Chronometer loadChronometer(JsonObject object) {
		JsonObject chronoObject = object.get("chronometer").getAsJsonObject();
		int millis = chronoObject.get("millis").getAsInt();
		int seconds = chronoObject.get("seconds").getAsInt();
		int minuts = chronoObject.get("minuts").getAsInt();
		int hours = chronoObject.get("hours").getAsInt();
		return new Chronometer(millis, seconds, minuts, hours);
	}

	private static ArrayList<Platform> loadPlatforms(String name, JsonObject object) {
		JsonArray platformArray = object.get(name).getAsJsonArray();
		ArrayList<Platform> platforms = new ArrayList<>();
		for (JsonElement jsonElement : platformArray) {
			int x = jsonElement.getAsJsonObject().get("x").getAsInt();
			int y = jsonElement.getAsJsonObject().get("y").getAsInt();
			int width = jsonElement.getAsJsonObject().get("width").getAsInt();
			int heigth = jsonElement.getAsJsonObject().get("heigth").getAsInt();
			platforms.add(new Platform(x, y, width, heigth));
		}
		return platforms;
	}

	private static Player loadPlayer(JsonObject object) {
		JsonObject playerObject = object.get("player").getAsJsonObject();
		boolean isColliding = playerObject.get("isColliding").getAsBoolean();
		boolean isFrontColliding = playerObject.get("isFrontColliding").getAsBoolean();
		boolean isDown = playerObject.get("isDown").getAsBoolean();
		int x = playerObject.get("x").getAsInt();
		int y = playerObject.get("y").getAsInt();
		return new Player(isColliding, isFrontColliding, isDown, x, y);
	}
}
