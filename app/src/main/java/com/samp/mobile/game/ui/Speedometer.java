package com.samp.mobile.game.ui;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.*;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.samp.mobile.R;
import com.samp.mobile.launcher.util.Util;
import com.samp.mobile.game.ui.SeekArc;
import com.nvidia.devtech.NvEventQueueActivity;

import java.util.Formatter;

public class Speedometer {
    public Activity activity;
    public TextView mCarHP;
    public ImageView mStrela;
    public ImageView mStrela2;
    public ImageView mEngine;
    public TextView mFuel;
    public ConstraintLayout mInputLayout;
    public ImageView mLight;
    public ImageView mLock;
    public ImageView mBG;
    public ImageView mBG2;
    public TextView mMileage;
    public TextView mSpeed;
    public SeekArc mSpeedLine;
    static final int BUTTON_TURN_LEFT = 1;
    static final int BUTTON_TURN_RIGHT = 2;
    static final int BUTTON_TURN_ALL = 3;
    static final int BUTTON_ENGINE = 4;
    static final int BUTTON_LIGHT = 5;
    static final int BUTTON_DOOR = 6;

    static final int TURN_SIGNAL_LEFT = 1;
    static final int TURN_SIGNAL_RIGHT = 2;
    static final int TURN_SIGNAL_ALL = 3;
    Thread timer_turnlight_left = null;
    Thread timer_turnlight_right = null;
    Thread timer_turnlight_all = null;

    boolean turnlight_left_state;
    boolean turnlight_right_state;
    boolean turnlight_all_state;

    int turnlight_tick_sound_1;
    int turnlight_tick_sound_2;
    public Speedometer(Activity activity){
        this.activity = activity;
        ConstraintLayout relativeLayout = activity.findViewById(R.id.speedometer);
        mInputLayout = relativeLayout;
        mBG = activity.findViewById(R.id.imageView5);
        mBG2 = activity.findViewById(R.id.blinker);
        mSpeed = activity.findViewById(R.id.speed_text);
        mStrela = activity.findViewById(R.id.left_nactive);
        mStrela2 = activity.findViewById(R.id.right_nactive);
        mFuel = activity.findViewById(R.id.speed_fuel_text);
        mCarHP = activity.findViewById(R.id.speed_car_hp_text);
        mMileage = activity.findViewById(R.id.textView2);
        mSpeedLine = activity.findViewById(R.id.speed_line);
        mEngine = activity.findViewById(R.id.speed_engine_ico);
        turnlight_tick_sound_1 = NvEventQueueActivity.soundPool.load(NvEventQueueActivity.getInstance(), R.raw.turnlight_tick_1, 0);
        turnlight_tick_sound_2 = NvEventQueueActivity.soundPool.load(NvEventQueueActivity.getInstance(), R.raw.turnlight_tick_2, 0);
        mLock = activity.findViewById(R.id.speed_lock_ico);
        mBG.setOnClickListener( view -> {
            //NvEventQueueActivity.getInstance().clickSpeedometr(BUTTON_TURN_ALL);
        });
        mStrela.setOnClickListener(view -> {
            //NvEventQueueActivity.getInstance().clickSpeedometr(BUTTON_TURN_LEFT);
        });
        mStrela2.setOnClickListener(view -> {
            //NvEventQueueActivity.getInstance().clickSpeedometr(BUTTON_TURN_RIGHT);
        });
        Util.HideLayout(relativeLayout, false);
    }//by t.me/rkkdev

    public void UpdateSpeedInfo(int speed, int fuel, int hp, int mileage, int engine, int light, int belt, int lock){
        hp= (int) hp/10;
        //Utils.HideLayout(mStrela, false);
        //Utils.HideLayout(mStrela2, false);
        mFuel.setText(new Formatter().format("%d", Integer.valueOf(fuel)).toString());
        mMileage.setText(new Formatter().format("%06d", Integer.valueOf(mileage)).toString());
        mCarHP.setText(new Formatter().format("%d%s", Integer.valueOf(hp), "%").toString());
        mSpeedLine.setProgress(speed);
        mSpeed.setText(String.valueOf(speed));
        if (speed == 0) {
            this.mSpeed.setAlpha(0.4f);
            this.mSpeed.setText("000");
            this.mSpeed.setTextColor(Color.parseColor("#D3D3D3"));
        }
        if (speed != 0) {
            this.mSpeed.setAlpha(1.0f);
            this.mSpeed.setText(String.valueOf(speed));
            this.mSpeed.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(engine == 1)
            mEngine.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.SRC_IN);
        else
            mEngine.setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
        if(lock == 1)
            mLock.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.SRC_IN);
        else
            mLock.setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
    }

    public void ShowSpeed() {
        Util.ShowLayout(mInputLayout, false);
    }

    /*public void BlinkOn() {//by t.me/rkkdev
        Utils.ShowLayout(mBG2, false);
    }

    public void BlinkOff() {
        Utils.HideLayout(mBG2, false);
    }*/

    public void HideSpeed() {
        Util.HideLayout(mInputLayout, false);
    }
    public void setTurnlight(int turnlight){
        switch (turnlight)
        {
            case TURN_SIGNAL_LEFT: {
                if(timer_turnlight_left == null || !timer_turnlight_left.isAlive()) {
                    deleteThreads();
                    timer_turnlight_left = new Thread(runnable_turnlight_left);
                    timer_turnlight_left.start();
                }
                break;
            }
            case TURN_SIGNAL_RIGHT: {
                if(timer_turnlight_right == null || !timer_turnlight_right.isAlive()) {
                    deleteThreads();
                    timer_turnlight_right = new Thread(runnable_turnlight_right);
                    timer_turnlight_right.start();
                }
                break;
            }
            case TURN_SIGNAL_ALL: {
                if(timer_turnlight_all == null || !timer_turnlight_all.isAlive()) {
                    deleteThreads();
                    timer_turnlight_all = new Thread(runnable_turnlight_all);
                    timer_turnlight_all.start();
                }
                break;
            }
            default: {
                deleteThreads();
                mBG2.setImageResource(R.drawable.speedo_blinker);
                mStrela.setImageResource(R.drawable.speedo_arrow);
                mStrela2.setImageResource(R.drawable.speedo_arrow);
            }
        }
    }


    void deleteThreads()
    {
        if(timer_turnlight_left != null && timer_turnlight_left.isAlive())
            timer_turnlight_left.interrupt();

        if(timer_turnlight_right != null && timer_turnlight_right.isAlive())
            timer_turnlight_right.interrupt();

        if(timer_turnlight_all != null && timer_turnlight_all.isAlive())
            timer_turnlight_all.interrupt();
    }
    Runnable runnable_turnlight_all = new Runnable() {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted())
            {
                activity.runOnUiThread(() -> {
                    if(turnlight_all_state) {
                        mBG2.setImageResource(R.drawable.speedo_blinker);
                        mStrela.setImageResource(R.drawable.speedo_arrow);
                        mStrela2.setImageResource(R.drawable.speedo_arrow);
                        NvEventQueueActivity.soundPool.play(turnlight_tick_sound_1, 0.2f, 0.1f, 1, 0, 1.0f);
                        turnlight_all_state = false;
                    }
                    else {
                        mBG2.setImageResource(R.drawable.speedo_ablinker);
                        mStrela.setImageResource(R.drawable.speedo_arrowa);
                        mStrela2.setImageResource(R.drawable.speedo_arrowa);
                        NvEventQueueActivity.soundPool.play(turnlight_tick_sound_2, 0.2f, 0.1f, 1, 0, 1.0f);
                        turnlight_all_state = true;
                    }
                });
                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    break;
                }
            }

        }
    };

    Runnable runnable_turnlight_left = new Runnable() {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                activity.runOnUiThread(() -> {
                    if (turnlight_left_state) {
                        mStrela.setImageResource(R.drawable.speedo_arrow);
                        NvEventQueueActivity.soundPool.play(turnlight_tick_sound_1, 0.2f, 0.1f, 1, 0, 1.0f);
                        turnlight_left_state = false;
                    } else {
                        mStrela.setImageResource(R.drawable.speedo_arrowa);
                        NvEventQueueActivity.soundPool.play(turnlight_tick_sound_2, 0.2f, 0.1f, 1, 0, 1.0f);
                        turnlight_left_state = true;
                    }

                });
                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    break;
                }

            }

        }
    };

    Runnable runnable_turnlight_right = new Runnable() {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted())
            {
                activity.runOnUiThread(() -> {
                    if(turnlight_right_state) {
                        mStrela2.setImageResource(R.drawable.speedo_arrow);
                        NvEventQueueActivity.soundPool.play(turnlight_tick_sound_1, 0.1f, 0.2f, 1, 0, 1.0f);
                        turnlight_right_state = false;
                    }
                    else {
                        mStrela2.setImageResource(R.drawable.speedo_arrowa);
                        NvEventQueueActivity.soundPool.play(turnlight_tick_sound_2, 0.1f, 0.2f, 1, 0, 1.0f);
                        turnlight_right_state = true;
                    }

                });
                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    break;
                }
            }

        }
    };
}