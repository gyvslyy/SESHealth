package group2.seshealthpatient.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.R;

public class PatientListActivity extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ListView patientList;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user =mAuth.getCurrentUser();


        mToolbar = (Toolbar)findViewById(R.id.patient_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Patient List");

        patientList = findViewById(R.id.PatientListView);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<Map<String,Object>> patitentmap = new ArrayList<Map<String,Object>>();

                for (DataSnapshot ds: dataSnapshot.child("Doctors").child(user.getUid()).child("Patients").getChildren()) {


                    String firstname = ds.getValue(Profile.class).getFirstname();
                    String lastname = ds.getValue(Profile.class).getLastname();
                    String age = ds.getValue(Profile.class).getAge();
                    String gender = ds.getValue(Profile.class).getGender();

                    Map<String, Object> patientitem = new HashMap<String, Object>();
                    patientitem.put("FirstName", firstname);
                    patientitem.put("LastName"," "+lastname);
                    patientitem.put("Gender", "Gender: "+ gender);
                    patientitem.put("Age","Age: "+ age);

                    patitentmap.add(patientitem);}

                    SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), patitentmap, R.layout.singleofpatientlist, new String[]{"FirstName","LastName", "Gender", "Age"}, new int[]{R.id.PatientFirstName,R.id.PatientLastName, R.id.PatientGender, R.id.PatientAge});

                    patientList.setAdapter(myAdapter);

                    patientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String ChoosedPatientFirstname = patitentmap.get(i).get("FirstName").toString();
                            Log.v("Test",ChoosedPatientFirstname);

                            Intent intent1 = new Intent(PatientListActivity.this, PatientDetailActivity.class);
                            intent1.putExtra("name",ChoosedPatientFirstname);
                            startActivity(intent1);
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


