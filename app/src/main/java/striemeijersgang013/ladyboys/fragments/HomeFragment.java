package striemeijersgang013.ladyboys.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import striemeijersgang013.ladyboys.MainActivity;
import striemeijersgang013.ladyboys.R;
import striemeijersgang013.ladyboys.interfaces.mainActivityInterface;

public class HomeFragment extends Fragment implements mainActivityInterface {

    private Context context;
    private ArrayList<String> backlog;
    private TextView title;
    private MaterialButton again;
    private LinearLayout options;
    private View view;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        ((MainActivity) context).setActivityListener(HomeFragment.this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_layout, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        MainActivity parent = (MainActivity) context;
        parent.setupActionBar(view, getString(R.string.app_name ));

        // Initialise vars
        title = view.findViewById(R.id.home_adviesText);
        again = view.findViewById(R.id.home_opnieuw);
        again.setOnClickListener(v -> createInitial());
        options = view.findViewById(R.id.home_options);

        createInitial();

        SwipeRefreshLayout refresh = view.findViewById(R.id.home_swiperefresh);
        refresh.setOnRefreshListener(() -> {
            createInitial();
            refresh.setRefreshing(false);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void back() {
        // Handle backlog is not empty, handle in fragment
        if(backlog.size() > 1) { // If there's something in the backlog, do:
            String goBackTo = backlog.get(backlog.size()-2);
            again.setVisibility(View.GONE);
            backlog.remove(backlog.size() - 1); // Remove last entry from backlog
            klik(goBackTo);
        }
        // Return to start screen if there is only one element
        else if(backlog.size() == 1)
            createInitial();
        // Return to activity if backlog is empty
        else
             ((MainActivity) context).goBack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintColours(String tekstKleur, String randKleur, String achtergrondKleur, String knopKleur) {

    }

    private void createInitial() {
        backlog = new ArrayList<>();
        title.setText(getString(R.string.situatie));
        title.setGravity(Gravity.CENTER);
        again.setVisibility(View.GONE);
        clearList();
        createOption("Mijn situatie is drank gerelateerd", "0");
        createOption("Ik weet niet zeker of sex met een man gay is", "1");
        createOption("Ik heb de boot gemist", "2");
        createOption("Doe mij maar de spreuk van de dag!", "13");
        createOption("Ik heb ernstig behoefte aan een willekeurige uitspraak van Willem z'n vader", "3");
        createOption("Moet ik met Willem op de scooter?", "7");
        createOption("Waar moet ik trouwen?", "9");
        createOption("Wat heb je liever?", "11");
        createOption("Wanneer is Gijs z'n feest?", "14");
    }

    private void clearList() {
        options.removeAllViews();
    }


    private void createOption(String text, String id) {
        Button button = new Button(new ContextThemeWrapper(context, R.style.options_button_style), null, R.style.options_button_style);
        button.setText(text);
        button.setTag(id);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(v -> klik((String)button.getTag()));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 20, 40);
        options.addView(button, params);
    }

    private void klik(String id) {
        if(backlog.size() == 0)
            backlog.add(id);
        else if (!backlog.get(backlog.size() - 1).equals(id))
            backlog.add(id);

        String[] ans = getAns(id);
        switch (ans[0]) {
            case "11": { lieverDan(); break; }
            case "12": { magIkBier(ans); break; }
            case "13": { spreuk(); break; }
            case "14": { geinzFeest(); break; }
            case "0": { pils(); break; }
            case "1": { ladyboy(ans); break; }
            case "2": { finalMessage("Geen nood, er komt altijd een volgende boot!!"); break; }
            case "3": { WolmVader(); break; }
            case "4": { finalMessage("Dan is het feest voorbij, mijn jongens"); break; }
            case "5": { finalMessage("Neeeeeee"); break; }
            case "6": { Flugel(ans); break; }
            case "7": { GriekseIJ(ans); break; }
            case "8": { finalMessage("Ja"); break; }
            case "9": { finalMessage("Je moet niet ver trouwen maar je moet dichtbij trouwen"); break; }
        }
    }

    private void finalMessage(String text) {
        clearList();
        title.setText(getString(R.string.advies));
        TextView t = new TextView(new ContextThemeWrapper(context, R.style.finalmessage_style), null, R.style.finalmessage_style);
        t.setText(text);
        options.addView(t);
        again.setVisibility(View.VISIBLE);
    }

    private static String[] getAns(String ans) {
        String[] arr = ans.split("\\.");
        ArrayList<String> arrlist = new ArrayList<>(Arrays.asList(arr));
        arrlist.add("");
        return arrlist.toArray(new String[0]);
    }

    // Individual cases
    @SuppressLint("SetTextI18n")
    private void pils() {
        clearList();
        title.setText("Wat is je situatie?");
        createOption("Er is iemand dronken", "4");
        createOption("Kun je van bier dronken worden?", "5");
        createOption("Mag ik flügel drinken?", "6");
        createOption("Kun je van bier je voortanden kwijtraken?", "8");
        createOption("Mag ik bier?", "12");
    }

    @SuppressLint("SetTextI18n")
    private void ladyboy(String[] ans) {
        String nietgay = "Je bent niet gay";
        String gay = "Je bent 100% gay";

        clearList();
        title.setText("Hadden jullie allebei je sokken aan?");
        createOption("Ja", "1.y");
        createOption("Nee", "1.n");

        if (ans[1].equals("y")) finalMessage(nietgay);
        if (ans[1].equals("n")) {
            clearList();
            title.setText("Raakten de ballen elkaar aan?");
            createOption("Ja", "1.n.y");
            createOption("Nee", "1.n.n");

            if (ans[2].equals("n")) finalMessage(nietgay);
            if (ans[2].equals("y")) {
                clearList();
                title.setText("Zei je direct daarvoor of daarna 'No homo'?");
                createOption("Ja", "1.n.y.y");
                createOption("Nee", "1.n.y.n");

                if (ans[3].equals("y")) finalMessage(nietgay);
                if (ans[3].equals("n")) {
                    clearList();
                    title.setText("Was het een ladyboy?");
                    createOption("Ja", "1.n.y.n.y");
                    createOption("Nee", "1.n.y.n.n");

                    if (ans[4].equals("y")) finalMessage(nietgay);
                    if (ans[4].equals("n")) {
                        clearList();
                        title.setText("Heet je Yoram?");
                        createOption("Ja", "1.n.y.n.n.y");
                        createOption("Nee", "1.n.y.n.n.n");

                        if (ans[5].equals("n")) finalMessage(gay);
                        if (ans[5].equals("y")) finalMessage("Je bent 50% gay");
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void WolmVader() {
        clearList();
        String[] uitspraken = {
                "Als je dronken bent, is het feest voorbij!",
                "Denk eraan mijn jongens, school heeft prioriteit!",
                "Mijn jongens, wie kan mij vertellen hoeveel bar er in een vrachtwagenband zit?",
                "Niet dronken worden, mijn jongens!",
                "Als er iemand dronken wordt, is het feest voorbij!",
                "*slaat hond*"
        };

        int n = uitspraken.length;
        int prob = (int) Math.floor(Math.random() * n);
        finalMessage(uitspraken[prob]);
    }

    @SuppressLint("SetTextI18n")
    private void Flugel(String[] ans) {
        clearList();
        title.setText("Heet je Willem?");
        createOption("Ja", "6.y");
        createOption("Nee", "6.n");

        if (ans[1].equals("y")) {
            finalMessage("Neeeeeee dat mag niet!");
        }
        if (ans[1].equals("n")) {
            clearList();
            if(ans.length == 3) {
                Toast.makeText(context.getApplicationContext(), "Niet lieggen Willem!", Toast.LENGTH_SHORT).show();
            }
            title.setText("Neem je het mee naar Willem's verjaardag?");
            createOption("Ja", "6.n.y");
            createOption("Nee", "6.n.n");

            if (ans[2].equals("n")) finalMessage("Ja je mag flügel meenemen");
            if (ans[2].equals("y")) {
                clearList();
                title.setText("Wil je graag het huis uitgezet worden door z'n kobold?");
                createOption("Ja", "6.n.y.y");
                createOption("Nee", "6.n.y.n");

                if (ans[3].equals("y")) finalMessage("Ja je mag flügel meenemen");
                if (ans[3].equals("n")) {
                    finalMessage("Neeeeeee dat mag niet!");
                    Toast.makeText(context.getApplicationContext(), "Kut Willem", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void GriekseIJ(String[] ans) {
        clearList();
        title.setText("Begint je naam met een Griekse ij of heet je Willem?");
        createOption("Ja", "7.y");
        createOption("Nee", "7.n");

        if (ans[1].equals("y")) {
            Toast.makeText(context.getApplicationContext(), "Harses Willem", Toast.LENGTH_SHORT).show();
            finalMessage("Je moet met Willem op de scooter");
        }
        if (ans[1].equals("n")) finalMessage("Nee");
    }

    @SuppressLint("SetTextI18n")
    private void lieverDan() {
        clearList();
        title.setText("Waar gaat het over?");

        EditText edit = new EditText(context);
        edit.setHint("Typ in wat je liever hebt...");
        edit.setTag("lieverEdit");
        edit.setTextSize(30);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        edit.setGravity(Gravity.CENTER);
        int color = getResources().getColor(R.color.colorPrimary);
        edit.setBackgroundTintList(ColorStateList.valueOf(color));
        params.setMargins(50, 50, 50, 50);
        options.addView(edit, params);

        Button volgende = new MaterialButton(new ContextThemeWrapper(context, R.style.opnieuw_style), null, R.style.opnieuw_style);
        volgende.setText("Volgende");
        volgende.setTextColor(getResources().getColor(R.color.text_white));
        volgende.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        volgende.setGravity(Gravity.CENTER);
        volgende.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(edit.getText()))
                lieverText(edit.getText().toString());
        });
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 50, 30, 20);
        edit.setGravity(Gravity.CENTER);
        options.addView(volgende, params);

    }

    private void lieverText(String text) {
        text = text.toLowerCase();
        String[] arr = text.split(" ");
        final Set<String> list = new HashSet<>(Arrays.asList(
                arr
        ));

        if (list.contains("bier"))
            Toast.makeText(context.getApplicationContext(), "Lekker hoor!", Toast.LENGTH_SHORT).show();
        if (list.contains("willem"))
            finalMessage("Liever geen " + text + " dan " + /*TextUtils.join("  ", arr)*/ text  + "!!");
        else if (list.contains("wolm"))
            finalMessage("Liever geen " + text + " dan " + text + "!!");
        else if (arr[0].equals("een")) {
            if (arr.length > 3 && !arr[2].equals("beetje"))
                finalMessage("Liever " + text + " dan helemaal geen " + arr[arr.length - 2] + " " + arr[arr.length - 1] + "!!");
            else
                finalMessage("Liever " + text + " dan helemaal geen " + arr[arr.length - 1] + "!!");
        } else if (arr[0].equals("in")) {
            if (arr[arr.length - 2].equals("de") || arr[arr.length - 2].equals("het") || arr[arr.length - 2].equals("een"))
                finalMessage("Liever " + text + " dan helemaal niet in " + arr[arr.length - 2] + " " + arr[arr.length - 1] + "!!");
            else
                finalMessage("Liever " + text + " dan helemaal niet in " + arr[arr.length - 1] + "!!");
        } else if (list.contains("geen") || list.contains("niet"))
            finalMessage("Liever geen " + arr[arr.length - 1] + " dan een heleboel " + arr[arr.length - 1] + "!!");
        else
            finalMessage("Liever een beetje " + text + " dan helemaal geen " + text + "!!");
    }

    @SuppressLint("SetTextI18n")
    private void magIkBier(String[] ans) {
        clearList();
        title.setText("Heet je Michiel?");
        createOption("Ja", "12.y");
        createOption("Nee", "12.n");

        if (ans[1].equals("y")) finalMessage("Neeeeeee, je mag geen bier.");
        if (ans[1].equals("n")) {
            clearList();
            title.setText("Ben je de pilsverzamelaar?");
            createOption("Ja", "12.n.y");
            createOption("Nee", "12.n.n");

            if (ans[2].equals("y")) finalMessage("Dan mag je zeker bier!");
            if (ans[2].equals("n")) finalMessage("Alleen als je het heel graag wil, hoor!");
        }
    }

    @SuppressLint("SetTextI18n")
    private void spreuk() {
        String[] spreuken = {
                "Beter een klein bakje water, dan helemaal geen bakje water!",
                "Beter een gratis lul in je kont, dan een Thaise kech die stinkt naar stront.",
                "Als niks moet, kan alles!",
                "Te veel haast is niet oké, want dan lig je op de IC.",
                "Een gat is een gat.",
                "Feesten als gekken!",
                "Ik hou helemaal niet van bier, ik haat bier!!",
                "Meer dan je best kun je niet doen.",
                "Bijzijn is meemaken.",
                "Maar ja, zo is het leeeeven hè.",
                "*tfoe*",
                "Wij betalen helemaal nergens meer voor!",
                "Het leven is een feest, maar je moet zelf de slingers ophangen!",
                "Maximaal lenen is maximaal leven.",
                "Het is misschien niet de slechtste beslissing ooit van God om Willem een mongool te maken.",
                "Yeah, let's go.",
                "Denk eraan mijn jongens, we hebben maar één planeet!",
                "Bijna telt alleen bij koersbal.",
                "Als ze oud genoeg is om te schommelen kan ze ook wippen.",
                "T leave is gein ponykamp",
                "Zeg nou zelf, wat is belangrijker: nieuwe stage of nieuwe poeni?",
                "Boven de elf bepaalt kelf.",
                "Gang is alles remmen is angst.",
                "Een, twee, drie hoedje van papier.",
                "Het is pas feest als zanger Bas is geweest.",
                "Het hoeft niet lekker te zijn als het maar veel is."
        };

        String[] auteurs = {
                "Cpt. Yoramir",
                "Korn",
                "De twee Mallorca Kechs",
                "Thomans",
                "Yorm",
                "Kevin Kelvin Strandbaas",
                "Korn",
                "Kelf",
                "Kevin Kelvin Strandbaas",
                "Yorm",
                "Kelf",
                "StRiEmEiJeRs GaNg",
                "",
                "Kevin",
                "Magic Mike",
                "Kapitein Yoramir",
                "Yorm",
                "Gijs z'n euma",
                "De Kelfstick",
                "Kelf",
                "Yoram Carboex",
                "Kelf",
                "Skileraar Kevin",
                "Dbl Yoramir",
                "Zanger Bas",
                "StRiEmEiJeRs GaNg"
        };

        int n = spreuken.length;
        double millis = System.currentTimeMillis() + (1000 * 60 * 60);
        double weekTime = (1000 * 60 * 60 * 24);
        int prob = (int) Math.floor(millis / weekTime) % n;

        clearList();
        title.setText("De spreuk van de dag is: ");

        this.getLayoutInflater().inflate(R.layout.spreuklayout, view.findViewById(R.id.home_options));

        TextView spreuk = view.findViewById(R.id.spreuk);
        TextView auteur = view.findViewById(R.id.auteur);

        spreuk.setText(spreuken[prob]);
        auteur.setText(auteurs[prob]);

        Typeface pacifico = Typeface.createFromAsset(context.getAssets(),  "fonts/Pacifico-Regular.ttf");
        spreuk.setTypeface(pacifico, Typeface.ITALIC);
        auteur.setTypeface(pacifico, Typeface.ITALIC);
        again.setVisibility(View.VISIBLE);
    }

    private void geinzFeest() {
        int saturday = Calendar.SATURDAY;
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int feestMonth = Calendar.getInstance().get(Calendar.MONTH);
        int feestYear = Calendar.getInstance().get(Calendar.YEAR);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, feestMonth);
        int daysInMonth = calendar.getActualMaximum(Calendar.DATE);
        int monthsInYear = Calendar.getInstance().getMaximum(Calendar.MONTH);

        int daysTillSaturday = saturday - dayOfWeek;
        int feestDay = dayOfMonth + daysTillSaturday;

        if(feestDay > daysInMonth) {
            feestDay = feestDay - daysInMonth;
            feestMonth++;
        }

        if(feestMonth > monthsInYear) {
            feestMonth = Calendar.getInstance().getMinimum(Calendar.MONTH);
            feestYear++;
        }

        Log.d("saturday", Integer.toString(saturday));
        Log.d("dayOfWeek", Integer.toString(dayOfWeek));
        Log.d("dayOfMonth", Integer.toString(dayOfMonth));
        Log.d("daysInMonth", Integer.toString(daysInMonth));
        Log.d("monthsInYear", Integer.toString(monthsInYear));
        Log.d("feestMonth", Integer.toString(feestMonth));
        Log.d("daysTillSaturday", Integer.toString(daysTillSaturday));
        Log.d("feestDay", Integer.toString(feestDay));

        String feestMonthInLetters;

        switch(feestMonth) {
            case 0: { feestMonthInLetters = "januari"; break; }
            case 1: { feestMonthInLetters = "februari"; break; }
            case 2: { feestMonthInLetters = "maart"; break; }
            case 3: { feestMonthInLetters = "april"; break; }
            case 4: { feestMonthInLetters = "mei"; break; }
            case 5: { feestMonthInLetters = "juni"; break; }
            case 6: { feestMonthInLetters = "juli"; break; }
            case 7: { feestMonthInLetters = "augustus"; break; }
            case 8: { feestMonthInLetters = "september"; break; }
            case 9: { feestMonthInLetters = "oktober"; break; }
            case 10: { feestMonthInLetters = "november"; break; }
            case 11: { feestMonthInLetters = "december"; break; }
            default: feestMonthInLetters = "van een of andere maand in";
        }

        if(daysTillSaturday == 0)
            finalMessage("Geiz z'n feest is vandaag!");
        else
            finalMessage(
                    "Geiz z'n feest is op zaterdag " +
                            feestDay + " " +
                            feestMonthInLetters + " " +
                            feestYear
            );
    }
}
