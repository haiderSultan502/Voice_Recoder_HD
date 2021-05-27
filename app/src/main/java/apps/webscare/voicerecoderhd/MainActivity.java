package apps.webscare.voicerecoderhd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import apps.webscare.voicerecoderhd.fragments.RecorderFragment;
import apps.webscare.voicerecoderhd.fragments.SettingFragment;
import apps.webscare.voicerecoderhd.fragments.TrackFragment;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;
import soup.neumorphism.NeumorphTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 100;
//    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 200;
//    private static final String[] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean audioRecordingPermissionGranted = false;


    FragmentTransaction fragmentTransaction;
    NeumorphCardView neumorphRecorderCardView,neumorphTrackCardView,neumorphSettingCardView;
    int IDArray[] = {R.id.btn_recorder,R.id.echo_icon,R.id.txt_recorder,R.id.btn_track,R.id.music_icon,R.id.txt_track,R.id.btn_setting,R.id.setting_icon,R.id.txt_setting};
    NeumorphCardView neumorphCardView;
    NeumorphImageView neumorphImageView;
    NeumorphTextView neumorphTextView;
    int btnBgColor,btnTxtColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewBndings();

        replaceFragmnet(new RecorderFragment());

        Util.requestMultplePermission(this);


//        Util.requestPermission(this,permissions[0],REQUEST_RECORD_AUDIO_PERMISSION);
//        Util.requestPermission(this,permissions[1],REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode)
//        {
//            case REQUEST_RECORD_AUDIO_PERMISSION:
//                audioRecordingPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                break;
//        }
//
//        if (!audioRecordingPermissionGranted){
//            finish();
//        }
//    }


    private void viewBndings() {

        neumorphRecorderCardView = findViewById(R.id.btn_recorder);
        neumorphTrackCardView = findViewById(R.id.btn_track);
        neumorphSettingCardView = findViewById(R.id.btn_setting);

        neumorphRecorderCardView.setOnClickListener(this);
        neumorphTrackCardView.setOnClickListener(this);
        neumorphSettingCardView.setOnClickListener(this);
    }

    private void replaceFragmnet(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.btn_recorder:
                setBtn(0,"ON");
                setBtn(3,"OFF");
                setBtn(6,"OFF");
                replaceFragmnet(new RecorderFragment());
                break;
            case R.id.btn_track:
                setBtn(3,"ON");
                setBtn(0,"OFF");
                setBtn(6,"OFF");
                replaceFragmnet(new TrackFragment());
                break;
            case R.id.btn_setting:
                setBtn(6,"ON");
                setBtn(0,"OFF");
                setBtn(3,"OFF");
                replaceFragmnet(new SettingFragment());
                break;


        }
    }


    private void setBtn(int index, String checkBtnStatus) {

        neumorphCardView = findViewById(IDArray[index]);
        neumorphImageView = findViewById(IDArray[index+1]);
        neumorphTextView = findViewById(IDArray[index+2]);

            if (checkBtnStatus == "ON")
            {
                neumorphCardView.setShapeType(2); // basin btn shape
                btnBgColor = R.color.buttonPressedColor;
                btnTxtColor = R.color.clickedBtntext;

            }else {
                neumorphCardView.setShapeType(0);  // flat btn shape
                btnBgColor = R.color.buttonUnPressecColor;
                btnTxtColor = R.color.unclickedBtntext;
            }

            neumorphCardView.setBackgroundColor(getResources().getColor(btnBgColor));
            neumorphImageView.setColorFilter(getResources().getColor(btnTxtColor));  // Change fillColor of a vector
            neumorphTextView.setTextColor(getResources().getColor(btnTxtColor));

    }
}