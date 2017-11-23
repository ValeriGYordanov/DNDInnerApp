package app.inner.drinkanddrivebrothers.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.Course;

/**
 * Created by plame_000 on 23-Nov-17.
 */

public class CurrentStatsRecycler extends RecyclerView.Adapter<CurrentStatsRecycler.ViewHolder>{
    private ArrayList<Course> arrCource;
    private Context ctx;
    private LayoutInflater mInflater;

    public CurrentStatsRecycler(Context ctx, ArrayList<Course> arrCource){
        this.mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.arrCource = arrCource;
    }

    @Override
    public CurrentStatsRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_current_stats_row, parent, false);

        CurrentStatsRecycler.ViewHolder viewHolder = new CurrentStatsRecycler.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Course course = arrCource.get(position);

        String phone = course.getNumber();
        Log.e("statusche", phone);
        String hour = course.getHour();
        Log.e("statusche", hour);
        String km = String.valueOf(course.getKm());
        Log.e("statusche", km);
        String price = String.valueOf(course.getPrice());
        Log.e("statusche", price);

        holder.phone.setText("Телефон: " + phone);
        holder.hour.setText("Час: " + hour);
        holder.km.setText("КМ: " + km);
        holder.price.setText("Цена: " + price);

    }

    @Override
    public int getItemCount() {
        return arrCource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView hour, km, phone, price;

        public ViewHolder(View itemView) {
            super(itemView);

            hour = (TextView) itemView.findViewById(R.id.rec_curr_stats_hour);
            km = (TextView) itemView.findViewById(R.id.rec_curr_stats_km);
            phone = (TextView) itemView.findViewById(R.id.rec_curr_stats_phone);
            price = (TextView) itemView.findViewById(R.id.rec_curr_stats_price);
        }
    }
}
