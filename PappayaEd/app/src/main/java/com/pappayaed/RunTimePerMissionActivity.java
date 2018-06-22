package com.pappayaed;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class RunTimePerMissionActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String[] LOCATION_AND_CONTACTS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS};
    private static final int RC_LOCATION_CONTACTS_PERM = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_time_per_mission);


    }


    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void methodRequiresTwoPermission() {
//        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS)) {
            // Already have permission, do the thing
            // ...

            Toast.makeText(this, "Already Granted", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show();

            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
                    RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void onRequestPermission(View view) {

        methodRequiresTwoPermission();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        Toast.makeText(this, "P Grant", Toast.LENGTH_SHORT).show();

        if (EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS)) {
            // Already have permission, do the thing
            // ...

            Toast.makeText(this, "All per Granted Thank u", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {


        Toast.makeText(this, "Denied ", Toast.LENGTH_SHORT).show();

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, R.string.returned_from_app_settings_to_activity, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
