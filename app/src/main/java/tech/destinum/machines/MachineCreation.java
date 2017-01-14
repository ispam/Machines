package tech.destinum.machines;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MachineCreation extends AppCompatActivity {

    private EditText etName;
    private EditText etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_creation);

        etName = (EditText) findViewById(R.id.Nombre);
        etLocation = (EditText) findViewById(R.id.Ubicacion);

    }
}
