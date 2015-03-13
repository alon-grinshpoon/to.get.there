package listWindow;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.DynamoDBManager;
import database.TaskAdminPanel;
import database.UserAdminPanel;
//import application.HelpRequestListViewItem;
import application.ListViewItem;
import application.Main;
import application.TaskListViewItem;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;

public class ListWindow implements Initializable {
	public GridPane gridPane 				= new GridPane();
	private StackPane volunteerStackPane	= new StackPane();
	private HBox viewAllButtonVHox 			= new HBox();
	private ScrollPane scrollPaneLeft		= new ScrollPane();
	private ScrollPane scrollPaneRight		= new ScrollPane();
		
	File volunteerFile	= new File("misc/volunteers.png");
	File taskFile		= new File("misc/tasks.png");
	File viewAllFile	= new File("misc/icon_view.png");
	File logoFile		= new File("misc/logo.png");
	
	private Image volunteer = new Image(volunteerFile.toURI().toString(), 400, 65, false, true);
	private Image taskImg	= new Image(taskFile.toURI().toString(), 400, 65, false, true);
	private Image viewAll	= new Image(viewAllFile.toURI().toString(), 35, 37, false, true);
	private Image logo		= new Image(logoFile.toURI().toString(), 800, 57, false, true);
	
	private ImageView volImageView		= new ImageView(volunteer);
	private ImageView taskImageView		= new ImageView(taskImg);
	private ImageView viewAllImageView	= new ImageView(viewAll);
	private ImageView logoImageView		= new ImageView(logo);
	
	public void updateTaskTable() {
		ArrayList<TaskAdminPanel> tasks = DynamoDBManager.getTasks();
		Main.setTasks(tasks);
		TaskAdminPanel[] taskArr = new TaskAdminPanel[tasks.size()];
		tasks.toArray(taskArr);
		ObservableList<TaskAdminPanel> obsrvTaskArr = FXCollections.observableArrayList(taskArr);
		final ListView<TaskAdminPanel> taskLV	= new ListView<>(obsrvTaskArr);
		taskLV.setPrefSize(400, 535);
		
		taskLV.getSelectionModel().select(Main.getSelectedItem());
		
		taskLV.setCellFactory(new Callback<ListView<TaskAdminPanel>, ListCell<TaskAdminPanel>>() {
            @Override
            public ListCell<TaskAdminPanel> call(ListView<TaskAdminPanel> param) {
                return new TaskListViewItem();
            }
        });

		taskLV.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	        	TaskAdminPanel task = taskLV.getSelectionModel().getSelectedItem();
	        	
	        	if (task != null) {
	        		int k = taskLV.getSelectionModel().getSelectedIndex();
	        		Main.setSelectedItem(k);
		        	ArrayList<UserAdminPanel> users = task.volunteers();
		    		UserAdminPanel[] usersArr = new UserAdminPanel[users.size()];
		    		users.toArray(usersArr);
		    		ObservableList<UserAdminPanel> obsrvTaskArr = FXCollections.observableArrayList(usersArr);
		    		final ListView<UserAdminPanel> usersLV	= new ListView<>(obsrvTaskArr);
		    		
		    		usersLV.setCellFactory(new Callback<ListView<UserAdminPanel>, ListCell<UserAdminPanel>>() {
		                @Override
		                public ListCell<UserAdminPanel> call(ListView<UserAdminPanel> param) {
		                    return new ListViewItem();
		                }
		            });
		    		usersLV.setPrefSize(400, 535);
		    		scrollPaneRight.setContent(usersLV);
		    		
		    		usersLV.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent event) {
							int k = usersLV.getSelectionModel().getSelectedIndex();
							Main.setLastSelectedUser(k);
						}
		    			
					});
	        	}
	        }
	    });
		
		viewAllImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for (TaskAdminPanel currTask : Main.getCurrentTask()) {
					currTask.setAttachedUserToTask("canceled");
					currTask.updateUserToDB();
				}
				
//	        	scrollPaneRight.setContent(volunteerList);
				// TODO - add a getAllVolunteers method.
			}
		});
		
		scrollPaneLeft.setContent(taskLV);
//		scrollPaneLeft.setContent(taskLV2);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		viewAllButtonVHox.getChildren().add(viewAllImageView);
		viewAllButtonVHox.setPadding(new Insets(10));
		viewAllButtonVHox.setAlignment(Pos.CENTER_RIGHT);
		volunteerStackPane.getChildren().add(volImageView);
		volunteerStackPane.getChildren().add(viewAllButtonVHox);
		gridPane.add(taskImageView, 0, 0);
		gridPane.add(volunteerStackPane, 1, 0);
		gridPane.add(scrollPaneLeft, 0, 1);
		gridPane.add(scrollPaneRight, 1, 1);
		gridPane.add(logoImageView, 0, 2);
		scrollPaneLeft.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPaneRight.setHbarPolicy(ScrollBarPolicy.NEVER);

		try {
			DynamoDBManager.init();
//			ArrayList<TaskAdminPanel> tasks = DynamoDBManager.getTasks();
//			System.out.println(tasks.size());
//			DynamoDBManager.addTaskToDB(tasks.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		updateTaskTable();
		
		Timeline updateTaskList = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
//		    	System.out.println("update task list");
		    	updateTaskTable();
		    }
		}));
		updateTaskList.setCycleCount(Animation.INDEFINITE);
		updateTaskList.play();
	}
}
