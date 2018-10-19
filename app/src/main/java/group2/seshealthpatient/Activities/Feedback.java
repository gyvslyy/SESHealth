package group2.seshealthpatient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import group2.seshealthpatient.R;
//
public class Feedback extends AppCompatActivity {
    private int MAX_LENGTH = 200; // MAX NUMBER = 200
    private int Rest_Length = MAX_LENGTH;
    private EditText mEdt_view;
    private TextView mTv_num;
    private Button mBtn_confirm;

    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DataSnapshot ds;
    private String ID;
    private String feedback;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int minute;
    private int second;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        ID = intent.getStringExtra("id");

        mEdt_view = (EditText) findViewById(R.id.edt);
        mTv_num = (TextView) findViewById(R.id.edt_tv_num);
        mBtn_confirm = (Button) findViewById(R.id.btnConfirm);

        setFirebase();

        mBtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String currentTime = sdf.format(c.getTime());

                feedback = mEdt_view.getText().toString();
                mref.child("Users").child(ID).child("Feedback").child(user.getUid()).child(currentTime).setValue(feedback);
                toast("Sent successfully!");
                mEdt_view.setText("");
            }
        });
    }

    private void setFirebase(){
        mref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ds = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void toast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


/*        mEdt_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        setListener();
    }

    private void setListener() {
        mEdt_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (Rest_Length > 0) {
                    Rest_Length = MAX_LENGTH - mEdt_view.getText().length();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTv_num.setText(Rest_Length + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTv_num.setText(Rest_Length + "/200");
            }
        });
    }*/
}