package timer;

import java.text.DecimalFormat;

public class SimpleTimer {
	private static final SimpleTimer sTimer	= new SimpleTimer();
	public static final int MAINTIME	= 1500;
	public static final int SUBTIME		= 300;
	private Thread countDownThread	= null;
	
	
	private boolean isMain	= true;
	private boolean isStart	= true;
	private int countTime	= 1500;
	
	
	private SimpleTimer(){}
	public static SimpleTimer getInstance(){return SimpleTimer.sTimer;}
	
	//機能
	public void startTimer(){
		isStart	= false;
		countTime 	= isMain ? MAINTIME : SUBTIME;
		countDownThread	= this.getCountDownThread();
		this.countDownThread.start();
	}
	public void restartTimer(){
		countDownThread	= this.getCountDownThread();
		this.countDownThread.start();
	}
	public void pauseTimer(){
		this.countDownThread.interrupt();
	}
	public void stopTimer(){
		isStart	= true;
		this.countDownThread.interrupt();
		
	}
	private Thread getCountDownThread(){
		return new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						if(--countTime==0)break;
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		});
	}
	
	//設定
	public void setTime(int time){
		this.countTime	= time;
	}
	public void setMain(){this.isMain=true;}
	public void setBreak(){this.isMain=false;}
	public void resetTime(){
		this.init();
	}
	private void init(){
		countTime 	= isMain ? MAINTIME : SUBTIME;
		countDownThread	= null;
	}
	
	//データ渡し
	public boolean isStart(){return this.isStart;}
	public boolean isEnd(){return this.countTime==0;}
	public boolean isCountingDown(){
		if(countDownThread==null)return false;
		return countDownThread.isAlive();
	}
//	public boolean isCountingDown(){return this.countDownThread!=null || this.countDownThread.isAlive();}
	public String getTime(){return this.parseTime();}
	private String parseTime(){
		if(this.countTime==SimpleTimer.MAINTIME)return "25:00";
		DecimalFormat d	= new DecimalFormat("00");
		String min	= "25";
		String sec	= "00";
		min	= d.format(countTime/60);
		sec	= d.format(countTime%60);
		return min+":"+sec;
	}
}
