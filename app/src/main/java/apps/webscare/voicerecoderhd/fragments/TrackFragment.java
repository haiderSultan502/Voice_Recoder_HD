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
import android.util.Log;
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
    static SeekBar seekBar;
    static double currentPosition = 0 ,totalDuration,currentPositionInSec,totalDurationInSecond;
    MediaMetadataRetriever retriever ;

    static boolean recFoundStatus = true;

    TextView current,total,recordingName;
    ConstraintLayout constraintLayout;

    public static  MediaPlayer player;

    ArrayList<ModelRecordings> audioArrayList;
    static int audio_index = 0;
    Animation animSlideUp;

    private static boolean isRecordingPlay =  false, isRecordingFinished = false;
    static RecordingItemAdapter recordingItemAdapter;

    static String title;
    static boolean firstTimePlay = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.track_fragment,container,false);

//        recordingItemAdapter.notifyDataSetChanged();
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

//            seekBar.setProgress((int) currentPosition);
//            seekBar.setMax((int) totalDuration);
//            current.setText(timeConversion((long) currentPosition));



//        setDataInRecyclerView();

        return view;
    }

    private void getAudioRecordings() {

        ContentResolver contentResolver = getActivity().getContentResolver();  //The Content Resolver is the single, global instance in your application that provides access to your (and other applications') content providers.

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // uri : uniform resource identifier provide the actual data that we want to access


        Cursor cursor = contentResolver.query(uri,null, MediaStore.Audio.Media.DATA + " like ?",new String[]{"%VoiceRecorderHD%"},null);

        Cursor cursor1 = contentResolver.query(uri,null, MediaStore.Audio.Media.DATA + " like ?",new String[]{"%content://%"},null,null); // this line for getting the uri of row that insert in table so we can delete the recording

        if (cursor != null && cursor1 != null && cursor.moveToFirst() && cursor1.moveToFirst()){
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String recordingFormat = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));



                try {
                    retriever.setDataSource(data);
                }catch (Exception e){
                    recFoundStatus = false;

                    Log.d("TAGG", "getAudioRecordings: " + e.getMessage());
                }
                //fetch the audio duration using MediaMetadataRetriever class

                if (recFoundStatus == false){

                    recFoundStatus = true;
                }
                else {
                    String duration = timeConversion(Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));

                    // below two lines for get row uri for delete the recording

                    String strRecordingRowUri = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Media.DATA));
                    Uri recordingRowUri = Uri.parse(strRecordingRowUri);

                    ModelRecordings modelRecordings = new ModelRecordings();
                    modelRecordings.setDbRowUri(recordingRowUri);
                    modelRecordings.setTitle(title);
                    File file = new File(data); //This constructor creates wave_anim new File instance by converting the given pathname string into an abstract pathname which helps to get the value when the file was last modified by calling file.lastModified()
                    Date date = new Date(file.lastModified());
                    SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");

                    modelRecordings.setDate(format.format(date));
                    modelRecordings.setUri(Uri.parse(data));

                    modelRecordings.setDuration(duration);

                    String fileFomat = recordingFormat;
                    modelRecordings.setFormat(fileFomat);

                    audioArrayList.add(modelRecordings);
                }



            } while(cursor.moveToNext() && cursor1.moveToNext());
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

        if (player != null){
            player.reset();
        }
        isRecordingPlay = true;

        firstTimePlay = true;





        btnStoprecording.setVisibility(View.VISIBLE);
        btnPlayRecording.setVisibility(View.GONE);

        title = audioArrayList.get(pos).getTitle();

        recordingName.setText(title);




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


        current.setText(timeConversion((long) currentPosition));
        total.setText(timeConversion((long) totalDuration));

//        currentPositionInSec = (int) TimeUnit.MILLISECONDS.toSeconds(currentPosition);
//        totalDurationInSecond = (int) TimeUnit.MILLISECONDS.toSeconds(totalDuration);
//
//
//        seekBar.setProgress(currentPositionInSec);

        seekBar.setMax((int) totalDuration);






        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                currentPosition = player.getCurrentPosition();
//                currentPositionInSec = (int) TimeUnit.MILLISECONDS.toSeconds(currentPosition);
                current.setText(timeConversion((long) currentPosition));
                seekBar.setProgress((int) currentPosition);
//                seekBar.setKeyProgressIncrement(1);
                handler.postDelayed(this,1000);
//                int progress = seekBar.getProgress();
//                if (progress == totalDuration) {
////                    Toast.makeText(getActivity(), "Progress", Toast.LENGTH_SHORT).show();
//                }
            }
        };
        handler.postDelayed(runnable,1000);


        resetSeekbar();


    }

    private void resetSeekbar() {

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

//                Toast.makeText(getActivity(), "Set seekbar", Toast.LENGTH_SHORT).show();
                seekBar.setProgress(0);
                player.reset();
                RecordingItemAdapter.playStatus = false;
                recordingItemAdapter.notifyDataSetChanged();

                RecordingItemAdapter.recordingCompleteStatus =true;
                recordingItemAdapter.notifyDataSetChanged();
//
                btnStoprecording.setVisibility(View.GONE);
                btnPlayRecording.setVisibility(View.VISIBLE);

                isRecordingFinished = true;


            }
        });
    }

    private void initialization() {

        retriever = new MediaMetadataRetriever();

        audioArrayList = new ArrayList<>();

        animSlideUp = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up);
        constraintLayout.startAnimation(animSlideUp);

//        if (player==null){
//            player = new MediaPlayer();
//        }
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
        recordingName = view.findViewById(R.id.recording_name);


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
            if (player != null){
                player.start();
            }

        }
        if (firstTimePlay == false){

            playRecording(audio_index);
            updateDataAdapterItem();

            firstTimePlay = true;
        }
        if (isRecordingFinished){
            playRecording(audio_index);
            isRecordingFinished = false;
            updateDataAdapterItem();
        }

        btnPlayRecording.setVisibility(View.GONE);
        btnStoprecording.setVisibility(View.VISIBLE);
        isRecordingPlay = true;

        RecordingItemAdapter.btnPlayStatus =true;
        recordingItemAdapter.notifyDataSetChanged();



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

            RecordingItemAdapter.btnStopStatus = true;
            recordingItemAdapter.notifyDataSetChanged();
        }
    }

    public void updateDataAdapterItem(){
        //these two lines work for making the previous item selected in recycler view
        RecordingItemAdapter.nextPreviousFromBtn = true;
        RecordingItemAdapter.row_index = audio_index;
        recordingItemAdapter.notifyDataSetChanged();  // without it next recording not show selected
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isRecordingPlay){
            player.pause();

        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if (player!=null){


            recordingName.setText(title);

            currentPosition = player.getCurrentPosition();
            totalDuration = player.getDuration();

            current.setText(timeConversion((long) currentPosition));
            total.setText(timeConversion((long) totalDuration));

            seekBar.setMax((int) totalDuration);
            seekBar.setProgress((int) currentPosition);

            isRecordingPlay = false;

            RecordingItemAdapter.btnStopStatus =true;
            recordingItemAdapter.notifyDataSetChanged();

            setAudioProgress();


        }
    }
}
