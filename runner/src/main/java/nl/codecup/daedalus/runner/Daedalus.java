package nl.codecup.daedalus.runner;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import nl.codecup.daedalus.config.ConfigManager;
import nl.codecup.daedalus.objects.Game;
import nl.codecup.daedalus.objects.Manager;
import nl.codecup.daedalus.runner.util.DaedalusHelpFormatter;
import nl.codecup.daedalus.wrapper.WrapperManager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

public class Daedalus implements Runnable{

	private static final File DIRECTORY_CWD = new File(System.getProperty("user.dir"));

	private static final File DIRECTORY_GAMES = new File(Daedalus.DIRECTORY_CWD,"games");
	private static final File DIRECTORY_MANAGERS = new File(Daedalus.DIRECTORY_CWD,"managers");

	private static Daedalus INSTANCE;

	private static final OptionParser OPTIONPARSER = new OptionParser();
	private static final OptionSpec<?> OPTION_NONE = OPTIONPARSER.nonOptions();
	private static final OptionSpec<?> OPTION_HELP = OPTIONPARSER.acceptsAll(Arrays.asList("h","help","?"),"Show help").forHelp();
	private static final OptionSpec<String> OPTION_LIST = OPTIONPARSER.acceptsAll(Arrays.asList("l","list"),"List items of specific type").withRequiredArg().describedAs("list");
	private static final OptionSpec<?> OPTION_VERSION = OPTIONPARSER.acceptsAll(Arrays.asList("v","version"),"Show version");
	private static final OptionSpec<File> OPTION_CONFIG = OPTIONPARSER.acceptsAll(Arrays.asList("c","config"),"Config file that should be used").withRequiredArg().describedAs("config").ofType(File.class);
	private static final OptionSpec<?> OPTION_DEBUG = OPTIONPARSER.acceptsAll(Arrays.asList("d","debug"),"Show debug log");
	private static final OptionSpec<File> OPTION_FILE = OPTIONPARSER.acceptsAll(Arrays.asList("f","file"),"File to save logs in").withRequiredArg().describedAs("file").ofType(File.class);
	private static final OptionSpec<File> OPTION_GAME = OPTIONPARSER.acceptsAll(Arrays.asList("g","game"),"Game to play").requiredUnless(Daedalus.OPTION_NONE,Daedalus.OPTION_HELP,Daedalus.OPTION_LIST,Daedalus.OPTION_VERSION).withRequiredArg().describedAs("game").ofType(File.class);
	private static final OptionSpec<File> OPTION_MANAGER = OPTIONPARSER.acceptsAll(Arrays.asList("m","manager"),"Manager that should run the game").withRequiredArg().describedAs("manager").ofType(File.class);
	private static final OptionSpec<File> OPTION_WRAPPER = OPTIONPARSER.acceptsAll(Arrays.asList("w","wrapper"),"Wrapper that should be used to load the code").withRequiredArg().describedAs("wrapper").ofType(File.class);

	private static final String VERSION = "Daedalus v1.0.0";

	private ConfigManager configManager;
	private Game game;
	private Manager manager;
	private WrapperManager wrapperManager;

	public Daedalus(String... args){
		OptionSet set = null;
		try{
			set = Daedalus.OPTIONPARSER.parse(args);
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}

		if(set.has(Daedalus.OPTION_VERSION)){
			System.out.println(Daedalus.VERSION);
			System.exit(0);
		}

		if(set.hasArgument(Daedalus.OPTION_LIST)){
			String list = set.valueOf(Daedalus.OPTION_LIST);
			System.err.println(System.getProperty("user.dir"));
			if("games".equals(list)){
				File[] games = Daedalus.DIRECTORY_GAMES.listFiles(new FileFilter(){

					@Override
					public boolean accept(File file){
						return file.isDirectory();
					}

				});
				if(games==null){
					games = new File[0];
				}
				if(games.length==0){
					System.out.println("There are no games");
				}else{
					System.out.println("Games:");
					for(File g : games){
						System.out.println(" - "+g.getName());
					}
				}
				System.exit(0);
			}else if("managers".equals(list)){
				File[] managers = Daedalus.DIRECTORY_MANAGERS.listFiles(new FileFilter(){

					@Override
					public boolean accept(File file){
						return file.isFile();
					}

				});
				if(managers==null){
					managers = new File[0];
				}
				if(managers.length==0){
					System.out.println("There are no managers");
				}else{
					System.out.println("Managers:");
					for(File m : managers){
						System.out.println(" - "+m.getName());
					}
				}
				System.exit(0);
			}else if(list!=null && list.startsWith("players")){
				String[] parts = list.split(":");
				if("players".equals(parts[0])){
					String gameName = parts.length>=2?parts[1]:null;
					if(gameName!=null){
						File game = new File(Daedalus.DIRECTORY_GAMES,gameName);
						File[] players = new File(game,"players").listFiles(new FileFilter(){

							@Override
							public boolean accept(File file){
								return file.isFile();
							}

						});
						if(players==null){
							players = new File[0];
						}
						if(players.length==0){
							System.out.println("There are no players");
						}else{
							System.out.println("Players:");
							for(File p : players){
								System.out.println(" - "+p.getName());
							}
						}
						System.exit(0);
					}
				}
			}
			System.err.println("Unknown list");
			System.exit(0);
		}

		if(!set.hasOptions() || set.has(Daedalus.OPTION_HELP) || !set.has(Daedalus.OPTION_GAME)){
			Daedalus.OPTIONPARSER.formatHelpWith(new DaedalusHelpFormatter());
			try{
				Daedalus.OPTIONPARSER.printHelpOn(System.out);
			}catch(IOException e){
				System.err.println("Error when printing help");
			}
			System.exit(0);
		}

		Daedalus.INSTANCE = this;

		File configFile = new File(Daedalus.DIRECTORY_CWD,"config.json");
		if(set.hasArgument(Daedalus.OPTION_CONFIG)){
			File newConfigFile = set.valueOf(Daedalus.OPTION_CONFIG);
			if(newConfigFile.isAbsolute()){
				configFile = newConfigFile;
			}else{
				configFile = new File(Daedalus.DIRECTORY_CWD,newConfigFile.getName());
			}
		}
		try{
			this.configManager = new ConfigManager(configFile);
		}catch(IOException e){
			System.err.println("Cannot load config file");
		}

		File wrapperFile = new File(Daedalus.DIRECTORY_CWD,"wrapper.json");
		if(set.hasArgument(Daedalus.OPTION_WRAPPER)){
			File newWrapperFile = set.valueOf(Daedalus.OPTION_WRAPPER);
			if(newWrapperFile.isAbsolute()){
				wrapperFile = newWrapperFile;
			}else{
				wrapperFile = new File(Daedalus.DIRECTORY_CWD,newWrapperFile.getName());
			}
		}
		try{
			this.wrapperManager = new WrapperManager(wrapperFile);
		}catch(IOException e){
			System.err.println("Cannot load wrapper file");
		}

		File gameFile = null;
		if(set.hasArgument(Daedalus.OPTION_GAME)){
			File newGameFile = set.valueOf(Daedalus.OPTION_GAME);
			if(newGameFile.isAbsolute()){
				gameFile = newGameFile;
			}else{
				gameFile = new File(Daedalus.DIRECTORY_GAMES,newGameFile.getName());
			}
		}
		try{
			this.game = new Game(gameFile);
		}catch(IOException e){
			System.err.println("Cannot load game");
		}

		File managerFile = new File(Daedalus.DIRECTORY_MANAGERS,"manager.jar");;
		if(set.hasArgument(Daedalus.OPTION_MANAGER)){
			File newManagerFile = set.valueOf(Daedalus.OPTION_MANAGER);
			if(newManagerFile.isAbsolute()){
				managerFile = newManagerFile;
			}else{
				managerFile = new File(Daedalus.DIRECTORY_MANAGERS,newManagerFile.getName());
			}
		}
		try{
			this.manager = new Manager(managerFile);
		}catch(IOException e){
			System.err.println("Cannot load manager");
		}

		if(set.has(Daedalus.OPTION_FILE)){
			// Set LOG file
		}

		if(set.has(Daedalus.OPTION_DEBUG)){
			// Set LOG debug on
		}
	}

	public void run(){
		//TODO Here is where the fun begins
		//this.wrapperManager.getWrappers(new File("manager.jar"));
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