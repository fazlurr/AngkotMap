package id.fazlur.angkotmap.database.model;

public class Route {
	private long id;
	private long location_id_from;
	private long location_id_to;
	private String steps;
	private String directions;
	
	public Route(long id, long location_id_from, long location_id_to, String steps, String directions) {
		this.id = id;
		this.location_id_from = location_id_from;
		this.location_id_to = location_id_to;
		this.steps = steps;
		this.directions = directions;
	}
	
	public Route(long id) {
		this.id = id;
	}
	
	public Route() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getLocation_id_from() {
		return location_id_from;
	}
	public void setLocation_id_from(long location_id_from) {
		this.location_id_from = location_id_from;
	}
	public long getLocation_id_to() {
		return location_id_to;
	}
	public void setLocation_id_to(long location_id_to) {
		this.location_id_to = location_id_to;
	}
	public String getSteps() {
		return steps;
	}
	public void setSteps(String steps) {
		this.steps = steps;
	}
	public String getDirections() {
		return directions;
	}
	public void setDirections(String directions) {
		this.directions = directions;
	}
	
	@Override
	public String toString(){
		return "Route " + id;
	}
}
