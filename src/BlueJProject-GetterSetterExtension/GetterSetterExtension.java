
import bluej.extensions2.*;
import bluej.extensions2.event.*;
import bluej.extensions2.editor.*;
import javafx.event.EventHandler;

import java.net.URL;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 * This extension help to:
 * 1) Generate source code of GET/SET methods of all class's properties
 * 2) Generate source code of GET method of one propertie
 * 3) Generate source code of SET method of one propertie
 * 4) Generate GUI JavaFX to view/edit object's properties with GET/SET methods
 * 
 * @author (Milton Jes&uacute;s Vera Contreras - miltonjesusvc@ufps.edu.co) 
 * @version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)
 */
public class GetterSetterExtension extends Extension
{
    private BPackage curPackage;
    private BClass curClass;
    private BObject curObject;

    /**
     * When this method is called, the extension may start its work.
     */
    public void startup (BlueJ bluej) {
        MenuBuilder menuBuilder = new MenuBuilder(this);
        bluej.setMenuGenerator(menuBuilder);
        Preferences myPrefs = new Preferences(bluej);
        bluej.setPreferenceGenerator(myPrefs);
    }

    /**
     * This method must decide if this Extension is compatible with the 
     * current release of the BlueJ Extensions API
     */
    public boolean isCompatible () { 
        return true; 
    }

    /**
     * Returns the version number of this extension
     */
    public String  getVersion () { 
        return ("0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)");  
    }

    /**
     * Returns the user-visible name of this extension
     */
    public String  getName () { 
        return ("GetterSetter Extension");  
    }

    /**
     * Extension is terminated
     */
    public void terminate() {
        System.out.println ("GetterSetter Extension terminates");
    }

    /**
     * Extension Description
     */
    public String getDescription () {
        return ("GetterSetter Extension");
    }

    /**
     * Returns a URL where you can find info on this extension.
     * The real problem is making sure that the link will still be alive in three years...
     */
    public URL getURL () {
        try {
            return new URL("http://www.bluej.org/doc/writingextensions.html");
        } catch ( Exception e ) {
            // The link is either dead or otherwise unreachable
            System.out.println ("GetterSetterExtension: getURL: Exception="+e.getMessage());
            return null;
        }
    }

    /**Only to Manifiest*/
    public static void main(String [] args)
    {
    }//end main

    /**GET Method propertie curPackage*/
    public BPackage getCurPackage(){
        return this.curPackage;
    }//end method getCurPackage

    /**SET Method propertie  curPackage*/
    public void setCurPackage(BPackage curPackage){
        this.curPackage = curPackage;
    }//end method setCurPackage

    /**GET Method propertie  curClass*/
    public BClass getCurClass(){
        return this.curClass;
    }//end method getCurClass

    /**SET Method propertie  curClass*/
    public void setCurClass(BClass curClass){
        this.curClass = curClass;
    }//end method setCurClass

    /**GET Method propertie  curObject*/
    public BObject getCurObject(){
        return this.curObject;
    }//end method getCurObject

    /**SET Method propertie  curObject*/
    public void setCurObject(BObject curObject){
        this.curObject = curObject;
    }//end method setCurObject

    /**
     * Generate source code of GET/SET methods of all class's properties
     */ 
    public void genGetterSetter() throws Exception {
        EditorUtil editorUtil;
        CodeGenerator codeGenerator = null;
        String code ="";
        editorUtil = new EditorUtil(this.curClass);
        codeGenerator = new CodeGenerator();
        code = codeGenerator.generateGetterSetter(this.curClass.getJavaClass());
        editorUtil.appendText(code);
        curClass.compile(false);
    }//fin genGetterSetter

    /**
     * Generate source code of GET method of one propertie
     */
    public void genGetter(String propertie) throws Exception {
        EditorUtil editorUtil;
        CodeGenerator codeGenerator = null;
        String code ="";
        editorUtil = new EditorUtil(this.curClass);
        codeGenerator = new CodeGenerator();
        code = codeGenerator.generateGetter(this.curClass.getJavaClass(), propertie);
        editorUtil.appendText(code);
        curClass.compile(false);
    }//fin genGetter

    /**
     *  Generate source code of SET method of one propertie
     */
    public void genSetter(String propertie) throws Exception {
        EditorUtil editorUtil;
        CodeGenerator codeGenerator = null;
        String code ="";
        editorUtil = new EditorUtil(this.curClass);
        codeGenerator = new CodeGenerator();
        code = codeGenerator.generateSetter(this.curClass.getJavaClass(), propertie);
        editorUtil.appendText(code);
        curClass.compile(false);
    }//fin genSetter    

    /**
     * Generate GUI JavaFX to view/edit object's properties with GET/SET methods
     */
    public void setterObject() throws Exception
    {
        GetterSetterPane gsPane = new GetterSetterPane(this.curObject);
        Stage stage = new Stage();
        Scene scene = new Scene(gsPane);

        gsPane.setStage(stage);

        scene.getStylesheets().add(getClass().getResource("gettersetter.css").toExternalForm());

        stage.setScene(scene); 
        stage.setTitle("GetterSetter");
        stage.sizeToScene(); 
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }//fin setterObject

    /**
     * Helper method to show Alert about Exception
     */
    public static void showError(Exception e){
        java.io.StringWriter errors = new java.io.StringWriter();
        e.printStackTrace(new java.io.PrintWriter(errors));
        Alert alert = new Alert(Alert.AlertType.ERROR,"GetterSetter Exception:\n" + errors.toString(), ButtonType.OK);
        alert.setTitle("GetterSetter Exception");
        alert.setHeaderText("GetterSetter Exception");
        alert.initStyle(StageStyle.UTILITY);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
    }

}//fin class GetterSetterExtension