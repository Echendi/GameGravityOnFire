package models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Chronometer extends Thread {
	private static final int TIMER_DELAY = 10;

	private int millis;
	private int seconds;
	private int minuts;
	private int hours;
	private boolean isRun;
	private boolean isPaused;

	public Chronometer() {
		millis = 0;
		seconds = 0;
		minuts = 0;
		hours = 0;
		isRun = false;
	}

//	public Chronometer(String time) {
//		String times[] = time.split(":");
//		this.millis = Integer.parseInt(times[3]);
//		this.seconds = Integer.parseInt(times[2]);
//		this.minuts = Integer.parseInt(times[1]);
//		this.hours = Integer.parseInt(times[0]);
//	}

	public synchronized void stopped() {
		isRun = false;
		notifyAll();
	}

	public synchronized void pause() {
		isPaused = true;
		notifyAll();
	}

	public synchronized void resumed() {
		isPaused = false;
		notifyAll();
	}

	public void resetTime() {
		millis = 0;
		seconds = 0;
		minuts = 0;
		hours = 0;
	}

//	public void setRun(boolean isRun) {
//		this.isRun = isRun;
//	}

//	public Timer getTime() {
//		return time;
//	}

	@Override
	public void run() {
		isRun = true;
		while (isRun) {
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
			sleeping();
			synchronized (this) {
				if (isPaused) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!isRun) {
						break;
					}
				}
			}
		}
	}

	private void sleeping() {
		try {
			sleep(TIMER_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

//	@Override
//	public String toString() {
//		return (hours < 10 ? "0" : "") + hours + ":" + (minuts < 10 ? "0" : "") + minuts + ":"
//				+ (seconds < 10 ? "0" : "") + seconds + ":" + (millis < 10 ? "0" : "") + millis;
//	}

}
