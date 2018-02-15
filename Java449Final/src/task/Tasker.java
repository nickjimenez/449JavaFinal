package task;

import commons.Config;
import commons.OutputWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;


/* Tasker class contains optimize() function that utilizes Branch and Bound algorithm
 * to find the assignment of tasks to machines that has the minimal penalty value.
 * 
 * Tasker assumes that the input matrix is a square matrix
 */
public class Tasker {

	// Used by optimize() to calculate the lower bound least promising cost of a node
	private static int calculatePromisingCost(int[][] costMatrix, Node node) {
		
		int promisingCost = 0;
		
		// Creates an array that contains the available assignments 
		boolean[] availableNodesArray = new boolean[costMatrix.length];
		Arrays.fill(availableNodesArray, Boolean.TRUE);
		
		// Iterates through the remaining unassigned machine
		// and finds the task that has the minimal penalty cost
		for (int currMach = node.mach + 1; currMach < costMatrix.length; currMach++) {
			
			// Holds the least possible penalty cost for a particular machine
			int minTaskCost = Config.MAX_VALUE;
			// Holds the index of task with least penalty cost
			int minTaskIndex = -1;
			
			// Iterates through the task and gets the task that is unassigned and has the lowest penalty cost
			for (int currTask = 0; currTask < costMatrix.length; currTask++) {
				if ((!node.assignedNodesArray[currTask] || availableNodesArray[currTask]) && costMatrix[currMach][currTask] < minTaskCost && costMatrix[currMach][currTask] != -1) {
					minTaskIndex = currTask;
					minTaskCost = costMatrix[currMach][currTask];
				}
			}
			
				promisingCost += minTaskCost;
			
			// Guard for root node
			if (minTaskIndex != -1)
				// Sets task with minimal cost unavailable for next iteration
				availableNodesArray[minTaskIndex] = false;
		}
		
		return promisingCost;
	}

	// Calculates penalty for assignment that have tasks that are too near to each other
	private static int calcTooNearPenalties(Node child, ArrayList<Integer> tooNearArray) {

		int penalty = 0;
		
		// Iterates through tooNearArray and checks if an assignment is too near each other
		for (int i = 0; i <= tooNearArray.size() - 3; i+=3) {
			if (child.task == tooNearArray.get(i + 1) && child.parent.task == tooNearArray.get(i))
				penalty = tooNearArray.get(i + 2);
		
			// Includes checking for first machine and last machine
			if (child.mach == Config.GLOBAL_SIZE - 1) {
				Node firstMach = child;
				for (int j = Config.GLOBAL_SIZE - 2; j >= 0; j--)
					firstMach = firstMach.parent;
				if (firstMach.task == tooNearArray.get(i + 1) && child.parent.task == tooNearArray.get(i))
					penalty += tooNearArray.get(i + 2);
					
			}	
		}

		return penalty;
	}
	
	// Outputs solution
	private static void displaySolution(int[][] costMatrix, Node activeNode, String errorString) {
		
		int[] solutionArray = new int[costMatrix.length];
		
		Node currNode = activeNode;
		for (int i = costMatrix.length - 1; i >= 0; i--) {
			
			solutionArray[i] = currNode.task;
			currNode = currNode.parent;
		}
		
		OutputWriter.writeFile(solutionArray, activeNode.promisingCost, errorString);
		
	}
	

	
	// Finds the optimal job scheduling cost using Branch and Bound algorithm 
	// and implements a list of active nodes as a min-heap
	public static void optimize(int[][] costMatrix, ArrayList<Integer> tooNearArray, String errorString) {
		
		// Contains the list of active nodes stored in a min-heap priority queue
		PriorityQueue<Node> activeNodesArray = new PriorityQueue<Node>();
		
		// Initialize root node
		Node root = new Node(costMatrix);
		
		// Adds the root node to list of active nodes;
		activeNodesArray.add(root);
		
		while (!activeNodesArray.isEmpty()) {
			// Finds the live node with least estimated cost and deletes it from list of live nodes
			Node activeNode = activeNodesArray.poll();
			
			// Goes to next machine 
			int currMach = activeNode.mach + 1;
			
			// If all machines are assigned to a task, prints solution
			if (currMach == costMatrix.length) {
				displaySolution(costMatrix, activeNode, errorString);
				break;
			}
			
			// Iterate through the tasks 
			for (int currTask = 0; currTask < costMatrix.length; currTask++)
		      {
		        // Creates a child node for the unassigned task
		        if (!activeNode.assignedNodesArray[currTask] && costMatrix[currMach][currTask] != -1) {
		        	Node child = new Node(currMach, currTask, activeNode.assignedNodesArray, activeNode);
		 
		        	// Calculates the path cost of the node
		        	child.pathCost = activeNode.pathCost + costMatrix[currMach][currTask] + calcTooNearPenalties(child, tooNearArray);
		 
		          // Calculates the least promising cost of the node
		        	child.promisingCost = child.pathCost + calculatePromisingCost(costMatrix, child);
		 
		          // Adds node to list of active nodes;
		        	activeNodesArray.add(child);
		          
		        }
		  
		      }
		}
	}
	
	// Node class that represents a choice of assignment between a machine and a task
	private static class Node implements Comparable<Node> {
		
		// Contains parent node of node 
		private Node parent;
		// Contains the past cost of node
		private int pathCost;
		// Contains least promising cost
		private int promisingCost;
		// Contains machine
		private int mach;
		// Contains task
		private int task;
		// Contains information about assignment status of task in a particular choice
		private boolean[] assignedNodesArray;
		
		// Root node constructor
		public Node(int[][] costMatrix) {
			
			this.assignedNodesArray = new boolean[costMatrix.length];
			this.mach = -1;
			this.task = -1;
			this.parent = null;
		}
		
		// Node constructor
		public Node(int machine, int task, boolean[] assigned, Node parent) {
			
			// Copies the input assigned array to the node's assigned array
			this.assignedNodesArray = new boolean[assigned.length];
			for (int i = 0; i < assigned.length; i++)
				this.assignedNodesArray[i] = assigned[i];
			// Sets the assignment status of the nodes task to 'assigned'
			this.assignedNodesArray[task] = true;
			this.mach = machine;
			this.task = task;
			this.parent = parent;
		}
		
		// Comparison method used by pQueue to compare and order nodes in the pQueue
		public int compareTo(Node n) {	
			if (this.promisingCost < n.promisingCost)
	            return -1;
	        else if(this.promisingCost > n.promisingCost)
	            return 1;
	        else
	            return 0;
		}
		
	}
	
}
