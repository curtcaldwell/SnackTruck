package com.curtcaldwell.snacktruck;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class SnackViewHolder extends RecyclerView.ViewHolder {
    CheckBox checkBox;
    View root;


    public SnackViewHolder(@NonNull View itemView) {
        super(itemView);
        root = itemView;
        checkBox = itemView.findViewById(R.id.snack_name);
    }

    public void setData(Snack snack, View.OnClickListener listener) {
        checkBox.setText(snack.getName());
        checkBox.setChecked(snack.getIsChecked());
        checkBox.setOnClickListener(listener);
    }
}
