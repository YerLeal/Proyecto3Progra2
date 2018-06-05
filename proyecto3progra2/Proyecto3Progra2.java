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
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
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
import org.omg.PortableServer.THREAD_POLICY_ID;

public class Proyecto3Progra2 extends Application implements Runnable {
    public static String chronometer="00-00-000";
    public String num="";
    private int width = 1360;
    private final int HEIGTH = 720;
    private int canvasWidth;
    private Canvas canvas;
    private Pane pane;
    private GraphicsContext gc;
    private Logica logica;
    private boolean itemCharge = false;
    private Thread thread;
    private Thread cronometro;
    private HBox hbox;
    private VBox vbox;
    private VBox vb;
    private javafx.scene.control.Label lblType;
    private javafx.scene.control.Label lblQuantity;
    private javafx.scene.control.Label lblName;
    private Text tiempo=new Text(); 
    
    
    private ObservableList<String> listType, listDifficult;
    private Button btnSet;
    private Button btnPause;
    private Button btnStop;
    private Button btnName;
    private Button btnLogin;
    private TextField tfdQuantity;
    private TextField tfdName;
    private int cantP;
    private SharedBuffer buffer;
    private ArrayList<Character> characters = new ArrayList<>();
    private int initCont = 0;
    private Scene scene;
    private final Stage stage=new Stage();
    private Timer t;
    private int h=0, m, s, cs;
    private ActionListener action;
    private boolean flag = true;
    private TableView<Character> table;
    

    private Runnable hilos = new Runnable() {
        @Override
        public void run() {
            ArrayList<Block> aux = logica.getStart();
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
    private Runnable crono = new Runnable() {
        @Override
        public void run() {
            int minutos = 0 , segundos = 0, milesimas = 0;
            //min es minutos, seg es segundos y mil es milesimas de segundo
            String min="", seg="", mil="";
            while(flag){
                try{
                    Thread.sleep( 4 );
                    //Incrementamos 4 milesimas de segundo
                    milesimas += 4;

                    //Cuando llega a 1000 osea 1 segundo aumenta 1 segundo
                    //y las milesimas de segundo de nuevo a 0
                    if( milesimas == 1000 ){
                        milesimas = 0;
                        segundos += 1;
                        //Si los segundos llegan a 60 entonces aumenta 1 los minutos
                        //y los segundos vuelven a 0
                        if( segundos == 60 )
                        {
                            segundos = 0;
                            minutos++;
                        }
                    }

                    //Esto solamente es estetica para que siempre este en formato
                    //00:00:000
                    if( minutos < 10 ){ min = "0" + minutos;
                    } else 
                    {min = String.valueOf(minutos);}
                    if( segundos < 10 ) seg = "0" + segundos;
                    else seg = String.valueOf(segundos);
//
//                    if( milesimas < 10 ) mil = "00" + milesimas;
//                    else if( milesimas < 100 ) mil = "0" + milesimas;
//                    else mil = milesimas.toString();

                    //Colocamos en la etiqueta la informacion
                    tiempo.setText( minutos + ":" + seg );
            }
        catch(Exception e){
        //Cuando se reincie se coloca nuevamente en 00:00:000
        tiempo.setText( "00:00:000" );
        }
            }
//tiempo.setText("");
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
        
        this.hbox = new HBox(10);
        this.vbox = new VBox(10);
        hbox.setSpacing(10);
        vbox.setSpacing(10);
        cronometro=new Thread(crono);
        btnSet = new Button("Start Simulation");
        btnLogin=new Button("Set the name and Characters");
        btnStop = new Button("Stop Threads");
        btnPause = new Button("Pause Characters");
        btnName = new Button("Set Player Name");
        btnSet.setDisable(true);
        btnPause.setDisable(true);
        btnStop.setDisable(true);

        
        tfdQuantity = new TextField();
        
        tfdName = new TextField();
        this.lblType = new javafx.scene.control.Label();
        this.lblQuantity = new javafx.scene.control.Label("Quantity of Characters");
        this.lblName = new javafx.scene.control.Label("Name of Player");
        //this.tiempo.setText("jj");

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
        TableColumn name = new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn type = new TableColumn("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn position = new TableColumn("Position");
        position.setCellValueFactory(new PropertyValueFactory<>("position"));
        ObservableList<Character> datos = getTableData();
        table.setItems(datos);
        table.getColumns().addAll(name, type, position);

        this.canvasWidth = 1120;
        this.canvas = new Canvas(canvasWidth, HEIGTH);

        Button btRun = new Button("Run");
        Button btItem = new Button("Item");
        
        btRun.relocate(400, 400);
        btItem.relocate(700, 400);
        tiempo.relocate(1000, 600);

        btRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Block> finish = logica.getFinish();
                for (int i = 0; i < 18; i++) {

                    if (i < 6) {
                        characters.add(new SmartCharacter(logica.getSize(), buffer, finish, tfdName.getText()));
                    } else if (i < 12) {
                        characters.add(new FastCharacter(logica.getSize(), buffer, finish, tfdName.getText()));
                    } else {
                        characters.add(new FuriousCharacter(logica.getSize(), buffer, finish, tfdName.getText()));
                    }
                }
                thread.start();
                logica.startItems();
                new Thread(hilos).start();
                cronometro=new Thread(crono);
                cronometro.start();

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
                logica.startItems();
                new Thread(hilos).start();
                cronometro=new Thread(crono);
                cronometro.start();
                

        });
        btnLogin.setOnAction((ActionEvent t) -> {
            stage.show();
        });
        btnStop.setOnAction((ActionEvent t) -> {
            flag=false;
        });
        cbx.setOnAction((ActionEvent e) -> {
            System.out.println(tfdQuantity.getText());
            for(int i=0; i<Integer.parseInt(tfdQuantity.getText()); i++){ 
                    if (cbx.getValue().equalsIgnoreCase("Fast")) {
                        characters.add(new FastCharacter(logica.getSize(), buffer, logica.getFinish(), tfdName.getText()));

                        this.lblType.setText(tfdName.getText()+" Fast");
                    } else if (cbx.getValue().equalsIgnoreCase("Furious")) {
                        characters.add(new FuriousCharacter(logica.getSize(), buffer, logica.getFinish(), tfdName.getText()));

                        this.lblType.setText(tfdName.getText()+" Furious");
                    } else {
                        characters.add(new SmartCharacter(logica.getSize(), buffer, logica.getFinish(), tfdName.getText()));
                        this.lblType.setText(tfdName.getText()+" Smart");
                    }
                    System.out.println("num");
            }
                    if (cantP == Integer.parseInt(tfdQuantity.getText())) {
                        //btnSet.setVisible(true);
//                        btnSet.setDisable(true);
                    }
            cantP++;
            tfdQuantity.setText("");
            btnSet.setDisable(false);
        btnPause.setDisable(false);
        btnStop.setDisable(false);
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
        pane.getChildren().add(btItem);
        pane.getChildren().add(tiempo);

        this.vbox.getChildren().addAll(cbxD, this.lblName, this.btnLogin, this.btnSet, this.btnPause, this.btnStop, tiempo, table);
        this.hbox.getChildren().add(pane);
        this.hbox.getChildren().add(vbox);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() == canvas && !itemCharge) {
                    logica.cambiarTipo((int) event.getX(), (int) event.getY(), gc);
                    logica.imprimirTipo((int) event.getX(), (int) event.getY());
                } else {
                    if (thread.isAlive()) {
                        logica.addItem((int) event.getX(), (int) event.getY(), buffer, true,gc);
                    } else {
                        logica.addItem((int) event.getX(), (int) event.getY(), buffer, false,gc);
                    }
                }
            }
        });

        Scene scene = new Scene(hbox, width, HEIGTH);
        primaryStage.setScene(scene);
        vb=new VBox(10);
        vb.getChildren().addAll(lblQuantity, tfdQuantity, lblType, cbx, lblName,tfdName, btnName);
        //popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));
        
        scene=new Scene(vb, 400, 400);
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

    }

    public void draw(GraphicsContext gc) throws InterruptedException {
        gc.clearRect(0, 0, canvasWidth, HEIGTH);
        logica.drawMaze(gc);
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).isAlive()) {
                characters.get(i).draw(gc);
            }
        }

    }

    public ObservableList<Character> getTableData() {
        ArrayList array = new ArrayList();

        for (int i = 0; i < characters.size(); i++) {
            array.add(characters.get(i));
        }
        ObservableList<Character> listData = FXCollections.observableArrayList(array);
        return listData;
    }
//    public int setNum(int num){
//        return num;
//    }

    public static void main(String[] args) {
        launch(args);
    } // main

} // end class
