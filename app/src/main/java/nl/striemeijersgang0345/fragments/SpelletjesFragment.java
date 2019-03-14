package nl.striemeijersgang0345.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.striemeijersgang0345.MainActivity;
import nl.striemeijersgang0345.R;
import nl.striemeijersgang0345.interfaces.mainActivityInterface;

public class SpelletjesFragment extends Fragment implements mainActivityInterface {

    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spelletjes_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // setup actionbar
        MainActivity parent = (MainActivity) context;
        parent.setupActionBar(view, getString(R.string.spelletjes));

        Button button2048 = view.findViewById(R.id.button_2048);
        button2048.setOnClickListener(v -> startActivity(new Intent(context.getPackageName() + ".2048")));

        Button minesweeperButton = view.findViewById(R.id.mine_button);
        minesweeperButton.setOnClickListener(v ->  startActivity(new Intent(context.getPackageName() + ".mine")));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        ((MainActivity) context).setActivityListener(SpelletjesFragment.this);
    }

    public void back() {
        ((MainActivity)context).swapFragmentsFromMeer(new MeerFragment(), "Meer");
    }

    public void paintColours(String tekstKleur, String randKleur, String achtergrondKleur, String knopKleur) { }
}
