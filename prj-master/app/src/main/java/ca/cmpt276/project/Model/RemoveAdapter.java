package ca.cmpt276.project.Model;

//This class is used as an adapter for the recyclerview for the Removing Images screen

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

public class RemoveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnNoteListener mOnNoteListener;
    Context context;
    List<Drawable> items;

    public RemoveAdapter(Context context, List<Drawable> items, OnNoteListener onNoteListener) {
        this.context =context;
        this.items= items;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.single_item, parent, false);
        Item item = new Item(row, mOnNoteListener);
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

    public class Item extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        OnNoteListener onNoteListener;
        public Item(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
