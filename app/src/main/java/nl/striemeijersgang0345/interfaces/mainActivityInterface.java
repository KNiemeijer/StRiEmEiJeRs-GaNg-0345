package nl.striemeijersgang0345.interfaces;

/**
 * Interface to communicate with the fragments
 */
public interface mainActivityInterface {

    /**
     * Method to go back in the webview.
     * Should be a simple goBack() call to the webview in javascript.
     */
    void back();

    /**
     * Paint the colours in the fragment's webview.
     * @param tekstKleur Colour of the text
     * @param achtergrondKleur Colour of the inner background.
     * @param randKleur Colour of the most outer background
     * @param knopKleur Colour of the buttons.
     */
    void paintColours(String tekstKleur, String achtergrondKleur, String randKleur, String knopKleur);
}
