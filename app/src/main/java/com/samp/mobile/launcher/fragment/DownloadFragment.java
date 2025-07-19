package com.samp.mobile.launcher.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.samp.mobile.R;
import com.samp.mobile.game.GTASA;
import com.samp.mobile.launcher.activity.MainActivity;
import com.samp.mobile.launcher.other.Util;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.hzy.libp7zip.P7ZipApi;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.Random;


public class DownloadFragment extends Fragment {

    public File folder;
    public static String nick = null;
    public ImageView download_render;
    public TextView download_guide_text;
    public int idText = 0;
    public int idImage = 0;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final String[] TextInfo = {
            "Жили были три богатыря: Kuzia, HikaSear и Welsi",
            "Ну я типа хз что еще писать",
            "У вас есть технические или игровые вопросы? Напишите нам в ВКонтакте!",
            "Команда опытных разработчиков день и ночь работает для обеспечения комфортной игры каждому",
            "На главной странице можно узнать последние новости игры.",
            "Скоро у нас будет доступна удобнейшая авторизация через Google и ВКонтакте."
    };

    Fragment MainFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_download, container, false);

        download_render = inflate.findViewById(R.id.download_render);

        download_guide_text = inflate.findViewById(R.id.download_guide_text);

        MainFragment = new MainFragment();

        startDownload(inflate);

        return inflate;
    }

    public void startDownload(View inflate) {
        folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String url = "https://api-free.edgars.site/matrp/cache.zip";
        startAnim();
        PRDownloader.download(url, folder.getPath(), "cache.7z")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Toast.makeText(getContext(), "Установка отменена", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        float floor = ((float) Math.floor((double) (progress.currentBytes / 20.0f))) * 20.0f;
                        ProgressBar progressbar = (ProgressBar) inflate.findViewById(R.id.download_progressbar);

                        TextView textloading = (TextView) inflate.findViewById(R.id.download_text);

                        textloading.setText(new Formatter().format("Скачивание архивов %.2f%s", new Object[]{Float.valueOf((int)progressPercent), "%"}).toString());//(new Formatter().format("Скачивание архивов: %s.0f %", progressPercent).toString());
                        progressbar.setProgress((int) progressPercent);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        TextView textloading = (TextView) inflate.findViewById(R.id.download_text);


                        textloading.setText("Распаковка архивов...");
                        UnZipCache();
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(getContext(), "Произошла ошибка начните заново установку", Toast.LENGTH_SHORT).show();
                        MainActivity.getMainActivity().replaceFragment(MainFragment);
                    }
                });
    }
    public void UnZipCache(){
        String mInputFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/cache.7z";
        String mOutputPath = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.samp.mobile";
        new Un7z().execute(mInputFilePath, mOutputPath);
    }

    public class Un7z extends AsyncTask<String, Void, Void> {
        public String str;
        @Override
        protected void onPreExecute() {

        }

        public Void doInBackground(String... strings) {
            String str = strings[0];
            P7ZipApi.executeCommand(String.format("7z x '%s' '-o%s' -aoa", str, strings[1]));
            return null;
        }

        public void onPostExecute(Void aVoid) {
            OnLoaded();
        }
    }
    public void OnLoaded() {
        Util.delete(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/cache.7z"));
        Util.delete(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/cache.7z.temp"));
        Toast.makeText(getContext(), "Игра успешно установлена!", Toast.LENGTH_SHORT).show();
        try {
            Wini w =
                    new Wini(
                            new File(
                                    Environment.getExternalStorageDirectory()
                                            + "/Android/data/com.samp.mobile/SAMP/settings.ini"));
            w.put("client", "name", nick);
            w.store();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.getMainActivity().replaceFragment(MainFragment);
    }

    public class UpdateImage implements Runnable {
        public UpdateImage() {
        }

        public final void run() {
           // if (!getActivity().isDestroyed()) {
                ImageView imageView = download_render;
                imageView.setTranslationX((float) imageView.getWidth());
                download_render.setAlpha(0.0f);
                ImageView imageView2 = download_render;
                Resources resources = getResources();
                StringBuilder v9 = new StringBuilder("render_pic_");
                v9.append(idImage);
                imageView2.setImageResource(resources.getIdentifier(v9.toString(), "drawable", getContext().getPackageName()));
                download_render.animate().translationX(0.0f).alpha(1.0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
            //}
        }
    }

    public final void startAnim() {
            mHandler.removeCallbacksAndMessages((Object) null);
            this.idText = new Random().nextInt(this.TextInfo.length);
            idImage = 0;
            ImageView imageView = this.download_render;
            Resources resources = getResources();
            StringBuilder v9 = new StringBuilder("render_pic_");
            v9.append(idImage);
            imageView.setImageResource(resources.getIdentifier(v9.toString(), "drawable", getContext().getPackageName()));
            this.download_guide_text.setText(this.TextInfo[this.idText]);
            this.download_guide_text.setOnClickListener(new ponClick());
            mHandler.postDelayed(new pon(), 5000);
    }
    public class pon implements Runnable {
        public pon() {
        }

        public final void run() {
            Update();
        }
    }

    public class ponClick implements View.OnClickListener {
        public ponClick() {
        }

        public final void onClick(View view) {
            Update();
        }
    }
    public final void Update() {
        mHandler.removeCallbacksAndMessages((Object) null);
        int i10 = this.idText + 1;
        this.idText = i10;
        int i11 = idImage + 1;
        idImage = i11;
        if (i10 >= this.TextInfo.length) {
            this.idText = 0;
        }
        if (i11 >= 6) {
            idImage = 0;
        }
        download_render.clearAnimation();
        download_render.animate().translationX((float) (-this.download_render.getWidth())).alpha(0.0f).setDuration(300).setInterpolator(new AccelerateInterpolator()).withEndAction(new UpdateImage()).start();
        download_guide_text.clearAnimation();
        download_guide_text.animate().translationX((float) (-this.download_guide_text.getWidth())).alpha(0.0f).setDuration(300).setInterpolator(new AccelerateInterpolator()).withEndAction(new textEdit()).start();
        mHandler.postDelayed(new pon(), 5000);
    }
    public class textEdit implements Runnable {
        public textEdit() {
        }

        public final void run() {
            TextView textView = download_guide_text;
            textView.setTranslationX((float) textView.getWidth());
            download_guide_text.setAlpha(0.0f);
            download_guide_text.setText(TextInfo[idText]);
            download_guide_text.animate().translationX(0.0f).alpha(1.0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    /*public final void c() {
    }*/

        //new Timer().cancel();
        //new Timer().purge();
    /*public void Update() {
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                //Log.i("Log", "2,5 sec");
                v();
                ImageEdgar();
            }
        }, 0, 1400);
        Log.i("пон", "анбоксинг");//new Handler().postDelayed(new Runnable()
    }*/

    /*public final void v() {
        int i10 = f8402p + 1;
        f8402p = i10;
        if (i10 >= 6) {
            this.f8402p = 0;
        }
        download_render.clearAnimation();
        new d();
        //f8393f.postDelayed(new f(), 5000);
    }
    public void ImageEdgar(){
        ImageView imageView = download_render;
        imageView.setTranslationX((float) imageView.getWidth());
        download_render.setAlpha(0.0f);
        ImageView imageView2 = download_render;
        Resources resources = getResources(); //("render_pic_")
        StringBuilder v9 = new StringBuilder("render_pic_");
        v9.append(f8402p);
        imageView2.setImageResource(resources.getIdentifier(v9.toString(), "drawable", getContext().getPackageName()));
        download_render.animate().translationX(0.0f).alpha(1.0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
        //Handler.postDelayed(ImageEdgar() , 5000);
        //v();
    }*/

}