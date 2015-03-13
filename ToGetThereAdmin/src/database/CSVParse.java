package database;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class CSVParse {
	//Delimiter used in CSV file
	 private static final   String DELIMITER = "," ;
	 private static ArrayList<ArrayList<String>> mainFile = new ArrayList<ArrayList<String>>();
	 private static String[] idToType = new String[2323];
	 private static final int ID = 0;
	 private static final int NAME = 1;
	 private static final int ADDRESS = 6;
	 private static final int ADDRESS_2 = 7;
	 private static final int PHONE = 3;
	 private static final int GEOLAT = 4;
	 private static final int GEOLON = 5;
	 public static void parseMainFile() {
		//Input file which needs to be parsed
		    String fileToParse = "/home/eliran/mainData.csv";
		    BufferedReader fileReader = null;
		    try
		    {
		        String line = "";
		        //Create the file reader
		        
		        fileReader = new BufferedReader(new FileReader(fileToParse));
		         
		        //Read the file line by line
		        while ((line = fileReader.readLine()) != null)
		        {
		            //Get all tokens available in line
		            String[] tokens = line.split(DELIMITER);
		            ArrayList<String> tokenArr = new ArrayList<String>();
		            for(String token : tokens)
		            {
		            	tokenArr.add(token);
		            	
		            }
		            mainFile.add(tokenArr);
		        }
		        System.out.println("finish to parse mainFile");
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    finally
		    {
		        try {
		            fileReader.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    } 
	 }
	 public static void parseTypeFile() {
		 //Input file which needs to be parsed
		    String fileToParse = "/home/eliran/idToType.csv";
		    BufferedReader fileReader = null;

		    try
		    {
		        String line = "";
		        //Create the file reader
		        fileReader = new BufferedReader(new FileReader(fileToParse));
		         
		        //Read the file line by line
		        while ((line = fileReader.readLine()) != null)
		        {
		            //Get all tokens available in line
		            String[] tokens = line.split(DELIMITER);
		            int index = Integer.parseInt(tokens[0]);
		            idToType[index] += tokens[1] + ",";
		      /*      for (int i=1; i<tokens.length;i++) {
		            	idToType[index] += tokens[i] + ","; 
		            }*/
		           
		        }
		        System.out.println("finish to parse idToType");
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    finally
		    {
		        try {
		            fileReader.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    } 
	 }

	public static void addSPTODB(ServiceProvider sp) {
		try {
			DynamoDBManager.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DynamoDBManager.addServiceProvider(sp);
		System.out.println("sp updated");
	}
	
	 public static void createDB() {
		 boolean toilets;
		 boolean entrance;
		 boolean parking;
		 boolean facilities;
		 boolean elevator;
		 String name;
		 String address;
		 String phone;
		 double lat;
		 double lon;
		 String type = "Restaurants";
		 int index;
		 int discount = 0;
		 try {
			DynamoDBManager.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 for (ArrayList<String> line: mainFile) {
			
			 index = Integer.parseInt(line.get(ID));
			 if (idToType[index] !=null && line.size() > 6 && line.size() < 9) {
				 toilets = idToType[index].contains("221") || idToType[index].contains("222") || idToType[index].contains("616") || idToType[index].contains("617");
				 entrance = idToType[index].contains("210") || idToType[index].contains("211") || idToType[index].contains("606") || idToType[index].contains("607");
				 parking = idToType[index].contains("196") || idToType[index].contains("200") || idToType[index].contains("592") || idToType[index].contains("595");
				 facilities = idToType[index].contains("237") || idToType[index].contains("239") || idToType[index].contains("635") || idToType[index].contains("637") || idToType[index].contains("633");
				 elevator = false; // irrelevant in restuartns \ bars
				 name = line.get(NAME);
				 address = line.get(ADDRESS);
				 if (line.size() == 8  && line.get(ADDRESS_2) != null)  address +=", " + line.get(ADDRESS_2);
				 phone = line.get(PHONE);
				 try {
				 lat = Double.parseDouble(line.get(GEOLAT));
				 lon = Double.parseDouble(line.get(GEOLON));
				 ServiceProvider sp = new ServiceProvider(address,type,name,phone, discount, lat, lon,false, entrance, toilets, parking, facilities, elevator);
				 DynamoDBManager.addServiceProvider(sp);
				
				 } catch (Exception e) {
					 e.getMessage();
				 }
			 }	 
		 }
		 
	 }
	 
}
