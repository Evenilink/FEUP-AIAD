package agents;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import entities.Exit;
import entities.Obstacle;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.domain.DFService;
import utils.Coordinates;
import utils.Utils.ExplorerState;

public class Explorer extends Agent {
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private Coordinates coordinates;
	private int radious;
	
	private int[][] matrix;
	private ExplorerState state;
	private int iteration = 0;
	
	

	
	public Explorer(ContinuousSpace<Object> space, Grid<Object> grid, int radious, int posX, int posY) {
		this.space = space;
		this.grid = grid;
		this.radious = radious;
		coordinates = new Coordinates(posX, posY);
		state = ExplorerState.ALEATORY_DFS;
		
		matrix = new int[grid.getDimensions().getHeight()][grid.getDimensions().getWidth()];
		for(int row = 0; row < grid.getDimensions().getHeight(); row++) {
			for(int column = 0; column < grid.getDimensions().getWidth(); column++)
				matrix[row][column] = 0;
		}		
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	@Override
	public void setup() {
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		dfAgentDescription.setName(getAID());
		
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setName(getName());
		serviceDescription.setType("Explorer");
		
		dfAgentDescription.addServices(serviceDescription);
		
		try {
			DFService.register(this, dfAgentDescription);
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		// Sets his initial position in the matrix.
		GridPoint initLocation = grid.getLocation(this);
		matrix[grid.getDimensions().getHeight() - 1 - initLocation.getY()][initLocation.getX()] = 1;
		
		addBehaviour(new AleatoryDFS(this));
	}
	
	private void getDirectionBlocking() {
		// Returns the first direction that is blocking the pledge.
	}
	
	private void printMatrix() {
		int numOnes = 0;
		
		System.out.println("Iteration " + iteration);
		for(int row = 0; row < matrix.length; row++) {
			for(int column = 0; column < matrix[row].length; column++) {
				if(matrix[row][column] == 1)
					numOnes++;
				System.out.print(matrix[row][column] + " | ");
			}
			System.out.println();
		}
		
		System.out.println("Number of ones: " + numOnes + "\n");

	}
	
	class VerticalMovementBehaviour extends CyclicBehaviour {
		
		private Agent agent;
		
		public VerticalMovementBehaviour(Agent agent) {
			super(agent);
			this.agent = agent;
		}

		@Override
		public void action() {
			GridPoint pt = grid.getLocation(agent);
			NdPoint origin = space.getLocation(agent);
			space.moveByDisplacement(agent, 0, 1);
			origin = space.getLocation(agent);
			grid.moveTo(agent, (int) origin.getX(), (int) origin.getY());
			System.out.println("x: " + (int) Math.round(origin.getX()) + ", y: " + (int) Math.round(origin.getY()));
		}
		
		
		
	}
	
	class AleatoryDFS extends CyclicBehaviour {
		
		private Agent agent;
		
		public AleatoryDFS(Agent agent) {
			super(agent);
			this.agent = agent;
		}
		
		@Override
		public void action() {
			if(state == ExplorerState.EXIT)
				return;
			
			GridPoint pt = grid.getLocation(agent);
			GridCellNgh<Object> nghCreator = new GridCellNgh<Object>(grid, pt, Object.class, radious, radious);
			List<GridCell<Object>> gridCells = nghCreator.getNeighborhood(false);
			GridCell<Object> cell = null;

			// Check if the exit is in sight.
			for(GridCell<Object> tempCell : gridCells) {
				for(Object obj : grid.getObjectsAt(tempCell.getPoint().getX(), tempCell.getPoint().getY())) {
					if(obj instanceof Exit) {
						cell = tempCell;
						state = ExplorerState.EXIT;
						break;
					}
				}
			}
			
			// Check if we see obstacles.
			for(GridCell<Object> tempCell : gridCells) {
				for(Object obj : grid.getObjectsAt(tempCell.getPoint().getX(), tempCell.getPoint().getY())) {
					if(obj instanceof Obstacle) {
						//grid.
						
						state = ExplorerState.PLEDGE;
						break;
					}
				}
			}
			
			if(state != ExplorerState.EXIT) {
				SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
				
				int i = -1;
				int row, column;
				do {
					if(i >= gridCells.size()) {
						cell = gridCells.get(ThreadLocalRandom.current().nextInt(0, gridCells.size()));
						break;
					}
					i++;
					cell = gridCells.get(i);
					row = grid.getDimensions().getHeight() - 1 - cell.getPoint().getY();
					column = cell.getPoint().getX();
					System.out.println("row:" + (cell.getPoint().getY()) + ", column: " + cell.getPoint().getX());
				} while(matrix[row][column] == 1);
			}
			
			moveAgent(cell.getPoint());
		}
		
		private void moveAgent(GridPoint targetPoint) {			
			NdPoint origin = space.getLocation(agent);
			NdPoint target = new NdPoint(targetPoint.getX(), targetPoint.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, origin, target);
			space.moveByVector(agent, 1, angle, 0);
			origin = space.getLocation(agent);
			grid.moveTo(agent, (int) origin.getX(), (int) origin.getY());
			
			matrix[grid.getDimensions().getHeight() - 1 - (int) origin.getY()][(int) origin.getX()] = 1;
			
			iteration++;
			printMatrix();
		}
	}
	
	@Override
	public void takeDown() {
		try {
			DFService.deregister(this);
		} catch(FIPAException e) {
			e.printStackTrace();
		}
	}
}
