package apps.webscare.voicerecoderhd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.skydoves.elasticviews.ElasticLayout;

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
    public  SharedPreferences sharedPreferences;
    public  Util util;
    public  boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("recordingInfo", Context.MODE_PRIVATE);

        checkPermissionStatus();

        viewBndings();

        replaceFragmnet(new RecorderFragment());

    }

    public void checkPermissionStatus() {



        permissionGranted = sharedPreferences.getBoolean("allPermissionGranted",false);

        if (permissionGranted == false){
            util = new Util(this);
            util.requestMultplePermission(this);
        }
    }

//    @SuppressLint("RestrictedApi")
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.recording_menu, menu);
//
//        if(menu instanceof MenuBuilder){
//            MenuBuilder m = (MenuBuilder) menu;
//            m.setOptionalIconsVisible(true);
//        }
//
//        return true;
//    }


    private void viewBndings() {



//        neumorphRecorderCardView = findViewById(R.id.btn_recorder);
//        neumorphTrackCardView = findViewById(R.id.btn_track);
//        neumorphSettingCardView = findViewById(R.id.btn_setting);
        elasticLayoutBtnRecorder = findViewById(R.id.elastic_lyout_btn_recorder);
        elasticLayoutBtnTracker = findViewById(R.id.elastic_lyout_btn_tracker);
        elasticLayoutBtnSetting = findViewById(R.id.elastic_lyout_btn_setting);

//        neumorphRecorderCardView.setOnClickListener(this);
//        neumorphTrackCardView.setOnClickListener(this);
//        neumorphSettingCardView.setOnClickListener(this);
        elasticLayoutBtnRecorder.setOnClickListener(this);
        elasticLayoutBtnTracker.setOnClickListener(this);
        elasticLayoutBtnSetting.setOnClickListener(this);
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


                permissionGranted = sharedPreferences.getBoolean("allPermissionGranted",false);

                if (permissionGranted == false){
                    util = new Util(this);
                    util.requestMultplePermission(this);
                }
                else {
                    setBtn(3,"ON");
                    setBtn(0,"OFF");
                    setBtn(6,"OFF");
                    replaceFragmnet(new TrackFragment());
                }

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