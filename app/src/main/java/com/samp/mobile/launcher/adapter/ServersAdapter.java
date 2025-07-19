package com.samp.mobile.launcher.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build;

import android.util.Log;
import android.view.View.OnClickListener;
import android.graphics.PorterDuff;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.Context;

import com.samp.mobile.R;
import com.samp.mobile.game.SAMP;
import com.samp.mobile.launcher.activity.MainActivity;
import com.samp.mobile.launcher.model.Servers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import org.ini4j.Wini;

public class ServersAdapter extends RecyclerView.Adapter<ServersAdapter.ServersViewHolder> {
	Context context;
	
	ArrayList<Servers> slist;
	
	public ServersAdapter(Context context, ArrayList<Servers> slist){
		 this.context = context;
		 this.slist = slist; 
	}
	
	@NonNull
	@Override
    public ServersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.serverlist_item, parent, false);
		return new ServersViewHolder(v); 
    }
  
    @Override
    public void onBindViewHolder(@NonNull ServersViewHolder holder, int position) {
        Servers servers = slist.get(position);
		holder.ponka.setColorFilter(Color.parseColor("#" + servers.getColor()),PorterDuff.Mode.SRC_ATOP);
		holder.people.setColorFilter(Color.parseColor("#" + servers.getColor()),PorterDuff.Mode.SRC_ATOP);
		holder.backColor.setColorFilter(Color.parseColor("#" + servers.getColor()),PorterDuff.Mode.SRC_ATOP);
		holder.name.setText(servers.getname());
		holder.textonline.setText(Integer.toString(servers.getOnline()) + "/" + Integer.toString(servers.getmaxOnline()));

		boolean status = servers.getRecommend();
		Log.e("бля", "pon ==== " + status);
		if(status)
		{
			holder.server_recommend_text.setVisibility(View.VISIBLE);
			holder.backColor.setVisibility(android.view.View.VISIBLE);
		} else {
			holder.server_recommend_text.setVisibility(android.view.View.GONE);
			holder.backColor.setVisibility(View.GONE);
		}
		holder.container.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click));
				try {
					Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/com.samp.mobile/files/SAMP/settings.ini"));
					w.put("client", "serverid", servers.getId());
					w.store();
					MainActivity.getMainActivity().closeServers();
					MainActivity.getMainActivity().startActivity(new Intent(MainActivity.getMainActivity(), SAMP.class));
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
    }

    @Override
    public int getItemCount() {
        return slist.size();
    }

    public static class ServersViewHolder extends RecyclerView.ViewHolder {
        
		ConstraintLayout container;
		ImageView ponka, people;
		TextView name, textonline, server_recommend_text;
		ImageView backColor;
	    
        public ServersViewHolder(View itemView) {
            super(itemView);
            
		    name = itemView.findViewById(R.id.server_item_name);
			server_recommend_text = itemView.findViewById(R.id.server_recommend_text);
			ponka = itemView.findViewById(R.id.server_item_background);
			people = itemView.findViewById(R.id.server_item_image);
			textonline = itemView.findViewById(R.id.server_item_online);
			backColor = itemView.findViewById(R.id.server_recommend_card);
			container = itemView.findViewById(R.id.edgar_con);
        }
    }
	
}