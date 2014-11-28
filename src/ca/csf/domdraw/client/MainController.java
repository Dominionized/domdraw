package ca.csf.domdraw.client;

import ca.csf.simpleFx.SimpleFXController;
import ca.csf.simpleFx.dialogs.SimpleFXDialogChoiceSet;
import ca.csf.simpleFx.dialogs.SimpleFXDialogIcon;
import ca.csf.simpleFx.dialogs.SimpleFXDialogResult;
import ca.csf.simpleFx.dialogs.SimpleFXDialogs;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainController extends SimpleFXController {

    @FXML
    public Canvas drawingCanvas;
    public TextField portTextField;
    public TextField addressTextField;
    public GraphicsContext gc;
    public Button disconnectButton;
    public Button connectButton;
    private Shape shapeToDraw;
    private boolean drawingPending;
    private double drawingPendingX;
    private double drawingPendingY;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    @Override
    public void onLoadedStage() {

    }

    @FXML
    public void setLine() {
        shapeToDraw = Shape.LINE;
    }

    @FXML
    public void setSquare() {
        shapeToDraw = Shape.RECTANGLE;
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
                            // draw;line;0;0;25;25;
                            break;
                        case RECTANGLE:
                            gc.fillRect(x1, y1, width, height);
                            break;
                        case ELLIPSE:
                            gc.fillOval(x1, y1, width, height);
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

    public void connectToServer() {

        String address = addressTextField.getText();
        int port;
        try {
            port = Integer.parseInt(portTextField.getText());

            socket = new Socket(address, port);
            System.out.println("Connected to " + address + ":" + port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());
            disconnectButton.setDisable(false);
            connectButton.setDisable(true);

            Task waitForMethods = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (!socket.isClosed()) {
                        final String receivedString = String.valueOf(Character.toChars(input.read()));
                        System.out.println("SERVER : " + receivedString);
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                getSimpleFxStage().setTitle(receivedString);
                            }
                        });
                    }
                    return null;
                }
            };

            Thread th = new Thread(waitForMethods);
            th.run();

            initializeDrawingEvents();

        } catch (IOException e) {
            e.printStackTrace();
            SimpleFXDialogs.showMessageBox("Cannot connect.", "Address not found", SimpleFXDialogIcon.ERROR, SimpleFXDialogChoiceSet.OK, SimpleFXDialogResult.OK, getSimpleFxStage());
        } catch (NumberFormatException e) {
            SimpleFXDialogs.showMessageBox("Invalid port", "Invalid port number. Cannot connect.", SimpleFXDialogIcon.ERROR, SimpleFXDialogChoiceSet.OK, SimpleFXDialogResult.OK, this.getSimpleFxStage());
        }

    }

    public void disconnectFromServer() {
        try {
            socket.close();
            System.out.println("Disconnected.");
            disconnectButton.setDisable(true);
            connectButton.setDisable(false);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Shape {
        LINE("line"), RECTANGLE("rect"), ELLIPSE("ell");

        String shapeString;

        Shape(String shapeString) {

            this.shapeString = shapeString;
        }
    }
}
