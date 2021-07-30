package models;


public abstract class GameThread implements Runnable {

	private Thread thread;
	protected boolean isExecute;
	protected boolean isPaused;
	protected int sleepTime;

	public GameThread(int sleepTime) {
		this.sleepTime = sleepTime;
		this.isExecute = false;
		this.thread = new Thread(this);
	}

	public void start() {
		isExecute = true;
		thread.start();
	}

	public synchronized void stop() {
		isExecute = false;
		notifyAll();
	}

	public synchronized void pause() {
		isPaused = true;
		notifyAll();
	}

	public synchronized void resume() {
		isPaused = false;
		notifyAll();
	}

	public boolean isExecute() {
		return isExecute;
	}

	protected abstract void executeTask();

	@Override
	public void run() {
		while (isExecute) {
			executeTask();
			synchronized (this) {
				if (isPaused) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!isExecute) {
						break;
					}
				}
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
