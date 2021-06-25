package apps.webscare.voicerecoderhd;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class Util {


    Context context;

    private  SharedPreferences sharedPreferences;

    public Util(Context context){
        this.context = context;
    }

    public void requestMultplePermission(final Activity activity) {

        sharedPreferences = context.getSharedPreferences("recordingInfo", Context.MODE_PRIVATE);

//        if (sharedPreferences.getBoolean("recordingPermision",false  == false)){
//            Dexter.withActivity(activity).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
//                @Override
//                public void onPermissionGranted(PermissionGrantedResponse response) {
//                    sharedPreferences.edit().putBoolean("recordingPermision",true).commit();
//                }
//
//                @Override
//                public void onPermissionDenied(PermissionDeniedResponse response) {
//                    sharedPreferences.edit().putBoolean("recordingPermision",false).commit();
//                }
//
//                @Override
//                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                    token.continuePermissionRequest();
//                }
//            }).check();
//        }
//
//        if (sharedPreferences.getBoolean("writeExternalStoragePermission",false) == false){
//            Dexter.withActivity(activity).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
//                @Override
//                public void onPermissionGranted(PermissionGrantedResponse response) {
//                    sharedPreferences.edit().putBoolean("writeExternalStoragePermission",true).commit();
//                }
//
//                @Override
//                public void onPermissionDenied(PermissionDeniedResponse response) {
//                    sharedPreferences.edit().putBoolean("writeExternalStoragePermission",false).commit();
//                }
//
//                @Override
//                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                    token.continuePermissionRequest();
//                }
//            }).check();
//        }
//        if (sharedPreferences.getBoolean("readExternalStoragePermission",false == false)){
//            Dexter.withActivity(activity).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
//                @Override
//                public void onPermissionGranted(PermissionGrantedResponse response) {
//                    sharedPreferences.edit().putBoolean("readExternalStoragePermission",true).commit();
//                }
//
//                @Override
//                public void onPermissionDenied(PermissionDeniedResponse response) {
//                    sharedPreferences.edit().putBoolean("readExternalStoragePermission",false).commit();
//                }
//
//                @Override
//                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                    token.continuePermissionRequest();
//                }
//            }).check();
//        }


        Dexter.withActivity(activity)
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
                            sharedPreferences.edit().putBoolean("allPermissionGranted",true).commit();


                        }

                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                            Log.d("permission  Denied", "onPermissionsChecked: permission  denied " );
//                            activity.finish();

//                            sharedPreferences.edit().putBoolean("allPermissionGranted",false).commit();
                            showSettingsDialog();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);

    }




}
