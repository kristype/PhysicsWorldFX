package application;

import framework.PhysicsWorld;
import javafx.fxml.FXML;
import shapes.PhysicsRectangle;

public class AppController {
	
	@FXML
	private PhysicsWorld world;
	
	@FXML
	private PhysicsRectangle rectangle1;
	
	@FXML
	private PhysicsRectangle rectangle2;
	
	public AppController(){
		
	}
	
	@FXML
	private void initialize(){
		world.requestFocus();

		
		rectangle1.SetSpeed(10, 10);
	}
	
	public PhysicsWorld getWorld(){
		return world;
	}
	
}
