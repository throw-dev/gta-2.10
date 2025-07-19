package com.samp.mobile.game.ui;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.samp.mobile.R;

import java.util.Formatter;

public class LoadingScreen {

    private Activity activity;
    TextView percentText;
    private ConstraintLayout mainLayout;
    //ProgressBar progressbar2;

    public LoadingScreen(Activity activity)
    {
        this.activity = activity;

        mainLayout = (ConstraintLayout) activity.getLayoutInflater().inflate(R.layout.loadingscreen, null);
        activity.addContentView(mainLayout, new ConstraintLayout.LayoutParams(-1, -1));
    }

    public void hide()
    {
        mainLayout.setVisibility(View.INVISIBLE);
    }

    public void Update(int percent) {
        if (percent <= 100) {
            percentText.setText(new Formatter().format("%d%s", Integer.valueOf(percent), "%").toString());
            //progressbar2.setProgress(percent);
        }
    }
}