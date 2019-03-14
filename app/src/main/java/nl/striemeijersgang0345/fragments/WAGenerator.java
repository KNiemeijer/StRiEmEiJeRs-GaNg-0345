package nl.striemeijersgang0345.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.striemeijersgang0345.MainActivity;
import nl.striemeijersgang0345.R;
import nl.striemeijersgang0345.interfaces.mainActivityInterface;

public class WAGenerator extends Fragment implements mainActivityInterface,
        AdapterView.OnItemSelectedListener{

    private Context context;
    private int persoonId;
    private int nZinnen = 1;
    private TextView generatorText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        ((MainActivity) context).setActivityListener(WAGenerator.this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wagenerator_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity parent = (MainActivity) context;
        parent.setupActionBar(view, getString(R.string.WAgenerator));

        generatorText = view.findViewById(R.id.generatorText);
        generate();

        SeekBar mSeekbar = view.findViewById(R.id.wa_seekbar);
        nZinnen = mSeekbar.getProgress();
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 if(progress == 0) {
                     seekBar.setProgress(1);
                     progress = 1;
                 }
                nZinnen = progress;
                TextView progressText = view.findViewById(R.id.wa_text_seek);
                progressText.setText(String.format(Locale.ENGLISH, Integer.toString(progress), "%d"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        Spinner mSpinner = view.findViewById(R.id.generator_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.generator_personen, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setSelection(0, true); // Set default
        persoonId = 0;

        MaterialButton mButton = view.findViewById(R.id.comp_opnieuw);
        mButton.setOnClickListener(v -> generate());
    }

    public void back() {
        ((MainActivity)context).swapFragmentsFromMeer(new MeerFragment(), "Meer");
    }

    public void paintColours(String tekstKleur, String achtergrondKleur, String randKleur, String knopKleur) { }

    private void generate() {
        String persoon = id2Person();

        if(nZinnen > 0) {
            int linesInFile;
            InputStream in = null;
            BufferedReader br = null;
            LineNumberReader lnr = null;
            try {
                in = context.getResources().getAssets().open("WA_texts/WA_gentext_" + persoon + ".txt");
                br = new BufferedReader(new InputStreamReader(in));
                lnr = new LineNumberReader(br);

                try {
                    lnr.skip(Long.MAX_VALUE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                linesInFile = lnr.getLineNumber();
                Log.d("Lines in file", Integer.toString(linesInFile));
                lnr.close();
                br.close();
                in.close();

                StringBuilder output = new StringBuilder();
                if (linesInFile > 0) {
                    int anchor = (int) Math.floor(Math.random() * linesInFile);
//                    Random random = new Random();
//                    int anchor = (int) Math.round(random.nextGaussian() * nZinnen + 10); // From Gaussian distribution
                    int minZin = anchor - (int) Math.floor(nZinnen / 2.0);
                    int maxZin = anchor + (int) Math.ceil(nZinnen / 2.0);
                    if (anchor - nZinnen < 0) {
                        minZin = 0;
                        maxZin = nZinnen - 1;
                    } else if (anchor + nZinnen >= linesInFile) {
                        minZin = linesInFile - nZinnen + 1;
                        maxZin = linesInFile;
                    }

                    // Check if range is not greater than total line range
                    if ((maxZin - minZin) > linesInFile) {
                        minZin = 0;
                        maxZin = linesInFile;
                    }
                    Log.d("nZinnen", Integer.toString(nZinnen));
                    Log.d("anchor", Integer.toString(anchor));
                    Log.d("minZin", Integer.toString(minZin));
                    Log.d("maxZin", Integer.toString(maxZin));

                    in = context.getResources().getAssets().open("WA_texts/WA_gentext_" + persoon + ".txt");
                    br = new BufferedReader(new InputStreamReader(in));
                    String line;
                    for (int i = 0; i < maxZin; i++) {
                        line = br.readLine();
                        if (i >= minZin) {
                            if (line != null) {
                                output.append(line);
                                output.append("\n");
                            }
                        }
                    }
                }

                if (!output.toString().equals(""))
                    generatorText.setText(output.toString());
                else
                    throw new NullPointerException();
            } catch (IOException e) {
                e.printStackTrace();
                String res = "Oepsie woepsie! de app is stukkie wukkie! we sijn heul hard aan t werk dit te make." +
                        " mss kun je beter een andewe naam probere OwO \n\n" +
                        "Er is iets fout gegaan met het lezen van het bestand.\n\n" +
                        "Foutmelding: " + e.getMessage();
                generatorText.setText(res);
            } catch (NullPointerException e) {
                e.printStackTrace();
                String res = "Oepsie woepsie! de app is stukkie wukkie! we sijn heul hard aan t werk dit te make." +
                        " mss kun je beter een andewe naam probere OwO \n\n" +
                        "Ik kon niet de juiste regels uit het bestand lezen.";
                generatorText.setText(res);
            } finally {
                try {
                    if (in != null) in.close();
                    if (br != null) br.close();
                    if (lnr != null) lnr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String id2Person() {
        switch(persoonId) {
            case 1: return "Gijs Cunnen";
            case 2: return "Kevin Algera";
            case 3: return "Koen Niemeijer";
            case 4: return "Michiel Arts";
            case 5: return "Thijs van der Putten";
            case 6: return "Thomas Bardoel";
            case 7: return "Wescel Manders";
            case 8: return "Willem Smits";
            case 9: return "Yoram Carboex";
            default: return "";
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        persoonId = pos;
        generate();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
