package com.example.provider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();
    }

    private void getPermission() {
        if (XXPermissions.hasPermission(this, Permission.MANAGE_EXTERNAL_STORAGE)) {
            Log.i(TAG, "XXPermissions.hasPermission");
        } else {
            XXPermissions.with(this).permission(Permission.MANAGE_EXTERNAL_STORAGE).request(new OnPermission() {
                @Override
                public void hasPermission(List<String> granted, boolean all) {
                    if (all) {
                        Log.i(TAG, "XXPermissions.all Permission");
                    } else {
                        Log.i(TAG, "XXPermissions.part Permission");
                    }
                }

                @Override
                public void noPermission(List<String> denied, boolean never) {
                    if (never) {
                        Log.i(TAG, "XXPermissions.never Permission");
                    } else {
                        Log.i(TAG, "XXPermissions.fail Permission");
                    }
                    XXPermissions.startPermissionActivity(MainActivity.this, denied);
                }
            });
        }
    }
}