package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

//import nl.codecup.daedalus.io.ExecutableThread;
//import nl.codecup.daedalus.io.ExecutableWrapper;

public class Manager extends Executable{

	public Manager(File managerFile){
		super(managerFile);
	}

	public static Manager[] getManagers(File managersDirectory){
		File[] managerFiles = managersDirectory.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file){
				return file.isFile();
			}

		});
		if(managerFiles==null){
			return new Manager[0];
		}
		Manager[] managers = new Manager[managerFiles.length];
		for(int i=0;i<managers.length;i++){
			managers[i] = new Manager(managerFiles[i]);
		}
		return managers;
	}


















//	public Manager(File name) throws IOException{
//		super(name.getName());
//	}
//
//	public Manager(String name){
//		super(name);
//	}
	
//	public ExecutableThread getThread(){
//		try{
//			return new ExecutableThread(Runtime.getRuntime().exec(ExecutableWrapper.getCommand(new File("managers/"+this.getName()))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}

}