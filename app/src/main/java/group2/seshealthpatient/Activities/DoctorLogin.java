package group2.seshealthpatient.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import group2.seshealthpatient.R;


public class DoctorLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference mref;
    FirebaseUser user;
    EditText DoctorEmailEditText;
    EditText DoctorPasswordEditText;
    Button loginBtn;

    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        mAuth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        DoctorEmailEditText = findViewById(R.id.DoctorLoginEmail);
        DoctorPasswordEditText = findViewById(R.id.DoctorLoginPassword);
        loginBtn = findViewById(R.id.login_btn);

        mLoginProgress = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = DoctorEmailEditText.getText().toString();
                String password = DoctorPasswordEditText.getText().toString();
                if(!username.equals("")&& !password.equals("")){

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials!");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mLoginProgress.dismiss();

                                // Start a new activity
                                mref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String status = dataSnapshot.child("Doctors").child(user.getUid()).child("AccountStatus").getValue(String.class).toString();
                                        if (status.equals("New")){
                                            mref.child("Doctors").child(user.getUid()).child("Request").setValue("None");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                Intent intent = new Intent(DoctorLogin.this, DoctorMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                }
                            else{
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });



                }
                else{
                    mLoginProgress.hide();
                    Toast.makeText(getApplicationContext(), "You did not fill in all the fields.", Toast.LENGTH_SHORT).show();}

            }
        });
        }


}
