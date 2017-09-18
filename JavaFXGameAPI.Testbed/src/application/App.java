package application;
	
import framework.PhysicsGame;
import framework.PhysicsWorld;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	private AppController controller;
	PhysicsGame game;

	private void handleKey(KeyEvent keyEvent) {
		switch (keyEvent.getCode()) {
			case ESCAPE: {
				game.stopGame();
				break;
			}
			case SPACE: {
				game.startGame();
				break;
			}
			case BACK_SPACE: {
				//game.debugStep();
				break;
			}
			case DIGIT0: {

				break;
			}
			case LEFT: {
				controller.handleDirection(-100, 0);
				break;
			}
			case RIGHT: {
				controller.handleDirection(100, 0);
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

	@Override
	public void start(Stage primaryStage) throws Exception {
		try{

			FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("App.fxml"));
			PhysicsWorld world = fxmlLoader.load();
			controller = fxmlLoader.getController();

			Scene scene = new Scene(world);
			scene.setOnKeyPressed(this::handleKey);
			primaryStage.setScene(scene);
			primaryStage.show();

			game = new PhysicsGame();
			game.load(world);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
