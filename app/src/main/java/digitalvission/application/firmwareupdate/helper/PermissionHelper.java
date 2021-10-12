package digitalvission.application.firmwareupdate.helper;

import static digitalvission.application.firmwareupdate.data.Constant.REQUEST_ID_CAMERA_PERMISSIONS;
import static digitalvission.application.firmwareupdate.data.Constant.REQUEST_ID_LOCATION_PERMISSIONS;
import static digitalvission.application.firmwareupdate.data.Constant.REQUEST_ID_MULTIPLE_PERMISSIONS;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionHelper {
//    SharedPreferenceService sharedPreferenceService;
    Activity activity;

    public PermissionHelper(Activity activity){
        this.activity = activity;
    }
    public boolean checkSinglePermission(String... permissions){
        return ContextCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_GRANTED;
    }

    public void checkAndRequestPermissions(int requestId, String... permissions) {

//        sharedPreferenceService = new SharedPreferenceService(activity);

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestId);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:

//                sharedPreferenceService.setAppFirstTime(false);

                Map<String, Integer> perms = new HashMap<>();

                for (String permission : permissions) {
                    perms.put(permission, PackageManager.PERMISSION_GRANTED);
                }

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    boolean allPermissionsGranted = true;
                    for (String permission1 : permissions) {
                        allPermissionsGranted = allPermissionsGranted && (perms.get(permission1) == PackageManager.PERMISSION_GRANTED);
                    }

                    if (allPermissionsGranted) {
                        Log.d(PermissionHelper.class.getSimpleName(), "onRequestPermissionsResult: all permissions granted");
                    } else {

                        boolean done = false;
                        while(!done) {
                            done = true;
                            for (String permission2 : perms.keySet()) {
                                if (perms.get(permission2) == PackageManager.PERMISSION_GRANTED) {
                                    done = false;
                                    perms.remove(permission2);
                                    break;
                                }
                            }
                        }

                        StringBuilder message = new StringBuilder("The app has not been granted permissions:\n\n");
                        for (String permission : perms.keySet()) {
                            message.append(permission);
                            message.append("\n");
                        }
                        message.append("\nHence, it cannot function properly." +
                                "\nPlease consider granting it this permission in phone Settings.");

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setTitle("Permission is required");
                        builder.setMessage(message);

                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }

                break;

            case REQUEST_ID_CAMERA_PERMISSIONS:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setTitle("Camera permission is required");
                    builder.setMessage("Camera permission is required to access : \n - QR code scanner.\n" + "\nHence, it cannot function properly." +
                            "\nPlease consider granting camera permission in phone Settings.");

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;

            case REQUEST_ID_LOCATION_PERMISSIONS:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setTitle("Location permission is required");
                    builder.setMessage("Location permission is required to access : \n - Controller Setup.\n - Add Sensor.\n" + "\nHence, it cannot function properly." +
                            "\nPlease consider granting location permission in phone Settings.");

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
        }
    }
}
