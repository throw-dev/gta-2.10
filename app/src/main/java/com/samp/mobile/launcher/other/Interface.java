package com.samp.mobile.launcher.other;

import com.samp.mobile.launcher.model.Servers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Interface {
    @GET("https://www.dropbox.com/scl/fi/fr2zcvov38od2xkdu5h6t/servers.json?rlkey=tmf6wymqj2h9pzz1fadlqkujt&st=iv0gsv5b&dl=1")
    Call<List<Servers>> getServers();
}
