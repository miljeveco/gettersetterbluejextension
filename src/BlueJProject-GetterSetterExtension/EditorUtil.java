
import bluej.extensions2.*;
import bluej.extensions2.event.*;
import bluej.extensions2.editor.*;

import java.net.URL;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 * Simple Wrapper bluej.extensions2.editor.JavaEditor
 *  
 * @author (Milton Jes&uacute;s Vera Contreras - miltonjesusvc@ufps.edu.co) 
 * @version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)
 */
public class EditorUtil
{
    /**The BClass to be edited with GetterSetterExtension*/
    private BClass curClass;
    
    /**The bluej.extensions2.editor.JavaEditor*/
    private JavaEditor editor;

    public EditorUtil(BClass curClass)
    throws Exception
    {
        this.curClass = curClass;
        try{
            editor = curClass.getJavaEditor();
        } 
        catch (Exception e){ 
          throw new Exception("Can't create Editor for " + curClass); 
        }
        if(editor == null)
          throw new Exception("Can't create Editor for " + curClass);
    }

    /**
     * Append appendText before last Curly Braces
     * @param appendText the text to be appended
     */
    public void appendText(String appendText) {
        int length = editor.getTextLength();
        TextLocation myBegin = new TextLocation(0, 0);
        TextLocation myEnd = editor.getTextLocationFromOffset(length);
        String newText = null;
        String currentText = null;
        int lastCurlyBraces = 0;

        try
        {
            currentText = editor.getText(myBegin, myEnd);

            lastCurlyBraces = currentText.lastIndexOf("}");
            currentText = currentText.substring(0, lastCurlyBraces - 1);

            newText = currentText + 
            "\n\n//Start GetterSetterExtension Source Code\n\n" + 
            appendText + 
            "//End GetterSetterExtension Source Code\n\n" + 
            "\n}//End class";

            editor.setText(myBegin, myEnd, newText.toString());
        }catch(Exception e){
            GetterSetterExtension.showError(e);
        }
    }
}