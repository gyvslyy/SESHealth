package group2.seshealthpatient.Activities;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import group2.seshealthpatient.Fragments.DoctorFragment;
import group2.seshealthpatient.Model.Doctor;
import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.Model.Request;
import group2.seshealthpatient.R;

public class DocDetail extends AppCompatActivity {
    private DatabaseReference mRef;
    private FirebaseUser user;
    private ListView DocdetailList;
    private Button chooseBtn;
    String name;
    Doctor doctor;
    Doctor doctor1;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);
        DocdetailList = findViewById(R.id.DocDetailListView);
        chooseBtn = findViewById(R.id.ConfirmBtn);


        mToolbar = (Toolbar)findViewById(R.id.doc_detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Doctor Detail");

        mRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
         name = intent.getStringExtra("name");

         doctor = new Doctor();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.child("Users").child("Doctors").getChildren()){
                    String fullname = ds.getValue(Doctor.class).getFullname();
                    String age = ds.getValue(Doctor.class).getAge();
                    String gender= ds.getValue(Doctor.class).getGender();
                    String profession = ds.getValue(Doctor.class).getProfession();
                    String number = ds.getValue(Doctor.class).getPhoneNumber();
                    String brief = ds.getValue(Doctor.class).getBriefIntruduction();
                    String address = ds.getValue(Doctor.class).getAddress();
                    String id = ds.getValue(Doctor.class).getId();

                    doctor.setAge(age);
                    doctor.setFullname(fullname);
                    doctor.setGender(gender);
                    doctor.setProfession(profession);
                    doctor.setPhoneNumber(number);
                    doctor.setAddress(address);
                    doctor.setBriefIntruduction(brief);
                    doctor.setId(id);

                    if(doctor.getFullname().equals(name)){
                        ArrayList<String> array = new ArrayList<>();
                        array.add("FullName: "+ doctor.getFullname());
                        array.add("Age: "+ doctor.getAge());
                        array.add("Gender: "+ doctor.getGender());
                        array.add("Profession:"+doctor.getProfession());
                        array.add("Address: "+ doctor.getAddress());
                        array.add("PhoneNumber: "+doctor.getPhoneNumber());
                        array.add("Brief Introduction: "+doctor.getBriefIntruduction());


                        ArrayAdapter adapter = new ArrayAdapter(DocDetail.this, android.R.layout.simple_list_item_1,array);
                        DocdetailList.setAdapter(adapter);
                        }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);

            }
        });
    }

    private void showDialog(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.simple_dialog);
        builder.setMessage(R.string.dialog_message);

        //监听下方button点击事件
        builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),R.string.toast_positive,Toast.LENGTH_SHORT).show();
                doctor1 = new Doctor();
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Fisrtname = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("firstname").getValue().toString();
                        String Lastname = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("lastname").getValue().toString();
                        String Address = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("address").getValue().toString();
                        String Age = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("age").getValue().toString();
                        String Height = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("height").getValue().toString();
                        String Weight = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("weight").getValue().toString();
                        String Mc = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("mc").getValue().toString();
                        String Gender = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("gender").getValue().toString();
                        String ID = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("id").getValue().toString();

                        Profile profile = new Profile();
                        profile.setFirstname(Fisrtname);
                        profile.setLastname(Lastname);
                        profile.setAge(Age);
                        profile.setGender(Gender);
                        profile.setWeight(Weight);
                        profile.setHeight(Height);
                        profile.setAddress(Address);
                        profile.setMc(Mc);
                        profile.setId(ID);

                        for(DataSnapshot ds1: dataSnapshot.child("Users").child("Doctors").getChildren()) {


                            String fullname = ds1.getValue(Doctor.class).getFullname();
                            String age = ds1.getValue(Doctor.class).getAge();
                            String gender = ds1.getValue(Doctor.class).getGender();
                            String profession = ds1.getValue(Doctor.class).getProfession();
                            String number = ds1.getValue(Doctor.class).getPhoneNumber();
                            String brief = ds1.getValue(Doctor.class).getBriefIntruduction();
                            String address = ds1.getValue(Doctor.class).getAddress();
                            String id = ds1.getValue(Doctor.class).getId();

                            doctor1.setAge(age);
                            doctor1.setFullname(fullname);
                            doctor1.setGender(gender);
                            doctor1.setProfession(profession);
                            doctor1.setPhoneNumber(number);
                            doctor1.setAddress(address);
                            doctor1.setBriefIntruduction(brief);
                            doctor1.setId(id);



                            if (doctor1.getFullname().equals(name)) {


                                Request request = new Request();
                                request.setName(Fisrtname + " " + Lastname);
                                request.setStatus("Waiting for response");
                                request.setID(ID);
                                mRef.child("Users").child(user.getUid()).child("Doctor").setValue(doctor1);
                                mRef.child("Doctors").child(doctor1.getId()).child("Request").child(user.getUid()).setValue(request);
                                Intent intent = new Intent(DocDetail.this, MainActivity.class);
                                intent.putExtra("id", 77);
                                startActivity(intent);
                                finish();
                                break;
                            }



                        }}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
        builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), R.string.toast_negative, Toast.LENGTH_SHORT).show();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();


    }



}
