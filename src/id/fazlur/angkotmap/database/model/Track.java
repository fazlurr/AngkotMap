package id.fazlur.angkotmap.database.model;

public class Track {
	private long id;
	private long connection_id;
	private long angkot_id;
	private String path;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAngkot_id() {
		return angkot_id;
	}
	public void setAngkot_id(long angkot_id) {
		this.angkot_id = angkot_id;
	}
	public long getConnection_id() {
		return connection_id;
	}
	public void setConnection_id(long connection_id) {
		this.connection_id = connection_id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
