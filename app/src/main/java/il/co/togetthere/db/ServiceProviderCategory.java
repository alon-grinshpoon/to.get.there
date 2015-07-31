package il.co.togetthere.db;

/**
 * Enum representation of all possible categories of service providers.
 */
public enum ServiceProviderCategory {
	medical, restaurants, shopping, help, transport, public_services, none;

	/**
	 * Convert a string to the matching enum representation
	 * @param category A string naming a category
	 * @return The enum value of the given category
	 */
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

	/**
	 * Convert an enum representation to a matching string representation
	 * @param category A enum value of a category
	 * @return A string value of the given enum
	 */
	public static final String enumToString(ServiceProviderCategory category) {
		return category.toString();
	}
}

