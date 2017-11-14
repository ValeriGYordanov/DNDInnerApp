package app.inner.drinkanddrivebrothers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
/**
 * Created by HP on 14.11.2017 г..
 */

public class DriversRecView extends
        RecyclerView.Adapter<DriversRecView.ViewHolder> {


    private List<String> filterList;
    private Context context;

    public DriversRecView(List<String> filterModelList
            , Context ctx) {
        filterList = filterModelList;
        context = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String driverName = filterList.get(position);
        holder.selectionState.setText(driverName);

    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CheckBox selectionState;

        public ViewHolder(View view) {
            super(view);

            selectionState = view.findViewById(R.id.driver_checkbox);
            view.setOnClickListener(this);

            selectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(DriversRecView.this.context,
                                "the selected driver is " + selectionState.getText(),
                                Toast.LENGTH_LONG).show();
                    } else {

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            // Tova e onClick ako se cukne cqloto ime
        }
    }
}
