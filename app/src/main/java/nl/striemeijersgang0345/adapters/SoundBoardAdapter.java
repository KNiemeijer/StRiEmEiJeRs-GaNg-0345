package nl.striemeijersgang0345.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nl.striemeijersgang0345.R;
import nl.striemeijersgang0345.utils.CacheManager;

public class SoundBoardAdapter extends RecyclerView.Adapter<SoundBoardAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private String mPersoon;

    // data is passed into the constructor
    public SoundBoardAdapter(Context context, List<String> data, String persoon) {
        this.mPersoon = persoon;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewholder_sound, parent, false);
        return new ViewHolder(view, mPersoon);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = mData.get(position);
        text = text.substring(0, text.length()-4);
        holder.mButton.setText(text);
        holder.mButton.setBackgroundColor(
                android.graphics.Color.argb(120, getRandColor(), getRandColor(), getRandColor())
        );

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.mMediaPlayer != null)
            holder.mMediaPlayer.release();
    }

    private int getRandColor(){
        return (int) (Math.random() * 255);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    private String getItem(int id) {
        return mData.get(id);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        Button mButton;
        String persoon;
        MediaPlayer mMediaPlayer;

        ViewHolder(View itemView, String persoon) {
            super(itemView);
            this.persoon = persoon;
            mButton = itemView.findViewById(R.id.sound_button);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ViewGroup.LayoutParams lp = mButton.getLayoutParams();
            if(lp instanceof FlexboxLayoutManager.LayoutParams)
                ((FlexboxLayoutManager.LayoutParams) lp).setFlexGrow((float) Math.pow(Math.random(),2));
        }

        @Override
        public void onClick(View view) {
            if(mMediaPlayer != null)
                mMediaPlayer.release();
            mMediaPlayer = new MediaPlayer();
            try {
                String file = "soundboard/" + persoon + "/" + getItem(getAdapterPosition());
                Log.d("File: ", file);
                AssetFileDescriptor descriptor = mContext.getAssets().openFd(file);
                mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mMediaPlayer.prepare();
                mMediaPlayer.setVolume(1, 1);
                mMediaPlayer.start();
            } catch(IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.setOnCompletionListener(v -> mMediaPlayer.release() );
        }

        @Override
        public boolean onLongClick(View view) {
            try {
                String path = "soundboard/" + persoon + "/" + getItem(getAdapterPosition());

                Uri fileUri = CacheManager.getFileUri(mContext, path, getItem(getAdapterPosition()));

                // Create new intent
                Intent sendIntent = new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .setType(MimeTypeMap.getSingleton()
                                .getMimeTypeFromExtension("mp3"))
                        .putExtra(Intent.EXTRA_STREAM, fileUri);

                if (sendIntent.resolveActivity(mContext.getPackageManager()) != null)
                    mContext.startActivity(Intent.createChooser(sendIntent, "Verstuur je dblsgeluid"));
                else {
                    Log.d("resolveActivity", "No activity found to handle intent");
                    Toast.makeText(mContext.getApplicationContext(), "Het delen is mislukt.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), "Het delen is mislukt.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}