import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm {
	final static int POPULATION = 100;
	final static int GENES = 20;
	final static int FITNESS_TRESHOLD = 19;
	final static String OPTIMUM = generateGenes();
	final static double CROSSOVER_RATE = 0.05;
	final static double MUTATION_RATE = 0.01;

	public static void main(String[] args) {
		System.out.println("Optimum-Chromosome: " + OPTIMUM + "\n");
		geneOptimizer();
	}

	public static void geneOptimizer() {
		String[] population = generatePopulation();
		int[] fitness = calculateFitness(population);
		int maxFitness = 0;
		int fitness_treshold = 1;
		int generationCount = 0;
		String winner = "";
		
		List<String> nextGeneration = new ArrayList<String>();

		System.out.println("-----------------------------");
		
		while (maxFitness < FITNESS_TRESHOLD ) {
			// Get individuals for next generation via selection
			String[] selectedPopulation = selection(population, fitness);
			for (String pop : selectedPopulation) {
				nextGeneration.add(pop);
			}

			// Perform crossover for next generation
			List<String> crossoverPopulation = crossover(population, fitness);
			for (String pop : crossoverPopulation) {
				nextGeneration.add(pop);
			}

			// Perform mutation for next generation
			String[] mutatedPopulation = mutation(population, fitness);
			for (String pop : mutatedPopulation) {
				nextGeneration.add(pop);
			}

			//System.out.println("Next generation: " + nextGeneration);
			//System.out.println("Anzahl Individuen: " + nextGeneration.size());	
			
			nextGeneration.toArray(population);
			nextGeneration.clear();
			fitness = calculateFitness(population);
			for(int i = 0; i < fitness.length; i++) {
				if(fitness[i] > maxFitness) {
					maxFitness = fitness[i];
					winner = population[i];
				}
			}
			System.out.println("Generation: "+generationCount);
			System.out.println("Max Fitness: "+maxFitness);
			System.out.println("------------------------------");
			generationCount++;
		}
		System.out.println("Solution found");
		System.out.println("Generation: "+generationCount);
		System.out.println("Genes: " + winner);
	}

	//Selects an amount of individuals for the next generation
	private static String[] selection(String[] population, int[] fitness) {
		int populationSize = (int) ((1 - CROSSOVER_RATE) * POPULATION);
		//System.out.println("Population size for selection: "+populationSize);
		String[] nextGeneration = new String[populationSize];
		for (int i = 0; i < populationSize; i++) {
			String winner = population[selectHypothesis(fitness)];
			nextGeneration[i] = winner;
		}
	
		return nextGeneration;
	}
	
	//Generates 2 offspring from a pair of individuals
	private static List<String> crossover(String[] population, int[] fitness) {
		int populationSize = (int) ((CROSSOVER_RATE * POPULATION) / 2);
		//System.out.println("Population size for crossover: " + (CROSSOVER_RATE * POPULATION) / 2);
		List<String> nextGeneration = new ArrayList<String>();

		// Get pairs of individuals and generate offspring
		for (int i = 0; i < populationSize; i++) {
			// Generate random crossover point
			int crossOver = ThreadLocalRandom.current().nextInt(0, GENES);

			// Select individuals
			String individual1 = population[selectHypothesis(fitness)];
			String individual2 = population[selectHypothesis(fitness)];

			// Make sure both individuals are different
			while (individual1.equals(individual2)) {
				individual2 = population[selectHypothesis(fitness)];
			}

			// Gets genes from individual1
			String offspring1 = "";

			// Gets genes from individual2
			String offspring2 = "";
			
			// Swap genes up to crossover point
			for (int j = 0; j <= crossOver; j++) {
				offspring1 += individual1.charAt(j);
				offspring2 += individual2.charAt(j);
			}

			// Add remaining genes of parents
			for (int k = crossOver + 1; k < GENES; k++) {
				offspring1 += individual2.charAt(k);
				offspring2 += individual1.charAt(k);
			}

			// Add offspring to next generation
			nextGeneration.add(offspring1);
			nextGeneration.add(offspring2);
		}
		return nextGeneration;
	}
	
	//Generates new individuals by flipping a random gene of an individual
	private static String[] mutation(String[] population, int[] fitness) {
		int populationSize = (int) (MUTATION_RATE * POPULATION);
		//System.out.println("Population size for mutation: "+populationSize);
		String[] selectedPopulation = new String[populationSize];
		String[] mutatedPopulation = new String[populationSize];

		// Get population for mutating
		for (int i = 0; i < populationSize; i++) {
			selectedPopulation[i] = population[selectHypothesis(fitness)];
		}

		int count = 0;
		for (String pop : selectedPopulation) {
			// Get the gray code representation of the individuals genes
			String grayCode = binarytoGray(pop);

			// Select random gene
			int randomGene = ThreadLocalRandom.current().nextInt(0, GENES);
			char[] mutationHelper = new char[GENES];
			String mutatedGrayCode;
			// Create new mutated individual with flipped gene
			for (int i = 0; i < GENES; i++) {
				if (i == randomGene) {
					mutationHelper[i] = flip(grayCode.charAt(i));
				} else {
					mutationHelper[i] = grayCode.charAt(i);
				}
			}
			mutatedGrayCode = new String(mutationHelper);
			// Convert gray code back to binary
			String mutatedBinary = graytoBinary(mutatedGrayCode);

			mutatedPopulation[count] = mutatedBinary;
//			System.out.println("Unmutated Gene: " + grayCode);
//			System.out.println("Mutated Gene: " + mutatedGrayCode);
//			System.out.println("Unmutated binary: " + pop);
			//System.out.println("Mutated Binary: " + mutatedBinary.length());
			count++;
		}
		return mutatedPopulation;
	}
	
	//Get fitness of two gene strings by comparing them for equality
	private static int getFitness(String gene1, String gene2) {
		int score = 0;
		for (int i = 0; i < GENES; i++) {
			// Increase score if bits at the same position match
				if (gene1.charAt(i) == gene2.charAt(i)) {
					score++;
				}
		}
		return score;
	}
	

	private static double getNormalizedFitness(int individualFitness, int[] populationFitness) {
		int sum = 0;
		for (int fitness : populationFitness) {
			sum += fitness;
		}
		double result = (double) individualFitness / sum;
		return result;
	}
	
	//Calculate the fitness of a population
	private static int[] calculateFitness(String[] population) {
		int[] fitness = new int[POPULATION];
		// Get fitness of population
		for (int i = 0; i < POPULATION; i++) {
			fitness[i] = getFitness(population[i], OPTIMUM);
		}
	
		return fitness;
	}
	
	//Select hypothesis with equalized error
	private static int selectHypothesis(int[] fitness) {
		double sum = 0;
		double randomNum = Math.random();
		int index = ThreadLocalRandom.current().nextInt(0, POPULATION + 1);
		do {
			index = index + 1;
			index = index % POPULATION;
			sum += getNormalizedFitness(fitness[index], fitness);
	
		} while (sum < randomNum);
		return index;
	}
	
	//Converts a binary string to gray code
	private static String binarytoGray(String binary) {
		String gray = "";
	
		// MSB of gray code is the same as binary code
		gray += binary.charAt(0);
	
		// Compute remaining bits, next bit is computed by doing XOR of previous and
		// current in Binary
		for (int i = 1; i < binary.length(); i++) {
			// Concatenate XOR of previous bit with current bit
			gray += xor_c(binary.charAt(i - 1), binary.charAt(i));
		}
	
		return gray;
	}
	
	//COnverts a gray code string into a binary string
	private static String graytoBinary(String gray) {
		String binary = "";

		// MSB of binary code is same as gray code
		binary += gray.charAt(0);

		// Compute remaining bits
		for (int i = 1; i < gray.length(); i++) {
			// If current bit is 0, concatenate previous bit
			if (gray.charAt(i) == '0')
				binary += binary.charAt(i - 1);

			// Else, concatenate invert of previous bit
			else
				binary += flip(binary.charAt(i - 1));
		}

		return binary;
	}

	// Helper function to xor two characters
	private static char xor_c(char a, char b) {
		return (a == b) ? '0' : '1';
	}

	// Helper to flip a bit
	private static char flip(char c) {
		return (c == '0') ? '1' : '0';
	}

	private static String[] generatePopulation() {

		String[] population = new String[POPULATION];

		for (int i = 0; i < population.length; i++) {
			population[i] = generateGenes();
		}

		return population;
	}

	private static String generateGenes() {
		char[] gene = new char[GENES];
		for (int i = 0; i < gene.length; i++) {
			if ((Math.random() * 100) < 50) {
				gene[i] = '1';
			} else {
				gene[i] = '0';
			}
		}

		return new String(gene);
	}

}
