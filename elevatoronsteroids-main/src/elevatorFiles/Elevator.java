package elevatorFiles;

/**
 * The Elevator class represents an elevator in the simulation. It handles
 * movement between floors, loading passengers, and unloading passengers.
 */
public class Elevator {

	private final int MOVE_TIME = 1000;
	private final int LOAD_UNLOAD_TIME = 5000;

	private int currentFloor = 1;
	private String currentState = "Idle";

	/**
	 * Moves the elevator to the specified destination floor.
	 * 
	 * @param destination The destination floor to which the elevator will move.
	 */
	public void move(int destination) throws InterruptedException {
		if (this.currentFloor != destination) {

			// Print message indicating elevator movement
			System.out.println("Time: " + Clock.getTime() + " \t| " + getElevatorThreadName() + " is moving to Level "
					+ destination);
			// Simulate elevator movement
			Thread.sleep(MOVE_TIME);
			// Print message indicating arrival at destination floor
			System.out.println("Time: " + Clock.getTime() + " \t| " + getElevatorThreadName() + " has reached Level "
					+ destination);
			this.currentFloor = destination;
		} else {
			// Print message indicating elevator is already at the requested floor
			System.out.println("Time: " + Clock.getTime() + " \t| " + getElevatorThreadName()
					+ " is already on the requested floor: Floor " + this.getCurrentFloor());
		}
	}

	/**
	 * Loads passengers into the elevator.
	 */
	public void load() throws InterruptedException {
		// Print message indicating loading passengers
		System.out.println("Time: " + Clock.getTime() + " \t| " + "Loading passengers in " + getElevatorThreadName());
		// Simulate loading passengers
		Thread.sleep(LOAD_UNLOAD_TIME);
		// Print message indicating all passengers are aboard
		System.out.println("Time: " + Clock.getTime() + " \t| " + "Passengers all aboard " + getElevatorThreadName());
	}

	/**
	 * Unloads passengers from the elevator.
	 */
	public void offload() throws InterruptedException {
		// Print message indicating unloading passengers
		System.out
				.println("Time: " + Clock.getTime() + " \t| " + "Unloading passengers from " + getElevatorThreadName());
		// Simulate unloading passengers
		Thread.sleep(LOAD_UNLOAD_TIME);
		// Print message indicating all passengers are unloaded
		System.out.println(
				"Time: " + Clock.getTime() + " \t| " + "All passengers unloaded from " + getElevatorThreadName());
	}

	public String getElevatorThreadName() {
		return Thread.currentThread().getName();
	}

	// Getters and setters for currentFloor and currentState
	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
}