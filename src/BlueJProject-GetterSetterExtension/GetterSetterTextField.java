 

import bluej.extensions2.*;
import javafx.scene.control.TextField;


/**
 * GetterSetterTextField is a TextField Wrapper to invoke getter and setter
 * 
 * @author (Milton Jes&uacute;s Vera Contreras - miltonjesusvc@ufps.edu.co) 
 * @version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)
 */
public class GetterSetterTextField extends TextField
{
  /**The Introspector for invoque BMethod*/
  private GetterSetterIntrospector gsi;
  
  /**The propertie*/
  private String propertie;
  
  public GetterSetterTextField(GetterSetterIntrospector gsi, String propertie)
  {
    this.gsi= gsi;
    this.propertie = propertie;
    this.getStyleClass().add("getter-setter-text-field");
  }//end constructor
  
  /**Invoke set method using this.getText() value*/
  public boolean set()
  {
    return gsi.invokeSetter(this.propertie, this.getText());
  }//end validate
  
  /**Invoke get method on obejct and set textvalue*/
  public void get()
  {
     this.setText(gsi.invokeGetter(this.propertie));
  }//end updateData
  
  /**Getter propertie*/
  public String getPropertie(){return this.propertie;} 
  
}//fin class GetterSetterTextField