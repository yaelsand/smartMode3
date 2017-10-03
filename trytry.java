package yael.smartmode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class trytry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trytry);
        TextView trytry = (TextView)findViewById(R.id.trytry);
        Intent intent = getIntent();
        Mode mode = (Mode)intent.getSerializableExtra("Mode");
        trytry.setText(mode.getName());
    }
}
