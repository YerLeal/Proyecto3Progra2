package proyecto3progra2;

import domain.Block;
import domain.Character;
import domain.FastCharacter;
import domain.FuriousCharacter;
import domain.Item;
import domain.SmartCharacter;
import file.MazeFile;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Proyecto3Progra2 extends Application implements Runnable {

    private int width;
    private final int HEIGTH = 720;
    private Canvas canvas;
    private Pane pane;
    private GraphicsContext gc;
    private Logica logica;
    private boolean bol = false;
    private Thread thread;
    private Character c1,c2,c3;
    private MazeFile mazeFile;
    private Item i1;

    @Override
    public void start(Stage primaryStage) {
        this.mazeFile = new MazeFile();
        logica = new Logica();
        primaryStage.setTitle("Laberinto");
        init(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage.resizableProperty().set(false);

        primaryStage.show();
    } // start

    public void init(Stage primaryStage) {
        thread = new Thread(this);

        if (logica.getDifficulty() == 1) {
            this.width = 1320;
        } else {
            this.width = 1360;
        }
        canvas = new Canvas(width, HEIGTH);

        Button btRun = new Button("Run");
        Button btSave = new Button("Save");

        btRun.relocate(400, 400);
        btSave.relocate(700, 400);

        btRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c1 = new FastCharacter(logica.getSize(), logica.ini());
                c2 = new FuriousCharacter(logica.getSize(), logica.ini());
                c3 = new SmartCharacter(logica.getSize(), logica.ini());
                i1=new Item(logica.getSize(), logica.ini3());
                thread.start();
                c1.start();
                c2.start();
                c3.start();
                i1.start();
                
            }
        });

        btSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    mazeFile.addMaze(logica.getMaze());
                    System.out.println("Se supone que ya lo guardó !!!!!!!!!!!");
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Proyecto3Progra2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        gc = canvas.getGraphicsContext2D();
        pane = new Pane(canvas);
        pane.getChildren().add(btRun);
        pane.getChildren().add(btSave);
        logica.createMaze();
        logica.drawMaze(gc);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() == canvas) {
                    logica.cambiarTipo((int) event.getX(), (int) event.getY(), gc);
                    logica.imprimirTipo((int) event.getX(), (int) event.getY());
                }
            }
        });

        Scene scene = new Scene(pane, width, HEIGTH);
        primaryStage.setScene(scene);
    } // init

    @Override
    public void run() {
        long start;
        long elapsed;
        long wait;
        int fps = 20;
        long time = 1000 / fps;
        try {
            while (true) {

                start = System.nanoTime();
                elapsed = System.nanoTime() - start;
                wait = time - elapsed / 1000000;

                draw(gc);

                Thread.sleep(wait);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Proyecto3Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void draw(GraphicsContext gc) throws InterruptedException {
        gc.clearRect(0, 0, width, HEIGTH);
        logica.drawMaze(gc);
        c1.draw(gc);
        c2.draw(gc);
        c3.draw(gc);
        i1.draw(gc);
    }

    public static void main(String[] args) {
        launch(args);
    } // main

}
