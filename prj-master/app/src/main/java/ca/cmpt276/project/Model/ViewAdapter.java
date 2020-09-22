package ca.cmpt276.project.Model;

//This class is an adapter for the recyclerview used to view images

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.cmpt276.project.R;

public class ViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Drawable> items;

    public ViewAdapter(Context context, List<Drawable> items) {
        this.context =context;
        this.items= items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.single_item, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Item)holder).imageView.setImageDrawable(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public class Item extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Item(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageview);
        }
    }


}
