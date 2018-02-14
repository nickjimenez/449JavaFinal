package parse;

import java.io.*;
import java.util.ArrayList;

import commons.OutputWriter;

public class FileParser {

	public void openAndParse(int[][] penaltyMatrix, int[] forcedPartialAssignment, int[] forbiddenMachine,
			ArrayList<String> tooNearTasks, ArrayList<Integer> tooNearPenalties, String fileName) {

		String line = null;
		char[] lineArray;
		int penaltyColumn;
		String penaltyBuffer;
		int machine = 0;
		int task = 0;
		int machineidx;
		char taskLetter;
		String tooNear1;
		String tooNear2;
		int tooNearLetterVal;

		// attempt to open and parse the penalty matrix of the file
		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// continue to read lines until the end of the file
			while ((line = bufferedReader.readLine()) != null) {

				// replacing all white space on the line with the empty string
				line = line.replaceAll("\\s", "");

				// if we reach the forced partial assignment hard constraint
				if (line.equals("forcedpartialassignment:")) {

					// placeholder before filling array
					for (int i = 0; i < 8; i++) {
						forcedPartialAssignment[i] = -1;
					}
					
					// while we dont reach the next hard constraint, keep reading 
					while (!(line = bufferedReader.readLine().replaceAll("\\s", "")).equals("forbiddenmachine:")) {

						// if blank line, skip
						if (line.equals("")) {
							continue;
						} 
						
						// place index of appropriate letter in array at index of appropriate machine
						else {
							lineArray = line.toCharArray();
							machineidx = Character.getNumericValue(lineArray[1] - 1);
							taskLetter = lineArray[3];
							if (forcedPartialAssignment[machineidx] == -1) {						
								switch (taskLetter) {

								case 'A':
									forcedPartialAssignment[machineidx] = 0;
									break;
								case 'B':
									forcedPartialAssignment[machineidx] = 1;
									break;
								case 'C':
									forcedPartialAssignment[machineidx] = 2;
									break;
								case 'D':
									forcedPartialAssignment[machineidx] = 3;
									break;
								case 'E':
									forcedPartialAssignment[machineidx] = 4;
									break;
								case 'F':
									forcedPartialAssignment[machineidx] = 5;
									break;
								case 'G':
									forcedPartialAssignment[machineidx] = 6;
									break;
								case 'H':
									forcedPartialAssignment[machineidx] = 7;
									break;
								// if letter is not in our range of tasks
								default:
									//System.out.println("invalid machine/task");
									int[] forcedList = {};
									//int ignoreVal = -1;
									OutputWriter.writeFile(forcedList, 0, "invalid machine/task");
									System.exit(0);
								}
								//switch(forcedConflict(forcedPartialAssignment)) {
								//case 0:
								//	break;
								//case 1:
								//	OutputWriter.writeFile(forcedPartialAssignment, 0, "No valid solution possible!");
								//	System.exit(0);;
								//}
							} 
							
							// if a machine already has a task
							else {
								//System.out.println("partial assignment error");
								int[] forcedList = {};
								//int ignoreVal = -1;
								OutputWriter.writeFile(forcedList, 0, "partial assignment error");
								System.exit(0);
							}
						}
					}

					// check to make sure no machines have the same task
					for (int i = 0; i < forcedPartialAssignment.length - 1; i++) {
						for (int j = i + 1; j < forcedPartialAssignment.length; j++) {
							if (forcedPartialAssignment[i] == forcedPartialAssignment[j]
									& forcedPartialAssignment[i] != -1 & forcedPartialAssignment[j] != -1) {
								int[] forcedList = {};
								//int ignoreVal = -1;
								OutputWriter.writeFile(forcedList, 0, "partial assignment error");
								//System.out.println("partial assignment error");
								System.exit(0);
							}
						}
					}

				}

				// if we reach forbidden machine hard constraints
				if (line.equals("forbiddenmachine:")) {

					// placeholder before we fill array
					for (int i = 0; i < 8; i++) {
						forbiddenMachine[i] = -1;
					}

					// while we dont reach the next set of hard constraints
					while (!(line = bufferedReader.readLine().replaceAll("\\s", "")).equals("too-neartasks:")) {

						// if blank line, skip
						if (line.equals("")) {
							continue;
						} 
						
						// pplace index of appropriate letter in array at index of appropriate machine
						else {
							lineArray = line.toCharArray();
							machineidx = Character.getNumericValue(lineArray[1]) - 1;
							taskLetter = lineArray[3];
							switch (taskLetter) {

							case 'A':
								forbiddenMachine[machineidx] = 0;
								break;
							case 'B':
								forbiddenMachine[machineidx] = 1;
								break;
							case 'C':
								forbiddenMachine[machineidx] = 2;
								break;
							case 'D':
								forbiddenMachine[machineidx] = 3;
								break;
							case 'E':
								forbiddenMachine[machineidx] = 4;
								break;
							case 'F':
								forbiddenMachine[machineidx] = 5;
								break;
							case 'G':
								forbiddenMachine[machineidx] = 6;
								break;
							case 'H':
								forbiddenMachine[machineidx] = 7;
								break;
							default:
								int[] forcedList = {};
								//int ignoreVal = -1;
								OutputWriter.writeFile(forcedList, 0, "invalid machine/task");
								
								//System.out.println("invalid machine/task");
								System.exit(0);
							}

						}
					}
				}

				// if we reach too-near tasks hard constraints
				if (line.equals("too-neartasks:")) {

					// while we dont reach the penalty matrix
					while (!(line = bufferedReader.readLine().replaceAll("\\s", "")).equals("machinepenalties:")) {

						// if blank line, skip
						if (line.equals("")) {
							continue;
						} 
						
						// concatenate characters into a string and add them to the array
						else {
							lineArray = line.toCharArray();
							
							// if the ASCII value of the letter is not within A-H, notify user and exit
							if (lineArray[1] < 65 | lineArray[1] > 72 | lineArray[3] < 65 | lineArray[3] > 72) {
								//System.out.println("invalid machine/task");
								int[] forcedList = {};
								//int ignoreVal = -1;
								OutputWriter.writeFile(forcedList, 0, "invalid machine/task");
								System.exit(0);
							}
							tooNear1 = Character.toString(lineArray[1]);
							tooNear2 = Character.toString(lineArray[3]);
							tooNearTasks.add(tooNear1 + tooNear2);
						}

					}
				}

				// checking if we have reaches the line before the penalty
				// matrix
				if (line.equals("machinepenalties:")) {

					// while we havent reached the last soft constraint
					while (!(line = bufferedReader.readLine()).equals("too-near penalities")) {

						line = line.trim();

						// if line is blank, skip
						if (line.replaceAll("\\s", "").equals("")) {
							continue;
						}

						// convert the line to an array in order to access
						// individual penalty value
						lineArray = line.toCharArray();

						// reset column counter, penaltyBuffer, and task counter
						penaltyColumn = 0;
						penaltyBuffer = "";
						task = 0;
						// column of the 2D array
						while (penaltyColumn < lineArray.length) {

							try {
								
								// if the character read is a space and we have a
								// penalty value in the buffer, we can assume
								// that we have calculated the penalty for the
								// current task
								if (lineArray[penaltyColumn] == ' ' & !(penaltyBuffer.isEmpty())) {
									if (Integer.parseInt(penaltyBuffer) < 0) {
										int[] forcedList = {};
										//int ignoreVal = -1;
										OutputWriter.writeFile(forcedList, 0, "invalid penalty");
										//System.out.println("invalid penalty");
										System.exit(0);
									}
									penaltyMatrix[machine][task] = Integer.parseInt(penaltyBuffer);
									penaltyBuffer = "";
									task++;
								} 
								
								// if we reach a space and the buffer is empty, continue to next column
								else if (lineArray[penaltyColumn] != ' ') {
									penaltyBuffer += lineArray[penaltyColumn];
								}
							} catch (Exception e) {
								int[] forcedList = {};
								//int ignoreVal = -1;
								OutputWriter.writeFile(forcedList, 0, "machine penalty error");
								//System.out.println("machine penalty error");
								System.exit(0);
							}
							penaltyColumn++;
						}

						// if we have less than 8 tasks in a row, notify and exit
						if (task < 7) {
							int[] forcedList = {};
							//int ignoreVal = -1;
							OutputWriter.writeFile(forcedList, 0, "machine penalty error");
							//System.out.println("machine penalty error");
							System.exit(0);
						}
						// special case for last number
						try {
							if (penaltyBuffer != "") {
								penaltyMatrix[machine][task] = Integer.parseInt(penaltyBuffer);
							}
						} catch (Exception e) {
							int[] forcedList = {};
							//int ignoreVal = -1;
							OutputWriter.writeFile(forcedList, 0, "machine penalty error");
							//System.out.println("machine penalty error");
							System.exit(0);
						}

						machine++;

					}
					
					// if we have less than 8 machines, notify and exit
					if (machine - 1 < 7) {
						int[] forcedList = {};
						//int ignoreVal = -1;
						OutputWriter.writeFile(forcedList, 0, "machine penalty error");
						//System.out.println("machine penalty error");
						System.exit(0);
					}

				}
				
				// if we reach the too-near penalties contraints
				if (line.equals("too-near penalities")) {
					
					// while there are still lines to read
					while ((line = bufferedReader.readLine()) != null) {
						
						line = line.replaceAll("\\s", "");
						
						// if blank line, skip
						if (line.equals("")) {
							continue;
						} 
						
						// place the triples into an arrayList, using index of the tasks instead of letters
						else {
							lineArray = line.toCharArray();
							
							// if the ASCII value of the letter is not within A-H, notify user and exit
							if (lineArray[1] < 65 | lineArray[1] > 72 | lineArray[3] < 65 | lineArray[3] > 72) {
								int[] forcedList = {};
								//int ignoreVal = -1;
								OutputWriter.writeFile(forcedList, 0, "ERROR: Invalid Machine/Task");
								//System.out.println("invalid machine/task");
								System.exit(0);
							}
							tooNearLetterVal = Character.getNumericValue(lineArray[1]) - 10;
							tooNearPenalties.add(tooNearLetterVal);
							tooNearLetterVal = Character.getNumericValue(lineArray[3]) - 10;
							tooNearPenalties.add(tooNearLetterVal);
							penaltyBuffer = "";
							
							// find all characters from the natural number penalty and concatenate into string buffer
							for (int i = 5; i < lineArray.length - 1; i++) {
								
								penaltyBuffer += Character.toString(lineArray[i]);
							}
							
							// if negative penalty val, notify and exit
							if (Integer.parseInt(penaltyBuffer) < 0) {
								int[] forcedList = {};
								//int ignoreVal = -1;
								OutputWriter.writeFile(forcedList, 0, "ERROR: Invalid Penalty");
								//System.out.println("invalid penalty");
								System.exit(0);
							}
							tooNearPenalties.add(Integer.parseInt(penaltyBuffer));
						}
						
					}
				}

			}
			bufferedReader.close();
		} catch (ArrayIndexOutOfBoundsException exception) {
			//System.out.println("invalid machine/task");
			int[] forcedList = {};
			//int ignoreVal = -1;
			OutputWriter.writeFile(forcedList, 0, "ERROR: Invalid Machine/Task");
			exception.printStackTrace();
			System.exit(0);
		}

		catch (Exception e) {
			//System.out.println("Error while parsing input file");
			int[] forcedList = {};
			//int ignoreVal = -1;
			OutputWriter.writeFile(forcedList, 0, "ERROR: Error while parrsing input file");
			e.printStackTrace();
			System.exit(0);
		}

	}

	//private int forcedConflict(int[] forcedPartialAssignment) {
		// TODO Auto-generated method stub
	//	int isConflict = 0;
		/////
	//	return isConflict;
	//}
}
