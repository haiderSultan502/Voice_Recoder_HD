package apps.webscare.voicerecoderhd.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import apps.webscare.voicerecoderhd.MainActivity;
import apps.webscare.voicerecoderhd.R;
import apps.webscare.voicerecoderhd.models.ModelRecordings;
import soup.neumorphism.NeumorphFloatingActionButton;

public class RecorderFragment extends Fragment implements View.OnClickListener {


    View view;
    NeumorphFloatingActionButton btnStartRecording;
    static String filePath,audioFile;
    MediaRecorder recorder;
    Boolean recordingStartStatus=false;
    TextView timeCount;
    CountDownTimer countDownTimer;
    int second = -1,minute,hour;

    LottieAnimationView lottieAnimationView;

    public static int setSampleRate = 8000; // set as default
    public static String setRecordingFormat = "m4a";  // set as default
    public static int setEncodingBitRate = 48000;

    SharedPreferences sharedPreferences;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.recorder_fragment,container,false);

        initialization();
        viewBinds();

        return view;
    }

    private void initialization() {

        sharedPreferences = getActivity().getSharedPreferences("recordingInfo", Context.MODE_PRIVATE);
        setSampleRate = sharedPreferences.getInt("sampleRate",8000);
        setRecordingFormat = sharedPreferences.getString("recordingFormat","m4a");
        setEncodingBitRate = sharedPreferences.getInt("encodingBitRate",48000);
    }


    private void viewBinds() {

        btnStartRecording = view.findViewById(R.id.btn_record);
        timeCount = view.findViewById(R.id.time_count);
        lottieAnimationView = view.findViewById(R.id.audio_wave);

        btnStartRecording.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_record:
                if (recordingStartStatus == false){
                    Toast.makeText(getActivity(), "Recording Start", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.playAnimation();
                    btnStartRecording.setShapeType(2);
                    startRecording();
                    recordingStartStatus = true;
                }
                else {
                    Toast.makeText(getActivity(), "Recording End", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.pauseAnimation();
                    btnStartRecording.setShapeType(0);
                    stopRecording();
                    recordingStartStatus = false;
                }
                break;
        }

    }



    private void stopRecording() {

        //cancel the count down timer
        countDownTimer.cancel();
        second = -1;
        minute = 0;
        hour = 0;
        timeCount.setText("00:00:00");

        if (recorder != null)
        {
            recorder.release();
            recorder = null;
        }

        createContentValues();


    }

    private void createContentValues(){

        //creating content value object to store the data in table using  key value pair and here the key is column name,
        //and  the table in which we store data is  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI which is
        // simply insert data in database using content values

        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.DATA,filePath);
        values.put(MediaStore.Audio.Media.MIME_TYPE,"audio/mpeg4");
        values.put(MediaStore.Audio.Media.TITLE,audioFile);
        //store audio recorder file in the external content uri
        getActivity().getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,values);
    }

    private void startRecording() {

        createFolderToStoreRecording();


//        String uuid = UUID.randomUUID().toString();  //A class that represents an immutable universally unique identifier (UUID)
////        filePath = getActivity().getExternalCacheDir().getAbsolutePath() + "/" + uuid + ".3gp";
//        filePath = Environment.getExternalStorageDirectory().getPath() + "/" + uuid + ".3gp";
//        Log.d("file Location", "startRecording: " + filePath);



        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  //M4A is wave_anim file extension for an audio file encoded with advanced audio coding (AAC) which is wave_anim lossy compression. M4A stands for MPEG 4 Audio.
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioSamplingRate(setSampleRate);
        recorder.setAudioEncodingBitRate(setEncodingBitRate);
        Toast.makeText(getActivity(), "sample rate " + setSampleRate, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "encoding bitRate " + setEncodingBitRate, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "format " + setOutputFormat, Toast.LENGTH_SHORT).show();
        recorder.setOutputFile(filePath);


        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();
        
        showTimer();


    }

    private void showTimer() {

        countDownTimer = new CountDownTimer(Long.MAX_VALUE,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                second++;
                timeCount.setText(recorderTime());
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();
    }

    private String recorderTime() {
        if (second == 60){
            minute++;
            second = 0;
        }
        if (minute == 60){
            hour++;
            minute = 0;
        }

        return String.format("%02d:%02d:%02d",hour,minute,second);
    }

    private void createFolderToStoreRecording() {

        File myDirectory  = new File(Environment.getExternalStorageDirectory(),"VoiceRecorderHD");
        if (!myDirectory.exists()){
            myDirectory.mkdirs();
        }

        Date dates = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyyhhmmssSS");
        String date = dateFormat.format(new Date());
        audioFile = "REC" +  date;

        filePath = myDirectory.getAbsolutePath() + File.separator + audioFile + "."+ setRecordingFormat;

    }


}
