package elevatorFiles;

/**
 * The ElevatorCommand class represents a command for the elevator, specifying
 * its origin and destination floors.
 */
public class ElevatorCommand {

	private int origin;
	private int destination;

	/**
	 * Constructs an ElevatorCommand object with the specified origin and
	 * destination floors.
	 * 
	 * @param origin      The floor from which the elevator command originates.
	 * @param destination The floor to which the elevator command is destined.
	 */
	ElevatorCommand(int origin, int destination) {
		this.origin = origin;
		this.destination = destination;
	}

	// Getters and setters for origin and destination
	int getOrigin() {
		return origin;
	}

	void setOrigin(int origin) {
		this.origin = origin;
	}

	int getDestination() {
		return destination;
	}
	
	void setDestination(int destination) {
		this.destination = destination;
	}
}