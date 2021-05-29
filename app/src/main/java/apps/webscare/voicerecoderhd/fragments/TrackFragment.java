package apps.webscare.voicerecoderhd.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import apps.webscare.voicerecoderhd.R;
import apps.webscare.voicerecoderhd.adapters.RecordingItemAdapter;
import apps.webscare.voicerecoderhd.models.ModelRecordings;

public class TrackFragment extends Fragment implements View.OnClickListener {

    View view,views;
    RecyclerView rvRecordings;
    ImageView btnPlayRecording,nextRecording,previousRecording;

    MediaPlayer player;
    Boolean recordingPlayStatus=false;

    ArrayList<ModelRecordings> audioArrayList;
    int audio_index = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.track_fragment,container,false);


        viewBindings();
        initialization();
        getAudioRecordings();
//        setDataInRecyclerView();

        return view;
    }

    private void getAudioRecordings() {

        ContentResolver contentResolver = getActivity().getContentResolver();  //The Content Resolver is the single, global instance in your application that provides access to your (and other applications') content providers.

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // uri : uniform resource identifier provide the actual data that we want to access

        Cursor cursor = contentResolver.query(uri,null, MediaStore.Audio.Media.DATA + " like ?",new String[]{"%VoiceRecorderHD%"},null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String type = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
                ModelRecordings modelRecordings = new ModelRecordings();
                modelRecordings.setTitle(title);
                File file = new File(data); //This constructor creates a new File instance by converting the given pathname string into an abstract pathname which helps to get the value when the file was last modified by calling file.lastModified()
                Date date = new Date(file.lastModified());
                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");

                modelRecordings.setDate(format.format(date));
                modelRecordings.setUri(Uri.parse(data));

                //fetch the audio duration using MediaMetadataRetriever class
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(data);

                modelRecordings.setDuration(timeConversion(Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))));

                audioArrayList.add(modelRecordings);
            } while(cursor.moveToNext());
        }
        RecordingItemAdapter recordingItemAdapter = new RecordingItemAdapter(getActivity(),audioArrayList);
        rvRecordings.setAdapter(recordingItemAdapter);

        recordingItemAdapter.setOnItemClickListener(new RecordingItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                views = v;
                playRecording(pos);


            }
        });
    }

    //time conversion
    public String timeConversion(long value) {
        String audioTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            audioTime = String.format("%02d:%02d", mns, scs);
        }
        return audioTime;
    }

    private void playRecording(int pos) {

        player = new MediaPlayer();
        try {
            player.setDataSource(getActivity(),audioArrayList.get(pos).getUri());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        audio_index = pos;

    }

    private void setDataInRecyclerView() {

    }
    private void initialization() {

        audioArrayList = new ArrayList<>();

        player = new MediaPlayer();

    }

    private void viewBindings() {
        rvRecordings = view.findViewById(R.id.rv_recordings);
        btnPlayRecording = view.findViewById(R.id.recording_play_btn);
        nextRecording = view.findViewById(R.id.next_recording);
        previousRecording = view.findViewById(R.id.previous_recording);

        btnPlayRecording.setOnClickListener(this);
        nextRecording.setOnClickListener(this);
        previousRecording.setOnClickListener(this);
        setRecyclerView();
    }

    private void setRecyclerView() {
        // set a LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rvRecordings.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
    }

//    private void playRecording() {
//        player = new MediaPlayer();
//        try {
//            player.setDataSource(RecorderFragment.filePath);
//
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    stopPlayingRecording();
//                }
//            });
//
//            player.prepare();
//            player.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private void stopPlayingRecording() {
//
//        if (player!=null)
//        {
//            player.release();
//            player = null;
//        }
//
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

            case R.id.next_recording:

                if (audio_index == audioArrayList.size()-1){
                    audio_index = 0;
                    playRecording(audio_index);
                }else {
                    audio_index++;
                    playRecording(audio_index);
                }
                break;

            case R.id.previous_recording:

                if (audio_index == 0){
                    audio_index = audioArrayList.size()-1;
                    playRecording(audio_index);
                }else {
                    audio_index--;
                    playRecording(audio_index);
                }

                break;




//            case R.id.recording_play_btn:
//                if (recordingPlayStatus == false){
//                    Toast.makeText(getActivity(), "Recording Play", Toast.LENGTH_SHORT).show();
//                    playRecording();
//                    recordingPlayStatus = true;
//                }
//                else {
//                    Toast.makeText(getActivity(), "Recording Stop Playing", Toast.LENGTH_SHORT).show();
//                    stopPlayingRecording();
//                    recordingPlayStatus = false;
//                }

        }
    }
}
