package group2.seshealthpatient.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import group2.seshealthpatient.Model.Profile;
import group2.seshealthpatient.R;

/**
 * Class: PatientInformationFragment
 * Extends: {@link Fragment}
 * Author: Carlos Tirado < Carlos.TiradoCorts@uts.edu.au> and YOU!
 * Description:
 * <p>
 * This fragment's job will be that to display patients information, and be able to edit that
 * information (either edit it in this fragment or a new fragment, up to you!)
 * <p>

 */
public class PatientInformationFragment extends Fragment {


    // Note how Butter Knife also works on Fragments, but here it is a little different
    @BindView(R.id.EditViewFirstName)
    EditText EditViewFirstName;
    @BindView(R.id.EditViewLastName)
    EditText EditViewLastName;
    @BindView(R.id.EditViewAge)
    EditText EditViewAge;
    @BindView(R.id.SaveBtn)
    Button SaveBtn;
    @BindView(R.id.SpinnerGender)
    Spinner spinnerGender;
    @BindView(R.id.EditViewHeight)
    EditText EditViewHeight;
    @BindView(R.id.EditViewWeight)
    EditText EditViewWeight;
    @BindView(R.id.EditViewAdd)
    EditText EditViewAdd;
    @BindView(R.id.EditViewMC)
    EditText EditViewMC;

    @BindView(R.id.TextViewEmail)
    TextView textViewEmail;


    private DatabaseReference databaseUsers;
    private FirebaseAuth firebaseAuth;
    private String Email;
    private FirebaseUser user;


    public PatientInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Instead of hardcoding the title perhaps take the user name from somewhere?
        // Note the use of getActivity() to reference the Activity holding this fragment
        getActivity().setTitle("Username Information");


        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference();
        Email = firebaseAuth.getCurrentUser().getEmail();
        user = FirebaseAuth.getInstance().getCurrentUser();


    }

    @Override
    public void onStart() {
        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String AccountStatus = dataSnapshot.child("Users").child(user.getUid()).child("AccountStatus").getValue().toString();
                String emial = dataSnapshot.child("Users").child(user.getUid()).child("Email").getValue().toString();
                textViewEmail.setText(emial);

                if(AccountStatus.equals("New")){
                    Toast.makeText(getContext(), "Lets fill up the profile first.", Toast.LENGTH_LONG).show();

            }
                else{
                    String Fisrtname = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("firstname").getValue().toString();
                    String Lastname = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("lastname").getValue().toString();
                    String Address = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("address").getValue().toString();
                    String Age = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("age").getValue().toString();
                    String Height = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("height").getValue().toString();
                    String Weight = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("weight").getValue().toString();
                    String Mc = dataSnapshot.child("Users").child(user.getUid()).child("BasicInf").child("profile").child("mc").getValue().toString();

                    EditViewFirstName.setText(Fisrtname);
                    EditViewLastName.setText(Lastname);
                    EditViewAge.setText(Age);
                    EditViewWeight.setText(Weight);
                    EditViewHeight.setText(Height);
                    EditViewAdd.setText(Address);
                    EditViewMC.setText(Mc);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.SaveBtn)
        public void saveData(){
            String firstname = EditViewFirstName.getText().toString().trim();
            String lastname = EditViewLastName.getText().toString().trim();
            String age = EditViewAge.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();
            String weight = EditViewWeight.getText().toString().trim();
            String height = EditViewHeight.getText().toString().trim();
            String add = EditViewAdd.getText().toString().trim();
            String mc = EditViewMC.getText().toString().trim();


            if(!TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname)){

            Profile profile = new Profile();
            profile.setFirstname(firstname);
            profile.setLastname(lastname);
            profile.setAge(age);
            profile.setGender(gender);
            profile.setWeight(weight);
            profile.setHeight(height);
            profile.setAddress(add);
            profile.setMc(mc);
            profile.setId(user.getUid());


            FirebaseUser user = firebaseAuth.getCurrentUser();

            databaseUsers.child("Users").child(user.getUid()).child("BasicInf").child("profile").setValue(profile);

            databaseUsers.child("Users").child(user.getUid()).child("AccountStatus").setValue("Old");


                Toast.makeText(getContext(), "Saved the information successfully!", Toast.LENGTH_LONG).show();

        }
        else {
                Toast.makeText(getContext(),"You should enter your name!",Toast.LENGTH_LONG).show();}
       }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_information, container, false);

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
