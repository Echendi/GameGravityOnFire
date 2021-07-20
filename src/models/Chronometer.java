package models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Chronometer {
	private static final int TIMER_DELAY = 10;
	private Timer time;
	private int millis;
	private int seconds;
	private int minuts;
	private int hours;

	public Chronometer() {
		millis = 0;
		seconds = 0;
		minuts = 0;
		hours = 0;
		time = new Timer(TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
		});
	}

	public void start() {
		time.start();
	}

	public void pause() {
		time.stop();
	}

	public void resetTime() {
		millis = 0;
		seconds = 0;
		minuts = 0;
		hours = 0;
	}

	public Timer getTime() {
		return time;
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

}
