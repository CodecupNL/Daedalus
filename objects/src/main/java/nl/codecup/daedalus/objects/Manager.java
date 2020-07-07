package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

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

}