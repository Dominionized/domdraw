package ca.csf.domdraw.client;

import ca.csf.simpleFx.SimpleFXController;
import ca.csf.simpleFx.dialogs.SimpleFXDialogChoiceSet;
import ca.csf.simpleFx.dialogs.SimpleFXDialogIcon;
import ca.csf.simpleFx.dialogs.SimpleFXDialogResult;
import ca.csf.simpleFx.dialogs.SimpleFXDialogs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class MainController extends SimpleFXController {

    @FXML
    public Canvas drawingCanvas;
    public TextField portTextField;
    public TextField addressTextField;
    public GraphicsContext gc;
    public Button disconnectButton;
    private Shape shapeToDraw;
    private boolean drawingPending;
    private double drawingPendingX;
    private double drawingPendingY;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    @Override
    public void onLoadedStage() {

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

    private void initializeDrawingEvents() {
        drawingPending = false;
        gc = drawingCanvas.getGraphicsContext2D();
        drawingCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Mouse coordinates : " + event.getX() + "," + event.getY());
                if (drawingPending) {

                    Method methodToSend = null;

                    gc.setFill(Color.RED);
                    double x1;
                    double width;
                    double y1;
                    double height;
                    if (event.getX() > drawingPendingX) {
                        width = event.getX() - drawingPendingX;
                        x1 = drawingPendingX;
                    } else {
                        x1 = event.getX();
                        width = drawingPendingX - event.getX();
                    }
                    if (event.getY() > drawingPendingY) {
                        height = event.getY() - drawingPendingY;
                        y1 = drawingPendingY;
                    } else {
                        y1 = event.getY();
                        height = drawingPendingY - event.getY();
                    }
                    switch (shapeToDraw) {
                        case LINE:
                            gc.strokeLine(drawingPendingX, drawingPendingY, event.getX(), event.getY());
                            try {
                                methodToSend = GraphicsContext.class.getMethod("strokeLine", double.class, double.class, double.class, double.class);
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                            break;
                        case SQUARE:
                            gc.fillRect(x1, y1, width, height);
                            try {
                                methodToSend = GraphicsContext.class.getMethod("fillRect");
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                            break;
                        case ELLIPSE:
                            gc.fillOval(x1, y1, width, height);
                            try {
                                methodToSend = GraphicsContext.class.getMethod("fillOval", double.class, double.class, double.class, double.class);
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    try {
                        objectOutputStream.writeObject(methodToSend);
                    } catch (IOException e) {
                        e.printStackTrace();
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

    public void connectToServer(ActionEvent actionEvent) {

        String address = addressTextField.getText();
        int port = 0;
        try {
            port = Integer.parseInt(portTextField.getText());

            socket = new Socket(address, port);
            System.out.println("Connected to " + address + ":" + port);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            disconnectButton.setDisable(false);

        } catch (IOException e) {
            e.printStackTrace();
            SimpleFXDialogs.showMessageBox("Cannot connect.", "Address not found", SimpleFXDialogIcon.ERROR, SimpleFXDialogChoiceSet.OK, SimpleFXDialogResult.OK, getSimpleFxStage());
            initializeDrawingEvents();
        } catch (NumberFormatException e) {
            SimpleFXDialogs.showMessageBox("Invalid port", "Invalid port number. Cannot connect.", SimpleFXDialogIcon.ERROR, SimpleFXDialogChoiceSet.OK, SimpleFXDialogResult.OK, this.getSimpleFxStage());
        }

    }

    public void disconnectFromServer(ActionEvent actionEvent) {
        try {
            socket.close();
            System.out.println("Disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Shape {
        LINE, SQUARE, ELLIPSE
    }
}
