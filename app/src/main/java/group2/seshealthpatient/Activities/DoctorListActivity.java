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

import group2.seshealthpatient.Model.Doctor;
import group2.seshealthpatient.R;

public class DoctorListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ListView doclistview;
    private FirebaseUser user;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        doclistview = (ListView) findViewById(R.id.DoctorListView);

        mToolbar = (Toolbar)findViewById(R.id.doctor_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Doctor List");


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        user = mAuth.getInstance().getCurrentUser();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Map<String,String>> maplist = new ArrayList<Map<String,String>>();
                for(DataSnapshot ds: dataSnapshot.child("Users").child("Doctors").getChildren()){
                    Doctor doctor = new Doctor();
                    String fullname = ds.getValue(Doctor.class).getFullname();
                    String age = ds.getValue(Doctor.class).getAge();
                    String gender= ds.getValue(Doctor.class).getGender();
                    String profession = ds.getValue(Doctor.class).getProfession();


                    doctor.setAge(age);
                    doctor.setFullname(fullname);
                    doctor.setGender(gender);
                    doctor.setProfession(profession);


                    Map<String ,String> docitem = new HashMap<String, String>();
                    docitem.put("FullName","Name: "+fullname);
                    docitem.put("Gender","Gender: "+gender);
                    docitem.put("Profession","Profession: "+profession);
                    maplist.add(docitem);}

                    SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), maplist, R.layout.doctorlist_layout, new String[]{"FullName", "Gender", "Profession"}, new int[]{R.id.DocName, R.id.DocGender, R.id.DocProfession});

                    doclistview.setAdapter(myAdapter);

                    doclistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String ChoosedDoctorname = maplist.get(i).get("FullName").toString().substring(6);
                            Log.v("test1",ChoosedDoctorname);
                            Intent intent = new Intent(DoctorListActivity.this,DocDetail.class);
                            intent.putExtra("name",ChoosedDoctorname);
                            startActivity(intent);
                        }
                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
