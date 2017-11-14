package app.inner.drinkanddrivebrothers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {

    private RecyclerView driverRecView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        driverRecView = (RecyclerView) findViewById(R.id.driver_list);

        //RecyclerView layout manager
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        driverRecView.setLayoutManager(recyclerLayoutManager);

        //RecyclerView item decorator
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(driverRecView.getContext(),
                        recyclerLayoutManager.getOrientation());
        driverRecView.addItemDecoration(dividerItemDecoration);

        //RecyclerView adapater
        DriversRecView recyclerViewAdapter = new
                DriversRecView(getDrivers(),this);
        driverRecView.setAdapter(recyclerViewAdapter);
    }

    public List<String> getDrivers() {
        List<String> modelList = new ArrayList<String>();
        modelList.add("Pesho");
        modelList.add("gosho");
        modelList.add("Tosho");
        modelList.add("Ivan");
        modelList.add("Pencho");
        modelList.add("Uli");
        return modelList;
    }
}
