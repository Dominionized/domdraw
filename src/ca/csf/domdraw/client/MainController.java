package ca.csf.domdraw.client;

import ca.csf.simpleFx.SimpleFXController;
import com.sun.javafx.geom.Shape;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MainController extends SimpleFXController {

    @FXML
    public Canvas drawingCanvas;

    private Shape shapeToDraw;

    public GraphicsContext gc;

    @Override
    public void onLoadedStage(){
        gc = drawingCanvas.getGraphicsContext2D();
        drawingCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getX() + "," + event.getY());
                gc.setFill(Color.CHARTREUSE);
                gc.fillText("Salut", event.getX(), event.getY());
                event.consume();
            }
        });
    }

    @FXML
    public void addSquare() {

    }

    public void connectToServer(ActionEvent actionEvent) {
    }
}
