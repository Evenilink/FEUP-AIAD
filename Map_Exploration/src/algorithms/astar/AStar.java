package algorithms.astar;

import java.util.List;

import agents.Explorer;
import behaviours.AleatoryDFS;
import utils.Coordinates;
import utils.Utils.ExplorerState;

public class AStar {

	private Explorer agent;
	private AleatoryDFS behaviour;
	
	private Pathfinding pathfinding;
	private List<algorithms.astar.Node> path; 
	private int pathNode;
	
	public AStar(Explorer agent, AleatoryDFS behaviour) {
		this.agent = agent;
		this.behaviour = behaviour;
		pathfinding = new Pathfinding(agent.getGrid().getDimensions().getWidth(), agent.getGrid().getDimensions().getHeight());
		pathNode = 0;
	}
	
	public void run() {
		agent.moveAgent(new Coordinates(path.get(pathNode).getWorldPosition().getX(), path.get(pathNode).getWorldPosition().getY()));
		pathNode++;
		
		if(pathNode == path.size()) {
			behaviour.changeState(ExplorerState.ALEATORY_DFS);
			path = null;
			pathNode = 0;
		}
	}
	
	public void setPath(Coordinates sourceWorldPosition, Coordinates targetWorldPosition) {
		path = pathfinding.FindPath(sourceWorldPosition, targetWorldPosition);
	}
	
}