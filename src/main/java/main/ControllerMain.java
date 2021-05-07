package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import main.timer.MyTimer;
import points2d.Vec2df;
import points2d.Vec2di;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMain implements Initializable {

    private final int MAX_DATA = 100;

    @FXML
    public Label fpsLabel;

    @FXML
    public Label labelGrass;

    @FXML
    public Label labelSheep;

    @FXML
    public Label labelFoxes;

    @FXML
    public Canvas canvas;

    @FXML
    public ScatterChart<String, Number> graph;

    private XYChart.Series<String, Number> grass;

    private XYChart.Series<String, Number> sheep;

    private XYChart.Series<String, Number> foxes;

    private Game game;

    private MyTimer timer;

    private Vec2df offset = new Vec2df();

    float scale = 1.0f;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCanvasEvents();

        game = new Game(
                new Vec2di(200, 100),
                canvas.getWidth(),
                canvas.getHeight(),
                new Image(getClass().getResourceAsStream("/img/grass.png")),
                new Image(getClass().getResourceAsStream("/img/sheep.png")),
                new Image(getClass().getResourceAsStream("/img/fox.png"))
        );

        game.initialize();
        game.draw(canvas.getGraphicsContext2D(), offset, scale);

        setAllGraphParameters();

        timer = new MyTimer() {
            private int count = 0;
            @Override
            protected void update(float elapsedTime) {
                game.update(elapsedTime);
                game.draw(canvas.getGraphicsContext2D(), offset, scale);
                if ( accumulatedTime >= 1000000000L ) {
                    count++;
                    updateXYChartData(grass, count +"s", game.getField().getLiveTiles());
                    updateXYChartData(sheep, count +"s", 30 * game.getSheep().getPopulation().size());
                    updateXYChartData(foxes, count + "s", 30 * game.getFoxes().getPopulation().size());
                }
            }
        };

        fpsLabel.textProperty().bind(timer.getFpsText());
        labelGrass.textProperty().bind(game.getField().getText());
        labelSheep.textProperty().bind(game.getSheep().getText());
        labelFoxes.textProperty().bind(game.getFoxes().getText());
    }

    private void setCanvasEvents() {
        canvas.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            if ( deltaY < 0 ) {
                scale *= 0.95;
            }
            if ( deltaY > 0 ) {
                scale *= 1.05;
            }
        });

        canvas.setOnMouseClicked(event -> {
            offset.setX((float)event.getX());
            offset.setY((float)event.getY());
        });
    }

    private void setAllGraphParameters() {
        grass = new XYChart.Series<>();
        grass.setName("Plantas");
        sheep = new XYChart.Series<>();
        sheep.setName("Ovejas");
        foxes = new XYChart.Series<>();
        foxes.setName("Zorros");

        graph.getXAxis().setAnimated(false);
        graph.getXAxis().setLabel("Tiempo (segundos)");
        graph.getYAxis().setAnimated(false);
        graph.getYAxis().setLabel("Número individuos");
        graph.getData().add(grass);
        graph.getData().add(sheep);
        graph.getData().add(foxes);
    }

    private void updateXYChartData(XYChart.Series<String, Number> data, String xValue, Number yValue) {
        data.getData().add(new XYChart.Data<>(xValue, yValue));
        if ( data.getData().size() > MAX_DATA ) {
            data.getData().remove(0);
        }
    }

    @FXML
    public void start() {
        timer.start();
    }

    @FXML
    public void stop() {
        timer.stop();
    }

    @FXML
    public void restart() {
        game.initialize();
    }

}
