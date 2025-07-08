package com.samp.mobile.game.ui.hud;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.nvidia.devtech.NvEventQueueActivity;
import com.samp.mobile.R;
import com.samp.mobile.launcher.util.Util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Formatter;


public class HudManager {
    public Activity activity;
    public ConstraintLayout hud_layout, hud_layout_new, hud_layout_old;

    private final NvEventQueueActivity mContext = null;

    public TextView hud_money;

    public ImageView hud_weapon;

    public ImageView hud_radar;

    public ImageView hud_x2;

    public ImageView hud_menu;
    public ImageView hud_micro;

    public ImageView hud_gps;

    public ImageView hud_zona, hud_seat;

    public ArrayList<ImageView> hud_wanted;

    public ProgressBar progressHP;
    public ProgressBar progressArmor;

    private boolean isHudSetPos = false;

    public HudManager(Activity aactivity) {
        activity = aactivity;

        hud_layout = aactivity.findViewById(R.id.hud_main);
        hud_layout.setVisibility(View.GONE);

        //hud_layout_new = aactivity.findViewById(R.id.hud_main_new);
        //hud_layout_new.setVisibility(View.GONE);

        progressArmor = aactivity.findViewById(R.id.hud_armor_pb);
        progressHP = aactivity.findViewById(R.id.hud_health_pb);

        hud_radar = aactivity.findViewById(R.id.radar_hude);


        hud_gps = aactivity.findViewById(R.id.imageView16);
        hud_zona = aactivity.findViewById(R.id.grzona);
        hud_x2 = aactivity.findViewById(R.id.imageView17);

        hud_money = aactivity.findViewById(R.id.hud_money);
        hud_weapon = aactivity.findViewById(R.id.hud_weapon);
        hud_menu = aactivity.findViewById(R.id.hud_menuu);

        hud_wanted = new ArrayList<>();
        hud_wanted.add(activity.findViewById(R.id.hud_star_1));
        hud_wanted.add(activity.findViewById(R.id.hud_star_2));
        hud_wanted.add(activity.findViewById(R.id.hud_star_3));
        hud_wanted.add(activity.findViewById(R.id.hud_star_4));
        hud_wanted.add(activity.findViewById(R.id.hud_star_5));
        hud_wanted.add(activity.findViewById(R.id.hud_star_6));

        hud_menu.setOnClickListener( view -> {
            view.startAnimation(AnimationUtils.loadAnimation(aactivity, R.anim.click));
        });

        hud_seat = aactivity.findViewById(R.id.imageView31);
        hud_seat.setVisibility(View.GONE); //fix cplayerped spawn after show

    }

    public void ShowVehButtonG() {
        hud_seat.setVisibility(View.VISIBLE);
    }

    public void HideVehButtonG() {
        hud_seat.setVisibility(View.GONE);
    }

    public void UpdateHudInfo(int health, int armour, int hunger, int weaponidweik, int ammo, int playerid, int money, int wanted)
    {
        progressHP.setProgress(health);
        progressArmor.setProgress(armour);

        DecimalFormat formatter=new DecimalFormat();
        DecimalFormatSymbols symbols= DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        String s= formatter.format(money);
        hud_money.setText(s);

        int id = activity.getResources().getIdentifier(new Formatter().format("weapon_%d", Integer.valueOf(weaponidweik)).toString(), "drawable", activity.getPackageName());
        hud_weapon.setImageResource(id);

        if(wanted > 6) wanted = 6;
        for (int i2 = 0; i2 < wanted; i2++) {
            hud_wanted.get(i2).setBackgroundResource(R.drawable.ic_y_star);
        }
    }

    public void ShowHud()
    {
        Util.ShowLayout(hud_layout, true);
        hud_radar.post(() -> {
            if(!isHudSetPos) { // проверка чтобы не было краша при новом показе худа

                // все координаты
                int screenwidth = hud_layout.getWidth();
                int screenheight = hud_layout.getHeight();

                float real_prcX = ((hud_radar.getX() + (hud_radar.getWidth() / 2)) / screenwidth) * 100;
                float real_prcY = ((hud_radar.getY() + (hud_radar.getHeight() / 2.26f)) / screenheight) * 100; // fix
                float gtaX = (640 * (real_prcX / 100f));
                float gtaY = (480 * (real_prcY / 100f));

                // убираем из xml после того как взяли координаты
                activity.runOnUiThread(() -> {
                    hud_radar.setVisibility(View.INVISIBLE);
                });

                // обозначаем что больше находить координаты не нужно
                isHudSetPos = true;
            }});
    }

    public void ShowGps()
    {
        Util.ShowLayout(hud_gps, false);
    }

    public void HideGps()
    {
        Util.HideLayout(hud_gps, false);
    }

    public void ShowX2()
    {
        Util.ShowLayout(hud_x2, false);
    }

    public void HideX2()
    {
        Util.HideLayout(hud_x2, false);
    }

    public void ShowZona()
    {
        Util.ShowLayout(hud_zona, false);
    }

    public void HideZona()
    {
        Util.HideLayout(hud_zona, false);
    }

    public void ShowRadar()
    {
        Util.ShowLayout(hud_radar, true);
    }

    public void HideRadar()
    {
        Util.HideLayout(hud_radar, false);
    }

    public void HideHud()
    {
        Util.HideLayout(hud_layout, true);
    }
}
