import java.util.concurrent.ThreadLocalRandom;

public class SimulatedAnnealing {
	private static final double EPSILON = 0.01;
	
	public static void main(String[] args) {
		simulatedAnnealing(100);
	}
	
	public static void simulatedAnnealing(int numberOfCities) {
		double temperature = 10000;
		int[][] cityMatrix = new int[numberOfCities][numberOfCities];
		int[] route = new int[numberOfCities];
		
		//Init routes
		for (int i = 0; i < route.length; i++) {
			route[i] = i;
		}
		//Init matrix
		cityMatrix = initMatrix(cityMatrix);
		
		
		int lastFitness = (getDistance(cityMatrix, route)) * -1;
		
		while (temperature > EPSILON) {
			int[] newRoute = swapCities(route);
			int newFitness = (getDistance(cityMatrix, newRoute));
			
			//If new fitness is better the new route and fitness are saved
			//else drop route
			if (newFitness > lastFitness) {
				System.out.println("Temperatur: " + temperature + " Aktuelle Fitness: " + newFitness);
				lastFitness = newFitness;
				route = newRoute;
			} 
			else{
				if(temperature<10){
					System.out.println("Wahrscheinlichkeit vor Prüfung");
					System.out.println(Math.exp((newFitness - lastFitness)/temperature));
				}
				
				double rnd = Math.random();
				//Still save route if probability is met 
				if (rnd < Math.exp((newFitness - lastFitness)/temperature)){
					double chance = Math.exp((newFitness - lastFitness)/temperature);
					System.out.println("\n----Wahrscheinlichkeit erfüllt----");
					System.out.println("Wahrscheinlichkeit: "+chance+ " Treshold: "+ rnd);
					System.out.println("Temperatur: " + temperature + " Aktuelle Fitness: " + newFitness);
					System.out.println("------------\n");
					lastFitness = newFitness;
					route = newRoute;
				}

			}
			temperature = temperature - EPSILON;
		}
		
		System.out.println("Beste Route: ");
		for (int i = 0; i < route.length; i++) {
			System.out.print(route[i]+"-->");
		}
		
		System.out.println("\nTemperatur hat "+temperature+" erreicht. Grenzwert: "+EPSILON);
	}
	
	private static int[] swapCities(int[] route) {
		int random1 = ThreadLocalRandom.current().nextInt(0, route.length);
		int random2 = ThreadLocalRandom.current().nextInt(0, route.length);

		while(random1 == random2) {
			random2 = ThreadLocalRandom.current().nextInt(0, route.length);
		}
		int temp = route[random1];
		route[random1] = route[random2];
		route[random2] = temp;
		return route;
	}
	
	private static int getDistance(int[][] matrix, int[] route) {			
		int distance = 0;
		
		for (int i = 0; i < route.length - 1; i++) {

			// Add distances between stations of route
			distance += matrix[route[i]][route[i + 1]];
		}

		// Add distance from last stop to first
		distance += matrix[route[0]][route[route.length - 1]];
		
		return distance;

	}
	
	private static int[][] initMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int k = 0; k < matrix.length; k++) {
				if (i == k) {
					matrix[i][k] = 0;
				} else {
					matrix[i][k] = (int) Math.round(Math.random() * 100);
					matrix[k][i] = matrix[i][k];
				}
			}

		}
		
		for(int i=0; i < matrix.length; i++) {
			for(int k=0; k < matrix[i].length; k++) {
				System.out.print(matrix[i][k]+" ");
			}
			System.out.println();
		}
		
		return matrix;
	}

}
