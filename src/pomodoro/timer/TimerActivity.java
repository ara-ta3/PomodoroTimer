package pomodoro.timer;

import timer.SimpleTimer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends Activity {

	private SimpleTimer timer	= SimpleTimer.getInstance();
	private Button startBtn;
	private Button resetBtn;
	private TextView timeView;
	private RelativeLayout layout;
	
	private Handler timeHandler;
	private Thread timeThread;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        timeView	= (TextView)findViewById(R.id.TimerText);
        layout	= (RelativeLayout)findViewById(R.id.back_layout);
        
        timeHandler	= new Handler();
        
        startBtn	= (Button)findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new OnClickListener() {
			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				if(timer.isStart()){
					layout.setBackgroundColor(R.color.back_start);
				}
				if(timer.isCountingDown()){
					timeThread.interrupt();
					timer.pauseTimer();
					startBtn.setText("START");
				}else if(timer.isStart()){
					timeThread	= TimerActivity.this.getTimeThread();
					timer.restartTimer();
					timeThread.start();
					startBtn.setText("STOP");
				}else{
					timeThread	= TimerActivity.this.getTimeThread();
					timer.startTimer();
					timeThread.start();
					startBtn.setText("STOP");
				}
				
			}
		});
        
        resetBtn	= (Button)findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timer.setTime(2);
				if(timeThread==null)Toast.makeText(TimerActivity.this, "タイマーを止めてからリセットしてください", Toast.LENGTH_SHORT).show();
				if(!timeThread.isAlive()){
					timer.resetTime();
					timeView.setText(timer.getTime());
				}else Toast.makeText(TimerActivity.this, "タイマーを止めてからリセットしてください", Toast.LENGTH_SHORT).show();
			}
		});
    }

    private Thread getTimeThread(){
    	return new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						System.out.println(timer.getTime());
						if(timer.isEnd()){
							timeHandler.post(new Runnable() {
								@SuppressLint("ResourceAsColor")
								@Override
								public void run() {
									layout.setBackgroundColor(R.color.back_end);
								}
							});
						}
						timeHandler.post(new Runnable() {
							@Override
							public void run() {
								timeView.setText(timer.getTime());
							}
						});
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		});
    }
}
