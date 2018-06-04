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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private javafx.scene.control.Label lblName;
    private ObservableList<String> listType, listDifficult;
    private Button btnSet;
    private Button btnPause;
    private Button btnStop;
    private Button btnName;
    private TextField tfdQuantity;
    private TextField tfdName;
    private int cantP;
    private SharedBuffer buffer;
    private ArrayList<Character> lista = new ArrayList<>();
    private int initCont = 0;
    private boolean flag=true;
    private TableView<Character> table;
    
    private Runnable hilos = new Runnable() {
        @Override
        public void run() {
            ArrayList<Block> aux = logica.getStart();
            while (initCont < lista.size()) {

                if (aux.size() == 1) {
                    if (buffer.verifyStart(aux.get(0))) {
                        lista.get(initCont).setStarto(aux.get(0));
                        lista.get(initCont).setOrder(initCont);
                        buffer.getCharacters().add(lista.get(initCont));
                        lista.get(initCont).start();
                        initCont++;
                    }

                }else {
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
        table=new TableView<>();

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
        vbox.setSpacing(1);
        btnSet = new Button("Start Simulation");
        
        btnStop = new Button("Stop Threads");
        btnPause = new Button("Pause Characters");
        btnName = new Button("Set Player Name");
        
        tfdQuantity = new TextField();
        //tfdType = new TextField();
        tfdName = new TextField();
        this.lblType = new javafx.scene.control.Label();
        this.lblQuantity = new javafx.scene.control.Label("Quantity of Characters");
        this.lblName = new javafx.scene.control.Label("Name of Player");
        
        listType = FXCollections.observableArrayList();//para el combobox
        listType.addAll("Fast", "Furious", "Smart");//opciones del combobox
        ComboBox<String> cbx = new ComboBox<>(listType);//
        cbx.setPromptText("Type of Character");
        listDifficult = FXCollections.observableArrayList();//para el combobox
        listDifficult.addAll("Easy", "Medium", "Hard");//opciones del combobox
        ComboBox<String> cbxD = new ComboBox<>(listDifficult);//
        cbxD.setPromptText("Choose the Difficult");
        thread = new Thread(this);
        //thread.start();
        //tableColumn
        TableColumn name= new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn type= new TableColumn("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn position= new TableColumn("Position");
        position.setCellValueFactory(new PropertyValueFactory<>("position"));
        ObservableList<Character> datos=getTableData();
        table.setItems(datos);
        table.getColumns().addAll(name, type, position);
        
        
        this.canvasWidth = 1120;
        this.canvas = new Canvas(canvasWidth, HEIGTH);

        Button btRun = new Button("Run");
        Button btItem=new Button("Item");

        btRun.relocate(400, 400);
        btItem.relocate(700, 400);
        
        
        btRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Block> finish=logica.getFinish();
                for (int i = 0; i < 60; i++) {

                    if (i < 20) {
                        lista.add(new SmartCharacter(logica.getSize(), buffer,finish));
                    } else if (i < 40) {
                        lista.add(new FastCharacter(logica.getSize(), buffer,finish));
                    } else {
                        lista.add(new FuriousCharacter(logica.getSize(), buffer,finish));
                    }
                }

                thread.start();
;
                new Thread(hilos).start();

            }
        });

        btItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
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
                for (int i = 0; i < Integer.parseInt(tfdQuantity.getText()); i++) {
                    lista.add(new FastCharacter(logica.getSize(), buffer,logica.getFinish()));
                }
                this.lblType.setText("Fast");
            } else if (cbx.getValue().equalsIgnoreCase("Furious")) {
                for (int i = 0; i < Integer.parseInt(lblQuantity.getText()); i++) {
                    lista.add(new FuriousCharacter(logica.getSize(), buffer,logica.getFinish()));
                }
                this.lblType.setText("Furious");
            } else {
                for (int i = 0; i < Integer.parseInt(lblQuantity.getText()); i++) {
                    lista.add(new SmartCharacter(logica.getSize(), buffer,logica.getFinish()));
                }
                this.lblType.setText("Smart");
            }
            
            if (cantP == Integer.parseInt(tfdQuantity.getText())) {
                //btnSet.setVisible(true);
                btnSet.setDisable(true);
            }
            cantP++;
            tfdQuantity.setText("");
        });
        cbxD.setOnAction((ActionEvent e) -> {
            
            if (cbxD.getValue().equalsIgnoreCase("Easy")) {
                logica = new Logica(1);
                this.logica.setDifficulty(1);
                //setNum(1);
                
            } else if (cbxD.getValue().equalsIgnoreCase("Medium")) {
                logica = new Logica(2);
                this.logica.setDifficulty(2);
            } else {
                logica = new Logica(3);
                this.logica.setDifficulty(3);
            }
            
            logica.createMaze();
            logica.drawMaze(gc);
            cbxD.setVisible(false);
        });

        gc = canvas.getGraphicsContext2D();
        pane = new Pane(canvas);
        pane.getChildren().add(btRun);
//        pane.getChildren().add(btSave);
        
        this.vbox.getChildren().addAll(this.lblQuantity, tfdQuantity, cbx, cbxD, this.lblType,this.tfdName, this.lblName, this. btnName , this.btnSet, this.btnStop, table);
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
//        i1.draw(gc);
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).isAlive()) {
                lista.get(i).draw(gc);
            }
        }
        
    }
    public ObservableList<Character> getTableData(){
        ArrayList array= new ArrayList();
         
        for (int i = 0; i < lista.size(); i++) {
             array.add(lista.get(i));
        }
        ObservableList <Character> listData=FXCollections.observableArrayList(array);
        return listData;
    }
//    public int setNum(int num){
//        return num;
//    }

    public static void main(String[] args) {
        launch(args);
    } // main

} // end class
