package models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Trap extends Collider {

	public Trap(int x, int y, int width, int heigth) {
		super(x, y, width, heigth);
	}

}
