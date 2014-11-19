package ca.csf.domdraw_client;
	
import ca.csf.simpleFx.SimpleFXApplication;
import ca.csf.simpleFx.SimpleFXScene;
import ca.csf.simpleFx.SimpleFXStage;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends SimpleFXApplication {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		SimpleFXScene scene = new SimpleFXScene(this.getClass().getResource("Main.fxml"), (SimpleFXController) new MainController());
		SimpleFXStage stage = new SimpleFXStage("DomDraw", StageStyle.DECORATED, scene , this);
	}
}
