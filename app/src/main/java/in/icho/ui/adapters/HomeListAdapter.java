package in.icho.ui.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.icho.R;
import in.icho.data.Item;
import in.icho.utils.Radio;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private HomeListItemClickListener homeListItemClickListener;
    private ArrayList<Item> items;

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView = (ImageView) v.findViewById(R.id.imageView);
        }
    }

    public void add(int position, Item item) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    public HomeListAdapter(ArrayList<Item> myDataset, HomeListItemClickListener homeListItemClickListener) {
        this.homeListItemClickListener = homeListItemClickListener;
        items = myDataset;
    }

    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.textView.setText(item.getTitle());
        final String title = String.valueOf(item.getTitle());
        final String ext = String.valueOf(item.getImage_extension());

        holder.textView.setText(title);
        final ImageView im = holder.imageView;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Radio.fetchThumbailImage(im, title, ext);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeListItemClickListener.onClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size() > 10 ? 10 : items.size();
    }

}

