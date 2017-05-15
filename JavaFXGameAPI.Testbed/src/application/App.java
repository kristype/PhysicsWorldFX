package application;
	
import framework.PhysicsWorld;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class App extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("App.fxml"));
		    

		    Pane root = fxmlLoader.load();
		    AppController controller = (AppController) fxmlLoader.getController();
		    PhysicsWorld world = controller.getWorld();
		    
		    Scene scene = new Scene(root);
		    scene.setOnKeyPressed(v -> {
				switch (v.getCode()) {
					case ESCAPE: {
						world.Stop();
						break;
					}
					case SPACE: {
						world.Start();
						break;
					}
				default:
					break;
				}
			});
		    
			primaryStage.setScene(scene);
		    primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
