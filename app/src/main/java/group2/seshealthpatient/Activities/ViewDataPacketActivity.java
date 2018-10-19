package group2.seshealthpatient.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group2.seshealthpatient.Model.DataPacket;
import group2.seshealthpatient.R;

public class ViewDataPacketActivity extends AppCompatActivity {
    private DatabaseReference mRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    ListView Datapacket;
    String ID;
    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_packet);

        Intent intent = getIntent();
        ID = intent.getStringExtra("id");

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mToolbar = findViewById(R.id.view_datapacket_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("DataPacket List");


        Datapacket = findViewById(R.id.DataPacketListView);

        mRef.addValueEventListener(new ValueEventListener() {
            final List<Map<String,Object>> datamap = new ArrayList<Map<String,Object>>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.child("Users").child(ID).child("Datapackets").getChildren()){
                    String title = ds.getValue(DataPacket.class).getTitle();
                    String time = ds.getValue(DataPacket.class).getTime();

                    Map<String,Object> dataitem = new HashMap<>();
                    dataitem.put("Title","Title: "+title);
                    dataitem.put("Time","Time: "+time);
                    datamap.add(dataitem);
                    }
                SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), datamap,R.layout.singleofdatapacket, new String[]{"Title","Time"}, new int[]{R.id.DatapacketTitle,R.id.DataPacketTime});

                Datapacket.setAdapter(myAdapter);

                Datapacket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String Title = datamap.get(i).get("Title").toString().substring(7);

                        Intent intent6 = new Intent(ViewDataPacketActivity.this, DataPacketDetailActivity.class);
                        intent6.putExtra("title",Title);
                        intent6.putExtra("ID",ID);
                        startActivity(intent6);
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { } });
    }
}
