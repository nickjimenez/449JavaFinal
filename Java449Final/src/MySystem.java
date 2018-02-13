import java.util.ArrayList;

import commons.Config;
import commons.OutputWriter;
import parse.FileParser;
import process.HardConstraints;
import process.SoftConstraints;
import task.Tasker;

/*
 * @Authors:
 * Dan Dunareanu 30002346
 * Heavenel Cerna 30019914
 * Issack John 30031053
 * Nickole Jimenez 10052979
 * Riike Oyesoro 
 */

public class MySystem {

	public static void main(String[] args) throws Exception {

		String inputFileName = null;
		String outputFileName = null;
        String errorString = "x";

		try {
			inputFileName = args[0];
			outputFileName = args[1];
		}
		catch(Exception e) {
			int[] forcedList = {};
			//int ignoreVal = -1;
			//System.out.println("ERROR: Retry with valid argument for parameters INPUTFILE OUPUTFILE");
			OutputWriter.writeFile(forcedList, 0, "ERROR: Retry with valid argument for parameters INPUTFILE OUPUTFILE");
			//do nothing. go back to for loop
			//System.out.print("Error: Task out of bounds!");
			//System.exit(0);
			System.exit(0);
		}

		Config.OUTPUT_FILENAME = outputFileName;
		int[][] penaltyMatrix = new int[Config.GLOBAL_SIZE][Config.GLOBAL_SIZE];
		int[] forcedPartialAssignment = new int[Config.GLOBAL_SIZE];
		int[] forbiddenMachine = new int[Config.GLOBAL_SIZE];
		ArrayList<Integer> tooNearPenalties = new ArrayList<Integer>();
		ArrayList<String> tooNearTasks = new ArrayList<String>();

		FileParser setup = new FileParser();
		setup.openAndParse(penaltyMatrix, forcedPartialAssignment, forbiddenMachine, tooNearTasks, tooNearPenalties, inputFileName);
		HardConstraints hard = new HardConstraints();
		hard.doHard(penaltyMatrix, forcedPartialAssignment, forbiddenMachine, tooNearTasks, errorString);

		Tasker.optimize(penaltyMatrix, tooNearPenalties, errorString);
	}

}
