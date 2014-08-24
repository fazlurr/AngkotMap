package id.fazlur.angkotmap.database.model;

public class Connection {
	private long id;
	private long location_id_from;
	private long location_id_to;
	
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
	
}
