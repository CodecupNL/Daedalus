package nl.codecup.daedalus.objects;

public abstract class Executable{
	
	private final String name;
	
	protected Executable(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}