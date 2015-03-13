package application;
	
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.TaskAdminPanel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	public final static int year = 2014;
	private static ArrayList<TaskAdminPanel> tasks;
	private static String currVolunteerStr = "";
	private static long currVolID;
	private static int selectedItem = 0;
	private static int lastSelectedUser = 0;
	public static Map<Integer, Integer> volMap = new HashMap<>();
	
	public static void setSelectedItem(int i) {
		selectedItem = i;
	}
	
	public static int getSelectedItem() {
		return selectedItem;
	}
	
	public static void setLastSelectedUser(int i) {
		lastSelectedUser = i;
	}
	
	public static int getLastSelectedUser() {
		return lastSelectedUser;
	}
	public static void setTasks(ArrayList<TaskAdminPanel> tasksArr) {
		tasks = tasksArr;
	}
	
	public static ArrayList<TaskAdminPanel> getCurrentTask() {
		return tasks;
	}
	
	public static void setCurrVolunteerStr(String str) {
		currVolunteerStr = str;
	}
	
	public static String getCurrVolunteerStr() {
		return currVolunteerStr;
	}

	public static void setCurrVolID(long id) {
		currVolID = id;
	}
	
	public static long getCurrVolID() {
		return currVolID;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setResizable(false);
//			primaryStage.initStyle(StageStyle.UNDECORATED);
			
			Parent root = FXMLLoader.load(getClass().getResource("/application/layout.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
//		ServiceProvider serviceProviderF = new ServiceProvider("Derech Menachem Begin 132","Shopping","Azrieli Center", "03-6081199", 10, 31.752559,35.2166099, true, true, true, true, true,false );
//		ServiceProvider serviceProviderG = new ServiceProvider("Ashkenazi St 46, Tel Aviv-Yafo", "Medical", "Dr. Miri israel", "03-6477217", 5,31.752559,35.2166099, true, true,true,false,true,false );
//		ServiceProvider serviceProviderH = new ServiceProvider("Ramat Gan", "Transportation", "Ramat Gan Taxi", "03-7510606", 5, 31.752559,35.2166099, true, false, true,true,true,false );
//		
//		 ReviewObj reviewA = new ReviewObj("Alon", "review A", "test review 1", 1);
//		 ReviewObj reviewB = new ReviewObj("Eliran", "review B", "test review 2", 2);
//		 ReviewObj reviewC = new ReviewObj("Rony", "review C", "test review 3", 3);
//		 ReviewObj reviewD = new ReviewObj("Edo", "review D", "test review 4", 4);
//		 
//		 serviceProviderF.addReview(reviewA) ; serviceProviderF.addReview(reviewB) ; serviceProviderF.addReview(reviewC) ;serviceProviderF.addReview(reviewD) ;
//		 serviceProviderG.addReview(reviewA) ; serviceProviderG.addReview(reviewB) ; serviceProviderG.addReview(reviewC) ;serviceProviderG.addReview(reviewD) ;
//		 serviceProviderH.addReview(reviewA) ; serviceProviderH.addReview(reviewB) ; serviceProviderH.addReview(reviewC) ;serviceProviderH.addReview(reviewD) ;
//		CSVParse.addSPTODB(serviceProviderF);
//		CSVParse.addSPTODB(serviceProviderG);
//		CSVParse.addSPTODB(serviceProviderH);
//		
//		CSVParse.parseMainFile();
//		CSVParse.parseTypeFile();
//		CSVParse.createDB();
		
		launch(args);
	}
}
