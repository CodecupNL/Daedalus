package nl.codecup.daedalus.runner;

import nl.codecup.daedalus.config.ConfigManager;
import nl.codecup.daedalus.wrapper.ExecutableThread;
import nl.codecup.daedalus.objects.Game;
import nl.codecup.daedalus.objects.Log;
import nl.codecup.daedalus.objects.Manager;
import nl.codecup.daedalus.objects.Player;
import nl.codecup.daedalus.objects.Referee;
import nl.codecup.daedalus.runner.util.DaedalusHelpFormatter;
import nl.codecup.daedalus.wrapper.WrapperManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class Daedalus implements Runnable{

	private static final File DIRECTORY_CWD = new File(System.getProperty("user.dir"));

	private static final File DIRECTORY_GAMES = new File(Daedalus.DIRECTORY_CWD,"games");
	private static final File DIRECTORY_LOGS = new File(Daedalus.DIRECTORY_CWD,"logs");
	private static final File DIRECTORY_MANAGERS = new File(Daedalus.DIRECTORY_CWD,"managers");

	private static Daedalus INSTANCE;

	private static final OptionParser OPTIONPARSER = new OptionParser();
	private static final OptionSpec<?> OPTION_HELP = Daedalus.OPTIONPARSER.acceptsAll(Arrays.asList("h","help","?"),"Show help").forHelp();
	private static final OptionSpec<String> OPTION_LIST = Daedalus.OPTIONPARSER.acceptsAll(Arrays.asList("l","list"),"List items of specific type").withRequiredArg().describedAs("list");
	private static final OptionSpec<?> OPTION_VERSION = Daedalus.OPTIONPARSER.acceptsAll(Arrays.asList("v","version"),"Show version");
	private static final OptionSpec<File> OPTION_CONFIG = Daedalus.OPTIONPARSER.acceptsAll(Arrays.asList("c","config"),"Config file that should be used").withRequiredArg().describedAs("config").ofType(File.class);
	private static final OptionSpec<?> OPTION_DEBUG = Daedalus.OPTIONPARSER.acceptsAll(Arrays.asList("d","debug"),"Show debug log");
	private static final OptionSpec<File> OPTION_MANAGER = Daedalus.OPTIONPARSER.acceptsAll(Arrays.asList("m","manager"),"Manager that should run the game").withRequiredArg().describedAs("manager").ofType(File.class);
	private static final OptionSpec<File> OPTION_WRAPPER = Daedalus.OPTIONPARSER.acceptsAll(Arrays.asList("w","wrapper"),"Wrapper that should be used to load the code").withRequiredArg().describedAs("wrapper").ofType(File.class);

	private static final String VERSION = "Daedalus v1.0.0";

	private ConfigManager configManager;
	private Manager manager;
	private WrapperManager wrapperManager;

	public Daedalus(String... args){
		Daedalus.OPTIONPARSER.nonOptions();

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
				Game[] games = this.getGames();
				if(games.length==0){
					System.out.println("There are no games");
				}else{
					System.out.println("Games:");
					for(Game g : games){
						System.out.println(" - "+g.getName());
					}
				}
				System.exit(0);
			}else if("logs".equals(list)){
				Log[] logs = this.getLogs();
				if(logs.length==0){
					System.out.println("There are no logs");
				}else{
					System.out.println("Logs:");
					for(Log l : logs){
						System.out.println(" - "+l.getName());
					}
				}
				System.exit(0);
			}else if("managers".equals(list)){
				Manager[] managers = this.getManagers();
				if(managers.length==0){
					System.out.println("There are no managers");
				}else{
					System.out.println("Managers:");
					for(Manager m : managers){
						System.out.println(" - "+m.getName());
					}
				}
				System.exit(0);
			}else if(list!=null && list.startsWith("players")){
				String[] parts = list.split(":");
				String gameName = parts.length>=2?parts[1]:null;
				if("players".equals(parts[0]) && gameName!=null){
					Game game = null;
					for(Game g : this.getGames()){
						if(gameName.equals(g.getName())){
							game = g;
							break;
						}
					}
					if(game==null){
						System.out.println("Unknown game");
						System.exit(0);
					}
					Player[] players = game.getPlayers();
					if(players.length==0){
						System.out.println("There are no players");
					}else{
						System.out.println("Players:");
						for(Player p : players){
							System.out.println(" - "+p.getName());
						}
					}
					System.exit(0);
				}
			}else if(list!=null && list.startsWith("referees")){
				String[] parts = list.split(":");
				String gameName = parts.length>=2?parts[1]:null;
				if("referees".equals(parts[0]) && gameName!=null){
					Game game = null;
					for(Game g : this.getGames()){
						if(gameName.equals(g.getName())){
							game = g;
							break;
						}
					}
					if(game==null){
						System.out.println("Unknown game");
						System.exit(0);
					}
					Referee[] referees = game.getReferees();
					if(referees.length==0){
						System.out.println("There are no referees");
					}else{
						System.out.println("Referees:");
						for(Referee r : referees){
							System.out.println(" - "+r.getName());
						}
					}
					System.exit(0);
				}
			}
			System.err.println("Unknown list");
			System.exit(0);
		}

		if(set.has(Daedalus.OPTION_HELP)){
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

		File managerFile = new File(Daedalus.DIRECTORY_MANAGERS,"manager.jar");;
		if(set.hasArgument(Daedalus.OPTION_MANAGER)){
			File newManagerFile = set.valueOf(Daedalus.OPTION_MANAGER);
			if(newManagerFile.isAbsolute()){
				managerFile = newManagerFile;
			}else{
				managerFile = new File(Daedalus.DIRECTORY_MANAGERS,newManagerFile.getName());
			}
		}
		this.manager = new Manager(managerFile);

		if(set.has(Daedalus.OPTION_DEBUG)){
			// Set LOG debug on
		}
	}

	public ConfigManager getConfigManager(){
		return this.configManager;
	}

	public Game[] getGames(){
		return Game.getGames(Daedalus.DIRECTORY_GAMES);
	}

	public Log[] getLogs(){
		return Log.getLogs(Daedalus.DIRECTORY_LOGS);
	}

	public Manager getManager(){
		return this.manager;
	}

	public Manager[] getManagers(){
		return Manager.getManagers(Daedalus.DIRECTORY_MANAGERS);
	}

	public WrapperManager getWrapper(){
		return this.wrapperManager;
	}

	public void run(){
		ExecutableThread t = this.getManager().getThread();

		//TODO Here is where the fun begins
	}

	public static Daedalus getInstance(){
		return Daedalus.INSTANCE;
	}

}