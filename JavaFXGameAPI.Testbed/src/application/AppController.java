package application;

import framework.PhysicsWorld;
import javafx.fxml.FXML;
import framework.nodes.PhysicsRectangle;

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
		
	}
	
	public PhysicsWorld getWorld(){
		return world;
	}

	public void handleDirection(int i, int j) {
		rectangle1.ApplyForce(i, j);
	}
	
}
