package hu.bme.mit.train.controller;

import hu.bme.mit.train.interfaces.TrainController;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Timer;
import java.util.TimerTask;


public class TrainControllerImpl implements TrainController {

	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 0;

	public Table<Long, Integer, Integer> speedLog
			= HashBasedTable.create();

	public TrainControllerImpl() {
		starTimer();
	}


	public void starTimer(){
		Timer timer=new Timer("Timer");
		TimerTask repeatedTask = new TimerTask() {
			public void run() {
				followSpeed();
			}
		};

			long delay  = 1000L;
			long period = 1000L;
		timer.scheduleAtFixedRate(repeatedTask, delay, period);

	}

	@Override
	public void followSpeed() {
		if (referenceSpeed < 0) {
			referenceSpeed = 0;
		} else {
		    if(referenceSpeed+step > 0) {
                referenceSpeed += step;
                System.out.println("Thre reference speed changed: " + referenceSpeed);
            } else {
		        referenceSpeed = 0;
            }
		}

		enforceSpeedLimit();

		speedLog.put(System.currentTimeMillis(), step, referenceSpeed );
	}

	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();
		
	}

	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
	}

	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;		
	}

	@Override
	public Table<Long, Integer, Integer> getTachograph(){
		return speedLog;
	}

}
