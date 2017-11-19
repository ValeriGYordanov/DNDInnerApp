package app.inner.drinkanddrivebrothers.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.Course;
import app.inner.drinkanddrivebrothers.model.User;

/**
 * Created by HP on 19.11.2017 г..
 */

public class StatisticsRecView extends
        RecyclerView.Adapter<StatisticsRecView.ViewHolder>{


    private List<Course> listCourses;
    private Context context;

    public StatisticsRecView(Context ctx, ArrayList<Course> listCourses) {
        context = ctx;
        this.listCourses = listCourses;
    }

    @Override
    public StatisticsRecView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_recycler_view, parent, false);

        StatisticsRecView.ViewHolder viewHolder = new StatisticsRecView.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StatisticsRecView.ViewHolder holder, int position) {

        Course course = listCourses.get(position);
        holder.driver1Name.setText(course.getDriver1());
        holder.driver2Name.setText(course.getDriver2());
        holder.number.setText(course.getNumber());
        holder.hour.setText(course.getHour());
        holder.km.setText(Float.toString(course.getKm()));
        holder.price.setText(Double.toString(course.getPrice()));

    }

    @Override
    public int getItemCount() {
        return listCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number, km, driver1Name, driver2Name, hour, price;

        public ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.statistics_list_phone_number);
            km = itemView.findViewById(R.id.statistics_list_course_km);
            driver1Name = itemView.findViewById(R.id.statistics_list_first_driver);
            driver2Name = itemView.findViewById(R.id.statistics_list_second_driver);
            hour = itemView.findViewById(R.id.statistics_list_course_hour);
            price = itemView.findViewById(R.id.statistics_list_course_price);
        }
    }
}
