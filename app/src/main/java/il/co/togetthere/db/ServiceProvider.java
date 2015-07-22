package il.co.togetthere.db;
import java.util.Arrays;
import java.util.List;

public class ServiceProvider {

	private String id;
	private ServiceProviderCategory category;

	private String sp_name;
	private boolean is_verified;
	private int avg_rank;

	private boolean toilets;
	private String toilets_text;
	private boolean parking;
	private String parking_text;
	private boolean elevator;
	private String elevator_text;
	private boolean entrance;
	private String entrance_text;
	private boolean facilities;
	private String facilities_text;

	private String address;
	private double latitude;
	private double longitude;
	private String phone;
	private int discount;
	private String website;

	private Review[] reviews;

	/**
	 * Empty Constructor
	 */
	public ServiceProvider() {

		this.setId("");
		this.setCategory(ServiceProviderCategory.None);
		this.setSp_name("");
		this.setIs_verified(false);
		this.setAverageRank(0);
		this.setToilets(false);
		this.setToilets_text("");
		this.setParking(false);
		this.setParking_text("");
		this.setElevator(false);
		this.setElevator_text("");
		this.setEntrance(false);
		this.setEntrance_text("");
		this.setFacilities(false);
		this.setFacilities_text("");
		this.setAddress("");
		this.setLatitude(0);
		this.setLongitude(0);
		this.setPhone("");
		this.setDiscount(0);
		this.setWebsite("");
		this.setReviews(null);
	}

	/**
	 * Constructor
	 */
	public ServiceProvider(String id, ServiceProviderCategory category, String sp_name, boolean is_verified, int avg_rank, boolean toilets, String toilets_text, boolean parking, String parking_text, boolean elevator, String elevator_text, boolean entrance, String entrance_text, boolean facilities, String facilities_text, String address, double latitude, double longitude, String phone, int discount, String website, Review[] reviews) {
		this.id = id;
		this.category = category;
		this.sp_name = sp_name;
		this.is_verified = is_verified;
		this.avg_rank = avg_rank;
		this.toilets = toilets;
		this.toilets_text = toilets_text;
		this.parking = parking;
		this.parking_text = parking_text;
		this.elevator = elevator;
		this.elevator_text = elevator_text;
		this.entrance = entrance;
		this.entrance_text = entrance_text;
		this.facilities = facilities;
		this.facilities_text = facilities_text;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.phone = phone;
		this.discount = discount;
		this.website = website;
		this.reviews = reviews;
	}


	/*
	 * Setter and Getters
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ServiceProviderCategory getCategory() {
		return category;
	}

	public void setCategory(ServiceProviderCategory category) {
		this.category = category;
	}

	public String getSp_name() {
		return sp_name;
	}

	public void setSp_name(String sp_name) {
		this.sp_name = sp_name;
	}

	public boolean is_verified() {
		return is_verified;
	}

	public void setIs_verified(boolean is_verified) {
		this.is_verified = is_verified;
	}

	public int getAverageRank() {
		return avg_rank;
	}

	public void setAverageRank(int avg_rank) {
		this.avg_rank = avg_rank;
	}

	public boolean isToilets() {
		return toilets;
	}

	public void setToilets(boolean toilets) {
		this.toilets = toilets;
	}

	public String getToilets_text() {
		return toilets_text;
	}

	public void setToilets_text(String toilets_text) {
		this.toilets_text = toilets_text;
	}

	public boolean isParking() {
		return parking;
	}

	public void setParking(boolean parking) {
		this.parking = parking;
	}

	public String getParking_text() {
		return parking_text;
	}

	public void setParking_text(String parking_text) {
		this.parking_text = parking_text;
	}

	public boolean isElevator() {
		return elevator;
	}

	public void setElevator(boolean elevator) {
		this.elevator = elevator;
	}

	public String getElevator_text() {
		return elevator_text;
	}

	public void setElevator_text(String elevator_text) {
		this.elevator_text = elevator_text;
	}

	public boolean isEntrance() {
		return entrance;
	}

	public void setEntrance(boolean entrance) {
		this.entrance = entrance;
	}

	public String getEntrance_text() {
		return entrance_text;
	}

	public void setEntrance_text(String entrance_text) {
		this.entrance_text = entrance_text;
	}

	public boolean isFacilities() {
		return facilities;
	}

	public void setFacilities(boolean facilities) {
		this.facilities = facilities;
	}

	public String getFacilities_text() {
		return facilities_text;
	}

	public void setFacilities_text(String facilities_text) {
		this.facilities_text = facilities_text;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Review[] getReviews() {
		return reviews;
	}

	public List<Review> getReviewsAsList() {
		return (this.reviews != null) ? Arrays.asList(this.reviews) : null;
	}

	public void setReviews(Review[] reviews) {
		this.reviews = reviews;
	}
	public void setReviewsFromList(List<Review> reviews) {
		if (this.reviews == null && reviews != null){
			this.reviews = new Review[reviews.size()];
			reviews.toArray(this.reviews);
		}
	}
}