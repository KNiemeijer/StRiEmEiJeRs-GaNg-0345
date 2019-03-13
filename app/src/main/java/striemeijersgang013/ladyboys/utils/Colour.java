package striemeijersgang013.ladyboys.utils;

@Deprecated
class Colour {
    private static final Colour ourInstance = new Colour();

    static Colour getInstance() {
        return ourInstance;
    }

    private String theme;
    private MyThemes curTheme;

    private Colour() {
        theme = "Standaard";
    }

    public static void getColour(String which) {

    }

    void setTheme(String theme) {
        this.theme = theme;

        //if(theme.equals(StandaardThema.ThemeName))
            //curTheme = StandaardThema;
    }

    String getTheme() {
        return theme;
    }

    Boolean isStandardTheme() {
        return theme.equals("Standaard");
    }

    private static abstract class MyThemes {

    }

    private static class StandaardThema extends MyThemes {
        static String ThemeName = "Standaard";
        //static Color achtergrondKleur = new Color(Color.parseColor("#FFFFFF"));
    }

}
