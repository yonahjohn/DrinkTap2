package com.example.drinktap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Class that the manages the data contained in the a table.
 */
class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {
    // The list of the drink to be shown in the table.
    private ArrayList<Drink> mDrinks;

    // Provide a reference to the views for each data item
    public static class DrinkViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionText;
        public TextView volumeText;
        public TextView contentText;
        public TextView dateText;
        public DrinkViewHolder(View view) {
            super(view);
            // Need to find each element in the recyclerview_row.xml,
            // so we can set the text of each label later.
            descriptionText = view.findViewById(R.id.row_description);
            volumeText = view.findViewById(R.id.row_volume);
            contentText = view.findViewById(R.id.row_content);
            dateText = view.findViewById(R.id.row_date);
        }
    }

    public DrinkAdapter(ArrayList<Drink> drinks) {
        mDrinks = drinks;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DrinkAdapter.DrinkViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // The layout of a table row as defined in the file recyclerview_row.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row, parent, false);
        DrinkViewHolder vh = new DrinkViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DrinkViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.descriptionText.setText(mDrinks.get(position).getDescription());
        holder.volumeText.setText("Volume: "+mDrinks.get(position).getVolume().toString()+" oz");
        holder.contentText.setText("Content: "+mDrinks.get(position).getContent().toString()+" %");
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        holder.dateText.setText("Date: "+dt.format(mDrinks.get(position).getDate()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDrinks.size();
    }
}
