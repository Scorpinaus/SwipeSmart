package elevatorFiles;

public class Runner {
	public static void main(String[] args) {
		Manager manager = new Manager();
		final int NUMBER_OF_ELEVATORS = 10; // Set the number of elevators here

		// Create elevators and elevator threads dynamically
		for (int i = 1; i <= NUMBER_OF_ELEVATORS; i++) {
			Elevator elevator = new Elevator();
			ElevatorThread elevatorThread = new ElevatorThread();
			elevatorThread.setElevator(elevator);
			manager.getElevators().put(elevatorThread, new Thread(elevatorThread));
			manager.getElevators().get(elevatorThread).setName("Elevator " + i);
		}
		
		// Start elevator threads
		for (Thread thread : manager.getElevators().values()) {
			thread.start();
		}

		// Start actual clock
		Thread globalClock = new Thread(new Clock());
		globalClock.start();
		manager.consoleCommands();

	}
}
