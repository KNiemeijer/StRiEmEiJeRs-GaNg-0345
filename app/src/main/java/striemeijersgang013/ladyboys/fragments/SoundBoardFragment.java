package striemeijersgang013.ladyboys.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import striemeijersgang013.ladyboys.MainActivity;
import striemeijersgang013.ladyboys.R;
import striemeijersgang013.ladyboys.adapters.SoundBoardAdapter;
import striemeijersgang013.ladyboys.interfaces.mainActivityInterface;

public class SoundBoardFragment extends Fragment implements mainActivityInterface {

    private Context context;
    private final int HEADER_MESSAGE = 1;
    private final int RECYCLER_MESSAGE = 2;
    private final int LOADING_MESSAGE = 3;
    private Handler mHandler;
    private Runnable r;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.soundboard_layout, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Set up toolbar
        MainActivity parent = (MainActivity) context;
        parent.setupActionBar(view, getString(R.string.soundboard));

        ProgressBar loader = view.findViewById(R.id.soundboard_load);

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                LinearLayout ll = view.findViewById(R.id.soundboard_achtergrond);
                if(inputMessage.what == HEADER_MESSAGE) {
                    ll.addView((TextView) inputMessage.obj, ll.getChildCount());
                }
                if(inputMessage.what == RECYCLER_MESSAGE) {
                    ll.addView((RecyclerView) inputMessage.obj, ll.getChildCount());
                }
                if(inputMessage.what == LOADING_MESSAGE) {
                    loader.setVisibility(View.GONE);
                    mHandler.removeCallbacks(r);
                }
            }
        };

        r = () -> {
            ArrayList<String> wolmFiles = new ArrayList<String>() {{
                add("beeeeren.mp3");
                add("jawellll.mp3");
                add("naas.mp3");
                add("nee hoor!.mp3");
                add("neeee.mp3");
                add("neeeeger.mp3");
                add("netjes.mp3");
                add("smikkel smikkel smikkel.mp3");
            }};

            ArrayList<String> gijsFiles = new ArrayList<String>() {{
                add("de tandjes.mp3");
                add("gans.mp3");
                add("jullie mam.mp3");
                add("naas.mp3");
            }};

            ArrayList<String> mikeFiles = new ArrayList<String>() {{
                add("bloes.mp3");
                add("harses willem.mp3");
                add("kut willem.mp3");
                add("kut willem 2.mp3");
                add("moes.mp3");
            }};

            ArrayList<String> koenFiles = new ArrayList<String>() {{
                add("gratis snacks.mp3");
                add("ik hou helemaal niet van bier, ik haaaat bier.mp3");
                add("naassss.mp3");
                add("kut gijs.mp3");
                add("smikkel smikkel smikkel.mp3");
                add("hee willem kijk, een tempel!.mp3");
                add("hee gijs kijk, een foto van de koning!.mp3");
            }};
            ArrayList<String> kelfFiles = new ArrayList<String>() {{
                add("oooh sanidirect.mp3");
                add("oooh sanidirect 2.mp3");
            }};

            soundBoardFactory(view, wolmFiles, "Wolm", mHandler);
            soundBoardFactory(view, gijsFiles, "Guns", mHandler);
            soundBoardFactory(view, mikeFiles, "Mike", mHandler);
            soundBoardFactory(view, koenFiles, "Korn", mHandler);
            soundBoardFactory(view, kelfFiles, "Kelf", mHandler);

            // Kill loader
            mHandler.sendMessage(mHandler.obtainMessage(LOADING_MESSAGE));
        };
        mHandler.post(r);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        ((MainActivity) context).setActivityListener(SoundBoardFragment.this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void soundBoardFactory(View view, ArrayList<String> filenames, String name, Handler mHandler) {
        // Create new header
        TextView header = new TextView(view.getContext());
        header.setText(name);
        header.setTextSize(30);
        header.setAllCaps(true);
        header.setTextIsSelectable(true);
        header.setSelectAllOnFocus(true);
        header.setTextColor(getResources().getColor(R.color.textColor));
        header.setBackgroundColor(getResources().getColor(R.color.achtergrondkleur));
        header.setClickable(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        params.topMargin = 20;
        header.setLayoutParams(params);

        Message headerMessage = mHandler.obtainMessage(HEADER_MESSAGE, header);
        headerMessage.sendToTarget();

        // Create new Recyclerview
        RecyclerView recyclerView = new RecyclerView(view.getContext());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.achtergrondkleur));

        // Create FlexBoxLayout
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(view.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        recyclerView.setLayoutManager(layoutManager);

        // Create and set a new SoundBoardAdapter
        SoundBoardAdapter adapter = new SoundBoardAdapter(view.getContext(), filenames, name);
        recyclerView.setAdapter(adapter);

        Message recyclerMessage = mHandler.obtainMessage(RECYCLER_MESSAGE, recyclerView);
        recyclerMessage.sendToTarget();
    }

    public void back() {
        ((MainActivity) context).goBack();
    }

    public void paintColours(String tekstKleur, String randKleur, String achtergrondKleur, String knopKleur) { }
}
