package yael.smartmode;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;

    public static DBHelper dbHelper;
    private Cursor cursor;
    public static List<Mode> list_of_mode;
    private ModeAdapter list_adapter;
    private ListView mList;
    private CursorAdapter cursorAdapter;
    Button add;
    Button addGroup;
    ToggleButton tgl;
    static boolean alreadyOneActivated;

    private AlarmManager alarmMgr;
    private AlarmManager alarmMgrEnd;
    private AlarmManager alarmCall;
    private PendingIntent alarmnIntent;
    private PendingIntent alarmnIntentEnd;
    private PendingIntent alarmnCallEnd;

    private Mode currentMode;
    private Mode mode;

    public static boolean isEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alreadyOneActivated = false;
        isEnd = true;
        dbHelper = new DBHelper(this);
        dbHelper.createDB();
        String[] rdays = new String[2];
        rdays[0] = "t";
//        Mode meet = new Mode("meeting", 3);
//        Mode school = new Mode("school", 8);
//        dbHelper.addNewMode(meet);
//        dbHelper.addNewMode(school);
//        dbHelper.addNewTask("meeting",0,1,rdays,"11:45","11:46","0:10",1,50,"automessage",2);
//        dbHelper.addNewTask("school",0,1,rdays,"17:45","17:46","0:10",1,50,"automessage2",2);
        updateListFromDB();

        mList = (ListView) findViewById(R.id.modeList);
        mList.setLongClickable(true);
        //mList.setAdapter(list_adapter);
        mList.setAdapter(cursorAdapter);

        list_adapter.notifyDataSetChanged();
        add = (Button) findViewById(R.id.btnAddMode);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, UpdateModeActivity.class);
                Mode newMode = new Mode();
                intent.putExtra("Mode", newMode);
                startActivity(intent);
            }
        });

        Button specialGroupsBtn = (Button) findViewById(R.id.btnSpecialGroups);
        specialGroupsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GroupsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateListFromDB();
//        finish();
//        startActivity(getIntent());
    }

    protected void updateListFromDB() {
        cursor = dbHelper.getDBInformation();
        String columns_name[] = new String[]{DBHelper.NAME};
        //String columns_name[] = new String[]{DBHelper.NAME, DBHelper.ISACTIVATE, DBHelper.ISREPEAT,
        //DBHelper.REPEATDAYS, DBHelper.REPEATSTART, DBHelper.REPEATEND, DBHelper.TIMER, DBHelper.RING,
        //DBHelper.SCREENLIGHT, DBHelper.AUTOMSG, DBHelper.SPECIAL_GROUP};
        int txtViews[] = new int[]{R.id.modeName};
        cursorAdapter = new CursorAdapter(this, R.layout.activity_main, cursor, columns_name, txtViews);


        list_of_mode = new ArrayList<Mode>();
        list_adapter = new ModeAdapter(this, list_of_mode );
        while(cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            list_of_mode.add(new Mode(name, id));
            list_adapter.notifyDataSetChanged();
        }

        mList = (ListView) findViewById(R.id.modeList);
        mList.setLongClickable(true);
        //mList.setAdapter(list_adapter);
        mList.setAdapter(cursorAdapter);

        list_adapter.notifyDataSetChanged();
    }

}
