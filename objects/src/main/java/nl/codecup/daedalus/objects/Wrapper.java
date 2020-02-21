package nl.codecup.daedalus.objects;

import java.io.File;

public class Wrapper{

	private String name;

	private String[] commands;
	private String[] patterns;

	private Wrapper wrapper;

	public Wrapper(String name,String[] commands,String[] patterns){
		this(name,commands,patterns,null);
	}

	public Wrapper(String name,String[] commands,String[] patterns,Wrapper wrapper){
		this.name = name;
		this.commands = commands;
		this.patterns = patterns;
		this.wrapper = wrapper;
	}

	public boolean matches(File f){
		for(String pattern : this.patterns){
			if(f.getName().matches(pattern)){
				return true;
			}
		}
		return false;
	}

	public String getName(){
		return this.name;
	}

	public String[] getCommands(){
		return this.commands;
	}

	public String[] getPatterns(){
		return this.patterns;
	}

	public Wrapper getWrapper(){
		return this.wrapper;
	}

}