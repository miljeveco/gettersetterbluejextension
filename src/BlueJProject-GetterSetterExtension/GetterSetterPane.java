import bluej.extensions2.*;

import java.util.ArrayList;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;

import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ProgressIndicator;

/**
 * GetterSetterPane is JavaFX GUI to edit object with getter/setter
 * 
 * @author (Milton Jes&uacute;s Vera Contreras - miltonjesusvc@ufps.edu.co) 
 * @version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)
 */
public class GetterSetterPane extends BorderPane implements EventHandler<ActionEvent>
{
    private Stage stage;

    /**Arreglo de cuadros de texto donde de editaran las propiedades*/
    private GetterSetterTextField [] fields;

    private Label title;

    private Button setButton;
    private Button getButton;
    private Button exitButton;
    private Button aboutButton;

    private Button nextButton;
    private Button previousButton;
    private Button firstButton;
    private Button lastButton;
    private ComboBox elementCombo;

    private ArrayList controls;

    private GetterSetterIntrospector gsi;

    public GetterSetterPane(BObject theBObject) throws Exception
    {
        gsi = new GetterSetterIntrospector(theBObject);
        initComponents();
        registerListener();
        initCSS();
    }//end constructor

    /**Generate the GUI*/
    protected void initComponents()
    {
        HBox hPane1 = new HBox();
        HBox hPane2 = new HBox();
        Pane buttonPane = generateButtonPanel();
        Pane dataPane = generateDataPanel();
        title = new Label();

        try{title.setText("Editing " + gsi.getTheBObject().getInstanceName() + " With GetterSetter");}
        catch(Exception e){title.setText("Null Object");}

        this.setTop(title);

        hPane1.getChildren().add(dataPane);
        hPane1.setAlignment(Pos.CENTER);
        this.setCenter(hPane1);

        hPane2.getChildren().add(buttonPane);
        hPane2.setAlignment(Pos.CENTER);
        this.setBottom(hPane2);        

        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        BorderPane.setAlignment(hPane1, Pos.CENTER);        
        BorderPane.setAlignment(hPane2, Pos.BOTTOM_CENTER);

    }//end initComponents

    /**Generate the data Panel*/
    private Pane generateDataPanel()
    {
        String [] propertiesWithGetSet = gsi.getPropertiesWithGetSet();
        GridPane dataPanel = new GridPane();
        Label label;
        GetterSetterTextField text;
        fields = new GetterSetterTextField [propertiesWithGetSet.length];
        for(int i=0; i<propertiesWithGetSet.length;i++){
            label = new Label(propertiesWithGetSet[i]+":");
            label.getStyleClass().add("getter-setter-label");
            text = new GetterSetterTextField (gsi, propertiesWithGetSet[i]);
            dataPanel.add(label,0, i);
            dataPanel.add(text, 1, i);
            text.setEditable(true);
            fields[i] = text;
        }//end for generate data JTextField and JLabel
        return dataPanel;
    }//end generateDataPanel

    /**Set CSS class*/
    public void initCSS(){
        title.setId("getter-setter-title");
        getButton.getStyleClass().add("getter-setter-button");
        setButton.getStyleClass().add("getter-setter-button");
        exitButton.getStyleClass().add("getter-setter-button");
        aboutButton.getStyleClass().add("getter-setter-button");
        if(gsi.theBObjectIsArray()){
            firstButton.getStyleClass().add("getter-setter-button");
            previousButton.getStyleClass().add("getter-setter-button");
            nextButton.getStyleClass().add("getter-setter-button");
            lastButton.getStyleClass().add("getter-setter-button");
        }
    }

    /**Set event handler*/
    private void registerListener()
    {
        this.controls = new ArrayList();
        this.controls.add(getButton);
        this.controls.add(setButton);
        this.controls.add(exitButton);
        this.controls.add(aboutButton);
        getButton.setOnAction(this);
        setButton.setOnAction(this);
        exitButton.setOnAction(this);
        aboutButton.setOnAction(this);
        if(gsi.theBObjectIsArray()){
            this.controls.add(firstButton);
            this.controls.add(previousButton);
            this.controls.add(nextButton);
            this.controls.add(lastButton);
            this.controls.add(elementCombo);
            nextButton.setOnAction(this);
            previousButton.setOnAction(this);
            firstButton.setOnAction(this);
            lastButton.setOnAction(this);
            elementCombo.setOnAction(this);
        }//end isArtray
    }//end registerListener

    /**Generate the button Panel*/
    private Pane generateButtonPanel()
    {
        GridPane buttonsPanel = new GridPane();        
        getButton = new Button("Get");
        setButton = new Button("Set");
        exitButton = new Button("Exit");
        aboutButton = new Button("About");
        buttonsPanel.add(getButton, 0, 0);
        buttonsPanel.add(setButton, 1, 0);
        buttonsPanel.add(exitButton, 3, 0);
        buttonsPanel.add(aboutButton, 4, 0);
        if(gsi.theBObjectIsArray()){
          generatePanelArray(buttonsPanel);
        }
        return buttonsPanel;
    }//end generateButtonPanel

    /**Generate Array Button Panel*/
    private void generatePanelArray(GridPane buttonsPanel)
    {
        GridPane buttonsArray = new GridPane();
        nextButton = new Button("Next");
        previousButton = new Button("Previous");
        firstButton = new Button("First");
        lastButton = new Button("Last");
        elementCombo = new ComboBox<String>();
        buttonsPanel.add(firstButton, 0, 1);
        buttonsPanel.add(previousButton, 1, 1);
        buttonsPanel.add(elementCombo, 2, 1);
        buttonsPanel.add(nextButton, 3, 1);
        buttonsPanel.add(lastButton, 4, 1);
        for(int i=0;i<gsi.getTotalObjects();i++)
            elementCombo.getItems().add(String.valueOf(i));
    }//end generatePanelArray

    /**Handle events*/
    public void handle(ActionEvent evt)
    {
        int source = controls.indexOf(evt.getSource());
        handleEvent(source);
    }//end actionPerformed

    public static final int SHOW_STAGE=-1;
    public static final int GET=0;
    public static final int SET=1;
    public static final int CLOSE=2;
    public static final int ABOUT=3;
    public static final int FIRST=4;
    public static final int PREVIOUS=5;
    public static final int NEXT=6;
    public static final int LAST=7;
    public static final int LIST_SELECTED=8;

    /**Handle events*/
    public void handleEvent(int event){
        switch(event){
            case SHOW_STAGE:
            case GET: {
                InvokerGetter invokeGetter = new InvokerGetter(this);
                Thread th = new Thread(invokeGetter);
                th.setDaemon(true);
                th.start();
                break;
            }//end case 0
            case SET: {
                InvokerSetter invokeSetter = new InvokerSetter(this);
                Thread th = new Thread(invokeSetter);
                th.setDaemon(true);
                th.start();
                break;
            }//end case 1
            case CLOSE: {
                Stage stage = (Stage) this.getScene().getWindow();
                stage.close();
                break;
            }//end case 2
            case ABOUT: {
                showAbout();
                break;
            }//end case 3
            case FIRST: {
                gsi.firstBObject();
                break;
            }//end case 4
            case PREVIOUS: {
                gsi.previousBObject();
                break;
            }//end case 5
            case NEXT: {
                gsi.nextBObject();
                break;
            }//end case 6
            case LAST: {
                gsi.lastBObject();
                break;
            }//end case 7
            case LIST_SELECTED: {
                gsi.setTheBObjectPosition(elementCombo.getSelectionModel().getSelectedIndex());
                break;
            }//end case 8
            default: {
            }//end default
        }//end switch
        if(event >= FIRST && event <=LAST) elementCombo.getSelectionModel().select(gsi.getTheBObjectPosition());
    }

    /**Show Alert with about information*/
    private void showAbout()
    {
        String text = "Getter Setter Extension\n";
        text      +=  "Milton Jesús Vera Contreras\n";
        text      +=  "Universidad Francisco de Paula Santander\n";
        text      +=  "Cúcuta-Colombia\n";
        text      +=  "miltonjesusvc@ufps.edu.co - miljeveco@gmail.com\n";
        text      +=  "Version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)\n";
        Alert alert = new Alert(Alert.AlertType.NONE, text, ButtonType.OK);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();     
    }//end showAbout

    /**Getter textfields*/
    public GetterSetterTextField [] getFields(){return this.fields;}

    /**Getter Stage*/
    public Stage getStage(){return this.stage;}

    /**Setter Stage*/
    public void setStage(Stage stage){this.stage=stage;}

}//fin class GetterSetterFrameObject
