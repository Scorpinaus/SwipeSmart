package elevatorFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 * The Manager class manages the elevators and commands in the simulation.
 */
public class Manager {
	private Map<ElevatorThread, Thread> elevators = new HashMap<ElevatorThread, Thread>();
	private Queue<ElevatorCommand> commands = new LinkedList<>();
	static Object lock = new Object();
	int origin;
	int destination;

	/**
	 * Retrieves the elevators.
	 * 
	 * @return A map containing the elevators.
	 */
	public Map<ElevatorThread, Thread> getElevators() {
		return this.elevators;
	}

	/**
	 * Sets the elevators.
	 * 
	 * @param elevators1 The elevators to set.
	 */
	public void setElevators(ElevatorThread... elevators1) {
		for (ElevatorThread e : elevators1) {
			Thread t = new Thread(e);
			this.elevators.put(e, t);
		}
	}

	/**
	 * Retrieves the commands.
	 * 
	 * @return A queue containing the commands.
	 */
	public Queue<ElevatorCommand> getCommands() {
		return commands;
	}

	/**
	 * Sets the commands from a file.
	 * 
	 * @param file The file containing the commands.
	 */
	public void setCommands(File file) {
		System.out.println("Reading commands");
		try (BufferedReader bR = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = bR.readLine()) != null) {
				String[] input = line.split(" ");
				for (int i = 0; i < input.length; i += 2) {
					int origin = Integer.parseInt(input[i]);
					int destination = Integer.parseInt(input[i + 1]);

					ElevatorCommand newCommand = new ElevatorCommand(origin, destination);
					this.commands.add(newCommand);

				}
			}
			bR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Commands received");
	}

	/**
	 * sends command to nearest elevator calls selectedElevator() to check for
	 * nearest idle elevator calls waitForElevatorAvailability() synchronized lock
	 */
	public void deployElevators() throws InterruptedException {
		while (!commands.isEmpty()) {
			ElevatorThread selectedElevator = findNearestIdleElevator();

			if (selectedElevator != null) {
				ElevatorCommand commandToSend = commands.poll();
				selectedElevator.setTasks(commandToSend);
				System.out.println("Passengers call from Level " + commandToSend.getOrigin() + ", drop-off at Level "
						+ commandToSend.getDestination() + ".");
				selectedElevator.getElevator().setCurrentState("Moving");
				synchronized (selectedElevator.lock) {
					selectedElevator.lock.notifyAll();
				}
			} else {
				waitForElevatorAvailability();
			}
		}
	    while (true) {
	    	int numberOfIdleLifts = 0;
	    	for (ElevatorThread e : elevators.keySet()) {
	    		if (e.getElevator().getCurrentState() == "Idle") {
	    			numberOfIdleLifts += 1;
	    		}
	    	}
	    	if (numberOfIdleLifts == elevators.keySet().size()) {
	    		if (commands.size() == 0) {
	    			this.consoleCommands();
	    		}
	    	}
	    }
	}

	/**
	 * checks for idle elevators then filters through them to find the nearest one
	 * to next call
	 * 
	 * @return elevatorThread of the nearest elevator
	 */
	private ElevatorThread findNearestIdleElevator() {
		ElevatorThread selectedElevator = null;
		int minDistance = Integer.MAX_VALUE;

		for (ElevatorThread elevator : elevators.keySet()) {
			if (elevator.getElevator().getCurrentState().equals("Idle")) {
				int distance = Math.abs(elevator.getElevator().getCurrentFloor() - commands.peek().getOrigin());
				if (distance < minDistance) {
					selectedElevator = elevator;
					minDistance = distance;
				}
			}
		}

		return selectedElevator;
	}
	
	 /**
     * check if input is numeric
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) { 
    	  try {  
    	    Integer.parseInt(str);  
    	    return true;
    	  } catch(NumberFormatException e){  
    	    return false;  
    	  }  
    	}

    /*
     * asks the user if they want to input things to the console
     */
    public void consoleCommands() {
	    Scanner sc = new Scanner(System.in);
	    String response1 = null;
	    System.out.println("Do you want to start/continue using the elevator manager?");
	    while((response1 = sc.nextLine()).isEmpty()) {
	    	System.out.println("Invalid Response, please input a Yes or No answer");
	    }
	    if (response1.charAt(0) == 'Y' || response1.charAt(0) == 'y'){
	    	System.out.println("Do you want to input passenger commands to the console?");
		    String response2 = null;
		    while((response2 = sc.nextLine()).isEmpty()) {
		    	System.out.println("Invalid Response, please input a Yes or No answer");
		    }

		    // if they want to add inputs to the console
		    // only typing yes will allow you to add more commands
		    if (response2.charAt(0) == 'Y' || response2.charAt(0) == 'y'){
		    	getCommandsFromConsole();
		    }
		    else {
		    	setCommands(new File ("test.txt"));
				try {
					deployElevators();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		}
	    else {
	    	sc.close();
	    	System.exit(0);
	    }
    }
    
    /*
     * for an input floor and an output destination
     */
    public void getCommandsFromConsole(){

	    boolean morePassengerCommands = true;
	    Scanner sc1 = new Scanner(System.in);  // Create a Scanner object
	    while (morePassengerCommands) {
		    System.out.println("Enter which floor you're in");
		    String line1;	
		    
		    // checks if  the input was empty and if not numeric
		    	// while line1 is empty and not numeric
		    while((line1 = sc1.nextLine()).isEmpty() || !isNumeric(line1)) {
		    	
		    	System.out.println("Enter which floor you're in again");
		    }
		    

		    origin = Integer.parseInt(line1);  // Read user input and change it to integer
		    
		    // destination floor
		    System.out.println("Enter which floor to go");
		    String line2 = null;
		    // checks if  the input was empty
		    while((line2 = sc1.nextLine()).isEmpty() || !isNumeric(line1)) {
		    	System.out.println("Enter which floor you're in");
		    }
		    destination = Integer.parseInt(line2);  // Read user input and change it to integer
		    
		    // add them to the commands
		    this.commands.add( new ElevatorCommand(origin, destination));

		    
		    // checks if there are more passengers to be loaded
		    System.out.println("Any more passengers wanting to board?");
		    String response = null;
		    while((response = sc1.nextLine()).isEmpty()) {
		    	System.out.println("Any more passengers wanting to board?");
		    }

		    // only typing yes will allow you to add more commands
		    if (response.charAt(0) == 'Y' || response.charAt(0) == 'y'){
		    	morePassengerCommands = true;
		    }
		    else {
		    	try {
					deployElevators();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
	    }
	    sc1.close();
    }
    

	/**
	 * synchronized lock to check if elevators are available for calls
	 */
	private void waitForElevatorAvailability() throws InterruptedException {
		synchronized (lock) {
			lock.wait();
		}
	}
}