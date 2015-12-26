package in.icho.ui.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import in.icho.R;
import in.icho.data.Item;
import in.icho.ui.activities.MainActivity;
import in.icho.ui.adapters.HomeListAdapter;
import in.icho.ui.adapters.HomeListItemClickListener;
import in.icho.utils.URLStore;
import in.icho.utils.Radio;
import in.icho.utils.Store;

public class HomeFragment extends Fragment implements HomeListItemClickListener {

    private View loadingView;
    private RecyclerView recyclerView;
    private HomeListAdapter adapter;
    private ArrayList<Item> items;
    private String type;

    public HomeFragment() {
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        loadingView = v.findViewById(R.id.loadingView);
        recyclerView = (RecyclerView) v.findViewById(R.id.gridView);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        items = new ArrayList<>();
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new HomeListAdapter(items, this);
        recyclerView.setAdapter(adapter);

        int refresh = new Store().getSP().getInt("firstTime", 0);
        if (refresh == 0) {
            startLoadingAnimation();
            Radio.fetchList(URLStore.API + "items?type=" + this.type, new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    stopLoadingAnimation();
                    int newR = 0;
                    if (e != null) {
                        e.printStackTrace();
                        int refresh = new Store().getSP().getInt("firstTime", 0);
                        if (refresh == 0) {
                            newR = 0;
                        }
                    } else {
                        saveToStore(result);
                        newR = 1;
                    }
                    new Store().getSP().edit().putInt("firstTime", newR).commit();
                }
            });
        } else {
            refreshGrid();
            stopLoadingAnimation();
        }
    }

    private void refreshGrid() {

        final Set<String> hs = new Store().getSP().getStringSet("itemList", new HashSet<String>());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (hs != null) {
                    items.clear();
                    int j = 0;
                    for (String s : hs) {
                        j++;
                        Item i = (Item) new Store().readObject(s);
                        if (i != null) {
                            items.add(i);
                        }
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();

    }

    private void saveToStore(final String result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = new Store().getSP();
                SharedPreferences.Editor editor = sp.edit();
                Set<String> hs = sp.getStringSet("itemList", new HashSet<String>());

                Gson gson = new Gson();
                Item[] data = gson.fromJson(result, Item[].class);
                for (Item i : data) {
                    if (hs != null) {
                        hs.add(i.get_id());
                        hs.add(i.get_id());
                    }
                    new Store().saveItem(i);
                    items.add(i);
                }
                editor.putStringSet("itemList", hs).apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void startLoadingAnimation() {
        if (loadingView != null)
            loadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnimation() {
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(Item item) {
        PlayerFragment pf = ((MainActivity) getActivity()).getPlayerFragment();
        if (pf != null) {
            pf.init();
            pf.setCurrentItem(item);
        }
    }
}