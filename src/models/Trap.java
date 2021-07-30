package models;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Trap extends Collider {

	@ConstructorProperties({ "x", "y", "width", "heigth" })
	public Trap(int x, int y, int width, int heigth) {
		super(x, y, width, heigth);
	}

}
