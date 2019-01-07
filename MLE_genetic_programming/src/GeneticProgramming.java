import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class GeneticProgramming {
		// The amount of operations in a program
		private static final int MAX_OPERATIONS = 1000;
		// Prime Number Treshold
		private static final int THRESHHOLD = 100;
		// Amount of VMs
		private static final int P = 500;
		//Mutation rate
		private static final double M = 0.05;
		private ArrayList<VM> vMs = new ArrayList<>();
		private int maxFitness;


		public GeneticProgramming() {
			initVMs(vMs);
			simulate(vMs);
			Collections.sort(vMs);
			this.maxFitness = vMs.get(P - 1).getFitness();
			reset(vMs);
		}

		private void evolutionAlgorithm() {
			int lastFitness = this.maxFitness;
			while (this.maxFitness < THRESHHOLD) {
				// Select the best vMs mem
				selection(vMs);
				// Mutate one operation in the program
				Random rnd = ThreadLocalRandom.current();
				for (int i = 0; i < P * M; i++) {
					//Get random individual
					int vmIndex = rnd.nextInt(P);
					//Get random memory index
					int memIndex = rnd.nextInt(MAX_OPERATIONS);
					
					vMs.get(vmIndex).mutateMem(memIndex, rnd.nextInt(7000));
				}
				simulate(vMs);
				//Sort so individual with best fitness is at the end of the array list
				Collections.sort(vMs);
				maxFitness = vMs.get(P - 1).getFitness();
				
				//Clear stack and counter of the vms
				reset(vMs);
				// Print fitness only if it is better
				if (lastFitness < maxFitness) {
					lastFitness = maxFitness;
					System.out.println("Fitness: " + maxFitness + " prime numbers");
				}
			}
			// Get the best program and print its memory
			VM vm = vMs.get(P - 1);
			System.out.println("Memory: ");
			for (int i = 0; i < MAX_OPERATIONS; i++) {
				System.out.print(vm.mem[i] + " ,");
			}
		}
		
		//Select the best programs
		private void selection(ArrayList<VM> vMs) {
			//Sort VM list 
			Collections.sort(vMs);
			VM vm;
			VM vmToChange;
			int index = P - 1;
			int index2 = 0;
			//Override the lower half of the population with individuals from upper half
			while (index >= (P / 2)) {
				vm = vMs.get(index);
				vmToChange = vMs.get(index2);
				vmToChange.changeMem(vm.mem);
				index--;
				index2++;
			}
		}

		// Initialize VMs and add them to the population list
		private void initVMs(ArrayList<VM> vMs) {
			int[] mem = new int[MAX_OPERATIONS];
			for (int i = 0; i < P; i++) {
				vMs.add(i, new VM());
				for (int j = 0; j < mem.length; j++) {
					mem[j] = ThreadLocalRandom.current().nextInt(7000);
				}
				vMs.get(i).setMemAndResizeMAX(mem);
				mem = new int[MAX_OPERATIONS];
			}
		}
		
		//Calls the simulate method of each element of the given VM list
		private void simulate(ArrayList<VM> vMs) {
			for(int k = 0; k < P; k++) {
				vMs.get(k).simulate();
			}
		}
		
		//Calls reset method of each element of the given VM list
		private void reset(ArrayList<VM> vMs) {
			for(int k = 0; k < P; k++) {
				vMs.get(k).reset();
			}
		}

		public static void main(String[] args) {
			new GeneticProgramming().evolutionAlgorithm();
		}
}

