package app.inner.drinkanddrivebrothers.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.User;
import app.inner.drinkanddrivebrothers.recyclerview.DriversRecView;
import app.inner.drinkanddrivebrothers.utility.Util;

public class DriverActivity extends AppCompatActivity {

    private RecyclerView driverRecView;
    private Button btnFinishSelection;
    private ArrayList<User> resultFromRecycler;
    private DatabaseReference ref;
    private ArrayList<User> allDrivers;
    private DriversRecView recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        driverRecView = findViewById(R.id.driver_list);
        btnFinishSelection = findViewById(R.id.btn_recycler_continue);

        //RecyclerView layout manager
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        driverRecView.setLayoutManager(recyclerLayoutManager);

        //RecyclerView item decorator
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(driverRecView.getContext(),
                        recyclerLayoutManager.getOrientation());

        driverRecView.addItemDecoration(dividerItemDecoration);

        getDrivers();

        btnFinishSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultFromRecycler = recyclerViewAdapter.getSelectedUsers();
                if(resultFromRecycler == null){
                    Toast.makeText(DriverActivity.this, "Избери тично двама потребителя!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    String driver1Names = resultFromRecycler.get(0).getName() + " " + resultFromRecycler.get(0).getLastName();
                    String driver2Names = resultFromRecycler.get(1).getName() + " " + resultFromRecycler.get(1).getLastName();
                    Intent intent = new Intent(DriverActivity.this, DriverWorkActivity.class);
                    intent.putExtra("driver1",driver1Names);
                    intent.putExtra("driver2",driver2Names);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void getDrivers() {
        allDrivers = new ArrayList<>();
        ref = Util.getFirebaseReference();
        ref.child("drivers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                for (DataSnapshot data : childrens) {
                    User user = data.getValue(User.class);
                    allDrivers.add(user);
                }

                recyclerViewAdapter = new DriversRecView(DriverActivity.this, allDrivers);
                driverRecView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
