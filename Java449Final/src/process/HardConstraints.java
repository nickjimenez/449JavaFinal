package process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import commons.OutputWriter;

import java.lang.String;

/**
 * 
 * @author nickolejimenez
 * Hard Constraints class deals with Forced Partial Assignment, Forbidden Machine and Too-Near 
 * Tasks. 
 * 
 * Updates "global" main matrix of Hard Constraints for subsequent classes to use. 
 * Final Version
 */
public class HardConstraints{
	// change
	// doHard function takes the "global" matrix, list of forced pairings, forbidden pairings and too near Hard
	// constraints. doHard updates the "global" matrix of the Hard Constraints to be passed to soft constraints.
	public int[][] doHard(int[][] mainArray, int[] forced, int[] forbidden, ArrayList<String> tooNear, String errorString) {
		
		Set<Integer> forbiddenSet = new HashSet<Integer>();
		Set<Integer> forcedSet = new HashSet<Integer>();
		
		for (int counter = 0; counter < 8; counter++) {
			forbiddenSet.add(forbidden[counter]);
			forcedSet.add(forced[counter]);
		}
		
		if (forcedSet.size() == 1) {
			if (forcedSet.contains(-1)) {
				for (int counter = 0; counter < forced.length; counter++) {
					forcedPartial(mainArray, forced, forced[counter], counter);	
				}
			}else {
				OutputWriter.writeFile(forced, 0, "No valid solution possible!");
				System.exit(0);
			}
		}else {
			for (int counter = 0; counter < forced.length; counter++) {
				forcedPartial(mainArray, forced, forced[counter], counter);
			}
		}		
		
		
		if (forbiddenSet.size() == 1){
			if (forbiddenSet.equals(-1)) {
				for (int counter = 0; counter < forbidden.length; counter++) {
					forbidden(mainArray, forbidden, forbidden[counter], counter, forced);
				}
			}else {
				OutputWriter.writeFile(forced, 0, "No valid solution possible!");
				System.exit(0);
			}
		}else {
			for (int counter = 0; counter < forbidden.length; counter++) {
				forbidden(mainArray, forbidden, forbidden[counter], counter, forced);
			}
		}
			
			
		if (tooNear.size()>0) {
			tooNearRead(mainArray, tooNear, forced);
		}
		return mainArray;
	}

	//takes the too near tasks array list and converts it to integers for easier processing.
	private static void tooNearRead(int[][] mainArray, ArrayList<String> tooNearArray, int[] forcedList) {
		// TODO Auto-generated method stub
		String tooNearStringPair;
		for (int count = 0; count < tooNearArray.size(); count++) {
			tooNearStringPair = tooNearArray.get(count);
			tooNearComp(mainArray, tooNearStringPair, tooNearArray, forcedList);
		}
	}
	
	//too Near Comparison. Assigns numeric value to first task in too near tasks.
	private static void tooNearComp(int[][] mainArray, String tooNearStringPair, ArrayList<String> tooNearArray, int[] forcedList) {
		// TODO Auto-generated method stub
		String tooNearString = tooNearStringPair;
		
		switch(tooNearString.charAt(0)) {
		case 'A':
			tooNearComp2(mainArray, 0, tooNearString.charAt(1), tooNearArray, forcedList);
			break;
		case 'B':
			tooNearComp2(mainArray, 1, tooNearString.charAt(1), tooNearArray, forcedList);
			break;
		case 'C':
			tooNearComp2(mainArray, 2, tooNearString.charAt(1), tooNearArray, forcedList);
			break;
		case 'D':
			tooNearComp2(mainArray, 3, tooNearString.charAt(1), tooNearArray, forcedList);
			break;
		case 'E':
			tooNearComp2(mainArray, 4, tooNearString.charAt(1), tooNearArray, forcedList);
			break;
		case 'F':
			tooNearComp2(mainArray, 5, tooNearString.charAt(1), tooNearArray, forcedList);
			break;
		case 'G':
			tooNearComp2(mainArray, 6, tooNearString.charAt(1), tooNearArray, forcedList);
			break;
		case 'H':
			tooNearComp2(mainArray, 7, tooNearString.charAt(1), tooNearArray, forcedList);
			break;
		}
	}
	
	//too Near Comparison. assigns numeric to 2nd task in too near tasks
	private static void tooNearComp2(int[][] mainArray, int firstTask, char secondTask, ArrayList<String> tooNearArray, int[] forcedList) {
		// TODO Auto-generated method stub
		switch(secondTask) {
		case 'A':
			setTooNear(mainArray, firstTask, 0, tooNearArray, forcedList);
			break;
		case 'B':
			setTooNear(mainArray, firstTask, 1, tooNearArray, forcedList);
			break;
		case 'C':
			setTooNear(mainArray, firstTask, 2, tooNearArray, forcedList);
			break;
		case 'D':
			setTooNear(mainArray, firstTask, 3, tooNearArray, forcedList);
			break;
		case 'E':
			setTooNear(mainArray, firstTask, 4, tooNearArray, forcedList);
			break;
		case 'F':
			setTooNear(mainArray, firstTask, 5, tooNearArray, forcedList);
			break;
		case 'G':
			setTooNear(mainArray, firstTask, 6, tooNearArray, forcedList);
			break;
		case 'H':
			setTooNear(mainArray, firstTask, 7, tooNearArray, forcedList);
			break;
		}
		
	}
	
	//sets too near tasks in "global" mainMatrix
	private static void setTooNear(int[][] mainArray, int firstTask, int secondTask, ArrayList<String> tooNearArray, int[] forcedList) {
		// TODO Auto-generated method stub
	    for (int count = 0 ; count < 8; count++) {
	    		if(mainArray[count][firstTask] != -1){ //checks if machine has already been forced to a task. 
	    			//if it hasn't, go in. else ignore.
	    			if (count == 7) { //if machine 8, forbidden next is machine 1.
	    				if (isForced(count,firstTask,forcedList)) {
	    					if (isForced(count+1, secondTask, forcedList)) {
	    						OutputWriter.writeFile(forcedList, 0, "No valid solution possible!");
	    						System.exit(0);
	    					}
	    					else {	mainArray[0][secondTask] = -1;
	    					}
	    				}
	    			}else {
	    				if (isForced(count,firstTask, forcedList)) {
	    					if (isForced(count+1, secondTask, forcedList)) {
	    						OutputWriter.writeFile(forcedList, 0, "No valid solution possible!");
	    						System.exit(0);
	    					}
	    					else {	mainArray[count+1][secondTask] = -1;
	    					}
	    				}		
	    			}

	    		}
	    }
	}
	
	//used by too near tasks hard constraints	 to determine if task mentioned is hard assigned.
	//makes appropriate assignments
	private static boolean isForced(int machine, int task, int[] forcedList) {
		// TODO Auto-generated method stub
		//if (forcedList.length == 0) {setForcedNoChange()};
		if (forcedList[machine]==task) {return true;}
		else {return false;}
	}

	//deals with forbidden task list.
	private static void forbidden(int[][] mainArray, int[] forbidden, int forbiddenTask, int machine, int[] forcedList) {
		// TODO Auto-generated method stub
		//int ignoreVal = -1;
		//for (int count = 0; count < 8; count++) {
		//	int position = forbidden[count];
		//	if (position == forbidden[count]) {}
		//}
		if (checkTaskBounds(forbiddenTask) == false) {
			OutputWriter.writeFile(forcedList, 0, "No valid solution possible!");
			//do nothing. go back to for loop
			//System.out.println("ERROR: Task out of bounds");
			System.exit(0);
			
		}
		if (forbiddenTask == -1) {
			//do nothing go back to loop
			setNoChange(mainArray, machine);
		}
		else {	
		
			switch (machine) {
			case 0:
				setForbidden(mainArray, 0, forbiddenTask, forcedList);
				break;
			case 1:
				setForbidden(mainArray, 1, forbiddenTask, forcedList);
				break;
			case 2:
				setForbidden(mainArray, 2, forbiddenTask, forcedList);
				break;
			case 3:
				setForbidden(mainArray, 3, forbiddenTask, forcedList);
				break;
			case 4:
				setForbidden(mainArray, 4, forbiddenTask, forcedList);
				break;
			case 5:
				setForbidden(mainArray, 5, forbiddenTask, forcedList);
				break;
			case 6:
				setForbidden(mainArray, 6, forbiddenTask, forcedList);
				break;
			case 7:
				setForbidden(mainArray, 7, forbiddenTask, forcedList);
				break;
			}	
		}
	}
	
	//sets the forbidden pairs.
	private static void setForbidden(int[][] mainArray, int machine, int forbiddenTask, int[] forcedList) {
		// TODO Auto-generated method stub
		int ignoreVal = -1;
		for (int counter = 0; counter < mainArray.length; counter++) {
			if (counter == forbiddenTask) {
				if (isForced(machine, forbiddenTask, forcedList)) {
					OutputWriter.writeFile(forcedList, 0, "No valid solution possible!");
					
					//System.out.println("ERROR: FORCED and FORBIDDEN HARD CONSTRAINTS CONFLICT");
					System.exit(0);
				}else {
					mainArray[machine][counter] = ignoreVal;
				}
				//if (mainArray[machine][counter] != ignoreVal) {
				//	System.out.println("ERROR: FORCED and FORBIDDEN HARD CONSTRAINTS CONFLICT");
				//	System.exit(0);
				//}else {
				//	mainArray[machine][counter] = ignoreVal;
				//}
			}else {
				mainArray[machine][counter]=mainArray[machine][counter];
			}
		}
	}

		//deals with forced partial pairs
		private static void forcedPartial(int[][] mainArray, int[] forcedList, int task, int machine) {
			// TODO Auto-generated method stub
			//int ignoreVal = -1;
			if (checkTaskBounds(task) == false) {
				OutputWriter.writeFile(forcedList, 0, "No valid solution possible!");
				//do nothing. go back to for loop
				//System.out.print("Error: Task out of bounds!");
				System.exit(0);
			}
			if (task == -1) {
				//do nothing go back to loop
				setNoChange(mainArray, machine);
			}
			else {	
			
				switch (machine) {
				case 0:
					setForced(mainArray, 0, task, forcedList);
					break;
				case 1:
					setForced(mainArray, 1, task, forcedList);
					break;
				case 2:
					setForced(mainArray, 2, task, forcedList);
					break;
				case 3:
					setForced(mainArray, 3, task, forcedList);
					break;
				case 4:
					setForced(mainArray, 4, task, forcedList);
					break;
				case 5:
					setForced(mainArray, 5, task, forcedList);
					break;
				case 6:
					setForced(mainArray, 6, task, forcedList);
					break;
				case 7:
					setForced(mainArray, 7, task, forcedList);
					break;
				}	
			}
		}
		
		//called when element in 2Dmatrix is to be skipped to keep penalties
		private static void setNoChange(int[][] mainArray, int machine) {
			// TODO Auto-generated method stub
			for(int counter = 0; counter < mainArray.length; counter++) {
				mainArray[machine][counter]= mainArray[machine][counter];
			}
			
		}

		//sets forced pairs in "global" matrix
		private static void setForced(int[][] mainArray, int machine, int task, int[] forcedList) {
			int ignoreVal = -1;
			int[] ignoreList = {};
			
			for (int counter = 0; counter < mainArray.length; counter++) {
				//if (counter != task) {
				//	mainArray[machine][counter] = ignoreVal;
				//}
				if (counter == task) {
					if (isConflict(machine,task,forcedList)) {
						OutputWriter.writeFile(ignoreList, 0, "No valid solution possible!");
						System.exit(0);
					}else {mainArray[machine][counter]=mainArray[machine][counter];}
				}else {
					mainArray[machine][counter] = ignoreVal;
				}
			}
		}

		private static boolean isConflict(int machine, int task, int[] forcedList) {
			// TODO Auto-generated method stub
			boolean forcedConflict = false;
			int[] taskChecked = {};
			for (int count = 0; count<forcedList.length;count++) {
				if (forcedList[count]==task) {
					//if (taskChecked.length==0) {taskChecked[0] = forcedList[count];}
					//else {taskChecked[taskChecked.length] = forcedList[count];}
					taskChecked[taskChecked.length] = forcedList[count];
				}
			}if (taskChecked.length>1) {forcedConflict = false;}
			else {forcedConflict = true;}
			
			return forcedConflict;
		}

		//checks if tasks being assigned are within bounds (0-7)
		private static boolean checkTaskBounds(int task) {
			// TODO Auto-generated method stub
			boolean isInBound;
			
			if ((task < 0) || (task > 7)) {
				//isInBound = false;
				if (task == -1) {
					isInBound = true;
				}else {
					isInBound = false;
					//System.out.print("Error: Invalid Machine/Task");
					//System.exit(0);
				}	
			}
			else {
				isInBound = true;
			}
			return isInBound;
		}

	}
	
		