package apps.webscare.voicerecoderhd.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import apps.webscare.voicerecoderhd.R;
import apps.webscare.voicerecoderhd.adapters.RecordingItemAdapter;

public class TrackFragment extends Fragment implements View.OnClickListener {

    View view;
    RecyclerView rvRecordings;
    ImageView btnPlayRecording;

    MediaPlayer player;
    Boolean recordingPlayStatus=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.track_fragment,container,false);

        viewBindings();
        setDataInRecyclerView();

        return view;
    }

    private void setDataInRecyclerView() {
        RecordingItemAdapter recordingItemAdapter = new RecordingItemAdapter(getActivity());
        rvRecordings.setAdapter(recordingItemAdapter);
    }

    private void viewBindings() {
        rvRecordings = view.findViewById(R.id.rv_recordings);
        btnPlayRecording = view.findViewById(R.id.recording_play_btn);

        btnPlayRecording.setOnClickListener(this);
        setRecyclerView();
    }

    private void setRecyclerView() {
        // set a LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rvRecordings.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
    }

    private void playRecording() {
        player = new MediaPlayer();
        try {
            player.setDataSource(RecorderFragment.fileName);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.recording_play_btn:
                if (recordingPlayStatus == false){
                    Toast.makeText(getActivity(), "Recording Play", Toast.LENGTH_SHORT).show();
                    playRecording();
                    recordingPlayStatus = true;
                }
                else {
                    Toast.makeText(getActivity(), "Recording Stop Playing", Toast.LENGTH_SHORT).show();
                    stopPlayingRecording();
                    recordingPlayStatus = false;
                }

        }
    }
}
