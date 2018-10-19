package group2.seshealthpatient.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import group2.seshealthpatient.Model.Doctor;
import group2.seshealthpatient.R;


public class DoctorProfileActivity extends AppCompatActivity {
    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String accountstatus;

    private Toolbar mToolbar;

    EditText DocNameeditText;
    EditText DocageeditText;
    Spinner spinnergender;
    EditText Docprofession;
    EditText Docaddress;
    EditText DocBriefIntroduction;
    EditText DocNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        mref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        mToolbar = (Toolbar)findViewById(R.id.profile_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Doctor Profile");

        DocNameeditText= (EditText)findViewById(R.id.EditViewDocFullName);
        DocageeditText= (EditText)findViewById(R.id.EditViewDocAge);
        spinnergender = findViewById(R.id.SpinnerDocGender);
        Docprofession= (EditText)findViewById(R.id.EditViewProfession);
        Docaddress = findViewById(R.id.EditViewAddress);
        DocBriefIntroduction = findViewById(R.id.EditViewBriefIntroduction);
        DocNumber = findViewById(R.id.EditViewTelephone);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accountstatus = dataSnapshot.child("Doctors").child(user.getUid()).child("AccountStatus").getValue().toString();

                if (accountstatus.equals("New")){
                    Toast.makeText(DoctorProfileActivity.this,"Lets fill in the information first",Toast.LENGTH_LONG).show();
                }
                else {
                    String fullname = dataSnapshot.child("Doctors").child(user.getUid()).child("BasicInf").child("profile").child("fullname").getValue().toString();
                    String address = dataSnapshot.child("Doctors").child(user.getUid()).child("BasicInf").child("profile").child("address").getValue().toString();
                    String age = dataSnapshot.child("Doctors").child(user.getUid()).child("BasicInf").child("profile").child("age").getValue().toString();
                    String brief = dataSnapshot.child("Doctors").child(user.getUid()).child("BasicInf").child("profile").child("briefIntruduction").getValue().toString();
                    String phonenumber = dataSnapshot.child("Doctors").child(user.getUid()).child("BasicInf").child("profile").child("phoneNumber").getValue().toString();
                    String profession = dataSnapshot.child("Doctors").child(user.getUid()).child("BasicInf").child("profile").child("profession").getValue().toString();

                    DocNameeditText.setText(fullname);
                    DocageeditText.setText(age);
                    Docprofession.setText(profession);
                    Docaddress.setText(address);
                    DocBriefIntroduction.setText(brief);
                    DocNumber.setText(phonenumber);
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        findViewById(R.id.SaveDocBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDoc();
            }
        });


    }



        public void CreateDoc(){
            Doctor doctor = new Doctor();
            doctor.setId(user.getUid());
            doctor.setFullname(DocNameeditText.getText().toString().trim());
            doctor.setAge(DocageeditText.getText().toString().trim());
            doctor.setGender(spinnergender.getSelectedItem().toString());
            doctor.setProfession(Docprofession.getText().toString().trim());
            doctor.setAddress(Docaddress.getText().toString().trim());
            doctor.setBriefIntruduction(DocBriefIntroduction.getText().toString().trim());
            doctor.setPhoneNumber(DocNumber.getText().toString().trim());
            mref.child("Doctors").child(user.getUid()).child("BasicInf").child("profile").setValue(doctor);
            mref.child("Users").child("Doctors").child(user.getUid()).setValue(doctor);
            mref.child("Doctors").child(user.getUid()).child("AccountStatus").setValue("Old");
            Toast.makeText(this, "Saved the information successfully!", Toast.LENGTH_LONG).show();



        }
    }

