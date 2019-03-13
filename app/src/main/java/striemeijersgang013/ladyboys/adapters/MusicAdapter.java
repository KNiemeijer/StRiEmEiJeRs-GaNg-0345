package striemeijersgang013.ladyboys.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import striemeijersgang013.ladyboys.R;
import striemeijersgang013.ladyboys.utils.CacheManager;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public MusicAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = mData.get(position);
        text = text.substring(0, text.length()-4);
        holder.myTextView.setText(text);
        holder.myTextView.setTextColor(context.getResources().getColor(R.color.textColor));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.musicRow);
            myTextView.setOnClickListener(this);
            myTextView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            try {
                String path = "muziek/" + getItem(getAdapterPosition());

                Uri fileUri = CacheManager.getFileUri(context, path, getItem(getAdapterPosition()));

                // Create new intent
                Intent sendIntent = new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .setType(MimeTypeMap.getSingleton()
                                .getMimeTypeFromExtension("mp3"))
                        .putExtra(Intent.EXTRA_STREAM, fileUri);

                if (sendIntent.resolveActivity(context.getPackageManager()) != null)
                    context.startActivity(Intent.createChooser(sendIntent, "Verstuur je muziek"));
                else {
                    Log.d("resolveActivity", "No activity found to handle intent");
                    Toast.makeText(context.getApplicationContext(), "Het delen is mislukt.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context.getApplicationContext(), "Het delen is mislukt.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}