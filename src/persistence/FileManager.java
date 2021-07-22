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

public class FileManager {
	public static final String PATH = "data/game.json";

//	public static Game loadGame() {
//		FileReader jsonAll;
//		Game game;
//		try {
//			jsonAll = new FileReader(PATH);
//			game = new Gson().fromJson(jsonAll, Game.class);
//			jsonAll.close();
//
////			Platform[] plat = json.fromJson(platforms, Platform[].class);
//			return game;
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return new Game();
//	}

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
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(PATH), game);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
