package il.co.togetthere.db;

public enum ServiceProviderCategory {
	Medical, Restaurants, Shopping, Help, Transport, PublicServices, None;

	public static final ServiceProviderCategory stringToEnum(String category) {
		if (category.equalsIgnoreCase("medical")) {
			return ServiceProviderCategory.Medical;
		}
		if (category.equalsIgnoreCase("restaurants")) {
			return ServiceProviderCategory.Restaurants;
		}
		if (category.equalsIgnoreCase("help")) {
			return ServiceProviderCategory.Help;
		}
		if (category.equalsIgnoreCase("public_services") || category.equalsIgnoreCase("publicservices")) {
			return ServiceProviderCategory.PublicServices;
		}
		if (category.equalsIgnoreCase("shopping")) {
			return ServiceProviderCategory.Shopping;
		}
		if (category.equals("transport")) {
			return ServiceProviderCategory.Transport;
		} else {
			return ServiceProviderCategory.None;
		}
	}

	public static final String enumToString(ServiceProviderCategory category) {
		switch (category) {
			case Medical:
				return "medical";
			case Restaurants:
				return "restaurants";
			case Shopping:
				return "shopping";
			case Help:
				return "help";
			case Transport:
				return "transport";
			case PublicServices:
				return "publicservices";
		}
		return "none";
	}
	}

