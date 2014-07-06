package id.fazlur.angkotmap.database.model;

public class Node extends Connection {
	private boolean visited = false;
	
	public boolean isVisited() {
		return this.visited;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
