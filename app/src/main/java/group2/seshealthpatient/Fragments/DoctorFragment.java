package group2.seshealthpatient.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import group2.seshealthpatient.Activities.DoctorListActivity;
import group2.seshealthpatient.Activities.DoctorProfileActivity;
import group2.seshealthpatient.Model.Doctor;
import group2.seshealthpatient.R;

public class DoctorFragment extends Fragment {

    @BindView(R.id.DoctorTextView)
    TextView DoctorTextView;
    @BindView(R.id.DocInfName)
    TextView DoctorName;
    @BindView(R.id.DocInfGender)
    TextView DoctorGender;
    @BindView(R.id.DocInfProfession)
    TextView DoctorProfession;
    @BindView(R.id.fdback)
    TextView fdback;

    private DatabaseReference mRef;
    private FirebaseUser user;

    private String name;
    private String profession;
    private String gender;
    private String ID;
    private String feedback;



    public DoctorFragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Doctor Information");

        mRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user.getUid()).child("Doctor").getValue().toString().equals("None")) {
                    DoctorTextView.setText(getString(R.string.doctornone));

                } else {
                    name = dataSnapshot.child("Users").child(user.getUid()).child("Doctor").child("fullname").getValue().toString();
                    profession = dataSnapshot.child("Users").child(user.getUid()).child("Doctor").child("profession").getValue().toString();
                    gender = dataSnapshot.child("Users").child(user.getUid()).child("Doctor").child("gender").getValue().toString();
                    ID = dataSnapshot.child("Users").child(user.getUid()).child("Doctor").child("id").getValue().toString();

                    Log.v("iddddd",ID);
                    Log.v("111111111",dataSnapshot.child("Doctors").child(ID).child("Request").child(user.getUid()).child("status").getValue().toString());

                    DoctorName.setText(name);
                    DoctorGender.setText(gender);
                    DoctorProfession.setText(profession);

                    if ((dataSnapshot.child("Doctors").child(ID).child("Request").child(user.getUid()).child("status").getValue().toString()).equals("Waiting for response")) {
                        DoctorTextView.setText(getString(R.string.requestwaiting));
                    }

                    else if ((dataSnapshot.child("Doctors").child(ID).child("Request").child(user.getUid()).child("status").getValue().toString()).equals("Accepted")) {
                        DoctorTextView.setText(getString(R.string.requestaccepted));

                        for (DataSnapshot ds1 : dataSnapshot.child("Users").child(user.getUid()).child("Feedback").child(ID).getChildren()) {
                            feedback = ds1.getValue(String.class);
                            fdback.setText(feedback);}
                    }


                    else if ((dataSnapshot.child("Doctors").child(ID).child("Request").child(user.getUid()).child("status").getValue().toString()).equals("Refused")) {
                        DoctorTextView.setText(getString(R.string.requestrefuesed));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @OnClick(R.id.ChooseDocBtn)
    public void chooseDoc(){
        Intent intent  = new Intent(getActivity(), DoctorListActivity.class);
        startActivity(intent);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor, container, false);
        // Note how we are telling butter knife to bind during the on create view method
        ButterKnife.bind(this, v);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        // Now that the view has been created, we can use butter knife functionality


    }

}
