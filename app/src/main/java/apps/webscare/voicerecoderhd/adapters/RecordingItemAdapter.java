package apps.webscare.voicerecoderhd.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import apps.webscare.voicerecoderhd.MainActivity;
import apps.webscare.voicerecoderhd.R;
import apps.webscare.voicerecoderhd.fragments.RecorderFragment;
import apps.webscare.voicerecoderhd.models.ModelRecordings;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;
import soup.neumorphism.NeumorphTextView;

public class RecordingItemAdapter extends RecyclerView.Adapter<RecordingItemAdapter.ItemViewHolder> {

    View view;
    MainActivity context;
    ArrayList<ModelRecordings> audioArrayList;
    MediaPlayer player;
    OnItemClickListener onItemClickListener;
    public static int row_index = -1 ;
//    public static int rowIndex2 = -1;
    public boolean playStatus = false;


    public RecordingItemAdapter(Context context,ArrayList<ModelRecordings> audioArrayList){
        this.context = (MainActivity) context;
        this.audioArrayList = audioArrayList;
        player = new MediaPlayer();
    }

    @NonNull
    @Override
    public RecordingItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.recording_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordingItemAdapter.ItemViewHolder holder, final int position) {

        Log.d("notify", "onBindViewHolder: " + position);
        holder.cardViewParentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (playStatus == false){
//                    playStatus = true;
//                }
                row_index = position;
                notifyDataSetChanged();  //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
                onItemClickListener.onItemClick(position,v);


            }
        });

        if (row_index == position){


            holder.cardViewParentItem.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_item_bg));
            holder.btnPlay.setShapeType(2);
            holder.btnPlay.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonPressedColor));
            holder.neumorphImageView.setImageResource(R.drawable.ic_pause_icon_color);

        } else {

            holder.cardViewParentItem.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_recordingitemunpressedbg));
            holder.btnPlay.setShapeType(0);
            holder.btnPlay.setBackgroundColor(ContextCompat.getColor(context,R.color.recordingItemBgUnPressedColor));
            holder.neumorphImageView.setImageResource(R.drawable.ic_music);
        }

        holder.recordingName.setText(audioArrayList.get(position).getTitle());
        holder.dateAndTime.setText(audioArrayList.get(position).getDate());
        holder.duration.setText(audioArrayList.get(position).getDuration());
        holder.recordingFormat.setText(audioArrayList.get(position).getFormat());
    }

    @Override
    public int getItemCount() {
        return audioArrayList.size();
    }

    private void playRecording(Uri uri) {
        player = new MediaPlayer();
        try {
            player.setDataSource(context,uri);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayingRecording();
                }
            });

            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void stopPlayingRecording() {

        if (player!=null)
        {
            player.release();
            player = null;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
    {

        NeumorphCardView btnPlay;
        NeumorphImageView neumorphImageView;
        RelativeLayout cardViewParentItem;
        NeumorphTextView recordingName,dateAndTime,duration,recordingFormat;




        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            btnPlay = itemView.findViewById(R.id.btn_play);
            neumorphImageView = itemView.findViewById(R.id.play_image);
            cardViewParentItem = itemView.findViewById(R.id.card_view_parent_item);
            recordingName = itemView.findViewById(R.id.recordingName);
            dateAndTime = itemView.findViewById(R.id.time_and_date);
            duration = itemView.findViewById(R.id.duration);
            recordingFormat = itemView.findViewById(R.id.recording_format);
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){

        this.onItemClickListener = onItemClickListener;

    }
    //this interface is make for getting the position of selected item in adapter
    public interface OnItemClickListener{
        void onItemClick(int pos, View v);
    }
}
