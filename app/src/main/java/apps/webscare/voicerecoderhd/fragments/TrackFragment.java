package apps.webscare.voicerecoderhd.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import apps.webscare.voicerecoderhd.R;
import apps.webscare.voicerecoderhd.adapters.RecordingItemAdapter;
import apps.webscare.voicerecoderhd.models.ModelRecordings;

public class TrackFragment extends Fragment implements View.OnClickListener {

    View view,views;
    RecyclerView rvRecordings;
    static ImageView btnPlayRecording,btnStoprecording,nextRecording,previousRecording;
    SeekBar seekBar;
    int currentPosition,totalDuration,currentPositionInSec,totalDurationInSecond;

    TextView current,total;
    ConstraintLayout constraintLayout;

    public static  MediaPlayer player;

    ArrayList<ModelRecordings> audioArrayList;
    int audio_index = 0;
    Animation animSlideUp;

    private static boolean isRecordingPlay =  false;
    static RecordingItemAdapter recordingItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.track_fragment,container,false);

        viewBindings();
        initialization();

        getAudioRecordings();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                currentPosition = seekBar.getProgress();
                player.seekTo( (int) currentPosition );

            }
        });
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
                String recordingFormat = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));

                ModelRecordings modelRecordings = new ModelRecordings();
                modelRecordings.setTitle(title);
                File file = new File(data); //This constructor creates wave_anim new File instance by converting the given pathname string into an abstract pathname which helps to get the value when the file was last modified by calling file.lastModified()
                Date date = new Date(file.lastModified());
                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");

                modelRecordings.setDate(format.format(date));
                modelRecordings.setUri(Uri.parse(data));

                //fetch the audio duration using MediaMetadataRetriever class
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(data);

                String duration = timeConversion(Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));

                modelRecordings.setDuration(duration);

                String fileFomat = recordingFormat;
                modelRecordings.setFormat(fileFomat);

                audioArrayList.add(modelRecordings);
            } while(cursor.moveToNext());
        }
        recordingItemAdapter = new RecordingItemAdapter(getActivity(),audioArrayList);
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
    public String timeConversion(long milliseconds) {


        String audioTime;
        long hrs = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long mns = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long scs = TimeUnit.MILLISECONDS.toSeconds(milliseconds);

        if (hrs > 0) {
            audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            audioTime = String.format("%02d:%02d", mns, scs);
        }
        return audioTime;
    }

    private void playRecording(int pos) {

        player.reset();
        isRecordingPlay = true;

        btnStoprecording.setVisibility(View.VISIBLE);
        btnPlayRecording.setVisibility(View.GONE);


//        btnPlayRecording.setImageResource(R.drawable.ic_pause_recording);

        player = new MediaPlayer();
        try {
            player.setDataSource(getActivity(),audioArrayList.get(pos).getUri());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        audio_index = pos;

        setAudioProgress();

    }

    private void setAudioProgress() {

        currentPosition = player.getCurrentPosition();
        totalDuration = player.getDuration();


        currentPositionInSec = (int) TimeUnit.MILLISECONDS.toSeconds(currentPosition);
        totalDurationInSecond = (int) TimeUnit.MILLISECONDS.toSeconds(totalDuration);


        seekBar.setProgress(currentPositionInSec);
        seekBar.setMax(totalDurationInSecond);



        current.setText(timeConversion((long) currentPosition));
        total.setText(timeConversion((long) totalDuration));


        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                currentPosition = player.getCurrentPosition();
                currentPositionInSec = (int) TimeUnit.MILLISECONDS.toSeconds(currentPosition);
                current.setText(timeConversion((long) currentPosition));
                seekBar.setProgress(currentPositionInSec);
//                seekBar.setKeyProgressIncrement(1);
                handler.postDelayed(this,500);
//                int progress = seekBar.getProgress();
//                if (progress == totalDuration) {
////                    Toast.makeText(getActivity(), "Progress", Toast.LENGTH_SHORT).show();
//                }
            }
        };
        handler.postDelayed(runnable,500);

    }

    private int convertMillisecodToSecond(int millisecond){
        int second = (int) TimeUnit.MILLISECONDS.toSeconds(millisecond);
        return second;
    }

    private void initialization() {

        audioArrayList = new ArrayList<>();

        animSlideUp = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up);
        constraintLayout.startAnimation(animSlideUp);

        player = new MediaPlayer();

    }

    private void viewBindings() {

        constraintLayout = view.findViewById(R.id.parentConstrainLayout);
        rvRecordings = view.findViewById(R.id.rv_recordings);
        seekBar = view.findViewById(R.id.recording_progress);
        btnPlayRecording = view.findViewById(R.id.recording_play_btn);
        nextRecording = view.findViewById(R.id.next_recording);
        previousRecording = view.findViewById(R.id.previous_recording);
        btnStoprecording = view.findViewById(R.id.recording_stop_btn);
        current = view.findViewById(R.id.current);
        total = view.findViewById(R.id.total);

        btnPlayRecording.setOnClickListener(this);
        nextRecording.setOnClickListener(this);
        previousRecording.setOnClickListener(this);
        btnStoprecording.setOnClickListener(this);
        setRecyclerView();
    }

    private void setRecyclerView() {
        // set wave_anim LinearLayoutManager
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

                player.reset();

                if (audio_index == audioArrayList.size()-1){
                    audio_index = 0;
                    playRecording(audio_index);

                    updateDataAdapterItem();

                }else {
                    audio_index++;
                    playRecording(audio_index);

                    updateDataAdapterItem();
                }
                break;

            case R.id.previous_recording:

                player.reset();

                if (audio_index == 0){
                    audio_index = audioArrayList.size()-1;
                    playRecording(audio_index);

                    updateDataAdapterItem();
                }else {
                    audio_index--;
                    playRecording(audio_index);

                    updateDataAdapterItem();
                }

                break;




            case R.id.recording_play_btn:

                playRecordingCall();



                break;

            case R.id.recording_stop_btn:

                stopRecordingCall();


        }
    }

    public void stopRecordingCall() {

        if (isRecordingPlay)
        {
            player.pause();

            btnPlayRecording.setVisibility(View.VISIBLE);
            btnStoprecording.setVisibility(View.GONE);
            isRecordingPlay = false;

            RecordingItemAdapter.btnStopStatus =true;
            recordingItemAdapter.notifyDataSetChanged();
        }

    }

    public void playRecordingCall() {

        if (isRecordingPlay==false) {
            player.start();

            btnPlayRecording.setVisibility(View.GONE);
            btnStoprecording.setVisibility(View.VISIBLE);
            isRecordingPlay = true;

            RecordingItemAdapter.btnPlayStatus =true;
            recordingItemAdapter.notifyDataSetChanged();
        }



    }

    public void play(){

        if (isRecordingPlay==false) {
            player.start();

            btnPlayRecording.setVisibility(View.GONE);
            btnStoprecording.setVisibility(View.VISIBLE);
            isRecordingPlay = true;
        }
    }
    public void stop(){
        if (isRecordingPlay)
        {
            player.pause();

            btnPlayRecording.setVisibility(View.VISIBLE);
            btnStoprecording.setVisibility(View.GONE);
            isRecordingPlay = false;
        }
    }

    public void updateDataAdapterItem(){
        //these two lines work for making the previous item selected in recycler view
        RecordingItemAdapter.nextPreviousFromBtn = true;
        RecordingItemAdapter.row_index = audio_index;
        recordingItemAdapter.notifyDataSetChanged();  // without it next recording not show selected
    }


}
