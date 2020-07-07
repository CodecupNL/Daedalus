package nl.codecup.daedalus.manager;

public class DaedalusManager implements Runnable{

	public DaedalusManager(){

	}

	@Override
	public void run(){
		while(true){
			System.out.println("Running the manager");
		}
	}

}