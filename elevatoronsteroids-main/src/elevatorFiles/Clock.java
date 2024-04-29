package elevatorFiles;

public class Clock implements Runnable {

	// Attribute
	// Create a static time variable which increases forever as the simulation runs
	private static int time = 0;
	

	
	/** Returns a clock-display format of time for console printing purposes
	 * 
	 * @return displayTime time in a clock format xx:yy 
	 */
	public static String getTime() {
		String minutes = String.valueOf(time / 60);
		String seconds = String.format("%02d", time % 60);
		
		String displayTime = (minutes) + ":" + String.valueOf(seconds);
		return displayTime;
	}
	

	/**
	 * Method to increase simulated time
	 */
	public static void tick() {
		time++;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				Clock.tick();
			}
		}
		
	}
	
}
