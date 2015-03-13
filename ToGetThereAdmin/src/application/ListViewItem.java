package application;


import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import database.UserAdminPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ListViewItem extends ListCell<UserAdminPanel>{
	
//	private File defaultImage = new File("/Users/edocohen/eclipse43/workspace/ToGetThereAdmin/src/misc/user.png");
	private File defaultImage		= new File("misc/user.png");
	private File assignTaskFile 	= new File("misc/assign_task.png");
	private File volDoneTaskFile	= new File("misc/great_job.png"); 
	public ListViewItem() {
		super();
	}
	
	@Override
	protected void updateItem(final UserAdminPanel item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			StackPane itemSP = new StackPane();
			HBox newItem = new HBox();
			newItem.setPrefHeight(50);
			
			ImageView profileImageView = new ImageView(new Image(defaultImage.toURI().toString(), 50, 50, false, true));
			ImageView doneTaskImageView = new ImageView(new Image(volDoneTaskFile.toURI().toString(), 50, 50, false, true));
			URL url;
			HttpURLConnection con;
			try {
				
				String urlString = "http://graph.facebook.com/" + item.getID() + "/picture?type=square";
//				System.out.println(item.getID());
				url = new URL(urlString);
				con = (HttpURLConnection) (url.openConnection());
				con.setInstanceFollowRedirects(false);
				con.connect();
				int responseCode = con.getResponseCode();
//				System.out.println(responseCode);
				String location = con.getHeaderField("Location");
//				System.out.println(location);
				if (responseCode == 302) {
					profileImageView = new ImageView(location);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (profileImageView != null) {
				newItem.getChildren().add(profileImageView);
			}
			VBox personInfo = new VBox();
			int age = 0;
			if (!item.getBirthday().equals("null")) { // parse age only if it exists...
				age = Integer.parseInt(item.getBirthday().split("\\.")[2]);
			}
			Label firstRow = new Label();
			if (age != 0) {
				firstRow	= new Label(item.getFirstName() + " " + item.getLastName() + ", " + (Main.year-age));	
			} else {
				firstRow	= new Label(item.getFirstName() + " " + item.getLastName());
			}
			personInfo.getChildren().add(firstRow);
			personInfo.getChildren().add(new Label(item.getLocation()));
			personInfo.getChildren().add(new Label(item.getPhone()));
			personInfo.setAlignment(Pos.CENTER_LEFT);
			personInfo.setPadding(new Insets(5));
			newItem.getChildren().add(personInfo);
			itemSP.getChildren().add(newItem);
			HBox assignButton = new HBox();
			assignButton.getChildren().add(new ImageView(new Image(assignTaskFile.toURI().toString(), 120, 45, false, true)));
			assignButton.setAlignment(Pos.CENTER_RIGHT);
			itemSP.getChildren().add(assignButton);
			if (Main.getCurrVolunteerStr().equals(item.getID())) {
				itemSP.getChildren().add(doneTaskImageView);
			}
			setGraphic(itemSP);
			
			assignButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					Main.volMap.put(Main.getSelectedItem(), Main.getLastSelectedUser());
					Main.getCurrentTask().get(Main.getSelectedItem()).setAttachedUserToTask(item.getID());
					Main.getCurrentTask().get(Main.getSelectedItem()).updateUserToDB();
					Main.setCurrVolunteerStr(item.getID());
					Main.setCurrVolID(Long.parseLong(item.getID()));
				}
			});
		} else {
			setGraphic(null);
		}
	}
}
