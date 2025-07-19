package com.samp.mobile.launcher.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.samp.mobile.R;
import com.samp.mobile.launcher.activity.MainActivity;
import com.samp.mobile.launcher.other.Util;


public class MainFragment extends Fragment {
	public ImageView settings, social_vk, social_tg, social_yt;
	private Fragment DownloadFragment;




	@SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_main, container, false);
    	Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_click);

		DownloadFragment = new DownloadFragment();

		settings = inflate.findViewById(R.id.btn_settings);
		social_tg = inflate.findViewById(R.id.btn_social_telegram);
		social_vk = inflate.findViewById(R.id.btn_social_vk);
		social_yt = inflate.findViewById(R.id.btn_social_youtube);


		settings.setOnClickListener(v ->{
			v.startAnimation(animation);
			MainActivity.getMainActivity().replaceSettings();
		});

		social_tg.setOnClickListener(v ->{
			v.startAnimation(animation);
			openUrls("tg");
		});

		social_vk.setOnClickListener(v ->{
			v.startAnimation(animation);
			openUrls("vk");
		});

		social_yt.setOnClickListener(v ->{
			v.startAnimation(animation);
			openUrls("yt");
		});


		inflate.findViewById(R.id.btn_play).setOnClickListener(v -> {
			v.startAnimation(animation);
			MainActivity.getMainActivity().onClickPlay();
		});

        return inflate;
    }

	void openUrls(String Type) {
		switch (Type) {
			case "vk":
				startActivity(new Intent(Intent.ACTION_VIEW)
						.setData(Uri.parse("https://vk.com"))
				);
				break;
			case "tg":
				startActivity(new Intent(Intent.ACTION_VIEW)
						.setData(Uri.parse("https://t.me/hikaseardev"))
				);
				break;
			case "yt":
				startActivity(new Intent(Intent.ACTION_VIEW)
						.setData(Uri.parse("https://youtube.com/@hikasear"))
				);
				break;
		}
	}
}