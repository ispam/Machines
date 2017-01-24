package tech.destinum.machines;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MachineInfo extends AppCompatActivity {

    private TextView mLocation, mMoney, mNotes;
    private DBHelpter mDBHelpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        mDBHelpter = new DBHelpter(getApplicationContext());

        mLocation = (TextView) findViewById(R.id.tvLocation);
        mMoney = (TextView) findViewById(R.id.tvMoney);
        mNotes = (TextView) findViewById(R.id.tvNotes);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        mLocation.setText(location);

    }
}
