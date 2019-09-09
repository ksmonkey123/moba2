package ch.awae.moba2;

import ch.awae.utils.functional.T2;

import java.util.LinkedList;
import java.util.List;

final public class Model {
	private final static PathRegistry paths = new PathRegistry();
	//private final static Buttons buttons = new Buttons();

	private final static LinkedList<T2<Path, Boolean>> queue = new LinkedList<>();

	private static boolean stealthMode = false;
	private static long lastUpdate = System.currentTimeMillis();


	public static void executeCommands() {
		synchronized (queue) {
			for (T2<Path, Boolean> command : queue)
				executeCommand(command._1, command._2);
			queue.clear();
		}
	}

	public static void update() {
		lastUpdate = System.currentTimeMillis();
	}

	public static long getLastUpdate() {
		return lastUpdate;
	}

	//public static Buttons buttons() {
	//	return buttons;
	//}

	public static boolean isStealthMode() {
		return stealthMode;
	}

	public static void toggleStealthMode() {
		stealthMode = !stealthMode;
	}

	public static List<Path> getActivePaths() {
		return paths.getAllPaths();
	}

	public static List<Path> getActivePaths(Sector sector) {
		return paths.getPaths(sector);
	}

	public static boolean isPathActive(Path path) {
		return paths.getAllPaths().contains(path);
	}

	// COMMAND EXECUTORS

	private static void executeCommand(Path path, boolean register) {
		if (register)
			paths.register(path);
		else
			paths.unregister(path);
	}

	public static void queuePath(Path path, boolean state) {
		queue.add(new T2<>(path, state));
	}

}
