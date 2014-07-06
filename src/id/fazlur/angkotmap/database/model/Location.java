package id.fazlur.angkotmap.database.model;

public class Location {
	private long id;
	private String name;
	private Double lat ;
	private Double lng;
	
	public Location(){
		
	}
	
	public Location(long id, String name, Double lat, Double lng){
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
