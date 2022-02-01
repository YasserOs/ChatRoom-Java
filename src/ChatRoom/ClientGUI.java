package ChatRoom ;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class ClientGUI extends Application{
    Socket mysocket;
    DataInputStream fromServer;
    PrintStream fromClient;
    TextArea txtA;
    TextField txtF ;
    Button btnSend ;
    Button save;
    Button open; 
    Button close; 
    String replymsg;
    Thread socketThread;
    @Override
    public void init() throws IOException{
        mysocket = new Socket("127.0.0.1",9001);
        fromServer = new DataInputStream(mysocket.getInputStream());
        fromClient = new PrintStream(mysocket.getOutputStream());
        txtF = new TextField();
        txtA = new TextArea("Chat messages : ");txtA.setEditable(false);
        save =new Button("Save Chat");
        open =new Button("Open Chat");
        close =new Button("Close Chat");
        btnSend= new Button();btnSend.setText("Send");btnSend.setDefaultButton(true);
        //---------------------------------------------------
        txtA = new TextArea();
        //----------------------------------------------------
        txtF = new TextField();
        txtF.setPromptText("Enter Meesage Here ....");
        txtF.setPrefWidth(400);
        //------------------------------------------------------
         socketThread = new Thread( ()->{
                while(true){
                    try {
                        txtA.appendText("\n" + fromServer.readLine());
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }  
        });
         socketThread.start();
    }
    @Override
    public void start(Stage primaryStage) {
        btnSend.setOnAction((e)->{
                fromClient.println(" "+txtF.getText());
                txtF.setText("");
        });     
        primaryStage.setOnCloseRequest((e)->{
                try {
                    mysocket.close();
                    socketThread.stop();   
                } catch (IOException ex) {
                   System.out.println(ex.getMessage());
                }
        });
         open.setOnAction((e)-> {
              FileChooser fileChooser2 = new FileChooser();
              fileChooser2.setTitle("Open");
              fileChooser2.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", "*.txt*"));
              String absolutePath = fileChooser2.showOpenDialog(primaryStage).getAbsolutePath();
              try {
                  File fr = new File(absolutePath);
                  Scanner scan = new Scanner(fr) ; 
                  while(scan.hasNextLine()){
                  txtA.appendText(scan.nextLine()+"\n");
                  } 
              } catch (FileNotFoundException ex) {
                 System.out.println(ex.getMessage());
              }
      }); 
         //--------------------------------------------------------------------
         save.setOnAction((e)->{
                FileChooser fc = new FileChooser(); 
                FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("txt files", ".txt"); 
                fc.getExtensionFilters().add(ext);
                File savefile = fc.showSaveDialog(null); 
                try{
                    FileWriter fw = new FileWriter(savefile);
                    fw.write(txtA.getText()); 
                    fw.close(); 
                }catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
       });
        close.setOnAction((e)->{
              try {
                    mysocket.close();
                    socketThread.stop(); 
                    primaryStage.close();
                } catch (IOException ex) {
                   System.out.println(ex.getMessage());
                }
            
                });
        //-------------------------------------------------------------------
        FlowPane f1Pane = new FlowPane(save,open,close);
        f1Pane.setVgap(5);
        f1Pane.setMargin(save, new Insets(20, 0, 20, 20));
        f1Pane.setMargin(open, new Insets(20, 0, 20, 20));
        f1Pane.setMargin(close, new Insets(20, 0, 20, 20));
        FlowPane f2Pane = new FlowPane(txtF,btnSend);
        f2Pane.setVgap(15);
        f2Pane.setMargin(txtF, new Insets(20, 0, 20, 20));
        f2Pane.setMargin(btnSend, new Insets(20, 0, 20, 20));
         BorderPane root = new BorderPane();
        root.setTop(f1Pane);
        root.setCenter(txtA);
        root.setBottom(f2Pane);
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Chat Room GUI");
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    
} 