package com.samp.mobile.game.ui;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.nvidia.devtech.NvEventQueueActivity;
import com.samp.mobile.R;
import com.samp.mobile.launcher.other.Util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Formatter;

public class HudManager {
    public Activity activity;
    public ConstraintLayout hud_layout;
    private final NvEventQueueActivity mContext = null;


    public TextView hud_money, hud_ammo;
    public ImageView hud_weapon;

    public TextView textHP, textArmour, textPlID, textWanted;

    boolean init = false;

    public HudManager(Activity aactivity) {
        activity = aactivity;
        hud_layout = aactivity.findViewById(R.id.hud_main);

        textHP = aactivity.findViewById(R.id.healthtext);
        textArmour = aactivity.findViewById(R.id.armour_text);
        //textPlID = aactivity.findViewById(R.id.textPlayerID);

        hud_money = aactivity.findViewById(R.id.money_text);
        hud_weapon = aactivity.findViewById(R.id.weapon_ammo_image);
        hud_ammo = aactivity.findViewById(R.id.weapon_ammo_text);
        textWanted = aactivity.findViewById(R.id.wantedtext);

        Util.HideLayout(hud_layout, false);
    }

    public void UpdateHudInfo(int health, int armour, int weaponid, int ammo, int ammoinclip, int money, int wanted)
    {
        textHP.setText(String.valueOf(health));
        textArmour.setText(String.valueOf(armour));
        //textPlID.setText(String.format("%s", playerid));

        DecimalFormat formatter=new DecimalFormat();
        DecimalFormatSymbols symbols= DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        String s= formatter.format(money);
        hud_money.setText(String.format("$ %s", s));

        textWanted.setText(String.valueOf(wanted));


        int id = activity.getResources().getIdentifier(new Formatter().format("weapon_%d", Integer.valueOf(weaponid)).toString(), "drawable", activity.getPackageName());
        hud_weapon.setImageResource(id);
        hud_ammo.setText(String.format("%s / %s", ammo, ammoinclip));
        //hud_weapon.setOnClickListener(v -> NvEventQueueActivity.getInstance().onWeaponChanged());
    }

    public void ShowHud()
    {
        Util.ShowLayout(hud_layout, true);
    }

    public void HideHud()
    {
        Util.HideLayout(hud_layout, true);
    }
}

