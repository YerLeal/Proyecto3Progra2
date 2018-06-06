package proyecto3progra2;

import business.Logic;
import business.SharedBuffer;
import domain.Block;
import domain.Character;
import domain.FastCharacter;
import domain.FuriousCharacter;
import domain.Record;
import domain.SmartCharacter;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Proyecto3Progra2 extends Application implements Runnable {

    public String num = "";
    private int width = 1360;
    private final int HEIGTH = 720;
    private Canvas canvas;
    private Pane pane;
    private GraphicsContext gc;
    private Logic logic;
    private boolean itemCharge = false;
    private Thread thread;
    private Thread chronometer;
    private HBox hBox;
    private VBox vBox;
    private VBox vBox1;
    private javafx.scene.control.Label lbType;
    private javafx.scene.control.Label lbQuantity;
    private javafx.scene.control.Label lbName;
    private Text time = new Text();
    private ObservableList<String> listType, listDifficult;
    private Button btnSet;
    private Button btnPause;
    private Button btnStop;
    private Button btnName;
    private Button btnLogin;
    private TextField tfdQuantity;
    private TextField tfdName;
    private int quantityCharacters;
    private SharedBuffer buffer;
    private ArrayList<Character> characters = new ArrayList<>();
    private int initCont = 0;
    private Scene scene;
    private final Stage stage = new Stage();
    private String min = "", seg = "";
    private boolean flag = true;
    private TableView<Record> table;
    public static String timer="";
    
    private Runnable hilos = new Runnable() {
        @Override
        public void run() {
            ArrayList<Block> aux = logic.getStart();
            while (initCont < characters.size()) {

                if (aux.size() == 1) {
                    if (buffer.verifyStart(aux.get(0))) {
                        characters.get(initCont).setStarto(aux.get(0));
                        characters.get(initCont).setOrder(initCont);
                        buffer.getCharacters().add(characters.get(initCont));
                        characters.get(initCont).start();
                        initCont++;
                    }

                } else {
                    if (buffer.verifyStart(aux.get(0))) {
                        characters.get(initCont).setStarto(aux.get(0));
                        characters.get(initCont).setOrder(initCont);
                        buffer.getCharacters().add(characters.get(initCont));
                        characters.get(initCont).start();
                        initCont++;
                    } else if (buffer.verifyStart(aux.get(1))) {
                        characters.get(initCont).setStarto(aux.get(1));
                        characters.get(initCont).setOrder(initCont);
                        buffer.getCharacters().add(characters.get(initCont));
                        characters.get(initCont).start();
                        initCont++;
                    }

                }

            }
        }
    };
    
    private Runnable chronometerThread = new Runnable() {
        @Override
        public void run() {
            int minutes = 0, seconds = 0, thousandths = 0;
            while (flag) {
                try {
                    Thread.sleep(4);
                    thousandths += 4;
                    if (thousandths == 1000) {
                        thousandths = 0;
                        seconds += 1;
                        if (seconds == 60) {
                            seconds = 0;
                            minutes++;
                        }
                    }
                    if (minutes < 10) {
                        min = "0" + minutes;
                    } else {
                        min = String.valueOf(minutes);
                    }
                    if (seconds < 10) {
                        seg = "0" + seconds;
                    } else {
                        seg = String.valueOf(seconds);
                    }
                    timer=minutes + ":" + seg;
                    time.setText(minutes + ":" + seg);
                } catch (Exception e) {
                    time.setText("00:00:000");
                }
            }
        }
    };

    private Runnable tableUpdater = new Runnable() {
        @Override
        public void run() {
            while (true) {
                table.setItems(buffer.getRecords());
            }
        }

    };

    @Override
    public void start(Stage primaryStage) {
        table = new TableView<>();

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
        this.hBox = new HBox(10);
        this.vBox = new VBox(10);
        hBox.setSpacing(10);
        vBox.setSpacing(10);
        chronometer = new Thread(chronometerThread);
        btnSet = new Button("Start Simulation");
        btnLogin = new Button("Set the name and Characters");
        btnStop = new Button("Stop Threads");
        btnPause = new Button("Pause Characters");
        btnName = new Button("Set Player Name");
        btnSet.setDisable(true);
        btnPause.setDisable(true);
        btnStop.setDisable(true);

        tfdQuantity = new TextField();

        tfdName = new TextField();
        this.lbType = new javafx.scene.control.Label();
        this.lbQuantity = new javafx.scene.control.Label("Quantity of Characters");
        this.lbName = new javafx.scene.control.Label("Name of Player");
        //this.tiempo.setText("jj");

        listType = FXCollections.observableArrayList();//para el combobox
        listType.addAll("Fast", "Furious", "Smart");//opciones del combobox
        ComboBox<String> cbxType = new ComboBox<>(listType);//
        cbxType.setPromptText("Type of Character");
        listDifficult = FXCollections.observableArrayList();//para el combobox
        listDifficult.addAll("Easy", "Medium", "Hard");//opciones del combobox
        ComboBox<String> cbxDifficult = new ComboBox<>(listDifficult);//
        cbxDifficult.setPromptText("Choose the Difficult");
        thread = new Thread(this);
        //thread.start();
        //tableColumn
        TableColumn tcName = new TableColumn("Name");
        tcName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        TableColumn tcType = new TableColumn("Type");
        tcType.setCellValueFactory(new PropertyValueFactory<>("characterType"));
        TableColumn tcTime = new TableColumn("Time");
        tcTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        table.getColumns().addAll(tcName, tcType, tcTime);
        this.canvas = new Canvas(1120, HEIGTH);

        Button btRun = new Button("Run");
        Button btItem = new Button("Item");

        btRun.relocate(400, 400);
        btItem.relocate(700, 400);
        time.relocate(1000, 600);

        btRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Block> finish = logic.getFinish();
                for (int i = 0; i < 5; i++) {

                    if (i < 0) {
                        characters.add(new SmartCharacter(logic.getSize(), buffer, finish, tfdName.getText()));
                    } else if (i < 5) {
                        characters.add(new FastCharacter(logic.getSize(), buffer, finish, tfdName.getText()));
                    } else {
                        characters.add(new FuriousCharacter(logic.getSize(), buffer, finish, tfdName.getText()));
                    }
                }
                thread.start();
                logic.startItems();
                new Thread(hilos).start();
                chronometer = new Thread(chronometerThread);
                chronometer.start();
                new Thread(tableUpdater).start();
            }
        });

        btItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemCharge = !itemCharge;
            }
        });
        btnSet.setOnAction((ActionEvent t) -> {
            thread.start();
            logic.startItems();
            new Thread(hilos).start();
            chronometer = new Thread(chronometerThread);
            chronometer.start();

        });
        btnLogin.setOnAction((ActionEvent t) -> {
            stage.show();
        });
        btnStop.setOnAction((ActionEvent t) -> {
            flag = false;
        });
        cbxType.setOnAction((ActionEvent e) -> {
            System.out.println(tfdQuantity.getText());
            for (int i = 0; i < Integer.parseInt(tfdQuantity.getText()); i++) {
                if (cbxType.getValue().equalsIgnoreCase("Fast")) {
                    characters.add(new FastCharacter(logic.getSize(), buffer, logic.getFinish(), tfdName.getText()));

                    this.lbType.setText(tfdName.getText() + " Fast");
                } else if (cbxType.getValue().equalsIgnoreCase("Furious")) {
                    characters.add(new FuriousCharacter(logic.getSize(), buffer, logic.getFinish(), tfdName.getText()));

                    this.lbType.setText(tfdName.getText() + " Furious");
                } else {
                    characters.add(new SmartCharacter(logic.getSize(), buffer, logic.getFinish(), tfdName.getText()));
                    this.lbType.setText(tfdName.getText() + " Smart");
                }
                System.out.println("num");
            }
            quantityCharacters++;
            tfdQuantity.setText("");
            btnSet.setDisable(false);
            btnPause.setDisable(false);
            btnStop.setDisable(false);
        });
        cbxDifficult.setOnAction((ActionEvent e) -> {

            if (cbxDifficult.getValue().equalsIgnoreCase("Easy")) {
                logic = new Logic(1);
                this.logic.setDifficulty(1);
            } else if (cbxDifficult.getValue().equalsIgnoreCase("Medium")) {
                logic = new Logic(2);
                this.logic.setDifficulty(2);
            } else {
                logic = new Logic(3);
                this.logic.setDifficulty(3);
            }

            logic.createMaze();
            logic.drawMaze(gc);
            cbxDifficult.setVisible(false);
        });

        gc = canvas.getGraphicsContext2D();
        pane = new Pane(canvas);
        pane.getChildren().add(btRun);
        pane.getChildren().add(btItem);
        pane.getChildren().add(time);

        this.vBox.getChildren().addAll(cbxDifficult, this.lbName, this.btnLogin, this.btnSet, this.btnPause, this.btnStop, time, table);
        this.hBox.getChildren().add(pane);
        this.hBox.getChildren().add(vBox);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() == canvas && !itemCharge) {
                    logic.changeTypeBlock((int) event.getX(), (int) event.getY(), gc);
//                    logica.imprimirTipo((int) event.getX(), (int) event.getY());
                } else {
                    if (thread.isAlive()) {
                        logic.addItem((int) event.getX(), (int) event.getY(), buffer, true, gc);
                    } else {
                        logic.addItem((int) event.getX(), (int) event.getY(), buffer, false, gc);
                    }
                }
            }
        });

        Scene scene = new Scene(hBox, width, HEIGTH);
        primaryStage.setScene(scene);
        vBox1 = new VBox(10);
        vBox1.getChildren().addAll(lbQuantity, tfdQuantity, lbType, cbxType, lbName, tfdName, btnName);

        scene = new Scene(vBox1, 400, 400);
        stage.setTitle("Login Player");
        stage.setScene(scene);
    } // init

    @Override
    public void run() {
        long start;
        long elapsed;
        long wait;
        int fps = 20;
        long time = 1000 / fps;

        try {
            while (flag) {
                start = System.nanoTime();
                elapsed = System.nanoTime() - start;
                wait = time - elapsed / 1000000;
                draw(gc);
                Thread.sleep(wait);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Proyecto3Progra2.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // run

    public void draw(GraphicsContext gc) throws InterruptedException {
        gc.clearRect(0, 0, 1120, HEIGTH);
        logic.drawMaze(gc);
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).isAlive()) {
                characters.get(i).draw(gc);
            }
        }
    } // draw

    public ObservableList<Character> getTableData() {
        ArrayList array = new ArrayList();

        for (int i = 0; i < characters.size(); i++) {
            array.add(characters.get(i));
        }
        ObservableList<Character> listData = FXCollections.observableArrayList(array);
        return listData;
    } // getTableData

    public static void main(String[] args) {
        launch(args);
    } // main

} // end class
