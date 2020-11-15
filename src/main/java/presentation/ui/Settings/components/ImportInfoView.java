
package presentation.ui.Settings.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentation.utils.StyleAssist.CSSAttribute;
import presentation.utils.StyleAssist.UIColor;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.LanguageAssist.initLanguage;
import static presentation.utils.StyleAssist.applyColorsTo;


public class ImportInfoView extends VBox {

    public ImportInfoView(String content){
        super();

        Label header = new Label();
        header.setId("importHeader");
        initLanguage(header, "settings.success");

        Separator separator = new Separator();
        separator.getStyleClass().add("separator");
        try {
            applyColorsTo(UIColor.TEXT_COLOR_DARK, CSSAttribute.BG_COLOR, separator);
        } catch (UnsupportedRegionException e) {
            e.printStackTrace();
        }

        Label info = new Label();
        info.setText(content);

        try {
            applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, header, info);
            applyColorsTo(UIColor.BG_COLOR, CSSAttribute.BG_COLOR, this);
        } catch (UnsupportedRegionException e) {
            e.printStackTrace();
        }

        getChildren().addAll(header, separator, info);
        setPadding(new Insets(10));
        setSpacing(6);

    }



}
