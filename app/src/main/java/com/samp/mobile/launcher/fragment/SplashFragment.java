package com.samp.mobile.launcher.fragment;


import android.animation.TimeInterpolator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.view.LayoutInflater;

import com.samp.mobile.R;
import com.samp.mobile.launcher.other.Lists;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashFragment extends Fragment {
	
	Animation animation;

    private static SplashFragment instance;

    public final Handler mHandler = new Handler(Looper.getMainLooper());

    public ImageView splash_logo;

    public ee panzto;

    public static SplashFragment getSplash() {
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_splash, container, false);
		
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale2);

        instance = this;

        splash_logo = inflate.findViewById(R.id.splash_logo);
        m();
        Lists.slist = new ArrayList<>();
        Lists.nlist = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://t.me")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return inflate;
    }
    //
    public static final class ee {

        public float mFloat1;
        public float mFloat;
        public long mLong;
        public float mFloat2;
        public long Long;
        public boolean mBool;
        public ee.c eeC;
        public ee.b eeB;
        public TimeInterpolator mTimeInterpolator;
        public Handler mHandler = new Handler();

        public class EEE implements Runnable {
            public EEE() {
            }

            public final void run() {
                ee.this.e();
            }
        }

        public interface b {
            void a();

            void b();
        }

        public interface c {
            void a(ee eVar);
        }

        public ee(float f10, float f11) {
            this.mFloat1 = f10;
            this.mFloat = f11;
            this.mLong = 1000;
            this.mFloat2 = 0.0f;
            this.mBool = false;
            this.mTimeInterpolator = new LinearInterpolator();
        }

        public final void a() {
            if (this.mBool) {
                this.mBool = false;
                this.mHandler.removeCallbacksAndMessages((Object) null);
                ee.b bVar = this.eeB;
                if (bVar != null) {
                    bVar.a();
                }
            }
        }

        public final float b() {
            float f10 = this.mFloat1;
            return e(this.mFloat, this.mFloat1, this.mTimeInterpolator.getInterpolation(this.mFloat2), f10);
        }
        public static float e(float f10, float f11, float f12, float f13) {
            return ((f10 - f11) * f12) + f13;
        }

        public final void c() {
            ee.b bVar = this.eeB;
            if (bVar != null) {
                bVar.a();
            }
        }

        public final void d() {
            if (!this.mBool) {
                this.Long = System.currentTimeMillis();
                this.mBool = true;
                ee.b bVar = this.eeB;
                if (bVar != null) {
                    bVar.b();
                }
                e();
            }
        }

        public final void e() {
            if (this.mBool) {
                float currentTimeMillis = ((float) (System.currentTimeMillis() - this.Long)) / ((float) this.mLong);
                if (currentTimeMillis >= 1.0f) {
                    this.mFloat2 = 1.0f;
                    this.mBool = false;
                } else {
                    this.mFloat2 = currentTimeMillis;
                    this.mHandler.post(new EEE());
                }
                ee.c cVar = this.eeC;
                if (cVar != null) {
                    cVar.a(this);
                }
                if (!this.mBool) {
                    c();
                }
            }
        }
    }

    //

    public class d implements ee.c {
        public d() {
        }

        public final void a(ee eVar) {
            SplashFragment e1Var, e1Var2;
            float f11;
            float floatValue = Float.valueOf(eVar.b()).floatValue();
            float f10 = (floatValue - 1.3f) + 1.0f;
            if (floatValue <= 1.0f) {
                splash_logo.setScaleX(1.0f);
                splash_logo.setScaleY(1.0f);
                return;
            }
            if (floatValue <= 1.0f || floatValue > 1.15f) {
                if (floatValue > 1.15f && floatValue <= 1.3f) {
                    f10 = 1.15f - (floatValue - 1.15f);
                    splash_logo.setScaleX(f10);
                    e1Var = SplashFragment.this;
                } else if (floatValue > 1.3f && floatValue <= 1.45f) {
                    f11 = (floatValue - 1.3f) + 1.0f;
                    //f10 = 1.15f - (floatValue - 1.3f);
                    splash_logo.setScaleX(f11);
                    e1Var = SplashFragment.this;
                } else if (floatValue > 1.45f && floatValue <= 1.6f) {
                    f10 = 1.15f - (floatValue - 1.45f);
                    splash_logo.setScaleX(f10);
                    e1Var = SplashFragment.this;
                } else {
                    return;
                }
                e1Var.splash_logo.setScaleY(f10);
                return;
            }
            f11 = (floatValue - 1.0f) + 1.0f;
            splash_logo.setScaleX(f11);
            e1Var2 = SplashFragment.this;
            e1Var2.splash_logo.setScaleY(f11);
        }
    }

    public class e implements ee.b {
        public e() {
        }

        public final void a() {
            splash_logo.setScaleX(1.0f);
            splash_logo.setScaleY(1.0f);
            m();
        }

        public final void b() {
        }
    }

    public final void h() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        ee eVar = this.panzto;
        if (eVar != null) {
            eVar.eeB = null;
            eVar.eeC = null;
            eVar.a();
            this.panzto = null;
        }
        //super.h();
    }

    public final ViewPropertyAnimator j() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        ee eVar = this.panzto;
        if (eVar != null) {
            eVar.eeB = null;
            eVar.eeC = null;
            eVar.a();
            this.panzto = null;
        }
        this.splash_logo.clearAnimation();
        this.splash_logo.setScaleX(1.0f);
        this.splash_logo.setScaleY(1.0f);
        this.splash_logo.setTranslationY(0.0f);
        this.splash_logo.animate().setDuration(150).scaleX(0.0f).scaleY(0.0f).translationY((float) this.splash_logo.getHeight()).start();
        return null;
    }

    public final ViewPropertyAnimator k() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        return null;
    }

    public final void m() {
        ee eVar = this.panzto;
        if (eVar != null) {
            eVar.eeB = null;
            eVar.eeC = null;
            eVar.a();
            this.panzto = null;
        }
        ee eVar2 = new ee(0.0f, 1.6f);
        this.panzto = eVar2;
        eVar2.eeC = new d();
        eVar2.eeB = new e();
        eVar2.mTimeInterpolator = new LinearInterpolator();
        ee eVar3 = this.panzto;
        eVar3.mLong = 1600;
        eVar3.d();
    }
}

