package yael.smartmode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class SpecialGroupsActivity extends AppCompatActivity {

    Mode currMode;
    private List<SpecialGroup> allSpecialGroups;
    private ListView specialGroupsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_groups);

        currMode = (Mode) getIntent().getSerializableExtra("Mode");
        setTitle("Special Groups For Mode: " + currMode.getName());

        allSpecialGroups = MainActivity.dbHelper.getAllSpecialGroup();
        ListView specialGroupsList = (ListView) findViewById(R.id.specialGroupsList);
        specialGroupsList.setAdapter(new SpecialGroupAdapter(SpecialGroupsActivity.this, allSpecialGroups, currMode));
    }


    public void back (View view) {
        finish();
    }
}
