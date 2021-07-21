package models;

public class GameThread {
	public abstract class MyThread implements Runnable{

	    private Thread thread;
	    private boolean isExecute;
	    private boolean isPaused;
	    private long sleepTime;

		public MyThread() {
			sleepTime = 5;
			this.thread = new Thread(this);
		}

	    public void start(){
	        isExecute = true;
	        thread.start();
	    }

	    public synchronized void stop(){
	        isExecute = false;
	        notifyAll();
	    }

	    public synchronized void pause(){
	        isPaused = true;
	        notifyAll();
	    }

	    public synchronized void resume(){
	        isPaused = false;
	        notifyAll();
	    }

	    public void setSleepTime(long time){
	        this.sleepTime = time;
	    }

	    public abstract void executeTask();

	    @Override
	    public void run() {
	            while(isExecute){
	                executeTask();
	                synchronized (this){
	                    if(isPaused){
	                        try {
	                            wait();
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                        if(!isExecute){
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

}
