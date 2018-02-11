package commons;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputWriter {
	
	public static void writeFile(int[] solutionArray, int quality, String errorString) {
        
        if (errorString == "x"){
        
        		try {
			
        			FileWriter fw = new FileWriter(Config.OUTPUT_FILENAME);
        			PrintWriter pw = new PrintWriter(fw);

        			pw.print("\"Solution\"");
        			for (int j = 0; j < Config.GLOBAL_SIZE; j++)
        				switch (solutionArray[j]) {
        				case 0:
        					pw.print(" " + "A");
        					break;
        				case 1:
        					pw.print(" " + "B");
        					break;
        				case 2:
        					pw.print(" " + "C");
        					break;
        				case 3:
        					pw.print(" " + "D");
        					break;
        				case 4:
        					pw.print(" " + "E");
        					break;
        				case 5:
        					pw.print(" " + "F");
        					break;
        				case 6:
        					pw.print(" " + "G");
        					break;
        				case 7:
        					pw.print(" " + "H");
        					break;
        				}
        			pw.print("\"; Quality:\" " + quality);
			
        			fw.close(); 
			
        		} catch (IOException e) {
        			System.out.println("Could not write to file");
        		}
        
        } else if (errorString != "x"){
        	
        		try {
        			FileWriter fw = new FileWriter(Config.OUTPUT_FILENAME);
        			//FileWriter fw = new FileWriter(Config.OUTPUT_FILENAME);
                PrintWriter pw = new PrintWriter(fw);
                
                pw.print(errorString);
                
                fw.close();
        		}catch (IOException e) {
        			System.out.println("Could not write to file");
        		}
        	
        }
	}
}
