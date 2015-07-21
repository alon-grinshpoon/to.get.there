package il.co.togetthere.db;
import java.util.ArrayList;
import java.util.List;

public class ServiceProvider {

	private String id;
	private ServiceProviderType type;

	private String name;
	private boolean verified;
	private int rank;

	private boolean hasToilets;
	private String toiletsDescription;
	private boolean hasParking;
	private String parkingDescription;
	private boolean hasElevator;
	private String elevatorDescription;
	private boolean hasEntrance;
	private String entranceDescription;
	private boolean hasFacilities;
	private String facilitiesDescription;

	private String address;
	private double latitude;
	private double longitude;
	private String phone;
	private int discount;
	private String website;

	private List<Review> reviews;

	/**
	 * Empty Constructor
	 */
	public ServiceProvider() {

		this.setID("");
		this.setType(ServiceProviderType.None);
		this.setName("");
		this.setVerified(false);
		this.setRank(0);
		this.setHasToilets(false);
		this.setToiletsDescription("");
		this.setHasParking(false);
		this.setParkingDescription("");
		this.setHasElevator(false);
		this.setElevatorDescription("");
		this.setHasEntrance(false);
		this.setEntranceDescription("");
		this.setHasFacilities(false);
		this.setFacilitiesDescription("");
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
	public ServiceProvider(String id, ServiceProviderType type, String name, boolean verified, int rank, boolean hasToilets, String toiletsDescription, boolean hasParking, String parkingDescription, boolean hasElevator, String elevatorDescription, boolean hasEntrance, String entranceDescription, boolean hasFacilities, String facilitiesDescription, String address, double latitude, double longitude, String phone, int discount, String website, List<Review> reviews) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.verified = verified;
		this.rank = rank;
		this.hasToilets = hasToilets;
		this.toiletsDescription = toiletsDescription;
		this.hasParking = hasParking;
		this.parkingDescription = parkingDescription;
		this.hasElevator = hasElevator;
		this.elevatorDescription = elevatorDescription;
		this.hasEntrance = hasEntrance;
		this.entranceDescription = entranceDescription;
		this.hasFacilities = hasFacilities;
		this.facilitiesDescription = facilitiesDescription;
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
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public ServiceProviderType getType() {
		return type;
	}

	public void setType(ServiceProviderType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean hasToilets() {
		return hasToilets;
	}

	public void setHasToilets(boolean hasToilets) {
		this.hasToilets = hasToilets;
	}

	public String getToiletsDescription() {
		return toiletsDescription;
	}

	public void setToiletsDescription(String toiletsDescription) {
		this.toiletsDescription = toiletsDescription;
	}

	public boolean hasParking() {
		return hasParking;
	}

	public void setHasParking(boolean hasParking) {
		this.hasParking = hasParking;
	}

	public String getParkingDescription() {
		return parkingDescription;
	}

	public void setParkingDescription(String parkingDescription) {
		this.parkingDescription = parkingDescription;
	}

	public boolean hasElevator() {
		return hasElevator;
	}

	public void setHasElevator(boolean hasElevator) {
		this.hasElevator = hasElevator;
	}

	public String getElevatorDescription() {
		return elevatorDescription;
	}

	public void setElevatorDescription(String elevatorDescription) {
		this.elevatorDescription = elevatorDescription;
	}

	public boolean hasEntrance() {
		return hasEntrance;
	}

	public void setHasEntrance(boolean hasEntrance) {
		this.hasEntrance = hasEntrance;
	}

	public String getEntranceDescription() {
		return entranceDescription;
	}

	public void setEntranceDescription(String entranceDescription) {
		this.entranceDescription = entranceDescription;
	}

	public boolean hasFacilities() {
		return hasFacilities;
	}

	public void setHasFacilities(boolean hasFacilities) {
		this.hasFacilities = hasFacilities;
	}

	public String getFacilitiesDescription() {
		return facilitiesDescription;
	}

	public void setFacilitiesDescription(String facilitiesDescription) {
		this.facilitiesDescription = facilitiesDescription;
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

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

}