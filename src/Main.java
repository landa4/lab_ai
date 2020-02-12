import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.Domain;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.NotEqualConstraint;
import aima.core.search.csp.SolutionStrategy;
import aima.core.search.csp.Variable;

public class Main {

	private static CSP setupCSP() {
		CSP csp = null;
//		In five houses, each with a different color, live five persons of different nationality,
//		each of whom prefers a different brand of cigarettes, a different drink, and a different pet.
//		The five houses are arranged in a row (no house has more than 2 neighbors).   
//		# The Englishman lives in the red house.
//		# The Spaniard owns the dog.
//		# Coffee is drunk in the green house.
//		# The Ukrainian drinks tea.
//		# The green house is immediately to the right of the ivory house.
//		# The Old Gold smoker owns snails.
//		# Kools are smoked in the yellow house.
//		# Milk is drunk in the middle house.
//		# The Norwegian lives in the first house.
//		# The man who smokes Chesterfields lives in the house next to the man with the fox.
//		# Kools are smoked in the house next to the house where the horse is kept.
//		# The Lucky Strike smoker drinks orange juice.
//		# The Japanese smokes Parliaments.
//		# The Norwegian lives next to the blue house.
//
//		Now, who drinks water? Who owns the zebra?
				
		String[] colors = {"Red", "Green", "Ivory", "Yellow", "Blue"};
		String[] nations = {"Englishman", "Spaniard", "Norwegian", "Ukrainian", "Japanese"};
		String[] cigarettes = {"Old Gold", "Kools", "Chesterfields", "Lucky Strike", "Parliaments"};
		String[] drink = {"Water", "Orange juice", "Tea", "Coffee", "Milk"};
		String[] pet = {"Zebra", "Dog", "Fox", "Snails", "Horse"};
		
		// TODO create variables, e.g.,
		// Variable var1 = new Variable("name of the variable 1");
		// Variable var2 = new Variable("name of the variable 2");
		Variable [] color_var = new Variable[5];
		for (int i = 0; i < color_var.length; i++){
			color_var[i] = new Variable(colors[i]);
		}
		Variable [] nations_var = new Variable[5];
		for (int i = 0; i < nations_var.length; i++){
			nations_var[i] = new Variable(nations[i]);
		}
		Variable [] cigarettes_var = new Variable[5];
		for (int i = 0; i < cigarettes_var.length; i++){
			cigarettes_var[i] = new Variable(cigarettes[i]);
		}
		Variable [] drink_var = new Variable[5];
		for (int i = 0; i < drink_var.length; i++){
			drink_var[i] = new Variable(drink[i]);
		}
		Variable [] pet_var = new Variable[5];
		for (int i = 0; i < pet_var.length; i++){
			pet_var[i] = new Variable(pet[i]);
		}

		List<Variable> variables = new LinkedList<>();
		// TODO add all your variables to this list, e.g.,
		// variables.add(var1);
		// variables.add(var2);
		variables.addAll(Arrays.asList(color_var));
		variables.addAll(Arrays.asList(nations_var));
		variables.addAll(Arrays.asList(cigarettes_var));
		variables.addAll(Arrays.asList(drink_var));
		variables.addAll(Arrays.asList(pet_var));

		Variable v1 = new Variable("1");
		Variable v3 = new Variable("3");

		variables.add(v1);
		variables.add(v3);
		csp = new CSP(variables);

		// TODO set domains of variables, e.g.,
		// Domain d1 = new Domain(new String[]{"foo", "bar"});
		// csp.setDomain(var1, d1);
		Domain house = new Domain(new Integer[]{1, 2, 3, 4, 5});

		for(Variable v : variables)
		 	csp.setDomain(v, house);

		csp.setDomain(v3, new Domain(new Integer[]{3}));
		csp.setDomain(v1, new Domain(new Integer[]{1}));
		// TODO add constraints, e.g.,

		assignUnequal(csp, color_var);
		assignUnequal(csp, nations_var);
		assignUnequal(csp, cigarettes_var);
		assignUnequal(csp, drink_var);
		assignUnequal(csp, pet_var);


		csp.addConstraint(new EqualConstraint(nations_var[0], color_var[0]));
		csp.addConstraint(new EqualConstraint(nations_var[1], pet_var[1]));
		csp.addConstraint(new EqualConstraint(drink_var[3],color_var [1]));
		csp.addConstraint(new EqualConstraint(nations_var[3],drink_var[2]));
		csp.addConstraint(new SuccessorConstraint(color_var[1], color_var[2]));
		csp.addConstraint(new EqualConstraint(cigarettes_var[0],pet_var[3]));
		csp.addConstraint(new EqualConstraint(cigarettes_var[1],color_var[3]));
		csp.addConstraint(new EqualConstraint(drink_var[4],v3));
		csp.addConstraint(new EqualConstraint(nations_var[2],v1));
		csp.addConstraint(new DifferByOneConstraint(cigarettes_var[2],pet_var[2]));
//		csp.addConstraint(new DifferByOneConstraint(cigarettes_var[1],pet_var[4]));
//		csp.addConstraint(new EqualConstraint(cigarettes_var[3],drink_var[1]));
//		csp.addConstraint(new EqualConstraint(nations_var[4],cigarettes_var[4]));
//		csp.addConstraint(new DifferByOneConstraint(nations_var[2],color_var[4]));


		//csp.addConstraint(new NotEqualConstraint(var1, var2)); // meaning var1 != var2
		// csp.addConstraint(new EqualConstraint(var1, var2)); // meaning var1 == var2
		// csp.addConstraint(new SuccessorConstraint(var1, var2)); // meaning var1 == var2 + 1
		// csp.addConstraint(new DifferByOneConstraint(var1, var2)); // meaning var1 == var2 + 1 or var1 == var2 - 1 
		
		return csp;
	}

	private static void printSolution(Assignment solution) {
		// TODO print out useful answer
		// You can use the following to get the value assigned to a variable:
		// Object value = solution.getAssignment(var); 
		// For debugging it might be useful to print the complete assignment and check whether
		// it makes sense.
		System.out.println("solution:" + solution);
	}
	
	/**
	 * runs the CSP backtracking solver with the given parameters and print out some statistics
	 * @param description
	 * @param enableMRV
	 * @param enableDeg
	 * @param enableAC3
	 * @param enableLCV
	 */
	private static void findSolution(String description, boolean enableMRV, boolean enableDeg, boolean enableAC3, boolean enableLCV) {
		CSP csp = setupCSP();

		System.out.println("======================");
		System.out.println("running " + description);
		
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		SolutionStrategy solver = new ImprovedBacktrackingStrategy(enableMRV, enableDeg, enableAC3, enableLCV);
		final int nbAssignments[] = {0};
		solver.addCSPStateListener(new CSPStateListener() {
			@Override
			public void stateChanged(Assignment arg0, CSP arg1) {
				nbAssignments[0]++;
			}
			@Override
			public void stateChanged(CSP arg0) {}
		});
		Assignment solution = solver.solve(csp);
		endTime = System.currentTimeMillis();
		System.out.println("runtime " + (endTime-startTime)/1000.0 + "s" + ", number of assignments (visited states):" + nbAssignments[0]);
		printSolution(solution);
	}

	/**
	 * main procedure
	 */
	public static void main(String[] args) throws Exception {
		// run solver with different parameters
		findSolution("backtracking + AC3 + most constrained variable + least constraining value", true, true, true, true);
		findSolution("backtracking + AC3 + most constrained variable", true, true, true, false);
		findSolution("backtracking + AC3", false, false, true, false);
		findSolution("backtracking + forward checking + most constrained variable + least constraining value", true, true, false, true);
		findSolution("backtracking + forward checking + most constrained variable", true, true, false, false);
		findSolution("backtracking + forward checking", false, false, false, false);
	}

	public static void assignUnequal(CSP csp, Variable[] variables){
		for(int i = 0; i < variables.length; i++){
			for(int k = 0; k < variables.length; k++){
				if(i != k)
					csp.addConstraint(new NotEqualConstraint(variables[i], variables[k]));
			}
		}
	}

}
