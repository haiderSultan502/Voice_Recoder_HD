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
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skydoves.elasticviews.ElasticLayout;

import java.util.List;

import apps.webscare.voicerecoderhd.fragments.RecorderFragment;
import apps.webscare.voicerecoderhd.fragments.SettingFragment;
import apps.webscare.voicerecoderhd.fragments.TrackFragment;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;
import soup.neumorphism.NeumorphTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    FragmentTransaction fragmentTransaction;
    NeumorphCardView neumorphRecorderCardView,neumorphTrackCardView,neumorphSettingCardView;
    int IDArray[] = {R.id.btn_recorder,R.id.echo_icon,R.id.txt_recorder,R.id.btn_track,R.id.music_icon,R.id.txt_track,R.id.btn_setting,R.id.setting_icon,R.id.txt_setting};
    NeumorphCardView neumorphCardView;
    NeumorphImageView neumorphImageView;
    NeumorphTextView neumorphTextView;
    int btnBgColor,btnTxtColor;
    ElasticLayout elasticLayoutBtnRecorder,elasticLayoutBtnTracker,elasticLayoutBtnSetting;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Util.requestMultplePermission(this);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            Log.d("permission  granted", "onPermissionsChecked: permission  granted successfully" );

                            setContentView(R.layout.activity_main);
                            elasticLayoutBtnRecorder = findViewById(R.id.elastic_lyout_btn_recorder);
                            elasticLayoutBtnTracker = findViewById(R.id.elastic_lyout_btn_tracker);
                            elasticLayoutBtnSetting = findViewById(R.id.elastic_lyout_btn_setting);
//                            visualizer = view.findViewById(R.id.visulizer);
//                            visualizer.startListening();
                            elasticLayoutBtnRecorder.setOnClickListener(MainActivity.this);
                            elasticLayoutBtnTracker.setOnClickListener(MainActivity.this);
                            elasticLayoutBtnSetting.setOnClickListener(MainActivity.this);

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayout,new RecorderFragment());
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                            Log.d("permission  Denied", "onPermissionsChecked: permission  denied " );
//                            activity.finish();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();



//        viewBndings();

//        replaceFragmnet(new RecorderFragment());




    }

    private void viewBndings() {

//        neumorphRecorderCardView = findViewById(R.id.btn_recorder);
//        neumorphTrackCardView = findViewById(R.id.btn_track);
//        neumorphSettingCardView = findViewById(R.id.btn_setting);
//        elasticLayoutBtnRecorder = findViewById(R.id.elastic_lyout_btn_recorder);
//        elasticLayoutBtnTracker = findViewById(R.id.elastic_lyout_btn_tracker);
//        elasticLayoutBtnSetting = findViewById(R.id.elastic_lyout_btn_setting);

//        neumorphRecorderCardView.setOnClickListener(this);
//        neumorphTrackCardView.setOnClickListener(this);
//        neumorphSettingCardView.setOnClickListener(this);
//        elasticLayoutBtnRecorder.setOnClickListener(this);
//        elasticLayoutBtnTracker.setOnClickListener(this);
//        elasticLayoutBtnSetting.setOnClickListener(this);
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

            case R.id.elastic_lyout_btn_recorder:
                setBtn(0,"ON");
                setBtn(3,"OFF");
                setBtn(6,"OFF");
                replaceFragmnet(new RecorderFragment());
                break;
            case R.id.elastic_lyout_btn_tracker:
                setBtn(3,"ON");
                setBtn(0,"OFF");
                setBtn(6,"OFF");
                replaceFragmnet(new TrackFragment());
                break;
            case R.id.elastic_lyout_btn_setting:
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
            neumorphImageView.setColorFilter(getResources().getColor(btnTxtColor));  // Change fillColor of wave_anim vector
            neumorphTextView.setTextColor(getResources().getColor(btnTxtColor));

    }
}