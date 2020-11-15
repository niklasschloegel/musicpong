package presentation.ui.StartScreen.Warning;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentation.utils.LanguageAssist;
import presentation.utils.StyleAssist.CSSAttribute;
import presentation.utils.StyleAssist.UIColor;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.StyleAssist.applyColorsTo;

class Warning extends VBox {

    Button ok;

    Warning(String msg){
        super();

        Label header = new Label();
        LanguageAssist.initLanguage(header, "warning");

        Separator seppo = new Separator();
        seppo.getStyleClass().add("separator");

        Label message = new Label(msg);

        ok = new Button();
        LanguageAssist.initLanguage(ok, "ok");

        try {
            applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, header, message, ok);
            applyColorsTo(UIColor.TEXT_COLOR_DARK, CSSAttribute.TEXT_FILL, seppo);
            applyColorsTo(UIColor.BG_COLOR_DARK, CSSAttribute.BG_COLOR, ok);
            applyColorsTo(UIColor.BG_COLOR, CSSAttribute.BG_COLOR, this);
        } catch (UnsupportedRegionException e) {
            e.printStackTrace();
        }

        getChildren().addAll(header, seppo, message, ok);
        setPadding(new Insets(20));
        setSpacing(15);

    }

}
