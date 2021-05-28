package apps.webscare.voicerecoderhd.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.recorder_fragment,container,false);

        viewBinds();

        return view;
    }



    private void viewBinds() {

        btnStartRecording = view.findViewById(R.id.btn_record);


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
                    btnStartRecording.setShapeType(2);
                    startRecording();
                    recordingStartStatus = true;
                }
                else {
                    Toast.makeText(getActivity(), "Recording Stop", Toast.LENGTH_SHORT).show();
                    btnStartRecording.setShapeType(0);
                    stopRecording();
                    recordingStartStatus = false;
                }
                break;
        }

    }



    private void stopRecording() {

        if (recorder != null)
        {
            recorder.release();
            recorder = null;
        }

        createContentValues();


    }

    private void createContentValues(){

        //creating content resolver and put the values
        ContentValues values = new ContentValues(); //content values  used to store a set of values that the ContentResolver can process.
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

        Toast.makeText(getActivity(), filePath, Toast.LENGTH_SHORT).show();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //M4A is a file extension for an audio file encoded with advanced audio coding (AAC) which is a lossy compression. M4A stands for MPEG 4 Audio.
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(filePath);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();


    }

    private void createFolderToStoreRecording() {

        File myDirectory  = new File(Environment.getExternalStorageDirectory(),"VoiceRecorderHD");
        if (!myDirectory.exists()){
            myDirectory.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmm");
        String date = dateFormat.format(new Date());
        audioFile = "REC" +  date;

        filePath = myDirectory.getAbsolutePath() + File.separator + audioFile + ".m4a" ;

    }


}
