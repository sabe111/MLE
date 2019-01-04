
public class HillClimber {
	final static int TRESHOLD = 10000;
	
	public static void main(String[] args) {
		hillClimber(100);
	}
	
	public static void hillClimber(int numberOfCities) {
		int[][] cityMatrix = new int[numberOfCities][numberOfCities];
		int[] route = new int[numberOfCities];
		
		//Init routes
		for (int i = 0; i < route.length; i++) {
			route[i] = i;
		}
		//Init matrix
		cityMatrix = initMatrix(cityMatrix);
		
		
		int lastFitness = (getDistance(cityMatrix, route)) * -1;
		
		int count = 0;
		while (count <= TRESHOLD) {
			int[] newRoute = swapCities(route);
			int newFitness = (getDistance(cityMatrix, newRoute));
			
			//If new fitness is better the new route and fitness are saved
			//else drop route
			if (newFitness > lastFitness) {
				System.out.println("Schritt: " + count + " Aktuelle Fitness: " + newFitness);
				lastFitness = newFitness;
				route = newRoute;
			} 
			count++;
		}
		
		System.out.println("Beste Route: ");
		for (int i = 0; i < route.length; i++) {
			System.out.print(route[i]+"-->");
		}
	}
	
	private static int[] swapCities(int[] route) {
		int random1 = (int) (Math.random() * route.length);
		int random2 = (int) (Math.random() * route.length);

		if (random1 != random2) {
			int temp = route[random1];
			route[random1] = route[random2];
			route[random2] = temp;

		}
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
