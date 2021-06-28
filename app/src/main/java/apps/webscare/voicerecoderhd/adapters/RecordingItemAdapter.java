package apps.webscare.voicerecoderhd.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import apps.webscare.voicerecoderhd.MainActivity;
import apps.webscare.voicerecoderhd.R;
import apps.webscare.voicerecoderhd.fragments.RecorderFragment;
import apps.webscare.voicerecoderhd.fragments.TrackFragment;
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
    public static boolean playStatus = false;
    static int previousItemPosition,newItemPosition;
    TrackFragment trackFragment;
    static boolean sameRecordingPlayPauseStatus = false;
    public static boolean nextPreviousFromBtn = false;
    public static boolean btnPlayStatus,btnStopStatus,recordingCompleteStatus;


    public RecordingItemAdapter(Context context,ArrayList<ModelRecordings> audioArrayList){
        this.context = (MainActivity) context;
        this.audioArrayList = audioArrayList;
        player = new MediaPlayer();
        trackFragment = new TrackFragment();
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


                if (playStatus==false){

                    previousItemPosition = position;

                    row_index = position;
                    notifyDataSetChanged();  //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
                    onItemClickListener.onItemClick(position,v);
                    playStatus = true;

                } else {

                    newItemPosition = position;
                    if (previousItemPosition == newItemPosition )
                    {

                        Log.d("TAG", "onClick: " + sameRecordingPlayPauseStatus);

                        if (sameRecordingPlayPauseStatus == false){
                            holder.neumorphImageView.setImageResource(R.drawable.ic_play_icon);
                            trackFragment.stop();
                            sameRecordingPlayPauseStatus = true;
                        }else {
                            holder.neumorphImageView.setImageResource(R.drawable.ic_pause_icon_color);
                            trackFragment.play();
                            sameRecordingPlayPauseStatus = false;
                        }


                    }
                    else {


                        previousItemPosition = position;

                        row_index = position;
                        notifyDataSetChanged();  //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
                        onItemClickListener.onItemClick(position,v);

                        sameRecordingPlayPauseStatus = false;

//                        playStatus = false;
                    }

                }


            }
        });


        if (row_index == position){

            if (nextPreviousFromBtn == true){
                nextPreviousFromBtn = false;

                playStatus = true;
                previousItemPosition = position;

                sameRecordingPlayPauseStatus = false;
            }



            holder.cardViewParentItem.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_item_bg));
            holder.btnPlay.setShapeType(2);
            holder.btnPlay.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonPressedColor));
            holder.neumorphImageView.setImageResource(R.drawable.ic_pause_icon_color);

            if (btnPlayStatus == true){
                holder.neumorphImageView.setImageResource(R.drawable.ic_pause_icon_color);
                btnPlayStatus = false;
            }
            if (btnStopStatus == true){
                holder.neumorphImageView.setImageResource(R.drawable.ic_play_icon);
                btnStopStatus = false;
            }
            if (recordingCompleteStatus == true){
                holder.neumorphImageView.setImageResource(R.drawable.ic_play_icon);
                recordingCompleteStatus = false;
            }


        } else {

            holder.cardViewParentItem.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_recordingitemunpressedbg));
            holder.btnPlay.setShapeType(0);
            holder.btnPlay.setBackgroundColor(ContextCompat.getColor(context,R.color.recordingItemBgUnPressedColor));
            holder.neumorphImageView.setImageResource(R.drawable.ic_music);
        }

        holder.recordingPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.onCreateOptionsMenu();
                showPopup(v,position);
            }
        });

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

    private void showPopup(View v, final int pos) {

//        add oncreta option menu in main activity
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.recording_menu, popup.getMenu());


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.share:
                        Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();

                        return true;
                    case R.id.rename:
                        Toast.makeText(context, "rename", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.delete:
                        Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();

//                        File myDirectory  = Environment.getExternalStorageDirectory();
//                        File file = new File(myDirectory, "VoiceRecorderHD/" + audioArrayList.get(pos).getTitle() + "." + audioArrayList.get(pos).getFormat());
//                        boolean checkStatus = file.delete();

//                        String path = myDirectory.getAbsolutePath() + "VoiceRecorderHD/" + audioArrayList.get(pos).getTitle() + "." +  audioArrayList.get(pos).getFormat();

//                        Log.d("path", "onMenuItemClick: " + path);

//                        Uri urii = audioArrayList.get(pos).getDbRowUri();
//
//                        context.getContentResolver().delete(audioArrayList.get(pos).getDbRowUri(),null,null);

                        audioArrayList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos,audioArrayList.size());


//                        context.getContentResolver().notifyChange (uri, null);




                        return true;
                    default:
                        return false;
                }


            }
        });

        popup.show();

    }


//    private void setIconsVisible(Menu menu, boolean flag) {
//        // determine whether the menu is empty
//        if(menu != null) {
//            try {
//                // if not empty, get a reflection of setOptionalIconsVisible method menu
//                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//                // access the method violence
//                method.setAccessible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    class ItemViewHolder extends RecyclerView.ViewHolder
    {

        NeumorphCardView btnPlay;
        NeumorphImageView neumorphImageView;
        RelativeLayout cardViewParentItem;
        NeumorphTextView recordingName,dateAndTime,duration,recordingFormat;
        ImageButton recordingPopupMenu;




        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            btnPlay = itemView.findViewById(R.id.btn_play);
            neumorphImageView = itemView.findViewById(R.id.play_image);
            cardViewParentItem = itemView.findViewById(R.id.card_view_parent_item);
            recordingName = itemView.findViewById(R.id.recordingName);
            dateAndTime = itemView.findViewById(R.id.time_and_date);
            duration = itemView.findViewById(R.id.duration);
            recordingFormat = itemView.findViewById(R.id.recording_format);
            recordingPopupMenu = itemView.findViewById(R.id.recording_popup_menu);
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){

        this.onItemClickListener = onItemClickListener;

    }


    //this interface is make for getting the position of selected item in adapter
    public interface OnItemClickListener{
        void onItemClick(int pos, View v);
//        void onOptionsMenuChangeRequested();
    }


}
