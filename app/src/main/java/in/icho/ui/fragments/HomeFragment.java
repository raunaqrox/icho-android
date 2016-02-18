package in.icho.ui.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import in.icho.API.IchoService;
import in.icho.R;
import in.icho.model.Item;
import in.icho.model.Page;
import in.icho.model.Token;
import in.icho.ui.activities.MainActivity;
import in.icho.ui.adapters.HomeListAdapter;
import in.icho.ui.adapters.HomeListItemClickListener;
import in.icho.utils.URLStore;
import in.icho.utils.Radio;
import in.icho.utils.Store;
import in.icho.utils.EndlessRecyclerOnScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements HomeListItemClickListener {

    private View loadingView;
    private RecyclerView recyclerView;
    private HomeListAdapter adapter;
    private List<Item> items;
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

    public void getItemsAndShow(int current_page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLStore.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Page page = new Page(10, current_page, "P");
        IchoService service = retrofit.create(IchoService.class);
        retrofit2.Call<List<Item>> call = service.getList(page);
        call.enqueue(new Callback <List<Item>>(){

            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(response.body() != null) {
                    refreshGrid(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        items = new ArrayList<Item>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new HomeListAdapter(items, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                getItemsAndShow(current_page);
            }
        });
        startLoadingAnimation();
        getItemsAndShow(1);
    }

    private void refreshGrid(final List<Item> newItems) {
        items.addAll(newItems);
        final Set<String> hs = new Store().getSP().getStringSet("itemList", new HashSet<String>());
        new Thread(new Runnable() {
            @Override
            public void run() {
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