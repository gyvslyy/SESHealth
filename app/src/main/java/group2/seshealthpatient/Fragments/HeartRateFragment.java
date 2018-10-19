package group2.seshealthpatient.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.OnClick;
import group2.seshealthpatient.Activities.HeartbeatMainActivity;
import group2.seshealthpatient.ChooseFileDialog;
import group2.seshealthpatient.HeartbeatView;
import group2.seshealthpatient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeartRateFragment extends Fragment {

    @BindView(R.id.MeasurehbBtn)
    Button MeasureBtn;



    public HeartRateFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Heart Rate");
    }

    @OnClick(R.id.MeasurehbBtn)
    public void Heartbeat(){
        Intent intent = new Intent(getContext(), HeartbeatMainActivity.class);
        startActivity(intent);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heart_rate, container, false);

    }



}
