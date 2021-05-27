package apps.webscare.voicerecoderhd.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.UUID;

import apps.webscare.voicerecoderhd.MainActivity;
import apps.webscare.voicerecoderhd.R;
import soup.neumorphism.NeumorphFloatingActionButton;

public class RecorderFragment extends Fragment implements View.OnClickListener {


    View view;
    NeumorphFloatingActionButton btnStartRecording;
    static String fileName;
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
    }

    private void startRecording() {


        String uuid = UUID.randomUUID().toString();  //A class that represents an immutable universally unique identifier (UUID)
        fileName = getActivity().getExternalCacheDir().getAbsolutePath() + "/" + uuid + ".3gp";
        Log.i("file Location", "startRecording: " + fileName);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(fileName);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();


    }




}
