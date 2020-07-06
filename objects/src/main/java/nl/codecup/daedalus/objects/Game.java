package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.IOException;

public class Game{
	
	private final String name;

	public Game(File name) throws IOException {
		this.name = name.getName();
	}
	
	public Game(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}