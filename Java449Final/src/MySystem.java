import java.util.ArrayList;

import commons.Config;
import commons.OutputWriter;
import parse.FileParser;
import process.HardConstraints;
import task.Tasker;

/*
 * @Authors:
 * Dan Dunareanu 30002346
 * Heavenel Cerna 30019914
 * Issack John 30031053
 * Nickole Jimenez 10052979
 * Riike Oyesoro 30003141
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
			OutputWriter.writeFile(forcedList, 0, "ERROR: Retry with valid argument for parameters INPUTFILE OUPUTFILE");
			System.exit(0);
			
		}

		Config.OUTPUT_FILENAME = outputFileName;
		
		int[][] penaltyMatrix = new int[Config.GLOBAL_SIZE][Config.GLOBAL_SIZE];
		int[] forcedPartialAssignment = new int[Config.GLOBAL_SIZE];
		ArrayList<ArrayList<Integer>> forbiddenMachine = new ArrayList<ArrayList<Integer>>(Config.GLOBAL_SIZE);
		ArrayList<Integer> tooNearPenalties = new ArrayList<Integer>();
		ArrayList<String> tooNearTasks = new ArrayList<String>();
		
		//instantiate 2d arraylist
		 for(int i = 0; i < Config.GLOBAL_SIZE; i++)  {
			 forbiddenMachine.add(i, new ArrayList<Integer>());
		    }

		FileParser setup = new FileParser();	
		setup.openAndParse(penaltyMatrix, forcedPartialAssignment, forbiddenMachine, tooNearTasks, tooNearPenalties, inputFileName);
		
		
		HardConstraints hard = new HardConstraints();
		hard.doHard(penaltyMatrix, forcedPartialAssignment, forbiddenMachine, tooNearTasks, errorString);

		Tasker.optimize(penaltyMatrix, tooNearPenalties, tooNearTasks, errorString);
		
	}

}
