package miar.com.miar_project_client.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import miar.com.miar_project_client.R;
import miar.com.miar_project_client.model.User;

public class UserAdapter extends ArrayAdapter<User> {

    private TextView pseudo, solde;
    private CheckBox checkBox;
    private User admin;

    public UserAdapter(@NonNull Context context, List<User> users, User admin) {
        super(context, 0, users);
        this.admin = admin;
    }


    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_user_adapter, parent, false);
        }

        final User user = getItem(position);
        final View finalConvertView = convertView;

        if (user != null) {
            pseudo = convertView.findViewById(R.id.pseudo);
            pseudo.setText(user.pseudo);
            solde = convertView.findViewById(R.id.solde);

            checkBox = convertView.findViewById(R.id.checkbox);
            if (user.pseudo.equals(admin.pseudo)) {
                checkBox.setEnabled(false);
                checkBox.setVisibility(View.GONE);
            } else {
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        user.ischecked = isChecked;
                    }
                });
            }


            if (user.balance != 0.00) {
                solde.setText("Solde : " + String.format("%.2f", user.balance) + " €");
                if (user.balance > 0.00) {
                    solde.setTextColor(Color.parseColor("#019940"));
                } else {
                    solde.setTextColor(Color.RED);
                }
            }

        }

        return convertView;
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }
}
