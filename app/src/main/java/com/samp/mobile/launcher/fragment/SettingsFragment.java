package com.samp.mobile.launcher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.samp.mobile.R;
import com.samp.mobile.launcher.activity.MainActivity;
import com.samp.mobile.launcher.other.Util;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class SettingsFragment extends Fragment {

    Animation animation;
    public EditText nickname;
    String nickName;
    TextView account_not_auth_text;
    Timer i;
    public static SettingsFragment in;

    private Fragment gDownloadFragment;
    Fragment MainFragment;

    public static SettingsFragment getInSettings(){return in;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_settings, container, false);
        in = this;
        i = new Timer();

        gDownloadFragment = new DownloadFragment();
        MainFragment = new MainFragment();

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click);

        inflate.findViewById(R.id.btn_close).setOnClickListener(v -> {
            v.startAnimation(animation);
            MainActivity.getMainActivity().replaceFragment(MainFragment);
        });

        nickname = (EditText) inflate.findViewById(R.id.account_text);
        account_not_auth_text = (TextView) inflate.findViewById(R.id.account_not_auth_text);

        InitLogic();

        ((FrameLayout) inflate.findViewById(R.id.btn_reinstall_data))
                .setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View v) {
                                v.startAnimation(animation);
                                File gameDirectory = (new File(Environment.getExternalStorageDirectory() + "/Android/data/com.samp.mobile"));
                                Util.delete(gameDirectory);
                                MainActivity.getMainActivity().replaceFragment(gDownloadFragment);
                            }
                        });
        ((FrameLayout) inflate.findViewById(R.id.btn_reinstall_client))
                .setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View v) {
                                v.startAnimation(animation);
                                Toast.makeText(v.getContext(),"В разработке...", Toast.LENGTH_SHORT).show();
                            }
                        });


        ((EditText) nickname)
                .setOnEditorActionListener(
                        new EditText.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(
                                    TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_SEARCH
                                        || actionId == EditorInfo.IME_ACTION_DONE
                                        || event.getAction() == KeyEvent.ACTION_DOWN
                                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    try {
                                        File f =
                                                new File(
                                                        Environment.getExternalStorageDirectory()
                                                                + "/Android/data/com.samp.mobile/SAMP/settings.ini");
                                        if (!f.exists()) {
                                            f.createNewFile();
                                            f.mkdirs();

                                        }
                                        Wini w =
                                                new Wini(
                                                        new File(
                                                                Environment.getExternalStorageDirectory()
                                 + "/Android/data/com.samp.mobile/SAMP/settings.ini"));
								 if(checkValidNick(inflate)){
									 w.put("client", "name", nickname.getText().toString());
                                        Toast.makeText(
                                                getActivity(),
                                                "Ваш новый никнейм успешно сохранен!",
                                                Toast.LENGTH_SHORT).show();
                                     DownloadFragment.nick = nickname.getText().toString();
								 } else {
									 checkValidNick(inflate);
								 }
                                        w.store();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        DownloadFragment.nick = nickname.getText().toString();
										//Toast.makeText(getActivity(), "Установите игру!", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(
                                                getActivity(),
                                                "Ваш новый никнейм успешно сохранен!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                return false;
                            }
        });
        return inflate;
    }

    private void InitLogic() {
        try {
            Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/com.samp.mobile/SAMP/settings.ini"));
            nickname.setText(w.get("client", "name"));
            w.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public boolean checkValidNick(View inflate){
		EditText nick = (EditText) inflate.findViewById(R.id.account_text);
		if(nick.getText().toString().isEmpty()) {
			Toast.makeText(getActivity(), "Введите ник", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!(nick.getText().toString().contains("_"))){
			Toast.makeText(getActivity(), "Ник должен содержать символ \"_\"", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(nick.getText().toString().length() < 4){
			Toast.makeText(getActivity(), "Длина ника должна быть не менее 4 символов", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}