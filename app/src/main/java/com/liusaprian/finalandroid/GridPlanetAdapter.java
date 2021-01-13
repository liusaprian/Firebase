package com.liusaprian.finalandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.liusaprian.finalandroid.databinding.RowPlanetBinding;

import java.util.ArrayList;

public class GridPlanetAdapter extends RecyclerView.Adapter<GridPlanetAdapter.ViewHolder> {

    private ArrayList<Planet> planetList;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public GridPlanetAdapter(ArrayList<Planet> planetList) {
        this.planetList = planetList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RowPlanetBinding rowPlanetBinding;
        public ViewHolder(RowPlanetBinding rowPlanetBinding) {
            super(rowPlanetBinding.getRoot());
            this.rowPlanetBinding = rowPlanetBinding;
        }
    }

    @NonNull
    @Override
    public GridPlanetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowPlanetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GridPlanetAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(planetList.get(position).url)
                .into(holder.rowPlanetBinding.planetItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemCLicked(planetList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public interface OnItemClickCallback {
        void onItemCLicked(Planet planet);
    }
}
