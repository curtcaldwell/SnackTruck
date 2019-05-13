package com.curtcaldwell.snacktruck;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class SnackAdapter extends RecyclerView.Adapter<SnackViewHolder> {
    List<Snack> snackList;
    Context context;
    MainActivity.SnackListener listener;


    public SnackAdapter(Context c, List<Snack> list, MainActivity.SnackListener l) {
        context = c;
        snackList = list;
        listener = l;
    }

    @NonNull
    @Override
    public SnackViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.snack_item, viewgroup, false);
        SnackViewHolder vh = new SnackViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SnackViewHolder snackViewHolder, int i) {
        snackViewHolder.setData(snackList.get(i), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                snackList.get(snackViewHolder.getAdapterPosition()).setIsChecked(checkBox.isChecked());
                listener.onSnackClick(snackList.get(snackViewHolder.getAdapterPosition()), checkBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return snackList.size();
    }


    public void updateList(List<Snack> list) {
        snackList.clear();
        snackList.addAll(list);
        this.notifyDataSetChanged();
    }
}
