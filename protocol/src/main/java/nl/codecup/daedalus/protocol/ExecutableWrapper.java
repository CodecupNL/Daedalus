package nl.codecup.daedalus.protocol;

import java.io.File;

public class ExecutableWrapper{
	
	public static String getCommand(File f){
		if(!f.exists()){
			return null;
		}
		
		if(f.isFile()){
			String extension = "";
			int i = f.getName().lastIndexOf('.');
			if(i>0){
				extension = f.getName().substring(i+1);
			}
			
			if(extension.equals("jar")){
				return "java -jar "+f.getAbsolutePath();
			}
		}
		

		
		return f.getName();
	}

}