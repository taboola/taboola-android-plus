package com.example.homescreenexample;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout root;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);

        Switch enableHomeScreenSwitch = findViewById(R.id.hsn_switch);

        enableHomeScreenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if (isChecked) {
                    showSnackBar();
                } else {
                    dismissSnackBar();
                }
                // Set true or false to enable or disable first screen handling
                TBHomeScreenNewsManager.getInstance().setHomeScreenEnabled(isChecked);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissSnackBar();
    }

    private void showSnackBar() {
        snackbar = Snackbar.make(root, R.string.hsn_hint, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void dismissSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }
}
