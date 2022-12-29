package nl.codecup.daedalus.runner;

import nl.codecup.daedalus.config.ConfigManager;
import nl.codecup.daedalus.objects.Battle;
import nl.codecup.daedalus.objects.Game;
import nl.codecup.daedalus.objects.Log;
import nl.codecup.daedalus.objects.Manager;
import nl.codecup.daedalus.objects.Player;
import nl.codecup.daedalus.objects.Referee;
import nl.codecup.daedalus.protocol.IO;
import nl.codecup.daedalus.protocol.LineInputStream;
import nl.codecup.daedalus.protocol.Packet;
import nl.codecup.daedalus.protocol.PacketListener;
import nl.codecup.daedalus.runner.util.DaedalusHelpFormatter;
import nl.codecup.daedalus.wrapper.ExecutableThread;
import nl.codecup.daedalus.wrapper.WrapperManager;

import java.io.File;
import java.io.IOException;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.json.JSONArray;

public class Daedalus implements PacketListener,Runnable{

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

	private static final String TAG = "DAEDALUS";

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

		File managerFile = new File(Daedalus.DIRECTORY_MANAGERS,this.configManager.manager());
		if(set.hasArgument(Daedalus.OPTION_MANAGER)){
			File newManagerFile = set.valueOf(Daedalus.OPTION_MANAGER);
			if(newManagerFile.isAbsolute()){
				managerFile = newManagerFile;
			}else{
				managerFile = new File(Daedalus.DIRECTORY_MANAGERS,newManagerFile.getName());
			}
		}
		this.manager = new Manager(managerFile);

		Log.setDebug(set.has(Daedalus.OPTION_DEBUG) || this.configManager.debug());
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

	public WrapperManager getWrapperManager(){
		return this.wrapperManager;
	}

	private boolean isRunning = true;

	public boolean isRunning(){
		return this.isRunning;
	}

	public ExecutableThread managerThread = null;

	private Map<String,DaedalusBattle> battles = new HashMap<>();

	@Override
	public void run(){
//		ExecutableThread managerThread = null;
//		try{
//			managerThread = ExecutableThread.start(this.getManager());
//		}catch(Exception e){
//			System.err.println(e.getMessage());
//			System.exit(0);
//		}
//		this.isRunning = true;
//		System.err.println("TRY -> DAEDALUS");
//		try{
//			new Packet(Packet.COMMAND_MANAGER_STOP).toStream(managerThread.STDIN);
//			new Packet(Packet.COMMAND_MANAGER_START).toStream(managerThread.STDIN);
//		}catch(Exception e){
//			e.printStackTrace();
//		}System.err.println("TRY -> DAEDALUS 2");
//		while(this.isRunning && managerThread.isRunning()){
//			try{
//				if(managerThread.STDOUT.available()>0){
//					Packet p = Packet.fromStream(new LineInputStream(managerThread.STDOUT));
//					this.onReceivePacket(p);
//					p.toStream(managerThread.STDIN);
//				}
//				if(managerThread.STDERR.available()>0){
//					byte[] buf = new byte[managerThread.STDERR.available()];
//					managerThread.STDERR.read(buf);
//					System.out.print(new String(buf));
//					//System.out.println("["+buf.length+"] "+new String(buf).trim());
//					//Thread.sleep(1000);
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}

		try{
			managerThread = ExecutableThread.start(this.getManager());
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
		LineInputStream lis = new LineInputStream(managerThread.STDOUT);
		LineInputStream temp = new LineInputStream(managerThread.STDERR);



		try{
			Log.debug(Daedalus.TAG,"Start manager");
			new Packet("MANAGER_START").toStream(managerThread.STDIN);
		}catch(Exception e){
			e.printStackTrace();
		}

		this.loop(lis,temp);
	}

	private void loop(LineInputStream in,LineInputStream err2){
		Log.debug(TAG,"Joining loop");
		while(this.isRunning){
			try{
				//TEMP START
				if(err2.available()>0){
					System.err.print(new String(err2.readLine(true,true,true,false)));
				}
				//TEMP STOP
				if(in.available()>0){
					Log.debug(Daedalus.TAG,"Packet received");
					Packet packet = null;
					try{
						packet = Packet.fromStream(in);
					}catch(Exception e){
						Log.error(Daedalus.TAG,"Invalid packet");
					}
					if(packet!=null){
						this.onReceivePacket(packet);
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		Log.debug(Daedalus.TAG,"Quiting loop");
	}

	@Override
	public void onReceivePacket(Packet packet){
		if(Packet.COMMAND_MANAGER_START.equals(packet.getCommand())){
			if(packet.isResponse()){
				Log.info(Daedalus.TAG,"Started manager");
				return;
			}
			Log.error(Daedalus.TAG,"The command '"+packet.getCommand()+"' should be a response.");
			return;
		}
		if(Packet.COMMAND_DAEDALUS_START.equals(packet.getCommand())){
			if(!packet.isResponse()){
				Log.debug(Daedalus.TAG,"Starting Daedalus");
				try{
					new Packet(Packet.COMMAND_DAEDALUS_START,true).toStream(managerThread.STDIN);
				}catch(Exception e){
					e.printStackTrace();
				}
				return;
			}
			Log.error(Daedalus.TAG,"The command '"+packet.getCommand()+"' should not be a response.");
			return;
		}
		if(Packet.COMMAND_BATTLE_CREATE.equals(packet.getCommand())){
			if(!packet.isResponse()){
				Log.debug(Daedalus.TAG,"Creating battle");
				Battle battle = new Battle();
				battle.setId((String) packet.getData().get("id"));
				Game game = null;
				for(Game g : Game.getGames(new File("games"))){
					if(g.getName().equals(packet.getData().get("game"))){
						game = g;
						break;
					}
				}
				if(game==null){
					//TODO
					Log.error(Daedalus.TAG,"Game '"+packet.getData().get("game")+"' not found");
					return;
				}
				battle.setGame(game);
				Referee referee = null;
				for(Referee r : game.getReferees()){
					if(r.getName().equals(packet.getData().get("referee"))){
						referee = r;
						break;
					}
				}
				if(referee==null){
					//TODO
					Log.error(Daedalus.TAG,"Referee '"+packet.getData().get("referee")+"' not found for game '"+game.getName()+"'");
					return;
				}
				battle.setReferee(referee);
				JSONArray jsonArr = (JSONArray) packet.getData().get("players");
				String[] playersStr = new String[jsonArr.length()];
				for(int i=0;i<playersStr.length;i++){
					playersStr[i] = jsonArr.getString(i);
				}
				Player[] players = new Player[playersStr.length];
				for(int i=0;i<players.length;i++){
					Player player = null;
					for(Player p : game.getPlayers()){
						if(p.getName().equals(playersStr[i])){
							player = p;
							break;
						}
					}
					if(player==null){
						//TODO
						Log.error(Daedalus.TAG,"Player '"+playersStr[i]+"' not found for game '"+game.getName()+"'");
						return;
					}
					players[i] = player;
				}
				battle.setPlayers(players);
				this.battles.put(battle.getId(),new DaedalusBattle(battle));
				try{
					new Packet(Packet.COMMAND_BATTLE_CREATE,true).toStream(managerThread.STDIN);
				}catch(Exception e){
					e.printStackTrace();
				}
				return;
			}
			Log.error(Daedalus.TAG,"The command '"+packet.getCommand()+"' should not be a response.");
			return;
		}
		if(Packet.COMMAND_BATTLE_START.equals(packet.getCommand())){
			if(!packet.isResponse()){
				Log.debug(Daedalus.TAG,"Starting battle");
				String id = (String) packet.getData().get("id");
				DaedalusBattle battle = battles.get(id);
				new Thread(battle,"Battle "+battle.getBattle().getId()).start();
				try{
					new Packet(Packet.COMMAND_BATTLE_START,true).toStream(managerThread.STDIN);
				}catch(Exception e){
					e.printStackTrace();
				}
				return;
			}
			Log.error(Daedalus.TAG,"The command '"+packet.getCommand()+"' should not be a response.");
			return;
		}
		if(Packet.COMMAND_BATTLE_RESULT.equals(packet.getCommand())){
			if(packet.isResponse()){
				Log.debug(Daedalus.TAG,"Result from battle confirmed");
				return;
			}
			Log.error(Daedalus.TAG,"The command '"+packet.getCommand()+"' should be a response.");
			return;
		}
		if(Packet.COMMAND_DAEDALUS_STOP.equals(packet.getCommand())){
			if(!packet.isResponse()){
				Log.debug(Daedalus.TAG,"Stopping Daedalus");
				try{
					new Packet(Packet.COMMAND_DAEDALUS_STOP,true).toStream(managerThread.STDIN);
				}catch(Exception e){
					e.printStackTrace();
				}
				Log.debug(Daedalus.TAG,"Stop manager");
				try{
					new Packet(Packet.COMMAND_MANAGER_STOP).toStream(managerThread.STDIN);
				}catch(Exception e){
					e.printStackTrace();
				}
				return;
			}
			Log.error(Daedalus.TAG,"The command '"+packet.getCommand()+"' should not be a response.");
			return;
		}
		if(Packet.COMMAND_MANAGER_STOP.equals(packet.getCommand())){
			if(packet.isResponse()){
				Log.info(Daedalus.TAG,"Stopped manager");
				this.isRunning = false;//TODO Better place
				return;
			}
			Log.error(Daedalus.TAG,"The command '"+packet.getCommand()+"' should be a response.");
			return;
		}
		Log.warning(Daedalus.TAG,"Unknown command '"+packet.getCommand()+"'");
	}

	public static Daedalus getInstance(){
		return Daedalus.INSTANCE;
	}

}