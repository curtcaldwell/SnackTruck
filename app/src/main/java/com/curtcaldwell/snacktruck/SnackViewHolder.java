package com.curtcaldwell.snacktruck;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
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
        if (snack.getIsVeggie()) {
            //work around for now, will fix if other versions need to be supported.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkBox.setTextColor(root.getContext().getColor(R.color.Green));
                checkBox.setButtonTintList(ColorStateList.valueOf(root.getContext().getColor(R.color.Green)));
            }
        } else {
            //work around for now, will fix if other versions need to be supported.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkBox.setTextColor(root.getContext().getColor(R.color.Red));
                checkBox.setButtonTintList(ColorStateList.valueOf(root.getContext().getColor(R.color.Red)));
            }
        }

    }
}
