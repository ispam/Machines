package tech.destinum.machines;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MachineCreation extends AppCompatActivity {

    private EditText etName;
    private EditText etLocation;
    private Button mButton;
    private DBHelpter mDBHelpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_creation);

        mDBHelpter = new DBHelpter(this);
        etName = (EditText) findViewById(R.id.Nombre);
        etLocation = (EditText) findViewById(R.id.Ubicacion);
        mButton = (Button) findViewById(R.id.button);

        final String name = String.valueOf(etName.getText());
        final String location = String.valueOf(etLocation.getText());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBHelpter.insertNewMachine(name, location);
                Intent intent = new Intent(MachineCreation.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
