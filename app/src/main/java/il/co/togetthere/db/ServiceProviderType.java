package il.co.togetthere.db;

public enum ServiceProviderType {
	Medical, Restaurants, Shopping, Help, Transport, PublicServices, None;

	public static final ServiceProviderType stringToEnum(String type) {
		if (type.equalsIgnoreCase("medical")) {
			return ServiceProviderType.Medical;
		}
		if (type.equalsIgnoreCase("restaurants")) {
			return ServiceProviderType.Restaurants;
		}
		if (type.equalsIgnoreCase("help")) {
			return ServiceProviderType.Help;
		}
		if (type.equalsIgnoreCase("public_services") || type.equalsIgnoreCase("publicservices")) {
			return ServiceProviderType.PublicServices;
		}
		if (type.equalsIgnoreCase("shopping")) {
			return ServiceProviderType.Shopping;
		}
		if (type.equals("transport")) {
			return ServiceProviderType.Transport;
		} else {
			return ServiceProviderType.None;
		}
	}
	}

