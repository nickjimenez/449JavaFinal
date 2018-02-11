package process;

import java.util.ArrayList;

public class SoftConstraints{
	
	public int CalcFinalPenaltyValue(ArrayList<String> penaltyArray, int[][] logicalArray) throws Exception {
		
		int FinalPenaltyValue = CalcPenaltyFromLogical(logicalArray);
	 	
		String task1;
	 	String task2;
	 	int index = 0;
	 	
	 	for (int j = 0; j < penaltyArray.size(); j++){
		 	if (index < penaltyArray.size()){
		 	task1 = penaltyArray.get(index);
		 	index++;
		 	task2 = penaltyArray.get(index);
		 	index++;
		 	
		 	int posTask1 = changeStrtoInt(task1);
		 	int posTask2 = changeStrtoInt(task2);
		 	
		 	int additionalPenalty = Integer.parseInt(penaltyArray.get(index));
		 	
			for(int i = 0; i < logicalArray.length; i++) {
				if (i == 0 ){
					if(((logicalArray[i][posTask1] != -1) && (logicalArray[i+1][posTask2] != -1)) ||((logicalArray[i][posTask1] != -1) && (logicalArray[logicalArray.length -1][posTask2] != -1)))
						FinalPenaltyValue = FinalPenaltyValue + additionalPenalty;
					}
				else if (i == penaltyArray.size()){
					if(((logicalArray[i][posTask1] != -1) && (logicalArray[0][posTask2] != -1)) ||((logicalArray[i][posTask1] != -1) && (logicalArray[i-1][posTask2] != -1)))
						FinalPenaltyValue = FinalPenaltyValue + additionalPenalty;
				}
			else{
				if(((logicalArray[i][posTask1] != -1) && (logicalArray[i+1][posTask2] != -1)) ||((logicalArray[i][posTask1] != -1) && (logicalArray[i-1][posTask2] != -1))) {
					FinalPenaltyValue = FinalPenaltyValue + additionalPenalty;
				}
				}}
			index++;}
		 	else {
		 		break;
		 	}
		 	
	}
	 	return FinalPenaltyValue;
	 	}
	
	public int CalcPenaltyFromLogical(int[][] logicalArray) {
		int FinalPenaltyValue = 0;
		for(int i = 0; i < logicalArray.length; i++){
			for(int j = 0; j < logicalArray[i].length; j++){
				if (logicalArray[i][j] != -1){
					FinalPenaltyValue = FinalPenaltyValue + logicalArray[i][j];}}}
		return FinalPenaltyValue;
	}
	
	public int changeStrtoInt(String str) {
		  char[] chvalue  = str.toCharArray();
		    for(char c : chvalue)
		    {
		        int temp = (int)c;
		        int temp_integer = 64; 
		if(temp<=90 & temp>=65)
		    return ((temp-temp_integer)-1);
		    }
		    return 0;
	}
}	
