package models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Chronometer extends GameThread {
	private static final int TIMER_DELAY = 10;

	private int millis;
	private int seconds;
	private int minuts;
	private int hours;

	public Chronometer() {
		super(TIMER_DELAY);
		millis = 0;
		seconds = 0;
		minuts = 0;
		hours = 0;
	}

	public Chronometer(int millis, int seconds, int minuts, int hours) {
		super(TIMER_DELAY);
		this.millis = millis;
		this.seconds = seconds;
		this.minuts = minuts;
		this.hours = hours;
	}

	public void resetTime() {
		millis = 0;
		seconds = 0;
		minuts = 0;
		hours = 0;
	}

	public int getMillis() {
		return millis;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getMinuts() {
		return minuts;
	}

	public int getHours() {
		return hours;
	}

	@JsonIgnore
	public int[] getTime() {
		return new int[] { hours, minuts, seconds, millis };
	}

	@JsonIgnore
	public int getTimeInSeconds() {
		return (hours * 3600) + (minuts * 60) + seconds;
	}

	@Override
	public void executeTask() {
		millis++;
		if (millis == 100) {
			seconds++;
			millis = 0;
		}
		if (seconds == 60) {
			minuts++;
			seconds = 0;
		}
		if (minuts == 60) {
			hours++;
			minuts = 0;
		}
	}

}
