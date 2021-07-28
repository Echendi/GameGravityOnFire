package persistence;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import models.Chronometer;
import models.Collider;
import models.Game;
import models.Player;
import models.GameData;

public class FileManager {
	public static final String GAME_PATH = "data/gameSaved.json";
	public static final String DATA_PATH = "data/gameData.json";

	public static Game loadGame() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			Game game = mapper.readValue(new File(GAME_PATH), Game.class);
			return game;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			return (m.getDeclaringClass() != Game.class && m.getDeclaringClass() != Player.class
					&& m.getDeclaringClass() != Collider.class && m.getDeclaringClass() != Chronometer.class)
					|| super.hasIgnoreMarker(m);
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

	public static GameData loadScoreList() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			GameData data = mapper.readValue(new File(DATA_PATH), GameData.class);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveScores(GameData data) {
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
}
