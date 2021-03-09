
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import javafx.application.Platform;

import javafx.concurrent.Task;

/**
 * InvokerSetter Task
 * Weapper to concurrency. Invoke setter using Thread
 * 
 * @author (Milton Jes&uacute;s Vera Contreras - miltonjesusvc@ufps.edu.co) 
 * @version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)
 */
public class InvokerSetter extends Task<String>{
    private GetterSetterPane gsPane;
    public InvokerSetter(GetterSetterPane gsPane)
    {
        this.gsPane = gsPane;
    }//end constructor

    @Override
    protected String call() throws Exception {
        //public void run(){        
        GetterSetterTextField [] fields = gsPane.getFields();
        String error = "";        
        for(int i=0; i<fields.length;i++){
            if(! fields[i].set())
                error += "Error Propertie " + fields[i].getPropertie()+"\n";
        }//end for
        if(error.length()==0) error="Setter successfull";
        return error;
    }//end run

    @Override protected void succeeded() {
        super.succeeded();
        String result = this.getValue();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, result, ButtonType.OK);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("GetterSetter");
        alert.showAndWait();
        gsPane.getStage().requestFocus();
    }

}//fin class InvokerSetter
