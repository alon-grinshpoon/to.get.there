package application;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import database.TaskAdminPanel;
import database.UserAdminPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TaskListViewItem extends ListCell<TaskAdminPanel>{
//	private File defaultImage = new File("/Users/edocohen/eclipse43/workspace/ToGetThereAdmin/src/misc/user.png");
	private File defaultImage = new File("misc/user.png");
	private File taskDoneFile = new File("misc/checkmark.png");
	private StackPane itemSP;// = new StackPane();
	public TaskListViewItem() {
		super();
	}
	
	@Override
	protected void updateItem(TaskAdminPanel item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			UserAdminPanel user = item.getTaskOpener();
			itemSP = new StackPane();
			VBox itemVBox = new VBox();
			HBox newItem = new HBox();
			HBox doneHBox = new HBox();
			doneHBox.setAlignment(Pos.TOP_RIGHT);
			newItem.setPrefHeight(50);
			ImageView doneImageView = new ImageView(new Image(taskDoneFile.toURI().toString(), 50, 50, false, true));
			doneHBox.getChildren().add(doneImageView);
			ImageView profileImageView = new ImageView(new Image(defaultImage.toURI().toString(), 50, 50, false, true));
			URL url;
			HttpURLConnection con;
			try {
				String urlString = "http://graph.facebook.com/" + user.getID() + "/picture?type=square";
//				System.out.println(item.getID());
				url = new URL(urlString);
				con = (HttpURLConnection) (url.openConnection());
				con.setInstanceFollowRedirects(false);
				con.connect();
				int responseCode = con.getResponseCode();
//				System.out.println(responseCode);
				String location = con.getHeaderField("Location");
				if (responseCode == 302) {
					profileImageView = new ImageView(location);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			newItem.getChildren().add(profileImageView);			
			VBox personInfo = new VBox();
			VBox taskDescription = new VBox();
			int age = 0;
			if (!user.getBirthday().equals("null")) { // parse age only if it exists...
				age = Integer.parseInt(user.getBirthday().split("\\.")[2]);
			}
			Label firstRow = new Label();
			if (age != 0) {
				firstRow	= new Label(user.getFirstName() + " " + user.getLastName() + ", " + (Main.year-age));	
			} else {
				firstRow	= new Label(user.getFirstName() + " " + user.getLastName());
			}

			Label secondRow = new Label(user.getLocation());
			personInfo.getChildren().add(firstRow);
			personInfo.getChildren().add(new Label(user.getLocation()));
			personInfo.getChildren().add(new Label(user.getPhone()));
			personInfo.setAlignment(Pos.CENTER_LEFT);
			personInfo.setPadding(new Insets(5));
			newItem.getChildren().add(personInfo);
			taskDescription.setAlignment(Pos.CENTER_LEFT);
			taskDescription.setPadding(new Insets(5, 5, 5, 50));
			newItem.getChildren().add(taskDescription);
			itemVBox.getChildren().add(newItem);
			itemVBox.getChildren().add(new Label());
			itemVBox.getChildren().add(new Label(item.getTitle()));
			itemVBox.getChildren().add(new Label(item.getBody()));
//			itemVBox.getChildren().add(new Label(user.getEmail()));
			itemVBox.getChildren().add(new Label());
			itemSP.getChildren().add(itemVBox);
//			System.out.println(item.getAttachedUserToTask());
			
			if ((item.getAttachedUserToTask() != null) && !(item.getAttachedUserToTask().equals("canceled"))) {
				itemSP.getChildren().add(doneHBox);
			}
//			if (!item.getAttachedUserToTask().equals("null")) {
//				itemSP.getChildren().add(doneHBox);
//			}
			
			setGraphic(itemSP);
		} else {
			setGraphic(null);
		}
	}
}
