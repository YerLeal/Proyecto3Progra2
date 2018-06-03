package proyecto3progra2;

import business.Logica;
import business.SharedBuffer;
import domain.Block;
import domain.Character;
import domain.FastCharacter;
import domain.FuriousCharacter;
import domain.Item;
import domain.SmartCharacter;
import file.MazeFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Proyecto3Progra2 extends Application implements Runnable {

    private int width = 1360;
    private final int HEIGTH = 720;
    private int canvasWidth;
    private Canvas canvas;
    private Pane pane;
    private GraphicsContext gc;
    private Logica logica;
    private boolean bol = false;
    private Thread thread;
    private Character c1, c2, c3;
    private MazeFile mazeFile;
    private Item i1;
    private HBox hbox;
    private VBox vbox;
    private javafx.scene.control.Label lblType;
    private javafx.scene.control.Label lblQuantity;
    private ObservableList<String> listType, listDifficult;
    private Button btnSet;
    private Button btnPause;
    private Button btnStop;
    private TextField tfdSize;
    private TextField tfdType;
    private int cantP;
    private SharedBuffer buffer;
    private ArrayList<Character> lista = new ArrayList<>();
    private int initCont = 0;
    
    private Runnable hilos = new Runnable() {
        @Override
        public void run() {
            ArrayList<Block> aux = logica.getStart();
            while (initCont < lista.size()) {

                if (aux.size() == 2) {
                    if (buffer.verifyStart(aux.get(0))) {
                        lista.get(initCont).setStarto(aux.get(0));
                        lista.get(initCont).setOrder(initCont);
                        buffer.getCharacters().add(lista.get(initCont));
                        lista.get(initCont).start();
                        initCont++;
                    } else if (buffer.verifyStart(aux.get(1))) {
                        lista.get(initCont).setStarto(aux.get(1));
                        lista.get(initCont).setOrder(initCont);
                        buffer.getCharacters().add(lista.get(initCont));
                        lista.get(initCont).start();
                        initCont++;
                    }

                }

            }
        }
    };

    @Override
    public void start(Stage primaryStage) {
        mazeFile = new MazeFile();
        logica = new Logica();
        primaryStage.setTitle("The Maze of Threads");
        init(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        buffer = new SharedBuffer(new ArrayList<Character>(), new ArrayList<>());
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    public void init(Stage primaryStage) {
        this.hbox = new HBox(10);
        this.vbox = new VBox(10);
        hbox.setSpacing(10);
        vbox.setSpacing(10);
        btnSet = new Button("Start Simulation");
        btnSet.setDisable(false);

        btnStop = new Button("Stop Threads");
        btnPause = new Button("Pause Characters");
        tfdSize = new TextField();
        tfdType = new TextField();
        tfdSize = new TextField();
        this.lblType = new javafx.scene.control.Label();
        this.lblQuantity = new javafx.scene.control.Label("Choose the quantity of characters");
        listType = FXCollections.observableArrayList();//para el combobox
        listType.addAll("Fast", "Furious", "Smart");//opciones del combobox
        ComboBox<String> cbx = new ComboBox<>(listType);//
        cbx.setPromptText("Choose the type of character");
        listDifficult = FXCollections.observableArrayList();//para el combobox
        listDifficult.addAll("Easy", "Medium", "Hard");//opciones del combobox
        ComboBox<String> cbxD = new ComboBox<>(listDifficult);//
        cbxD.setPromptText("Choose the Difficult");
        thread = new Thread(this);
        //thread.start();

        this.canvasWidth = 1120;
        this.canvas = new Canvas(canvasWidth, HEIGTH);

        Button btRun = new Button("Run");
        Button btSave = new Button("Save");

        btRun.relocate(400, 400);
        btSave.relocate(700, 400);

        btRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                for (int i = 0; i < 10; i++) {

                    if (i < 3) {
                        lista.add(new SmartCharacter(logica.getSize(), buffer));
                    } else if (i < 6) {
                        lista.add(new FastCharacter(logica.getSize(), buffer));
                    } else {
                        lista.add(new FuriousCharacter(logica.getSize(), buffer));
                    }
                }

                thread.start();
                new Thread(hilos).start();

            }
        });

//        btSave.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                try {
//                    mazeFile.addMaze(logica.getMaze());
//                    System.out.println("Guardo");
//                } catch (IOException | ClassNotFoundException ex) {
//                    Logger.getLogger(Proyecto3Progra2.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
        btnSet.setOnAction((ActionEvent t) -> {
        });
        btnStop.setOnAction((ActionEvent t) -> {
            try {
                thread.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Proyecto3Progra2.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        cbx.setOnAction((ActionEvent e) -> {

            if (cbx.getValue().equalsIgnoreCase("Fast")) {
                this.lblType.setText("Fast");
            } else if (cbx.getValue().equalsIgnoreCase("Furious")) {
                this.lblType.setText("Furious");
            } else {
                this.lblType.setText("Smart");
            }

            if (cantP == Integer.parseInt(tfdSize.getText())) {
                //btnSet.setVisible(true);
                btnSet.setDisable(true);
            }
            cantP++;
        });
        cbxD.setOnAction((ActionEvent e) -> {

            if (cbxD.getValue().equalsIgnoreCase("Easy")) {
                this.lblType.setText("Easy");
            } else if (cbx.getValue().equalsIgnoreCase("Medium")) {
                this.lblType.setText("Medium");
            } else {
                this.lblType.setText("Hard");
            }

            if (cantP == Integer.parseInt(tfdSize.getText())) {
                //btnSet.setVisible(true);
                btnSet.setDisable(true);
            }
            cantP++;
        });

        gc = canvas.getGraphicsContext2D();
        pane = new Pane(canvas);
        pane.getChildren().add(btRun);
//        pane.getChildren().add(btSave);
        logica.createMaze();
        logica.drawMaze(gc);
        this.vbox.getChildren().addAll(this.lblQuantity, tfdSize, cbx, cbxD, this.lblType, this.btnSet, this.btnStop);
        this.hbox.getChildren().add(pane);
        this.hbox.getChildren().add(vbox);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() == canvas) {
                    logica.cambiarTipo((int) event.getX(), (int) event.getY(), gc);
                    logica.imprimirTipo((int) event.getX(), (int) event.getY());
                }
            }
        });

        Scene scene = new Scene(hbox, width, HEIGTH);
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
        gc.clearRect(0, 0, canvasWidth, HEIGTH);
        logica.drawMaze(gc);
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).isAlive()) {
                lista.get(i).draw(gc);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    } // main

} // end class
