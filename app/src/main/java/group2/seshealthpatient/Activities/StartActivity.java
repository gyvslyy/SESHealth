package group2.seshealthpatient.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import group2.seshealthpatient.R;

public class StartActivity extends AppCompatActivity {

    private Button doctor_entrance_btn;
    private Button patientLoginBtn;
    private Button patientRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        doctor_entrance_btn = (Button)findViewById(R.id.doctor_entrance_btn);
        patientLoginBtn = findViewById(R.id.already_reg_btn);
        patientRegisterBtn = findViewById(R.id.start_reg_btn);

        doctor_entrance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_intent = new Intent(StartActivity.this, DoctorLogin.class);
                startActivity(login_intent); }
        });


        patientLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent2);

            }
        });


        patientRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent (StartActivity.this, RegisterActivity.class);
                startActivity(intent1);
            }
        });
    }


    }
