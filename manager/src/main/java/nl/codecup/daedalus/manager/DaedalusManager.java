package nl.codecup.daedalus.manager;

import nl.codecup.daedalus.objects.Log;

public class DaedalusManager implements Runnable{

	private static final String TAG = "MANAGER";

	public DaedalusManager(){

	}

	@Override
	public void run(){
		while(true){
			Log.debug(DaedalusManager.TAG,"Debug");
			Log.error(DaedalusManager.TAG,"Error");
			Log.info(DaedalusManager.TAG,"Info");
			Log.warning(DaedalusManager.TAG,"Warning");
			//System.out.println("Running the manager");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}

}