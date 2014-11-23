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

    public enum Shape {
        LINE, SQUARE, ELLIPSE
    }

    private Shape shapeToDraw;

    public GraphicsContext gc;

    private boolean drawingPending;
    private double drawingPendingX;
    private double drawingPendingY;

    @Override
    public void onLoadedStage() {
        drawingPending = false;
        gc = drawingCanvas.getGraphicsContext2D();
        drawingCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Mouse coordinates : " + event.getX() + "," + event.getY());
                if (drawingPending) {
                    gc.setFill(Color.RED);
                    switch (shapeToDraw) {
                        case LINE:
                            gc.strokeLine(drawingPendingX, drawingPendingY, event.getX(), event.getY());
                            break;
                        case SQUARE:
                            gc.fillRect(drawingPendingX, drawingPendingY, event.getX() - drawingPendingX, event.getY() - drawingPendingY);
                            break;
                        case ELLIPSE:
                            gc.fillOval(drawingPendingX, drawingPendingY, event.getX() - drawingPendingX, event.getY() - drawingPendingY);
                            break;

                    }
                    drawingPending = false;

                } else {
                    drawingPendingX = event.getX();
                    drawingPendingY = event.getY();
                    drawingPending = true;
                }
                event.consume();
            }
        });
    }

    @FXML
    public void setLine() {
        shapeToDraw = Shape.LINE;
    }

    @FXML
    public void setSquare() {
        shapeToDraw = Shape.SQUARE;
    }

    @FXML
    public void setEllipse() {
        shapeToDraw = Shape.ELLIPSE;
    }

    public void connectToServer(ActionEvent actionEvent) {
    }
}
