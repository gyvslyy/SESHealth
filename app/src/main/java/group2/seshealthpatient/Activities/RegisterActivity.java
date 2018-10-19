package group2.seshealthpatient.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import group2.seshealthpatient.R;



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private static final String TAG = "RegisterActivity";
    EditText EmailText, PasswordText, Confirm_passwordText;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        EmailText = findViewById(R.id.EmailTextfield);
        PasswordText = findViewById(R.id.PasswordTextfield);
        Confirm_passwordText = findViewById(R.id.ConfirmTestfield);
        findViewById(R.id.CreateBtn).setOnClickListener(this);
    }

   private void RegisterUser() {

       final String Email = EmailText.getText().toString().trim();
       String Password = PasswordText.getText().toString().trim();
       String ConfirmPassword = Confirm_passwordText.getText().toString().trim();

       if (Email.isEmpty()) {
           EmailText.setError("Email is required!");
           EmailText.requestFocus();
           return;
       }

       if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
           EmailText.setError("Please enter a valid email!");
           EmailText.requestFocus();
           return;
       }


       if (Password.isEmpty()) {
           PasswordText.setError("Password is required!");
           PasswordText.requestFocus();
           return;
       }

       if (Password.length() < 6) {
           PasswordText.setError("Minimum length of password should be 6");
           PasswordText.requestFocus();
           return;
       }

       if (Password.equals(ConfirmPassword) == false) {
           Confirm_passwordText.setError("Please enter the same password!");
           Confirm_passwordText.requestFocus();
           return;
       }

       mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                   Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                   FirebaseUser user = mAuth.getCurrentUser();
                   mRef.child("Users").child(user.getUid()).child("AccountStatus").setValue("New");
                   mRef.child("Users").child(user.getUid()).child("Email").setValue(Email);
                   mRef.child("Users").child(user.getUid()).child("Doctor").setValue("None");

               } else {
                   if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                       Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_LONG).show();
                   } else {
                       Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                   }

               }
           }
       });
   }








    @Override public void onClick(View view) {

        switch (view.getId()) {
            case R.id.CreateBtn:
                RegisterUser();
                break;
                }
        }
    }
