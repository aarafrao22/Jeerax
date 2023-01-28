package com.aarafrao.jeerax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aarafrao.jeerax.databinding.MainRvItemBinding;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {
    private ArrayList<ItemModel> itemModelArrayList;
    private Context context;


    public RvAdapter(ArrayList<ItemModel> itemModelArrayList, Context context) {
        this.itemModelArrayList = itemModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainRvItemBinding mainRvItemBinding = MainRvItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(mainRvItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView txt;
        private ImageView img;

        public VH(MainRvItemBinding itemView) {
            super(itemView.getRoot());
            img = itemView.imgIcon;
            txt = itemView.txtName;
        }
    }
}
