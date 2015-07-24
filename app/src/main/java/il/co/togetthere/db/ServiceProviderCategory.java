package il.co.togetthere.db;

public enum ServiceProviderCategory {
	medical, restaurants, shopping, help, transport, public_services, none;

	public static final ServiceProviderCategory stringToEnum(String category) {
		if (category.equalsIgnoreCase("medical")) {
			return ServiceProviderCategory.medical;
		}
		if (category.equalsIgnoreCase("restaurants")) {
			return ServiceProviderCategory.restaurants;
		}
		if (category.equalsIgnoreCase("help")) {
			return ServiceProviderCategory.help;
		}
		if (category.equalsIgnoreCase("public_services") || category.equalsIgnoreCase("publicservices") || category.equalsIgnoreCase("public services")) {
			return ServiceProviderCategory.public_services;
		}
		if (category.equalsIgnoreCase("shopping")) {
			return ServiceProviderCategory.shopping;
		}
		if (category.toLowerCase().contains("transport")) {
			return ServiceProviderCategory.transport;
		} else {
			return ServiceProviderCategory.none;
		}
	}

	public static final String enumToString(ServiceProviderCategory category) {
		return category.toString();
	}
}

