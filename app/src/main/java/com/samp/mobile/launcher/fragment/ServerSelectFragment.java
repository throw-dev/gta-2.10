package com.samp.mobile.launcher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samp.mobile.R;
import com.samp.mobile.launcher.activity.MainActivity;
import com.samp.mobile.launcher.adapter.ServersAdapter;
import com.samp.mobile.launcher.model.Servers;
import com.samp.mobile.launcher.other.Lists;

import java.util.ArrayList;

public class ServerSelectFragment extends Fragment {

    ImageView close;

    RecyclerView recyclerServers;
    ServersAdapter serversAdapter;
    ArrayList<Servers> slist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_server_select, container, false);

        close = inflate.findViewById(R.id.btn_close);
        close.setOnClickListener(v -> {
            MainActivity.getMainActivity().closeServers();
        });

        recyclerServers = inflate.findViewById(R.id.serverlist_recycler);
		recyclerServers.setHasFixedSize(true);
		LinearLayoutManager layoutManagerr = new LinearLayoutManager(getActivity());
		recyclerServers.setLayoutManager(layoutManagerr);

		this.slist = Lists.slist;
		serversAdapter = new ServersAdapter(getContext(), this.slist);
		recyclerServers.setAdapter(serversAdapter);

        return inflate;
    }
}
