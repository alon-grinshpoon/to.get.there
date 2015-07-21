package il.co.togetthere.db;
import java.util.ArrayList;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAutoGeneratedKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;


@DynamoDBTable(tableName = "serviceProvider")
public class ServiceProvider {
	private String _type;
	private String _name;
	private String _address;
	private String _phone;
	private boolean _verified;
	private int _discount;
	private String _website;
	private int _location;
	private double mLatitude;
	private double mLongitude; 
	private String id;
	
	private String _reviews;
	private String _points;
	private String _reviewers;
	private String _titles;
	
	private boolean pToilets;
	private boolean pParking;
	private boolean pEntrance;
	private boolean pFacilites;
	private boolean pElevator;

	// Constructors. empty constructor required by aws dynamoDB
	public ServiceProvider() {
		this.setAddress(null);
		this.setDiscount(0);
		this.setLatitude(0);
		this.setLongitude(0);
		this.setPhone(null);
		this.setName(null);
		this.setReviews(null);
		this.setType("");
		this.setVerified(false);
		this.setElevator(false);
		this.setFacilities(false);
		this.setEntrance(false);
		this.setParking(false);
		this.setToilets(false);
		
		_reviewers = "";
		_reviews = "";
		_titles = "";
		_points = "";
	}

	public ServiceProvider(String address, String type, String name, String phone, 
			int discount, double latitude, double longitude, boolean verified,
			boolean entrance, boolean toilets, boolean parking, boolean facilities, boolean elevator) {
		this.setAddress(address);
		this.setDiscount(discount);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setPhone(phone);
		this.setName(name);
		this.setType(type);
		this.setVerified(verified);
		
		this.setElevator(elevator);
		this.setFacilities(facilities);
		this.setEntrance(entrance);
		this.setParking(parking);
		this.setToilets(toilets);

		
		_reviewers = "";
		_reviews = "";
		_titles = "";
		_points = "";
	}
	@DynamoDBHashKey(attributeName = "Id")
	@DynamoDBAutoGeneratedKey
  public String getId() { return id; }
	public void setId(String id) { this.id = id; } 
	
	@DynamoDBAttribute(attributeName = "phone")
	public String getPhone() {
		return _phone;
	}
	public void setPhone(String phone) {
		_phone = phone;
	}
	
	@DynamoDBAttribute(attributeName = "name")
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}

	@DynamoDBAttribute(attributeName = "address")
	public String getAddress() {
		return _address;
	}
	public void setAddress(String address) {
		_address = address;
	}
	
	@DynamoDBAttribute(attributeName = "type")
	public String getType() {
		return _type;
	}
	public void setType(String type) {
		_type = type;
	}
	@DynamoDBAttribute(attributeName = "verified")
	public boolean isVerified() {
		return _verified;
	}
	public void setVerified(boolean verified) {
		_verified = verified;
	}

	@DynamoDBAttribute(attributeName = "discount")
	public int getDiscount() {
		return _discount;
	}
	public void setDiscount(int discount) {
		_discount = discount;
	}

	public String getWebsite() {
		return _website;
	}
	public void setWebsite(String website) {
		_website = website;
	}

	@DynamoDBAttribute(attributeName = "latitude")
	public double getLatitude() {
		return mLatitude;
	}
	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}
	
	@DynamoDBAttribute(attributeName = "longitude")
	public double getLongitude() {
		return mLongitude;
	}
	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	@DynamoDBAttribute(attributeName = "reviews")
	public String getReviews() {
		return _reviews;
	}
	public void setReviews(String reviews) {
		_reviews = reviews;
	}

	@DynamoDBAttribute(attributeName = "points")
	public String getPoints() {
		return _points;
	}
	public void setPoints(String points) {
		_points = points;
	}

	@DynamoDBAttribute(attributeName = "reviewers")
	public String getReviewers() {
		return _reviewers;
	}
	public void setReviewers(String reviewers) {
		_reviewers = reviewers;
	}
	
	@DynamoDBAttribute(attributeName = "titles")
	public String getTitles() {
		return _titles;
	}
	public void setTitles(String titles) {
		_titles = titles;
	}

	
	public void addReview (ReviewObj revObj) {
		_reviews += revObj.getReview() + "-";
		_points += Integer.toString(revObj.getPoints()) + "-";
		_reviewers += revObj.getReviewer() + "-";
	    _titles += revObj.getTitle() + "-";
	}
	

	@DynamoDBAttribute(attributeName = "entrance")
	public boolean getEntrance() {
		return pEntrance;
	}
	public void setEntrance (boolean entrance) {
		pEntrance = entrance;
	}

	@DynamoDBAttribute(attributeName = "toilets")
	public boolean getToilets() {
		return pToilets;
	}
	public void setToilets (boolean toilets) {
		pToilets = toilets;
	}
	@DynamoDBAttribute(attributeName = "parking")
	public boolean getParking() {
		return pParking;
	}
	public void setParking (boolean parking) {
		pParking = parking;
	}
	@DynamoDBAttribute(attributeName = "facilities")
	public boolean getFacilities() {
		return pFacilites;
	}
	public void setFacilities (boolean facilities) {
		pFacilites = facilities;
	}
	@DynamoDBAttribute(attributeName = "elevator")
	public boolean getElevator() {
		return pElevator;
	}
	public void setElevator (boolean elevator) {
		pElevator = elevator;
	}

	
	// add review or update points of review.
	// update needed before sync with DB
	public void updatePoints(ReviewObj revObj, int reviewPosition) {
		String[] reviews = _reviews.split("-");
		String[] points = _points.split("-");
		if (reviewPosition < reviews.length && reviews[reviewPosition].equals(revObj.getReview())) { // validity check
			points[reviewPosition] = Integer.toString(revObj.getPoints());
			_points = "";
			for (int i = 0; i < points.length; i++) {
				_points += points[i] + "-";
			}
		}
	}

	// create an easy way to handle reviews & points objects
	public ArrayList<ReviewObj> parseReviews() {
		ArrayList<ReviewObj> result = new ArrayList<ReviewObj>();
		String[] reviews = _reviews.split("-");
		String[] points = _points.split("-");
		String[] reviewers = _reviewers.split("-");
		String[] titles = _titles.split("-");
		for (int i = 0; i < reviews.length; i++) {
			int currentPoints = 0;
			if(!points[i].equals("")) currentPoints = Integer.parseInt(points[i]);
			result.add(new ReviewObj(reviewers[i],titles[i],reviews[i], currentPoints));
		}
		return result;
	}

	public static ServiceProviderType stringToEnum(String type) {
		if (type.equals("medical")) {
			return ServiceProviderType.Medical;
		}
		if (type.equals("restaurants")) {
			return ServiceProviderType.Restaurants;
		}
		if (type.equals("help")) {
			return ServiceProviderType.Help;
		}
		if (type.equals("public_services")) {
			return ServiceProviderType.PublicServices;
		}
		if (type.equals("shopping")) {
			return ServiceProviderType.Shopping;
		} 
		if (type.equals("transport")) {
			return ServiceProviderType.Transportation;
		} else {
			return null;
		}
		
	}


}