package ca.csf.domdraw.client;

import ca.csf.simpleFx.SimpleFXApplication;
import ca.csf.simpleFx.SimpleFXScene;
import ca.csf.simpleFx.SimpleFXStage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class Main extends SimpleFXApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        try {
            SimpleFXScene scene = new SimpleFXScene(this.getClass().getResource("Main.fxml"), new MainController());
            SimpleFXStage primaryStage = new SimpleFXStage("DomDraw", StageStyle.DECORATED, scene, this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
