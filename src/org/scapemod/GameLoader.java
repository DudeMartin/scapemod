package org.scapemod;

import java.applet.Applet;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.scapemod.accessor.Client;
import org.scapemod.bytecode.asm.ClassReader;
import org.scapemod.util.WebUtilities;

/**
 * Provides methods for loading and starting game instances.
 * 
 * @author Martin Tuskevicius
 */
public final class GameLoader {

    /**
     * The format string for the address of a game page.
     */
    private static final String GAME_PAGE_ADDRESS = "http://oldschool%d.runescape.com/";

    /**
     * Represents a game stub. This stub acts as an interface between a game
     * instance and the application running the game.
     * 
     * @author Martin Tuskevicius
     */
    public final class GameStub {

	/**
	 * The instance of the <code>client</code> class.
	 */
	public final Object clientInstance;

	/**
	 * The accessor for the <code>client</code> class instance.
	 */
	public final Client client;

	/**
	 * The game applet.
	 */
	public final Applet applet;

	/**
	 * The game applet stub.
	 */
	public final GameAppletStub appletStub;

	/**
	 * Creates a new game stub.
	 * 
	 * @param clientInstance
	 *            the <code>client</code> class instance.
	 * @param client
	 *            the accessor for the <code>client</code> class instance.
	 * @param applet
	 *            the game applet.
	 * @param appletStub
	 *            the game applet stub.
	 */
	private GameStub(Object clientInstance, Client client, Applet applet, GameAppletStub appletStub) {
	    this.clientInstance = clientInstance;
	    this.client = client;
	    this.applet = applet;
	    this.appletStub = appletStub;
	}
    }

    private long checksum;
    private Map<String, ClassReader> readers;

    /**
     * Prevents external instantiation.
     */
    private GameLoader() {

    }

    /**
     * Loads the game.
     * 
     * @param world
     *            the initial game world to connect to.
     * @return the game stub.
     * @throws Exception
     *             if an error occurs while loading.
     */
    private GameStub load(int world) throws Exception {
	/*
	 * Construct the game page address for the given world number.
	 */
	String pageAddress = String.format(GAME_PAGE_ADDRESS, world);
	
	/*
	 * Download the gamepack.
	 */
	JarFile gamepack = ((JarURLConnection) new URL("jar:" + pageAddress + "gamepack.jar!/").openConnection()).getJarFile();
	
	/*
	 * Process the gamepack.
	 */
	processGamepack(gamepack);
	
	/*
	 * Download the mod script corresponding to the gamepack's checksum.
	 */
	ModScript modScript = new ModScript(ByteBuffer.wrap(WebUtilities.readContents(ModScriptConfiguration.getModScriptAddress(checksum))));
	
	/*
	 * Modify the classes in the gamepack.
	 */
	Map<String, byte[]> modifiedClasses = modScript.mod(readers);

	/*
	 * Create a class loader using the modified classes, then load and
	 * instantiate the "client" class.
	 */
	Object clientInstance = new DefinableClassLoader(modifiedClasses).loadClass("client").newInstance();
	
	/*
	 * Parse the game applet parameters.
	 */
	Map<String, String> parameters = WebUtilities.parseParameters(new String(WebUtilities.readContents(pageAddress)), "haveie6");
	return new GameStub(clientInstance, (Client) clientInstance, (Applet) clientInstance, new GameAppletStub(new URL(pageAddress), parameters));
    }

    /**
     * Processes a gamepack.
     * 
     * @param gamepack
     *            the gamepack archive.
     * @throws Exception
     *             if an error occurs while processing.
     */
    private void processGamepack(JarFile gamepack) throws Exception {
	readers = new HashMap<String, ClassReader>();
	Enumeration<JarEntry> entries = gamepack.entries();
	while (entries.hasMoreElements()) {
	    JarEntry entry = entries.nextElement();
	    String name = entry.getName();
	    if (name.endsWith(".class")) {
		checksum += entry.getCrc();
		readers.put(name.substring(0, name.indexOf('.')), new ClassReader(gamepack.getInputStream(entry)));
	    }
	}
    }

    /**
     * Loads, but does not start a game instance.
     * 
     * @param world
     *            the initial game world to connect to.
     * @return the game stub.
     * @throws Exception
     *             if an error occurs while loading.
     */
    public static GameStub loadGame(int world) throws Exception {
	return new GameLoader().load(world);
    }

    /**
     * Starts a loaded game instance. A game instance should only be started
     * once.
     * 
     * @param gameStub
     *            the game stub.
     */
    public static void startGame(GameStub gameStub) {
	Applet applet = gameStub.applet;
	GameAppletStub stub = gameStub.appletStub;
	applet.setStub(stub);
	applet.init();
	stub.setActive(true);
	applet.start();
    }

    /**
     * Loads and starts a game instance.
     * 
     * @param world
     *            the initial game world to connect to.
     * @return the game stub.
     * @throws Exception
     *             if an error occurs while loading.
     */
    public static GameStub startGame(int world) throws Exception {
	GameStub gameStub = loadGame(world);
	startGame(gameStub);
	return gameStub;
    }
}