package group2.seshealthpatient.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.PortUnreachableException;
import java.util.ArrayList;

import group2.seshealthpatient.Model.Doctor;
import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.R;



public class PatientDetailActivity extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    String name;
    ListView PatientDetailList;
    Button View_Upload_btn;
    String ID;
    Button feedbackBtn,viewHrBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mToolbar = (Toolbar)findViewById(R.id.patient_detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Patient Detail");

        PatientDetailList = findViewById(R.id.PatientDetailListView);
        View_Upload_btn = findViewById(R.id.ViewDatapacket);
        feedbackBtn = findViewById(R.id.feedbackBtn);
        viewHrBtn = findViewById(R.id.ViewHRBtn);

        final Intent intent = getIntent();
        name = intent.getStringExtra("name");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile profile = new Profile();
                for(DataSnapshot ds: dataSnapshot.child("Doctors").child(user.getUid()).child("Patients").getChildren()){

                    String Fisrtname = ds.getValue(Profile.class).getFirstname();
                    String Lastname = ds.getValue(Profile.class).getLastname();
                    String Address = ds.getValue(Profile.class).getAddress();
                    String Age = ds.getValue(Profile.class).getAge();
                    String Height = ds.getValue(Profile.class).getHeight();
                    String Weight = ds.getValue(Profile.class).getWeight();
                    String Mc = ds.getValue(Profile.class).getMc();
                    String Gender = ds.getValue(Profile.class).getGender();
                    String id = ds.getValue(Profile.class).getId();

                    profile.setFirstname(Fisrtname);
                    profile.setLastname(Lastname);
                    profile.setAddress(Address);
                    profile.setAge(Age);
                    profile.setHeight(Height);
                    profile.setWeight(Weight);
                    profile.setMc(Mc);
                    profile.setGender(Gender);
                    profile.setId(id);


                    if(profile.getFirstname().equals(name)){
                        ArrayList<String> array = new ArrayList<>();
                        array.add("FullName: "+ profile.getFirstname()+" "+ profile.getLastname());
                        array.add("Age: "+ profile.getAge());
                        array.add("Gender: "+ profile.getGender());
                        array.add("Address:"+profile.getAddress());
                        array.add("Height: "+ profile.getHeight());
                        array.add("Weight: "+profile.getWeight());
                        array.add("Medical condition: "+profile.getMc());
                        ID = profile.getId();

                        ArrayAdapter adapter = new ArrayAdapter(PatientDetailActivity.this, android.R.layout.simple_list_item_1,array);
                        PatientDetailList.setAdapter(adapter);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //查看datapacket
        View_Upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(PatientDetailActivity.this,ViewDataPacketActivity.class);
                intent3.putExtra("id",ID);
                startActivity(intent3);


            }
        });
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(PatientDetailActivity.this,Feedback.class);
                intent4.putExtra("id",ID);
                startActivity(intent4);
            }
        });
        viewHrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(PatientDetailActivity.this,ViewHeartRateActivity.class);
                intent5.putExtra("id",ID);
                startActivity(intent5);
            }
        });

    }

}
