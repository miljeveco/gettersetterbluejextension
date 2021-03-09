
import bluej.extensions2.*;
import bluej.extensions2.event.*;
import bluej.extensions2.editor.*;

import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;;

import java.net.URL;

/**
 * MenuBuilder to GetterSetterExtension
 * 
 * 
 * @author (Milton Jes&uacute;s Vera Contreras - miltonjesusvc@ufps.edu.co) 
 * @version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)
 */
class MenuBuilder extends MenuGenerator {

    /**The extension object*/
    private GetterSetterExtension ext;

    /**Default Constructor*/
    public MenuBuilder(GetterSetterExtension ext)
    {
        //super();
        this.ext = ext;
    }//fin constructor

    /**
     * Generate the Menu and MenuItem to BlueJ Class. If class is not compiled show MenuItem "Class No Compiled..."
    */
    public MenuItem getClassMenuItem(BClass aClass) {
        Menu jm=null, jm1=null, jm2=null;
        MenuItem tmp=null, jm0=null, jm3=null;        
        jm = new Menu("GetterSetter");
        jm0 = new MenuItem("About...");
        jm0.setOnAction(new AboutAction());        
        try{            
            if(aClass.isCompiled()){
                jm1 = new Menu("Generate Getter to ");
                jm2 = new Menu("Generate Setter to ");
                jm3 = new MenuItem("Generate All Getter & Setter");
                jm3.setOnAction(new GetterSetterAction(aClass));
                jm.getItems().addAll(jm0, jm3, jm2, jm1);
                
                GetterSetterIntrospector gsi = new GetterSetterIntrospector(aClass);
                String [] propertiesWithoutGet = gsi.getPropertiesWithoutGet();
                String [] propertiesWithoutSet = gsi.getPropertiesWithoutSet();
                for(int i=0;i<propertiesWithoutGet.length;i++) {
                    tmp = new MenuItem(propertiesWithoutGet[i]);
                    tmp.setOnAction(new GetterAction(aClass, propertiesWithoutGet[i]));
                    jm1.getItems().add(tmp);
                }
                for(int i=0;i<propertiesWithoutSet.length;i++){
                    tmp = new MenuItem(propertiesWithoutSet[i]);
                    tmp.setOnAction(new SetterAction(aClass, propertiesWithoutSet[i]));
                    jm2.getItems().add(tmp);
                }

            }
            else{
              jm.getItems().addAll(jm0, new MenuItem("(Class No compiled...)"));
            }
        }catch(Exception e){
            GetterSetterExtension.showError(e); 
        }
        return jm;
    }//fin getClassMenuItem

    /**
     * Generate the Menu and MenuItem to BlueJ Object
    */
    public MenuItem getObjectMenuItem(BObject anObject) {
        MenuItem objectMenuItem = new MenuItem("Getter & Setter GUI Helper Editor");
        objectMenuItem .setOnAction(new ObjectSetterAction (anObject));
        return objectMenuItem;
    }//fin getObjectMenuItem

    // These methods will be called when
    // each of the different menus are about to be invoked.
    public void notifyPostToolsMenu(BPackage bp, MenuItem jmi) {
        System.out.println("Post on Tools menu");
        ext.setCurPackage(bp);
        ext.setCurClass(null);
        ext.setCurObject(null);
    }//fin notifyPostToolsMenu

    public void notifyPostClassMenu(BClass bc, MenuItem jmi) {
        System.out.println("Post on Class menu");
        ext.setCurPackage(null);
        ext.setCurClass(bc);
        ext.setCurObject(null);
    }//fin notifyPostClassMenu

    public void notifyPostObjectMenu(BObject bo, MenuItem jmi) {
        System.out.println("Post on Object menu");
        ext.setCurPackage(null);
        ext.setCurClass(null);
        ext.setCurObject(bo);
    }//fin notifyPostObjectMenu

    /**
     * Inner Class to Generate ALL GET/SET methods
    */
    class GetterSetterAction implements EventHandler {
        public GetterSetterAction(BClass bc) {
            ext.setCurClass(bc);
        }

        public void handle(Event evt) {
            try{
                ext.genGetterSetter();
            }
            catch(Exception e){
                GetterSetterExtension.showError(e);   
            }
        }//fin 
    }//fin clase

    /**
     * Inner Class to Generate GET method for a propertie
    */
    class GetterAction implements EventHandler{
        private String propertie;
        public GetterAction(BClass bc, String propertie) {
            this.propertie = propertie;
            ext.setCurClass(bc);
        }

        public void handle(Event evt) {
            try{
                ext.genGetter(propertie);
            }
            catch(Exception e){
               GetterSetterExtension.showError(e);   
            }
        }//fin 
    }//fin clase

    /**
     * Inner Class to Generate GET method for a propertie
    */
    class SetterAction implements EventHandler {
        private String propertie;
        public SetterAction(BClass bc, String propertie) {
            this.propertie = propertie;
            ext.setCurClass(bc);
        }

        public void handle(Event evt) {
            try{
                ext.genSetter(propertie);
            }
            catch(Exception e){
                GetterSetterExtension.showError(e); 
            }
        }//fin 
    }//fin clase    

    /**
     * Inner Class to Generate GUI JavaFX to view/edit object'properties with GET/SET methods
    */
    class ObjectSetterAction implements EventHandler{
        public ObjectSetterAction(BObject bo) {
            ext.setCurObject(bo);
        }

        public void handle(Event evt) {
            try{
                ext.setterObject();
            }
            catch(Exception e){
                GetterSetterExtension.showError(e);  
            }
        }//fin 
    }//fin clase    

    /**
     * Inner Class to Generate About Alert
    */
    class AboutAction implements EventHandler{
        public AboutAction(){

        }

        public void handle(Event t){
            try{
                String text = "Getter Setter Extension\n";
                text      +=  "Milton Jesús Vera Contreras\n";
                text      +=  "Universidad Francisco de Paula Santander\n";
                text      +=  "Cúcuta-Colombia\n";
                text      +=  "miltonjesusvc@ufps.edu.co - miljeveco@gmail.com\n";
                Alert alert = new Alert(Alert.AlertType.NONE, text, ButtonType.OK);
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
            }
            catch(Exception e){
                GetterSetterExtension.showError(e); 
            }
        }
    }

}//fin clase