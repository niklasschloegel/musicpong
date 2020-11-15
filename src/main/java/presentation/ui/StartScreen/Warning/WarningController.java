package presentation.ui.StartScreen.Warning;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import presentation.FXMain;
import presentation.ui.AbstractViewController;

public class WarningController extends AbstractViewController<FXMain> {


    private Button ok;
    private SimpleBooleanProperty wantsToGetClosed;

    public WarningController(FXMain application, String msg) {
        super(application);
        rootView = new Warning(msg);
        ok = ((Warning) rootView).ok;
        wantsToGetClosed = new SimpleBooleanProperty(false);

        initialize();
    }

    @Override
    public void initialize() {
        ok.setOnAction(e->{
            wantsToGetClosed.set(true);
            wantsToGetClosed.set(false);
        });
    }

    public SimpleBooleanProperty wantsToGetClosedProperty() {
        return wantsToGetClosed;
    }
}
