package group2.seshealthpatient.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

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
import java.util.MissingResourceException;

import group2.seshealthpatient.Model.Doctor;
import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.Model.Request;
import group2.seshealthpatient.R;

public class RequestActivity extends AppCompatActivity {

    DatabaseReference mRef;
    FirebaseUser user;
    ListView RequestList;
    String Choosedname;
    String ChoosedID;
    TextView textView;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        RequestList = findViewById(R.id.RequestListView);
        textView = findViewById(R.id.RequestTextView);

        mToolbar = (Toolbar)findViewById(R.id.request_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("View Request");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Doctors").child(user.getUid()).child("Request").getValue().equals("None")){
                textView.setText("There is no invitation.");

//setText view
                }
                else {

                    textView.setText("Click to accept or refuse the invitation.");
                    final List<Map<String,String>> requestlist = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.child("Doctors").child(user.getUid()).child("Request").getChildren()){
                        String name = ds.getValue(Request.class).getName();
                        String status = ds.getValue(Request.class).getStatus();

                        Request request = new Request();

                        request.setStatus(status);
                        request.setName(name);

                        Map<String,String> requestmap = new HashMap<>();
                        requestmap.put("Name","Patient name: "+request.getName());
                        requestmap.put("Status","Request status: "+request.getStatus());

                        requestlist.add(requestmap);

                    }

                    SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), requestlist, R.layout.singleofrequest, new String[]{"Name","Status"}, new int[]{R.id.RequestName,R.id.RequestStatus});
                    RequestList.setAdapter(myAdapter);

                    RequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Choosedname = requestlist.get(i).get("Name").toString().substring(14);
                            Log.v("Name",Choosedname);
                            showDialog(view);
                        } });
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showDialog(View view){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.simple_dialog);
        builder.setMessage(R.string.dialog_request_message);

        //监听下方button点击事件
        builder.setPositiveButton(R.string.positive_request_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),R.string.toast_request_positive,Toast.LENGTH_SHORT).show();
                Log.v("Name",Choosedname);
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Request request = new Request();
                        for (DataSnapshot ds: dataSnapshot.child("Doctors").child(user.getUid()).child("Request").getChildren()){
                            String name = ds.getValue(Request.class).getName();
                            String status = ds.getValue(Request.class).getStatus();
                            String id = ds.getValue(Request.class).getID();

                             request.setID(id);
                             request.setName(name);
                             request.setStatus(status);
                             Log.v("name",name);
                            if (request.getName().equals(Choosedname)){
                                ChoosedID = request.getID();
                                Log.v("TestID",ChoosedID);

                                String Fisrtname = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("firstname").getValue().toString();
                                String Lastname = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("lastname").getValue().toString();
                                String Address = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("address").getValue().toString();
                                String Age = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("age").getValue().toString();
                                String Height = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("height").getValue().toString();
                                String Weight = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("weight").getValue().toString();
                                String Mc = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("mc").getValue().toString();
                                String Gender = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("gender").getValue().toString();
                                String ID = dataSnapshot.child("Users").child(ChoosedID).child("BasicInf").child("profile").child("id").getValue().toString();

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

                                request.setStatus("Accepted");

                                mRef.child("Doctors").child(user.getUid()).child("Patients").child(ID).setValue(profile);
                                mRef.child("Doctors").child(user.getUid()).child("Request").child(ID).setValue(request);
                            }
                        }



                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        builder.setNegativeButton(R.string.negative_request_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), R.string.toast_request_negative, Toast.LENGTH_SHORT).show();



                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.child("Doctors").child(user.getUid()).child("Request").getChildren()){
                            String name = ds.getValue(Request.class).getName();
                            String status = ds.getValue(Request.class).getStatus();
                            String id = ds.getValue(Request.class).getID();
                            Request request = new Request();
                            request.setID(id);
                            request.setName(name);
                            request.setStatus(status);
                            if (request.getName().equals(Choosedname)){
                                ChoosedID = request.getID();
                                request.setStatus("Refused");
                                mRef.child("Doctors").child(user.getUid()).child("Request").child(ChoosedID).setValue(request);

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();


    }


}

