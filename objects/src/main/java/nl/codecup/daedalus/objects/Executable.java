package nl.codecup.daedalus.objects;

import java.io.File;

public abstract class Executable{

	protected final File file;

	public Executable(File executableFile){
		this.file = executableFile;
	}

	public File getFile(){
		return this.file;
	}

	public String getName(){
		return this.file.getName();
	}

	public boolean isExisting(){
		return this.file.exists();
	}

	@Override
	public String toString() {
		return "Executable{" +
				"file=" + file +
				'}';
	}

}