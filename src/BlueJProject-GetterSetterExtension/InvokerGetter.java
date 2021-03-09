
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javafx.scene.control.ButtonType;

import javafx.application.Platform;

import javafx.concurrent.Task;

/**
 * InvokerGetter Task
 * Weapper to concurrency. Invoke getter using Thread
 * 
 * @author (Milton Jes&uacute;s Vera Contreras - miltonjesusvc@ufps.edu.co) 
 * @version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)
 */
public class InvokerGetter extends Task<Void>{
    private GetterSetterPane gsPane;
    public InvokerGetter(GetterSetterPane gsPane)
    {
        this.gsPane = gsPane;
    }//end constructor

   @Override
   protected Void call() throws Exception {
    //public void run(){
        GetterSetterTextField [] fields = gsPane.getFields();
        String error = "";
        
        for(int i=0; i<fields.length;i++){
            fields[i].get();
        }//end else error
        return null;
    }//end run
    
    @Override protected void succeeded() {
        super.succeeded();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Getter successfull", ButtonType.OK);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("GetterSetter");
        alert.showAndWait();
        gsPane.getStage().requestFocus();
    }

}//fin class InvokerGetter
