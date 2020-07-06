package nl.codecup.daedalus.runner;

//import nl.codecup.daedalus.objects.Manager;
//import org.fusesource.jansi.Ansi;
//import org.fusesource.jansi.AnsiConsole;

import nl.codecup.daedalus.wrapper.WrapperManager;

import java.io.File;
import java.util.Map;

public class Daedalus implements Runnable{

	private static final File FILE_WRAPPER = new File("./wrapper.json");

	private WrapperManager wm;

	public Daedalus(String... args){
		try{
			this.wm =  new WrapperManager(Daedalus.FILE_WRAPPER);
		}catch(Exception e){
			System.err.println("Wrapper file not found");
			System.exit(0);
		}
	}

	public void run(){
		this.wm.getWrappers(new File("manager.jar"));
	}


//		this.wm.getWrappers(null);
//
//
////		AnsiConsole.systemInstall();
////
////		System.out.println(Ansi.ansi().fgYellow().a("Environment variables").reset().toString());
//
//		for(Map.Entry<String,String> me : System.getenv().entrySet()){
//		System.err.println(me.getKey()+" = "+me.getValue());
//	}
//
////		System.out.println(Ansi.ansi().fgYellow().a("Environment variables").reset().toString());
//
//		for(Map.Entry<Object,Object> me : System.getProperties().entrySet()){
//		System.err.println(me.getKey()+" = "+me.getValue());
//	}
//
//	//TODO Implement
////		Manager m;

}