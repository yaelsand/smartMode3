package yael.smartmode;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;

import static yael.smartmode.DBHelper.COLUMN_ID_INDEX;
import static yael.smartmode.DBHelper.ISACTIVATE;
import static yael.smartmode.DBHelper.ISACTIVATE_INDEX;
import static yael.smartmode.DBHelper.MODE_TABLE;
import static yael.smartmode.DBHelper.NAME_INDEX;
import static yael.smartmode.MainActivity.list_of_mode;
import static yael.smartmode.MainActivity.dbHelper;
import static yael.smartmode.MainActivity.alreadyOneActivated;


/**
 * Created by Lea on 20/08/2017.
 */

public class CursorAdapter extends SimpleCursorAdapter {

    private AlarmManager alarmMgr;
    private AlarmManager alarmMgrEnd;
    private PendingIntent alarmnIntent;
    private PendingIntent alarmnIntentEnd;
    private PendingIntent alarmnIntentM;
    private PendingIntent alarmnIntentT;
    private PendingIntent alarmnIntentW;
    private PendingIntent alarmnIntentTh;
    private PendingIntent alarmnIntentF;
    private PendingIntent alarmnIntentS;
    private PendingIntent alarmnIntentSun;

    private PendingIntent alarmnIntentEndM;
    private PendingIntent alarmnIntentEndT;
    private PendingIntent alarmnIntentEndW;
    private PendingIntent alarmnIntentEndTh;
    private PendingIntent alarmnIntentEndF;
    private PendingIntent alarmnIntentEndS;
    private PendingIntent alarmnIntentEndSun;

    private Mode currentMode;
    private Mode mode1;

    private static final int adaptor_flag = 0;
    private Mode mode;
    public static String auto ;

    public CursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, adaptor_flag);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.row_mode, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final View v = view;
        final Context c = context;
        // Retrieving the title and date from the to-do list
        TextView name = (TextView) view.findViewById(R.id.modeName);
        name.setText(cursor.getString(NAME_INDEX));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) view.findViewById(R.id.modeName);
                String modeName = (String) name.getText();
                Intent intent = new Intent(c, UpdateModeActivity.class);
                Mode currMode = MainActivity.dbHelper.getModeByName(modeName);
                intent.putExtra("Mode", currMode);
                c.startActivity(intent);
            }
        });
        name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView txtModeName = (TextView) view.findViewById(R.id.modeName);
                final String modeName = (String) txtModeName.getText();
                // open a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(modeName)
                        .setMessage("Do you want to delete the mode " + modeName + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Remove the item from list and DB
                                Mode currMode = MainActivity.dbHelper.getModeByName(modeName);
                                MainActivity.dbHelper.removeMode(currMode.getId());
                                // TODO Refresh the adapter
                                //swapCursor(cursor_update);
                                //notifyDataSetChanged();
                                //MainActivity.updateListFromDB();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        for(int i = 0; i < list_of_mode.size(); i++){
            if (list_of_mode.get(i).getName().equals(cursor.getString(NAME_INDEX))){
                mode = list_of_mode.get(i);
            }
        }

        ToggleButton tgl =  (ToggleButton) view.findViewById(R.id.modeBtnActivation);

//        tgl.setChecked(mode.getActivate());
        String s = cursor.getString(NAME_INDEX);
//        if(cursor.getInt(ISACTIVATE_INDEX) == 1){//TODO check if button update
//            tgl.setChecked(true);
//        }
//        else{
//            tgl.setChecked(false);
//        }

        tgl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("myApp", "-------------------------- in on checked");
                TextView name = (TextView) v.findViewById(R.id.modeName);
                String modeName = (String) name.getText();
                Cursor curs = dbHelper.getRowByName(modeName);
                curs.moveToFirst();
                if (isChecked){
                    if (!alreadyOneActivated){
                        alreadyOneActivated = true;
                        mode.setActivate(true);
                        String strFilter = "_id=" + curs.getInt(COLUMN_ID_INDEX) ;
                        ContentValues args = new ContentValues();
                        args.put(ISACTIVATE, 1);
                        dbHelper.update(MODE_TABLE, args, strFilter, null);
                        Run(context);
                    }
                }
                else{
                    mode.setActivate(false);
                    String strFilter = "_id=" + curs.getInt(COLUMN_ID_INDEX) ;
                    ContentValues args = new ContentValues();
                    args.put(ISACTIVATE, 0);
                    dbHelper.update(MODE_TABLE, args, strFilter, null);
                    if(alreadyOneActivated == true)
                    {
                        if(alarmnIntent != null)
                        {
                            alarmMgr.cancel(alarmnIntent);
                        }
                        if(alarmnIntentM != null)
                        {
                            alarmMgr.cancel(alarmnIntentM);
                        }
                        if(alarmnIntentT != null)
                        {
                            alarmMgr.cancel(alarmnIntentT);
                        }
                        if(alarmnIntentW != null)
                        {
                            alarmMgr.cancel(alarmnIntentW);
                        }
                        if(alarmnIntentTh != null)
                        {
                            alarmMgr.cancel(alarmnIntentTh);
                        }
                        if(alarmnIntentF != null)
                        {
                            alarmMgr.cancel(alarmnIntentF);
                        }
                        if(alarmnIntentS != null)
                        {
                            alarmMgr.cancel(alarmnIntentS);
                        }
                        if(alarmnIntentSun != null)
                        {
                            alarmMgr.cancel(alarmnIntentSun);
                        }
                        if(alarmnIntent != null)
                        {
                            alarmMgr.cancel(alarmnIntent);
                        }
                        if(alarmnIntentM != null)
                        {
                            alarmMgr.cancel(alarmnIntentM);
                        }
                        if(alarmnIntentT != null)
                        {
                            alarmMgr.cancel(alarmnIntentT);
                        }
                        if(alarmnIntentW != null)
                        {
                            alarmMgr.cancel(alarmnIntentW);
                        }
                        if(alarmnIntentTh != null)
                        {
                            alarmMgr.cancel(alarmnIntentTh);
                        }
                        if(alarmnIntentF != null)
                        {
                            alarmMgr.cancel(alarmnIntentF);
                        }
                        if(alarmnIntentS != null)
                        {
                            alarmMgr.cancel(alarmnIntentS);
                        }
                        if(alarmnIntentSun != null)
                        {
                            alarmMgr.cancel(alarmnIntentSun);
                        }
                        if(alarmnIntentEnd != null)
                        {
                            alarmMgrEnd.cancel(alarmnIntentEnd);
                        }
                        if(alarmnIntentEndM != null)
                        {
                            alarmMgrEnd.cancel(alarmnIntentEndM);
                        }
                        if(alarmnIntentEndT != null)
                        {
                            alarmMgrEnd.cancel(alarmnIntentEndT);
                        }
                        if(alarmnIntentEndW != null)
                        {
                            alarmMgrEnd.cancel(alarmnIntentEndW);
                        }
                        if(alarmnIntentEndTh != null)
                        {
                            alarmMgrEnd.cancel(alarmnIntentEndTh);
                        }
                        if(alarmnIntentEndS != null)
                        {
                            alarmMgrEnd.cancel(alarmnIntentEndS);
                        }
                        if(alarmnIntentEndSun != null)
                        {
                            alarmMgrEnd.cancel(alarmnIntentEndSun);
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        alarmMgrEnd.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentEnd);
                        alreadyOneActivated = false;
                    }
                }
            }
        });

    }
    public void Run(Context context){
        currentMode = new Mode();
        AudioManager am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        currentMode.setRing(am.getRingerMode());
        int curBrightnessValue = 70;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        currentMode.setScreenLight(curBrightnessValue);

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        mode = dbHelper.getActivatedMode();
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgrEnd = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if( mode != null ){

            startRunning(context, intent, currentMode);
        }
    }

    public Calendar startRunning(Context context, Intent intent, Mode currentMode){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent intentEnd = new Intent(context, AlarmEndReceiver.class);
        intentEnd.putExtra("modeEnd", currentMode);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        if(mode.getRepeat()){
            calendar.set(Calendar.HOUR_OF_DAY,getHourOfDay(mode.getRepeatStart()));
            calendar.set(Calendar.MINUTE, getMinuteOfDay(mode.getRepeatStart()));
            calendar.set(Calendar.SECOND, 00);
            cal.set(Calendar.HOUR_OF_DAY,getHourOfDay(mode.getRepeatEnd()));
            cal.set(Calendar.MINUTE, getMinuteOfDay(mode.getRepeatEnd()));
            cal.set(Calendar.SECOND, 00);
            String[] days = mode.getRepeatDays();
            boolean containsM = false;
            boolean containsT = false;
            boolean containsW = false;
            boolean containsTh = false;
            boolean containsF = false;
            boolean containsS = false;
            boolean containsSun = false;

            for (String item : days) {
                if ("m".equalsIgnoreCase(item)) {
                    containsM = true;
                }
                if ("t".equalsIgnoreCase(item)) {
                    containsT = true;
                }
                if ("w".equalsIgnoreCase(item)) {
                    containsW = true;
                }
                if ("th".equalsIgnoreCase(item)) {
                    containsTh = true;
                }
                if ("f".equalsIgnoreCase(item)) {
                    containsF = true;
                }
                if ("s".equalsIgnoreCase(item)) {
                    containsS = true;
                }
                if ("sun".equalsIgnoreCase(item)) {
                    containsSun = true;
                }

            }
            if (containsM){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                final int _id = (int) System.currentTimeMillis();
                alarmnIntentM = PendingIntent.getBroadcast(context.getApplicationContext(), _id, intent, 0);
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentM);
                final int _id2 = (int) System.currentTimeMillis();
                alarmnIntentEndM = PendingIntent.getBroadcast(context.getApplicationContext(), _id2, intentEnd, 0);
                alarmMgrEnd.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentEndM);
            }
            if (containsT){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                final int _id = (int) System.currentTimeMillis();
                alarmnIntentT = PendingIntent.getBroadcast(context.getApplicationContext(), _id, intent, 0);
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentT);
                final int _id2 = (int) System.currentTimeMillis();
                alarmnIntentEndT = PendingIntent.getBroadcast(context.getApplicationContext(), _id2, intentEnd, 0);
                alarmMgrEnd.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentEndT);
            }

            if (containsW){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                final int _id = (int) System.currentTimeMillis();
                alarmnIntentW = PendingIntent.getBroadcast(context.getApplicationContext(), _id, intent, 0);
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentW);
                final int _id2 = (int) System.currentTimeMillis();
                alarmnIntentEndW = PendingIntent.getBroadcast(context.getApplicationContext(), _id2, intentEnd, 0);
                alarmMgrEnd.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentEndW);
            }

            if (containsTh){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                final int _id = (int) System.currentTimeMillis();
                alarmnIntentTh = PendingIntent.getBroadcast(context.getApplicationContext(), _id, intent, 0);
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentTh);
                final int _id2 = (int) System.currentTimeMillis();
                alarmnIntentEndTh = PendingIntent.getBroadcast(context.getApplicationContext(), _id2, intentEnd, 0);
                alarmMgrEnd.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentEndTh);
            }

            if (containsF){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                final int _id = (int) System.currentTimeMillis();
                alarmnIntentF = PendingIntent.getBroadcast(context.getApplicationContext(), _id, intent, 0);
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentF);
                final int _id2 = (int) System.currentTimeMillis();
                alarmnIntentEndF = PendingIntent.getBroadcast(context.getApplicationContext(), _id2, intentEnd, 0);
                alarmMgrEnd.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentEndF);
            }

            if (containsS){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                final int _id = (int) System.currentTimeMillis();
                alarmnIntentS = PendingIntent.getBroadcast(context.getApplicationContext(), _id, intent, 0);
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentS);
                final int _id2 = (int) System.currentTimeMillis();
                alarmnIntentEndS = PendingIntent.getBroadcast(context.getApplicationContext(), _id2, intentEnd, 0);
                alarmMgrEnd.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentEndS);
            }
            if (containsSun){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                final int _id = (int) System.currentTimeMillis();
                alarmnIntentSun = PendingIntent.getBroadcast(context.getApplicationContext(), _id, intent, 0);
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentSun);
                final int _id2 = (int) System.currentTimeMillis();
                alarmnIntentEndSun = PendingIntent.getBroadcast(context.getApplicationContext(), _id2, intentEnd, 0);
                alarmMgrEnd.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntentEndSun);
            }
        }
        else{
            calendar = Calendar.getInstance();
            cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, getHourOfDay(mode.getTimer()));
            cal.add(Calendar.MINUTE, getMinuteOfDay(mode.getTimer()));
            final int _id = (int) System.currentTimeMillis();
            alarmnIntent = PendingIntent.getBroadcast(context.getApplicationContext(), _id, intent, 0);
            alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmnIntent);
            final int _id2 = (int) System.currentTimeMillis();
            alarmnIntentEnd = PendingIntent.getBroadcast(context.getApplicationContext(), _id2, intentEnd, 0);
            alarmMgrEnd.set(AlarmManager.RTC, cal.getTimeInMillis(), alarmnIntentEnd);

        }
        return calendar;
    }
    public static int getHourOfDay(String time){
        int index = time.indexOf(":");
        String hour = time.substring(0,index);
        return Integer.parseInt(hour);
    }
    public static int getMinuteOfDay(String time){
        int index = time.indexOf(":");
        String hour = time.substring(index+1);
        return Integer.parseInt(hour);
    }

    public static boolean ScreenBrightness(int level, Context context) {

        try {
            android.provider.Settings.System.putInt(
                    context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, level);


            android.provider.Settings.System.putInt(context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            android.provider.Settings.System.putInt(
                    context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    level);


            return true;
        }

        catch (Exception e) {
            Log.e("Screen Brightness", "error changing screen brightness");
            return false;
        }
    }
}


