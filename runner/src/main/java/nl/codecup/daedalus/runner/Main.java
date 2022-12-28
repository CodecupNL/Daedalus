package nl.codecup.daedalus.runner;

import org.fusesource.jansi.AnsiConsole;

public class Main{

	public static void main(String... args){
		AnsiConsole.systemInstall();
		new Thread(new Daedalus(args),"Daedalus").start();
//		new Daedalus(args).run();
		AnsiConsole.systemUninstall();
	}

}