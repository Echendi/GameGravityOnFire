package persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

//	public static synchronized void saveGame(Game game) {
//		PrintWriter writer;
//		try {
//			writer = new PrintWriter(PATH);
//			writer.write(new Gson().toJson(game));
//			writer.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
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
//		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(PATH), game);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public static void saveGame(Player game) {
//		final ObjectMapper mapper = new ObjectMapper();
//		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
////		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//		mapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());
//		try {
//			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(PATH), game);
//		} catch (JsonProcessingException e1) {
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	public static void saveGame(Game game) {
//		try {
//			new ObjectMapper().writeValue(new File(PATH), game);
//		} catch (JsonGenerationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	public static synchronized void saveGame(Game game) {
//
//		
//		Gson json = new Gson();
//		try {
//			FileWriter fileWriter = new FileWriter(PATH, true);
////			fileWriter.
//			JsonWriter writer = json.newJsonWriter(new FileWriter(PATH, true));
//			writer.beginArray();
//			writer.beginObject();
//
//			writer.name("play");
//			writer.value(game.isPlay());
//
//			writer.name("maxPlatforms");
//			writer.value(game.getMaxPlatforms());
//
//			writer.name("player");
//			writer.value(json.toJson(game.getPlayerPosition()));
//
//			writer.name("velocity");
//			writer.value(game.getVelocity());
//
//			writer.name("platforms");
//			writer.value(json.toJson(game.getPlatforms()));
//
//			writer.name("ceilling");
//			writer.value(json.toJson(game.getCeilling()));
//
//			writer.name("floor");
//			writer.value(json.toJson(game.getFloor()));
//
//			writer.name("abyss");
//			writer.value(json.toJson(game.getAbyss()));
//
//			writer.name("fire");
//			writer.value(json.toJson(game.getFire()));
//
//			writer.name("platformCollision");
//			writer.value(game.getPlatformCollision());
//
//			writer.name("platformForntCollision");
//			writer.value(game.getPlatformForntCollision());
//
//			writer.name("floorCollision");
//			writer.value(game.getFloorCollision());
//
//			writer.name("floorFrontCollision");
//			writer.value(game.getFloorFrontCollision());
//
//			writer.name("ceillingCollision");
//			writer.value(game.getCeillingCollision());
//
//			writer.name("ceillingFrontCollision");
//			writer.value(game.getCeillingFrontCollision());
//
//			writer.endObject();
//			writer.endArray();
//
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
