package presentation.ui.YesNoPopupView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.LanguageAssist.initLanguage;
import static presentation.utils.StyleAssist.applyColorsTo;

class YesNoPopupView extends VBox {

    Button yes, no;

    YesNoPopupView(String messageKey) {
        super();

        Label msg = new Label();
        initLanguage(msg, messageKey);
        HBox btnContainer = new HBox();

        yes = new Button();
        no = new Button();

        initLanguage(yes, "yes");
        initLanguage(no, "no");

        try {
            applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, msg);
            applyColorsTo(StyleAssist.UIColor.BG_COLOR_DARK, StyleAssist.CSSAttribute.BG_COLOR, yes, no);
            applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, yes, no);
            applyColorsTo(StyleAssist.UIColor.BG_COLOR, StyleAssist.CSSAttribute.BG_COLOR, this);
        } catch (UnsupportedRegionException e) {
            e.printStackTrace();
        }

        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.setSpacing(12);
        btnContainer.getChildren().addAll(yes, no);

        setSpacing(14);
        setPadding(new Insets(20));

        getChildren().addAll(msg, btnContainer);
        setAlignment(Pos.CENTER);

    }
}
