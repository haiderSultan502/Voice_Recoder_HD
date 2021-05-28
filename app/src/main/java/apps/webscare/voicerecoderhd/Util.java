package apps.webscare.voicerecoderhd;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class Util {

    public static void requestMultplePermission(final Activity activity) {

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

    }




}
