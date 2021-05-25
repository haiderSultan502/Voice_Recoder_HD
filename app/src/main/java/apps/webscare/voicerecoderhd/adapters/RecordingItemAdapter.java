package apps.webscare.voicerecoderhd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import apps.webscare.voicerecoderhd.MainActivity;
import apps.webscare.voicerecoderhd.R;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;

public class RecordingItemAdapter extends RecyclerView.Adapter<RecordingItemAdapter.ItemViewHolder> {

    View view;
    MainActivity context;
    Boolean checkRecordingStatus= false;


    public RecordingItemAdapter(Context context){
        this.context = (MainActivity) context;
    }

    @NonNull
    @Override
    public RecordingItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.recording_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordingItemAdapter.ItemViewHolder holder, int position) {

        holder.cardViewParentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkRecordingStatus)
                {
                    holder.cardViewParentItem.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_recordingitemunpressedbg));
                    holder.btnPlay.setShapeType(0);
                    holder.btnPlay.setBackgroundColor(ContextCompat.getColor(context,R.color.recordingItemBgUnPressedColor));
                    holder.neumorphImageView.setImageResource(R.drawable.ic_pause_icon);

                    checkRecordingStatus=false;
                }
                else
                {
                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    holder.cardViewParentItem.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_item_bg));
                    holder.btnPlay.setShapeType(2);
                    holder.btnPlay.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonPressedColor));
                    holder.neumorphImageView.setImageResource(R.drawable.ic_play_icon);

                    checkRecordingStatus=true;
                }



            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
    {

        NeumorphCardView btnPlay;
        NeumorphImageView neumorphImageView;
        RelativeLayout cardViewParentItem;




        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            btnPlay = itemView.findViewById(R.id.btn_play);
            neumorphImageView = itemView.findViewById(R.id.play_image);
            cardViewParentItem = itemView.findViewById(R.id.card_view_parent_item);

        }

    }
}
