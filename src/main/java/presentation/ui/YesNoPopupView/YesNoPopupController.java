package presentation.ui.YesNoPopupView;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import presentation.FXMain;
import presentation.ui.AbstractViewController;

public class YesNoPopupController extends AbstractViewController<FXMain> {

    private SimpleBooleanProperty yesTrigger, noTrigger;

    private Button yes, no;

    public YesNoPopupController(FXMain application, String message) {
        super(application);
        rootView = new YesNoPopupView(message);

        yes = ((YesNoPopupView) rootView).yes;
        no = ((YesNoPopupView) rootView).no;

        yesTrigger = new SimpleBooleanProperty(false);
        noTrigger = new SimpleBooleanProperty(false);

        initialize();
    }

    @Override
    public void initialize() {

        yes.setOnAction(e->{
            yesTrigger.set(true);
            yesTrigger.set(false);
        });

        no.setOnAction(e->{
            noTrigger.set(true);
            noTrigger.set(false);
        });

    }

    public SimpleBooleanProperty yesTriggerProperty() {
        return yesTrigger;
    }

    public SimpleBooleanProperty noTriggerProperty() {
        return noTrigger;
    }
}
