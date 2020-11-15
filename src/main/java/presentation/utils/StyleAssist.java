package presentation.utils;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import presentation.FXMain;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class StyleAssist {

    private static final String DARK_VAL_1 = "#282c34";
    private static final String DARK_VAL_2 = "#181a1f";
    private static final String LIGHT_VAL_1 = "#dddddd";
    private static final String LIGHT_VAL_2 = "#bbbbbb";

    private static final double DARK_BORDER = 117.5;
    private static final double LIGHT_BORDER = 137.5;

    private static final String CSS_DIR = System.getProperty("user.dir") + "/assets/css/";

    private static SimpleBooleanProperty darkMode = new SimpleBooleanProperty(true);

    private static Map<UIColor, Set<Region>> colorMap = new HashMap<>();

    private static final String MAIN_COLOR_VALUE = "#27d9df";
    private static final String MAIN_COLOR_DARK_VALUE = "#3ea6a9";
    private static final String SECONDARY_COLOR_VALUE = "#ca3bb3";
    private static final String SECONDARY_COLOR_DARK_VALUE = "#a5369c";

    public enum UIColor {
        MAIN_COLOR(MAIN_COLOR_VALUE), MAIN_COLOR_DARK(MAIN_COLOR_DARK_VALUE), SECONDARY_COLOR(SECONDARY_COLOR_VALUE), SECONDARY_COLOR_DARK(SECONDARY_COLOR_DARK_VALUE),
        BG_COLOR(DARK_VAL_1), BG_COLOR_DARK(DARK_VAL_2), TEXT_COLOR(LIGHT_VAL_1), TEXT_COLOR_DARK(LIGHT_VAL_2);

        private SimpleStringProperty value = new SimpleStringProperty(this, "value");

        UIColor(String value) {
            this.value.set(value);
            initListener();
        }

        public void initListener() {
            valueProperty().addListener((observableValue, ov, nv) -> {
                if (colorMap.containsKey(this)) {
                    colorMap.get(this).forEach(region -> {

                        CSSAttribute cssAttribute;

                        if (this.equals(TEXT_COLOR)) {
                            cssAttribute = CSSAttribute.TEXT_FILL;
                        } else {
                            cssAttribute = CSSAttribute.BG_COLOR;
                        }

                        try {
                            StyleAssist.applyColorsTo(this, cssAttribute, region);
                        } catch (UnsupportedRegionException e) {
                            e.printStackTrace();
                        }

                    });
                }
            });

        }

        public String getValue() {
            return value.get();
        }

        public void setValue(String value) {
            this.value.set(value);
        }

        public SimpleStringProperty valueProperty() {
            return value;
        }

    }

    public enum CSSAttribute {
        BG_COLOR("-fx-background-color:"), TEXT_FILL("-fx-text-fill:");

        private String modifier;

        CSSAttribute(String modifier) {
            this.modifier = modifier;
        }

        public String getModifier() {
            return modifier;
        }
    }

    public static void refreshColors(Image image) {
        Image img = null;
        if (image == null) {
            try {
                img = new Image(new FileInputStream(System.getProperty("user.dir") + "/assets/images/defaultCover.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            img = image;
        }
        assert(img != null);

        PixelReader pr = img.getPixelReader();
        Map<Color, Long> count = new HashMap<>();

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color temp = pr.getColor(x, y);
                if (temp.getOpacity() == 1 && isUsable(temp)) {
                    if (count.containsKey(temp)) {
                        count.put(temp, count.get(temp) + 1);
                    } else {
                        count.put(temp, 1L);
                    }
                }
            }
        }

        List<Map.Entry<Color, Long>> sorted = getAvg(count);
        Color main, secondary;
        if (sorted.size() >= 1) {
            main = sorted.get((sorted.size() - 1)).getKey();
            secondary = sorted.get((int) (sorted.size() * 0.6)).getKey();

            if (main.getBrightness() * 255 > LIGHT_BORDER && !isDarkMode()) {
                setDarkMode(true);
            } else if (main.getBrightness() * 255 < DARK_BORDER && isDarkMode()) {
                setDarkMode(false);
            }

            UIColor.MAIN_COLOR.setValue(toRGBCode(main));
            UIColor.MAIN_COLOR_DARK.setValue(toRGBCode(main.darker()));
            UIColor.SECONDARY_COLOR.setValue(toRGBCode(secondary));
            UIColor.SECONDARY_COLOR_DARK.setValue(toRGBCode(secondary.darker()));
        } else {
            UIColor.MAIN_COLOR.setValue(MAIN_COLOR_VALUE);
            UIColor.MAIN_COLOR_DARK.setValue(MAIN_COLOR_DARK_VALUE);
            UIColor.SECONDARY_COLOR.setValue(SECONDARY_COLOR_VALUE);
            UIColor.SECONDARY_COLOR_DARK.setValue(SECONDARY_COLOR_DARK_VALUE);
            setDarkMode(true);
        }
    }

    /**
     * This method applies colors to JavaFx-Regions and to their suitable css
     * modifier. E.g. Buttons: <code>
     * StyleAssist.applyColorsTo(StyleAssist.UIColor.MAIN_COLOR, StyleAssist.CSSAttribute.BG_COLOR, loop, skipBack, playPause, skip, shuffle);
     * </code> Buttons need to get styled with <code>"-fx-background-color"</code>.
     *
     * @param uiColor:      Color in which the elements should get colored
     * @param cssAttribute: suitable CSS-Modifier (depends on which Region is
     *                      passed)
     * @param regions:      Element to be colored
     */
    public static void applyColorsTo(UIColor uiColor, CSSAttribute cssAttribute, Region... regions)
            throws UnsupportedRegionException {
        for (Region r : regions) {
            if (!isSupported(r)) {
                throw new UnsupportedRegionException(
                        "The support for this region (" + r.getClass() + ") cannot be ensured.");
            }
        }

        Platform.runLater(() -> {
            if (!colorMap.containsKey(uiColor)) {
                Set<Region> elementSet = new HashSet<>();
                colorMap.put(uiColor, elementSet);
            }

            for (Region r : regions) {
                if (r instanceof ProgressBar) {
                    updateProgressBarColor((ProgressBar) r);
                } else if (r instanceof ComboBox) {
                    updateComboBox((ComboBox) r);
                } else if (r instanceof TableView) {
                    updateTable((TableView) r);
                } else if (r instanceof JFXToggleButton) {
                    updateToggleButton((JFXToggleButton) r);
                } else if (r instanceof JFXSlider) {
                    updateSliderColor((JFXSlider) r);
                } else {
                    String tmp = r.getStyle();
                    r.setStyle(tmp + cssAttribute.getModifier() + uiColor.getValue() + ";");
                }
                colorMap.get(uiColor).add(r);
            }
        });
    }

    public static void unstyleRegion(Region region) {
        for (Map.Entry<UIColor, Set<Region>> m : colorMap.entrySet()) {
            if (m.getValue().contains(region)) {
                colorMap.get(m.getKey()).remove(region);
            }
        }
    }

    /**
     * This method helps you adding the same CSS-Class to a group of regions, except
     * using <code>getStyleClass().add("...")</code> for every Region.
     *
     * @param cssClass: The CSS Class the objects should get
     * @param regions:  The Regions, which should get the CSS Class
     */
    public static void addCSSClassTo(String cssClass, Region... regions) {
        for (Region r : regions) {
            r.getStyleClass().add(cssClass);
        }
    }

    private static String toRGBCode(Color color) {

        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private static void darkMode() {
        UIColor.BG_COLOR.setValue(DARK_VAL_1);
        UIColor.BG_COLOR_DARK.setValue(DARK_VAL_2);
        UIColor.TEXT_COLOR.setValue(LIGHT_VAL_1);
        UIColor.TEXT_COLOR_DARK.setValue(LIGHT_VAL_2);
    }

    private static void lightMode() {
        UIColor.BG_COLOR.setValue(LIGHT_VAL_1);
        UIColor.BG_COLOR_DARK.setValue(LIGHT_VAL_2);
        UIColor.TEXT_COLOR.setValue(DARK_VAL_1);
        UIColor.TEXT_COLOR_DARK.setValue(DARK_VAL_2);
    }

    private static void updateProgressBarColor(ProgressBar progressBar) {
        String css = ".progress-bar .bar{" + CSSAttribute.BG_COLOR.getModifier() + UIColor.MAIN_COLOR.getValue() + ";}"
                     + ".progress-bar .track{" + CSSAttribute.BG_COLOR.getModifier() + UIColor.SECONDARY_COLOR.getValue()
                     + ";}";

        String file = CSS_DIR + "progressbar.css";

        addNewCSS(css, file, progressBar);

    }

    private static void updateToggleButton(JFXToggleButton toggleButton) {
        String css = ".jfx-toggle-button{" +
                     "-jfx-toggle-color: " + UIColor.MAIN_COLOR_DARK.getValue() + ";" +
                     "-jfx-toggle-line-color: " + UIColor.MAIN_COLOR.getValue() + ";}";

        String file = CSS_DIR + "togglebutton.css";

        addNewCSS(css, file, toggleButton);
    }

    private static void updateSliderColor(JFXSlider slider) {
        String css = ".jfx-slider > .track{" + CSSAttribute.BG_COLOR.getModifier() + UIColor.TEXT_COLOR_DARK.getValue()
                     + ";}" + ".jfx-slider > .colored-track{" + CSSAttribute.BG_COLOR.getModifier()
                     + UIColor.MAIN_COLOR.getValue() + ";}" + " .jfx-slider > .thumb{" + CSSAttribute.BG_COLOR.getModifier()
                     + UIColor.TEXT_COLOR_DARK.getValue() + ";}" + " .jfx-slider > .animated-thumb{"
                     + CSSAttribute.BG_COLOR.getModifier() + "rgba(0,0,0,0);}" + " .jfx-slider .slider-value{"
                     + " -fx-fill: rgba(0,0,0,0); -fx-stroke: rgba(0,0,0,0);}";

        String file = CSS_DIR + "slider.css";

        addNewCSS(css, file, slider);

    }

    private static void updateComboBox(ComboBox comboBox) {
        String css = ".combo-box {" + CSSAttribute.BG_COLOR.getModifier() + UIColor.BG_COLOR.getValue() + ";"
                     + "-fx-border-color: " + UIColor.SECONDARY_COLOR.getValue() + ";" + CSSAttribute.TEXT_FILL.getModifier()
                     + UIColor.TEXT_COLOR.getValue() + ";}" +

                     ".combo-box .list-cell{" + CSSAttribute.BG_COLOR.getModifier() + UIColor.BG_COLOR.getValue() + ";"
                     + "-fx-border-color: " + UIColor.SECONDARY_COLOR.getValue() + ";" + CSSAttribute.TEXT_FILL.getModifier()
                     + UIColor.TEXT_COLOR.getValue() + ";}" +

                     ".combo-box .list-cell:hover{" + CSSAttribute.BG_COLOR.getModifier() + UIColor.SECONDARY_COLOR.getValue()
                     + ";" + CSSAttribute.TEXT_FILL.getModifier() + UIColor.TEXT_COLOR.getValue() + ";}" +

                     ".combo-box .arrow{" + CSSAttribute.BG_COLOR.getModifier() + UIColor.SECONDARY_COLOR.getValue() + ";}" +

                     ".combo-box .arrow-button{ -fx-border-style: none;}";

        String file = CSS_DIR + "combobox.css";

        addNewCSS(css, file, comboBox);
    }

    private static void updateTable(TableView tableView) {
        String css = String.format(".playlistView {\n" +
                                   "  -fx-background-color: %s;\n" +
                                   "  -fx-padding: 50px 40px 20px 20px;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".playlistTitle, .playlistInfo {\n" +
                                   "  -fx-text-fill: %s;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".playlistTable .column-header,\n" +
                                   ".playlistTable .column-header-background .filler {\n" +
                                   "  -fx-background-color: %s;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".playlistTable .column-header .label {\n" +
                                   "  -fx-text-fill: %s;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".playlistTable .cell {\n" +
                                   "  -fx-background-color: %s;\n" +
                                   "  -fx-text-fill: %s;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".table-row-cell .text {\n" +
                                   "  -fx-fill: %s;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".table-row-cell:selected .text {\n" +
                                   "  -fx-fill: %s;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".table-view .column-header-background .filler {\n" +
                                   "  -fx-background-color: %s;\n" +
                                   "  -fx-padding: 1em;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".scroll-bar:horizontal .track,\n" +
                                   ".scroll-bar:vertical .track,\n" +
                                   ".corner{\n" +
                                   "  -fx-background-color :%s;\n" +
                                   "  -fx-border-color : %s;\n" +
                                   "  -fx-background-radius : 0.0em;\n" +
                                   "  -fx-border-radius :2.0em;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".scroll-bar:horizontal .thumb,\n" +
                                   ".scroll-bar:vertical .thumb {\n" +
                                   "  -fx-background-color : %s;\n" +
                                   "  -fx-background-insets : 2.0, 0.0, 0.0;\n" +
                                   "  -fx-background-radius : 2.0em;\n" +
                                   "}\n" +
                                   "\n" +
                                   ".scroll-bar:horizontal .thumb:hover,\n" +
                                   ".scroll-bar:vertical .thumb:hover {\n" +
                                   "  -fx-background-color : %s;\n" +
                                   "  -fx-background-insets : 2.0, 0.0, 0.0;\n" +
                                   "  -fx-background-radius : 2.0em;\n" +
                                   "}", UIColor.BG_COLOR.getValue(), UIColor.TEXT_COLOR.getValue(),
                UIColor.BG_COLOR.getValue(), UIColor.TEXT_COLOR_DARK.getValue(), UIColor.BG_COLOR.getValue(),
                UIColor.TEXT_COLOR.getValue(), UIColor.TEXT_COLOR.getValue(), UIColor.MAIN_COLOR.getValue(),
                UIColor.BG_COLOR.getValue(), UIColor.BG_COLOR.getValue(), UIColor.BG_COLOR.getValue(),
                UIColor.BG_COLOR_DARK.getValue(), UIColor.BG_COLOR.getValue());

        String file = CSS_DIR + "table.css";

        addNewCSS(css, file, tableView);
    }

    private static void addNewCSS(String css, String file, Region r) {

        try (PrintWriter out = new PrintWriter(file)) {
            out.println(css);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scene sc = FXMain.getStaticScene();
        try {
            if (sc != null && sc.getStylesheets() != null && sc.getStylesheets().size() > 0) {
                sc.getStylesheets().remove(Paths.get(file).toUri().toURL().toString());
                sc.getStylesheets().add(Paths.get(file).toUri().toURL().toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static List<Map.Entry<Color, Long>> getAvg(Map<Color, Long> count) {
        return count.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
    }

    private static boolean isUsable(Color color) {
        return !isWhite(color) && !isBlack(color);
    }

    private static boolean isWhite(Color color) {
        return color.getRed() * 255 > 200 && color.getBlue() * 255 > 200 && color.getGreen() * 255 > 200;
    }

    private static boolean isBlack(Color color) {
        return color.getRed() * 255 < 55 && color.getBlue() * 255 < 55 && color.getGreen() * 255 < 55;
    }

    private static boolean isSupported(Region r) {
        return (r instanceof Pane || r instanceof Button || r instanceof Label || r instanceof Separator
                || r instanceof ComboBox || r instanceof ProgressBar || r instanceof TableView || r instanceof JFXSlider
                || r instanceof ScrollPane || r instanceof JFXToggleButton);
    }

    public static boolean isDarkMode() {
        return darkMode.get();
    }

    public static SimpleBooleanProperty darkModeProperty() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        StyleAssist.darkMode.set(darkMode);
    }

    static {
        darkModeProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                darkMode();
            } else {
                lightMode();
            }
        });
    }
}
