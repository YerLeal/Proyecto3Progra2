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
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

    private final int WIDTH = 1360, HEIGTH = 720;
    private final Stage stage = new Stage();

    private Canvas canvas;
    private Pane pane;
    private GraphicsContext gc;
    private Thread thread, chronometer;
    private Label lbType, lbQuantity, lbName;
    private TextField tfdQuantity, tfdName;
    private Button btnSet, btnPause, btnStop, btnName, btnLogin, btnRun, btnItem;
    private ObservableList<String> listType, listDifficult;
    private HBox hBox;
    private VBox vBox, vBox1;
    private ComboBox<String> cbxDifficult, cbxType;
    private TableView<Record> table;

    private SharedBuffer buffer;
    private String num = "", min = "", seg = "";

    private Logic logic;
    private boolean itemCharge = false, flag = true;
    private Text time;
    private int quantityCharacters, initCont = 0;

    private ArrayList<Character> characters;
//    private Scene scene;/////////////////////////////////// :v

    public static String timer = "";

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
                    timer = minutes + ":" + seg;
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
        primaryStage.setTitle("The Maze of Threads");
        init(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        this.buffer = new SharedBuffer(new ArrayList<Character>(), new ArrayList<>());
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    public void init(Stage primaryStage) {
        // Canvas
        this.canvas = new Canvas(1120, HEIGTH);

        // Pane
        this.pane = new Pane(this.canvas);

        // GraphicsContext
        this.gc = this.canvas.getGraphicsContext2D();

        // Threads
        this.chronometer = new Thread(this.chronometerThread);
        this.thread = new Thread(this);
        this.characters = new ArrayList<>();

        // Label
        this.lbType = new Label();
        this.lbQuantity = new Label("Quantity of Characters");
        this.lbName = new Label("Name of Player");

        // TextField
        this.tfdQuantity = new TextField();
        this.tfdName = new TextField();

        // Buttons
        this.btnRun = new Button("Run");
        this.btnItem = new Button("Item");
        this.btnSet = new Button("Start Simulation");
        this.btnLogin = new Button("Set the name and Characters");
        this.btnStop = new Button("Stop Threads");
        this.btnPause = new Button("Pause Characters");
        this.btnName = new Button("Set Player Name");

        // ObservableList
        this.listDifficult = FXCollections.observableArrayList();
        this.listDifficult.addAll("Easy", "Medium", "Hard");
        this.listType = FXCollections.observableArrayList();
        this.listType.addAll("Fast", "Furious", "Smart");

        // HBox
        this.hBox = new HBox(10);
        this.hBox.setSpacing(10);

        // VBox
        this.vBox = new VBox(10);
        this.vBox.setSpacing(10);
        vBox1 = new VBox(10);

        // ComboBox
        this.cbxDifficult = new ComboBox<>(this.listDifficult);
        this.cbxDifficult.setPromptText("Choose the Difficult");
        this.cbxType = new ComboBox<>(this.listType);
        this.cbxType.setPromptText("Type of Character");

        // Text
        this.time = new Text();

        // Table
        this.table = new TableView<>();
        TableColumn tcName = new TableColumn("Name");
        TableColumn tcType = new TableColumn("Type");
        TableColumn tcTime = new TableColumn("Time");
        tcName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("characterType"));
        tcTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.table.getColumns().addAll(tcName, tcType, tcTime);

        this.btnRun.setOnAction(actionButtons);
        this.btnSet.setOnAction(actionButtons);
        this.btnItem.setOnAction(actionButtons);
        this.btnLogin.setOnAction(actionButtons);
        this.btnStop.setOnAction(actionButtons);
        this.cbxDifficult.setOnAction(actionButtons);
        this.cbxType.setOnAction(actionButtons);

        this.btnSet.setDisable(true);
        this.btnPause.setDisable(true);
        this.btnStop.setDisable(true);

        this.btnRun.relocate(400, 400);
        this.btnItem.relocate(700, 400);
        this.time.relocate(1000, 600);

        // getChildren
        this.pane.getChildren().add(this.btnRun);
        this.pane.getChildren().add(this.btnItem);
        this.pane.getChildren().add(this.time);
        this.vBox.getChildren().addAll(this.cbxDifficult, this.lbName, this.btnLogin, this.btnSet, this.btnPause, this.btnStop, this.time, this.table);
        this.hBox.getChildren().add(this.pane);
        this.hBox.getChildren().add(this.vBox);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() == canvas && !itemCharge) {
                    logic.changeTypeBlock((int) event.getX(), (int) event.getY(), gc);
                } else {
                    if (thread.isAlive()) {
                        logic.addItem((int) event.getX(), (int) event.getY(), buffer, true, gc);
                    } else {
                        logic.addItem((int) event.getX(), (int) event.getY(), buffer, false, gc);
                    }
                }
            }
        });

        // :O
        Scene scene = new Scene(hBox, WIDTH, HEIGTH);
        primaryStage.setScene(scene);
        vBox1.getChildren().addAll(lbQuantity, tfdQuantity, lbType, cbxType, lbName, tfdName, btnName);
        scene = new Scene(vBox1, 400, 400);
        stage.setTitle("Login Player");
        stage.setScene(scene);
    } // init

    EventHandler<ActionEvent> actionButtons = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            if (e.getSource() == cbxDifficult) {
                if (cbxDifficult.getValue().equalsIgnoreCase("Easy")) {
                    logic = new Logic(1);
                    logic.setDifficulty(1);
                } else if (cbxDifficult.getValue().equalsIgnoreCase("Medium")) {
                    logic = new Logic(2);
                    logic.setDifficulty(2);
                } else {
                    logic = new Logic(3);
                    logic.setDifficulty(3);
                }
                logic.createMaze();
                logic.drawMaze(gc);
            } else if (e.getSource() == cbxType) {
                for (int i = 0; i < Integer.parseInt(tfdQuantity.getText()); i++) {
                    if (cbxType.getValue().equalsIgnoreCase("Fast")) {
                        characters.add(new FastCharacter(logic.getSize(), buffer, logic.getFinish(), tfdName.getText()));
                        lbType.setText(tfdName.getText() + " Fast");
                    } else if (cbxType.getValue().equalsIgnoreCase("Furious")) {
                        characters.add(new FuriousCharacter(logic.getSize(), buffer, logic.getFinish(), tfdName.getText()));
                        lbType.setText(tfdName.getText() + " Furious");
                    } else {
                        characters.add(new SmartCharacter(logic.getSize(), buffer, logic.getFinish(), tfdName.getText()));
                        lbType.setText(tfdName.getText() + " Smart");
                    }
                }
                quantityCharacters++;
                tfdQuantity.setText("");
                btnSet.setDisable(false);
                btnPause.setDisable(false);
                btnStop.setDisable(false);
            } else if (e.getSource() == btnRun) {
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
            } else if (e.getSource() == btnSet) {
                thread.start();
                logic.startItems();
                new Thread(hilos).start();
                chronometer = new Thread(chronometerThread);
                chronometer.start();
            } else if (e.getSource() == btnItem) {
                itemCharge = !itemCharge;
            } else if (e.getSource() == btnLogin) {
                stage.show();
            } else if (e.getSource() == btnStop) {
                flag = false;
            }
        }
    };

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
            } // if
        } // for i
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
