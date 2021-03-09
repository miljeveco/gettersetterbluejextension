import bluej.extensions2.BlueJ;
import bluej.extensions2.PreferenceGenerator;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;

/***
 * Wrapper to BlueJ Extension Preferences Panel 
 */
class Preferences implements PreferenceGenerator
{
    private Pane myPane;
    private TextField color;
    private BlueJ bluej;
    public static final String PROFILE_LABEL = "Favorite-Colour";

    // Construct the panel, and initialise it from any stored values
    public Preferences(BlueJ bluej)
    {
        this.bluej = bluej;
        myPane = new Pane();
        VBox vboxContainer = new VBox();
        vboxContainer.getChildren().add(new Label("Milton Jesús Vera Contreras miltonjesusvc@ufps.edu.co | miljeveco@gmail.com"));
        myPane.getChildren().add(vboxContainer);
        // Load the default value
        loadValues();
    }

    public Pane getWindow()
    {
        return myPane;
    }

    public void saveValues()
    {
        // Save the preference value in the BlueJ properties file
        //bluej.setExtensionPropertyString(PROPERTY, control.getText());
    }

    public void loadValues()
    {
        // Load the property value from the BlueJ properties file,
        // default to an empty string
        //control.setText(bluej.getExtensionPropertyString(PROPERTY, ""));
    }
}