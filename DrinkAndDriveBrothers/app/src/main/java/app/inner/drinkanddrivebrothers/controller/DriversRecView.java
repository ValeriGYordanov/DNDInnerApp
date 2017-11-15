package app.inner.drinkanddrivebrothers.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.User;

/**
 * Created by HP on 14.11.2017 г..
 */

public class DriversRecView extends
        RecyclerView.Adapter<DriversRecView.ViewHolder> {

    private List<User> listUsers;
    private Context context;
    private HashMap<Integer, User> checkedUsers;


    public DriversRecView(Context ctx, ArrayList<User> listUsers) {
        context = ctx;
        this.listUsers = listUsers;
        checkedUsers = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.clear();

        final User user = listUsers.get(position);
        holder.selectionState.setText(user.getName() + ", " + user.getLastName());

        holder.selectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    user.setChecked(true);
                    checkedUsers.put(position, user);
                    Log.e("testche", " " + checkedUsers.size());
                } else {
                    user.setChecked(false);
                    checkedUsers.remove(position);
                    Log.e("testche", " " + checkedUsers.size());
                }

            }
        });

        for (Integer i : checkedUsers.keySet()) {
            if (i == position) {
                holder.selectionState.setChecked(true);
            }
        }

    }

    //проверяваме дали са селектнати > или < от 2ма... и ако са... връщаме нъл коойто го прихващаме оттам
    // може да измислим някъв по-як начин
    public ArrayList<User> getSelectedUsers() {

        ArrayList<User> selectedUsers = new ArrayList<>();
        Log.e("testche", " " + checkedUsers.size());
        for (Iterator<Map.Entry<Integer, User>> it = checkedUsers.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = it.next();
            User user = (User) entry.getValue();
            if (user.isChecked()) {
                selectedUsers.add(user);
            }
        }
        if (selectedUsers.size() == 2) {
            return selectedUsers;
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CheckBox selectionState;

        public ViewHolder(View view) {
            super(view);
            selectionState = view.findViewById(R.id.driver_checkbox);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // Tova e onClick ako se cukne cqloto ime
        }

        private void clear() {
            selectionState.setChecked(false);
        }
    }
}
