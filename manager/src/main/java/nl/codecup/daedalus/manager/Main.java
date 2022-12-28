package nl.codecup.daedalus.manager;

class Main{

	public static void main(String... args){
		new Thread(new DaedalusManager(),"Daedalus Manager").start();
	}

}