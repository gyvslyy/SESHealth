package group2.seshealthpatient.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import group2.seshealthpatient.Model.DataPacket;
import group2.seshealthpatient.R;

public class DataPacketDetailActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference myRef;
    String title;
    String ID;
    String url;

    TextView DataTitle;
    TextView DataTime;
    TextView DataFile;
    TextView DataMessage;
    Button DownloadBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_packet_detail);

        myRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        ID = intent.getStringExtra("ID");

        DataTitle= findViewById(R.id.DataTitle);
        DataTime = findViewById(R.id.DataTime);
        DataFile = findViewById(R.id.DataFile);
        DataMessage = findViewById(R.id.DataMessage);
        DownloadBtn = findViewById(R.id.DownloadFile);

         myRef.addValueEventListener(new ValueEventListener() {
             DataPacket dataPacket = new DataPacket();
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 String time = dataSnapshot.child("Users").child(ID).child("Datapackets").child(title).child("time").getValue().toString();
                 String message = dataSnapshot.child("Users").child(ID).child("Datapackets").child(title).child("dataText").getValue().toString();

                 if (dataSnapshot.child("Users").child(ID).child("Datapackets").child(title).getChildrenCount() ==4) {
                     String filename = dataSnapshot.child("Users").child(ID).child("Datapackets").child(title).child("File").child("name").getValue().toString();
                     String type = dataSnapshot.child("Users").child(ID).child("Datapackets").child(title).child("File").child("type").getValue().toString();
                     url = dataSnapshot.child("Users").child(ID).child("Datapackets").child(title).child("File").child("url").getValue().toString();
                     DataFile.setText(filename+"."+type);
                 }
                 else{
                     DataFile.setText("The patient did not upload any file in this datapacket.");
                     DownloadBtn.setEnabled(false);

                 }
                 DataTitle.setText(title);
                 DataTime.setText(time);
                 DataMessage.setText(message);


             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

         DownloadBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Uri uri = Uri.parse(url);
                 Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                 startActivity(intent);

             }
         });

    }
}
