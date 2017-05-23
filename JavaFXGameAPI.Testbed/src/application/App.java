package application;
	
import java.io.IOException;

import framework.PhysicsGameApp;
import framework.PhysicsWorld;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;

public class App extends PhysicsGameApp {
	
	public static void main(String[] args) {
		launch(args);
	}

	private AppController controller;

	@Override
	public PhysicsWorld Load() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("App.fxml"));
	    PhysicsWorld world = fxmlLoader.load();
	    controller = (AppController) fxmlLoader.getController();
	    return world;
	}
	
	@Override
	protected void handleKey(KeyEvent keyEvent) {
		switch (keyEvent.getCode()) {
    	case ESCAPE: {
    		stopGame();
    		break;
    	}
    	case SPACE: {
    		startGame();
    		break;
    	}
    	case BACK_SPACE: {
    		debugStep();
    		break;
    	}
    	case LEFT: {
    		controller.handleDirection(-100,0);
    		break;
    	}
    	case RIGHT: {
    		controller.handleDirection(100,0);
    		break;
    	}
    	case UP: {
    		controller.handleDirection(0, 100);
    		break;
    	}
    	case DOWN: {
    		controller.handleDirection(0, -100);
    		break;
    	}
    	default:
    	}
	}
}
