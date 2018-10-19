package group2.seshealthpatient.Activities;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group2.seshealthpatient.Model.DataPacket;
import group2.seshealthpatient.Model.Doctor;
import group2.seshealthpatient.Model.Upload;
import group2.seshealthpatient.R;

public class PatientViewDataPacketActivity extends AppCompatActivity {

    private ListView patientdatapacketlistview;
    private DatabaseReference mRef;
    private FirebaseUser user;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_data_packet);

        mToolbar = (Toolbar)findViewById(R.id.datapacket_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("DataPacket List");

        mRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        patientdatapacketlistview = findViewById(R.id.PatientDataPacketListView);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Map<String,String>> maplist = new ArrayList<Map<String,String>>();
                for(DataSnapshot ds: dataSnapshot.child("Users").child(user.getUid()).child("Datapackets").getChildren()){
                    DataPacket dataPacket = new DataPacket();
                    String Title  = ds.getValue(DataPacket.class).getTitle();
                    String Message  = ds.getValue(DataPacket.class).getDataText();
                    String Time = ds.getValue(DataPacket.class).getTime();
                    Log.v("Title",Title);

                if (dataSnapshot.child("Users").child(user.getUid()).child("Datapackets").child(Title).getChildrenCount() ==3){
                    Map<String ,String> docitem1 = new HashMap<String, String>();
                    docitem1.put("Title","Title: "+Title);
                    docitem1.put("Message","Message: "+Message);
                    docitem1.put("Time","Time: "+Time);


                    maplist.add(docitem1);

                    SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), maplist, R.layout.singleofpatientdatapacket, new String[]{"Title","Message","Time"}, new int[]{R.id.PatientDataTitle, R.id.PatientDataMessage, R.id.PatientDataTime});

                    patientdatapacketlistview.setAdapter(myAdapter);


                }
                 else{   String Filename = dataSnapshot.child("Users").child(user.getUid()).child("Datapackets").child(Title).child("File").child("name").getValue().toString();
                    String Format = dataSnapshot.child("Users").child(user.getUid()).child("Datapackets").child(Title).child("File").child("type").getValue().toString();

                    //Log.v("Name2",Filename);
                        Map<String ,String> docitem = new HashMap<String, String>();
                       docitem.put("Title","Title: "+Title);
                       docitem.put("Message","Message: "+Message);
                       docitem.put("Time","Time: "+Time);
                       docitem.put("Filename","Filename: "+ Filename+"."+Format);

                       maplist.add(docitem);}

                SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), maplist, R.layout.singleofpatientdatapacket, new String[]{"Title","Message","Time","Filename"}, new int[]{R.id.PatientDataTitle, R.id.PatientDataMessage, R.id.PatientDataTime,R.id.PatientDataFilename});

                patientdatapacketlistview.setAdapter(myAdapter);


            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
