package tech.destinum.machines;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MachineCreation extends AppCompatActivity {

    private EditText etName, etLocation;
    private Button mButton;
    private DBHelpter mDBHelpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_creation);

        mDBHelpter = new DBHelpter(this);
        etName = (EditText) findViewById(R.id.etName);
        etLocation = (EditText) findViewById(R.id.etLocation);
        mButton = (Button) findViewById(R.id.button);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(etName.getText());
                String location = String.valueOf(etLocation.getText());
                mDBHelpter.insertNewMachine(name, location);

                Intent intent = new Intent(MachineCreation.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
