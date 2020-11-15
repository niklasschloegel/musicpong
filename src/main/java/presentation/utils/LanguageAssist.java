package presentation.utils;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.*;

public class LanguageAssist {

    public enum SupportedLanguage {
        GERMAN(Locale.GERMAN, "Deutsch"), ENGLISH(Locale.ENGLISH, "English");

        private Locale locale;
        private String key;
        private static ObservableList<SupportedLanguage> allSupportedLanguages;

        SupportedLanguage(Locale locale, String key) {
            this.locale = locale;
            this.key = key;
            this.initSupportedList();
        }

        @Override
        public String toString() {
            return key;
        }

        public Locale getLocale() {
            return locale;
        }

        public static ObservableList<SupportedLanguage> getAllSupportedLanguages() {
            return allSupportedLanguages;
        }

        private void initSupportedList() {
            if (allSupportedLanguages == null) {
                List<SupportedLanguage> ls = new ArrayList<>();
                allSupportedLanguages = FXCollections.observableList(ls);
            }
            allSupportedLanguages.add(this);
        }
    }

    private static URL URL;

    static {
        try {
            URL = Paths.get(System.getProperty("user.dir") + "/assets/language").toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static final ClassLoader LOADER = new URLClassLoader(new URL[]{URL});

    private static Map<Region, String> textedItems = new HashMap<>();
    private static Map<TableColumn, String> tableColumns = new HashMap<>();
    private static ResourceBundle language;

    private static SimpleObjectProperty<SupportedLanguage> currentLanguage = new SimpleObjectProperty<>();

    /**
     * Use this method to set Text to GUI Elements regarding the language.properties
     * Bundle
     * <p>
     * Make sure to call {@link #loadLanguage(SupportedLanguage)} first in the
     * application's main classes init Method to load a language package avoiding
     * NullPointerExceptions
     * <p>
     * How to set language text: 1. Go to /assets/language and choose a unique key
     * for your text 2. Insert to every .properties file the key and the language
     * specific text 3. Use this Method to set the text
     *
     * @param labeledElement:    Element where the Text should be set
     * @param languageBundleKey: The key from the properties file
     */
    public static void initLanguage(@NotNull Labeled labeledElement, String languageBundleKey) {
        textedItems.put(labeledElement, languageBundleKey);
        changeGUIText(labeledElement, languageBundleKey);
    }

    /**
     * Same as {@link #initLanguage(Labeled, String)}, but only for TableColumn.
     *
     * @param labeledElement:    Element where the Text should be set
     * @param languageBundleKey: The key from the properties file
     */
    public static void initLanguage(@NotNull TableColumn labeledElement, String languageBundleKey) {
        tableColumns.put(labeledElement, languageBundleKey);
        changeGUIText(labeledElement, languageBundleKey);
    }

    /**
     * This Method changes the Language package, which results in Text translation
     * on every text element set through {@link #initLanguage(Labeled, String)}.
     *
     * @param supportedLanguage: Represents the language which should be set
     */
    public static void loadLanguage(SupportedLanguage supportedLanguage) {
        ResourceBundle.clearCache();
        language = ResourceBundle.getBundle("language", supportedLanguage.getLocale(), LOADER);
        currentLanguage.set(supportedLanguage);
        if (textedItems.size() > 0) {
            refreshLanguage();
        }
    }

    public static String getValue(String key) {
        return language.getString(key);
    }

    public static SupportedLanguage getCurrentLanguage() {
        return currentLanguage.get();
    }

    public static SimpleObjectProperty<SupportedLanguage> currentLanguageProperty() {
        return currentLanguage;
    }

    private static void refreshLanguage() {
        textedItems.forEach(LanguageAssist::changeGUIText);
    }

    private static void changeGUIText(Region region, String languageBundleKey) {
        Platform.runLater(() -> {
            if (region instanceof Button) {
                Button b = (Button) region;
                b.setText(language.getString(languageBundleKey));
            } else if (region instanceof Label) {
                Label l = (Label) region;
                l.setText(language.getString(languageBundleKey));
            }
        });
    }

    private static void changeGUIText(TableColumn tableColumn, String languageBundleKey) {
        Platform.runLater(() -> {
            tableColumn.setText(language.getString(languageBundleKey));
        });
    }
}
