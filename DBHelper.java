package yael.smartmode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.mode;
import static android.os.Build.ID;
import static yael.smartmode.R.id.modeName;

/**
 * Created by Yael on 15/08/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private Mode modeActivated ;
    public static final String DB_NAME = "smartModeDB";
    public static final int DB_VERSION = 16;
    public static final String MODE_TABLE = "mode";
    public static final String SPECIAL_GROUP_TABLE = "specialGroup";
    public static final String SPECIAL_MODE_TABLE = "specialMode";



    public static final String COLUMN_ID = "_id";
    public static final String COLUMN = "column";
    public static final String NAME = "name";
    public static final String ISACTIVATE = "is_activate";
    public static final String ISREPEAT = "is_repeat";
    public static final String REPEATDAYS = "repeat_days";
    public static final String REPEATSTART = "repeat_start";
    public static final String REPEATEND = "repeat_end";
    public static final String TIMER = "timer";
    public static final String RING = "ring";
    public static final String NOTIFICATION = "notification";
    public static final String MESSAGE = "message";
    public static final String SCREENLIGHT = "screenlight";
    public static final String ALARM = "alarm";
    public static final String AUTOMSG = "auto_msg";
    public static final String SPECIAL_GROUP = "special_group";
    public static final String ID_MODE = "id_mode";
    public static final String ID_SPECIAL = "id_special";

    public static final String CONTACT = "contact";


    //index of MODE_TABLE
    public static final int COLUMN_ID_INDEX =    0;
    public static final int NAME_INDEX =         1;
    public static final int ISACTIVATE_INDEX =   2;
    public static final int ISREPEAT_INDEX =     3;
    public static final int REPEATDAYS_INDEX =   4;
    public static final int REPEATSTART_INDEX =  5;
    public static final int REPEATEND_INDEX =    6;
    public static final int TIMER_INDEX =        7;
    public static final int RING_INDEX =         8;
    public static final int NOTIFICATION_INDEX = 9;
    public static final int MESSAGE_INDEX =      10;
    public static final int SCREENLIGHT_INDEX =  11;
    public static final int ALARM_INDEX =        12;
    public static final int AUTOMSG_INDEX =      13;
    public static final int SPECIALGROUP_INDEX = 14;


    public static final String ISRING = "is_ring";
    public static final String ISMSG = "is_msg";

    //index of SPECIAL_MODE_TABLE
    public static final int CONTACT_INDEX = 2;

    //index of SPECIAL_MODE_TABLE
    public static final int ID_MODE_INDEX = 1;
    public static final int ID_SPECIAL_INDEX = 2;
    public static final int IS_RING_INDEX = 3;
    public static final int IS_MSG_INDEX = 4;
    public static final int AUTOMSG_SP_INDEX = 5;



    public static final String CREATE_MODE =
            "CREATE TABLE IF NOT EXISTS " + MODE_TABLE +
                    "(" + COLUMN_ID + " integer primary key autoincrement, " + NAME + " text, "
                    + ISACTIVATE + " integer, " + ISREPEAT + " integer, " + REPEATDAYS + " text," +
                    REPEATSTART + " text," + REPEATEND + " text," + TIMER + " text," + RING +
                    " integer,"+ NOTIFICATION + " integer," +  MESSAGE + " integer," + SCREENLIGHT +
                    " integer," + ALARM + " integer," + AUTOMSG  + " text," + SPECIAL_GROUP + " integer" +
                    ");";



    public static final String CREATE_SPECIAL_GROUP =
            "CREATE TABLE IF NOT EXISTS " + SPECIAL_GROUP_TABLE +
                    "(" + COLUMN_ID + " integer primary key autoincrement, " +
                    NAME + " text, " + CONTACT + " text" +
                    ");";

    public static final String CREATE_SPECIAL_MODE =
            "CREATE TABLE IF NOT EXISTS " + SPECIAL_MODE_TABLE +
                    "(" + COLUMN_ID + " integer primary key autoincrement, " +
                    ID_MODE + " integer, " + ID_SPECIAL + " integer," + ISRING + " integer," + ISMSG + " integer,"+ AUTOMSG + " text" +
                    ");";


    public static final String SQL_DELETE_ENTRY= "DROP TABLE if exists " + MODE_TABLE + ";";
    public static final String SQL_DELETE_ENTRY_SP= "DROP TABLE if exists " + SPECIAL_GROUP_TABLE + ";";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRY);
        db.execSQL(SQL_DELETE_ENTRY_SP);

        db.execSQL(CREATE_MODE);
        db.execSQL(CREATE_SPECIAL_GROUP);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MODE);
        db.execSQL(CREATE_SPECIAL_GROUP);
        db.execSQL(CREATE_SPECIAL_MODE);
    }


    public void createDB() {
        if (db == null || !db.isOpen())
            db = getWritableDatabase();
        //db.execSQL(SQL_DELETE_ENTRY_SP);
        //db.execSQL(SQL_DELETE_ENTRY);
        db.execSQL(CREATE_MODE);
        db.execSQL(CREATE_SPECIAL_GROUP);


    }
    public Cursor getDBInformationSP() {
        SQLiteDatabase SQ = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, NAME, CONTACT};
        Cursor cursor = SQ.query(SPECIAL_GROUP_TABLE, columns, null, null, null, null, null);
        return cursor;
    }
    public List<SpecialGroup> getAllSpecialGroup(){
        try {
            Cursor cursor = getDBInformationSP();
            List<SpecialGroup> specialGroup = new ArrayList<SpecialGroup>();
            // ArrayAdapter<SpecialGroup> list_adapter;
            //list_adapter = new ArrayAdapter(this, specialGroup );
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                List<Long> listOfNumbers = convertStringToListOfLong(cursor.getString(cursor.getColumnIndexOrThrow(CONTACT)));
                specialGroup.add(new SpecialGroup(id, name, listOfNumbers));
                //  list_adapter.notifyDataSetChanged();
            }
            return specialGroup;
        }
        catch(Exception ex)
        {
            System.out.println("EXC");
            System.out.println(ex.getMessage());
        }
        return new ArrayList<>();
    }

    public Cursor getDBInformation(){
        SQLiteDatabase SQ = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, NAME, ISACTIVATE, ISREPEAT, REPEATDAYS, REPEATSTART, REPEATEND, TIMER,
                RING, NOTIFICATION, MESSAGE, SCREENLIGHT, ALARM, AUTOMSG, SPECIAL_GROUP};
        Cursor cursor = SQ.query(MODE_TABLE, columns, null, null, null, null, null);
        return cursor;
    }
    public String getNameByPosition (int position) {
        Cursor cursor = this.getDBInformation();
        cursor.moveToPosition(position);
        return cursor.getString(NAME_INDEX);
    }


    public void addNewTask(String name, int is_activate, int is_repeat, String[] repeatdays, String repeat_start,
                           String repeat_end, String timer, int ring, int screenlight, String automsg, int special_group) {


        Cursor cursor = getRowByName(name);
        if(cursor.getCount() == 0) {
            SQLiteDatabase SQ = this.getWritableDatabase();

            ContentValues new_task = new ContentValues();
            new_task.put(NAME, name);
            new_task.put(ISACTIVATE, is_activate);
            new_task.put(ISREPEAT, is_repeat);
            new_task.put(REPEATDAYS, convertArrayToString(repeatdays));
            new_task.put(REPEATSTART, repeat_start);
            new_task.put(REPEATEND, repeat_end);
            new_task.put(TIMER, timer);
            new_task.put(RING, ring);
            new_task.put(SCREENLIGHT, screenlight);
            new_task.put(AUTOMSG,automsg);
            SQ.insert(MODE_TABLE, null, new_task);
        }
    }

    public void addNewMode(Mode mode) {

        int counter = 0;
        String name = mode.getName();
        while (getRowByName(name).getCount() != 0){
            counter++;
            name = mode.getName() + Integer.toString(counter);
        }
        mode.setName(name);
        int activated;
        int repeat;

        if (mode.getActivate()){
            activated = 1;
        }
        else {
            activated = 0;
        }
        if (mode.getRepeat()){
            repeat = 1;
        }
        else {
            repeat = 0;
        }
        addNewTask(mode.getName(),activated,repeat,mode.getRepeatDays(),mode.getRepeatStart(),mode.getRepeatEnd(),mode.getTimer(),mode.getRing(), mode.getScreenLight(),mode.getAutoMsg(),mode.getScreenLight());
    }

    public Cursor getRowByName(String name){
        Cursor cursor = null;
        try {
            String[] tableColumns = new String[] {COLUMN_ID,
                    NAME, ISACTIVATE, ISREPEAT, REPEATDAYS, REPEATSTART, REPEATEND, TIMER,
                    RING, NOTIFICATION, MESSAGE, SCREENLIGHT, ALARM, AUTOMSG
            };
            String whereClause =  NAME + " = ?";
            String[] whereArgs = new String[] {
                    name
            };
            cursor = db.query("mode", tableColumns, whereClause, whereArgs,
                    null, null, null);
        }
        finally {
        }
        return cursor;
    }

    public Mode getModeByName(String name){
        Mode mode = new Mode();
        Cursor cursor = getRowByName(name);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            mode.setId(cursor.getInt(COLUMN_ID_INDEX ));
            mode.setName(cursor.getString(NAME_INDEX ));
            mode.setActivate(cursor.getInt(ISACTIVATE_INDEX ) != 0);
            mode.setRepeat(cursor.getInt(ISREPEAT_INDEX ) != 0);
            mode.setRepeatDays(convertStringToArray(cursor.getString(REPEATDAYS_INDEX)));
            mode.setRepeatStart(cursor.getString(REPEATSTART_INDEX ));
            mode.setRepeatEnd(cursor.getString(REPEATEND_INDEX ));
            mode.setTimer(cursor.getString(TIMER_INDEX ));
            mode.setRing(cursor.getInt(RING_INDEX));
            mode.setScreenLight(cursor.getInt(SCREENLIGHT_INDEX ));
            mode.setAlarm(cursor.getInt(ALARM_INDEX ) != 0);
            mode.setAutoMsg(cursor.getString(AUTOMSG_INDEX ));
        }
        return mode;
    }
    public static String strSeparator = ",";
    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(",");
        return arr;
    }

    public static List<Long> convertStringToListOfLong(String str){
        String[] arr = str.split(",");
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            try {
                result.add(Long.parseLong(arr[i]));
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            };
        }

        return result;
    }
    public static String convertListLongToString(List<Long> array){
        String str = "";
        for (int i = 0; i<array.size(); i++) {
            str = str+ Long.toString(array.get(i));
            // Do not append comma at the end of last element
            if(i<array.size()-1){
                str = str+strSeparator;
            }
        }
        return str;
    }


    public void update(String name, ContentValues args, String strFilter, String[] whereargs) {
        db.update(name, args, strFilter, whereargs);
    }

    public void updateMode(Mode mode) {
        if(mode.getId() != -1){
            String strFilter = "_id=" + mode.getId() ;
            ContentValues args = new ContentValues();
            args.put(NAME, mode.getName());
            args.put(ISACTIVATE, mode.getActivate());
            args.put(ISREPEAT, mode.getRepeat());
            args.put(REPEATDAYS, convertArrayToString(mode.getRepeatDays()));
            args.put(REPEATSTART, mode.getRepeatStart());
            args.put(REPEATEND, mode.getRepeatEnd());
            args.put(TIMER, mode.getTimer());
            args.put(RING, mode.getRing());
            args.put(NOTIFICATION, mode.getNotification().toString());
            args.put(MESSAGE, mode.getMessages().toString());
            args.put(SCREENLIGHT, mode.getScreenLight());
            args.put(ALARM, mode.getAlarm());
            args.put(AUTOMSG, mode.getAutoMsg());
            db.update(MODE_TABLE, args, strFilter, null);

        }
        else {
            addNewMode(mode);
        }

    }
    public Mode getActivatedMode() {
        Cursor cursor = null;
        modeActivated = new Mode();
        try {
            String[] tableColumns = new String[] {COLUMN_ID,
                    NAME, ISACTIVATE, ISREPEAT, REPEATDAYS, REPEATSTART, REPEATEND, TIMER,
                    RING, NOTIFICATION, MESSAGE, SCREENLIGHT, ALARM, AUTOMSG
            };
            String whereClause =  ISACTIVATE + " = ?";
            String[] whereArgs = new String[] {
                    "1"
            };
            cursor = db.query("mode", tableColumns, whereClause, whereArgs,
                    null, null, null);

//            cursor = db.rawQuery("SELECT * FROM mode WHERE is_activate =  ?", new String[] {1 + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                modeActivated.setId(cursor.getInt(COLUMN_ID_INDEX ));
                modeActivated.setName(cursor.getString(NAME_INDEX ));
                modeActivated.setActivate(cursor.getInt(ISACTIVATE_INDEX ) != 0);
                modeActivated.setActivate(cursor.getInt(ISACTIVATE_INDEX ) != 0);
                modeActivated.setRepeat(cursor.getInt(ISREPEAT_INDEX ) != 0);
                modeActivated.setRepeatDays(convertStringToArray(cursor.getString(REPEATDAYS_INDEX)));
                modeActivated.setRepeatStart(cursor.getString(REPEATSTART_INDEX ));
                modeActivated.setRepeatEnd(cursor.getString(REPEATEND_INDEX ));
                modeActivated.setTimer(cursor.getString(TIMER_INDEX ));
                modeActivated.setRing(cursor.getInt(RING_INDEX));
                modeActivated.setScreenLight(cursor.getInt(SCREENLIGHT_INDEX ));
                modeActivated.setAlarm(cursor.getInt(ALARM_INDEX ) != 0);
                modeActivated.setAutoMsg(cursor.getString(AUTOMSG_INDEX ));

            }
            return modeActivated;
        }finally {
            cursor.close();
        }
    }
    public Mode getActivate(){
        return modeActivated;
    }

    public void addNewSpecialGroup(SpecialGroup sp){
        //TODO future add counter as in mode get row by name
        SQLiteDatabase SQ = this.getWritableDatabase();
        ContentValues new_task = new ContentValues();
        new_task.put(NAME, sp.getName());
        //String string = convertListLongToString(sp.getContactsIds());
        new_task.put(CONTACT, convertListLongToString(sp.getContactsIds()));
        SQ.insert(SPECIAL_GROUP_TABLE, null, new_task);

    }
    public void addNewTaskSpecialGroup(String name, List<Long> numbers){
        SQLiteDatabase SQ = this.getWritableDatabase();
        ContentValues new_task = new ContentValues();
        new_task.put(NAME, name);
        new_task.put(CONTACT, convertListLongToString(numbers));
        SQ.insert(SPECIAL_GROUP_TABLE, null, new_task);

    }
    public void updateAllSpecialGroup(List<SpecialGroup> sp) {
        for(SpecialGroup s : sp){
            if(s.getId()== -1){
                addNewSpecialGroup(s);
            }
            else{
                String strFilter = "_id=" + s.getId() ;
                ContentValues args = new ContentValues();
                args.put(NAME, s.getName());
                args.put(CONTACT, convertListLongToString(s.getContactsIds()));
                update(SPECIAL_GROUP_TABLE, args, strFilter, null);


            }
        }
    }

    public void updateModeSpecialGroup(Mode currMode, SpecialGroup currSpecialGroup, SpecialGroup currSpecialGroupMode) {
        // update if exist (currMode.getId() == coulmn modeId && currSpecialGroup.getId() == coulmn modeId)
        // else add new line to table SPECIAL_GROUP_MODE
        Cursor cursor = null;
        modeActivated = new Mode();
        try {
            String[] tableColumns = new String[] {
                    COLUMN_ID, ID_MODE, ID_SPECIAL, ISRING, ISMSG, AUTOMSG
            };
            String whereClause =  ID_MODE + " = ?" + "AND " + ID_SPECIAL + " = ?" ;
            String[] whereArgs = new String[] {
                    Integer.toString(currMode.getId()), Integer.toString(currSpecialGroup.getId())
            };
            cursor = db.query(SPECIAL_MODE_TABLE, tableColumns, whereClause, whereArgs,
                    null, null, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                String strFilter = "_id=" + cursor.getInt(COLUMN_ID_INDEX) ;
                ContentValues args = new ContentValues();
                args.put(ISRING, currSpecialGroupMode.getRing());
                args.put(ISMSG, currSpecialGroupMode.getMsg());
                args.put(AUTOMSG, currSpecialGroupMode.getAutoMsg());
                db.update(SPECIAL_MODE_TABLE, args, strFilter, null);

            }
            else{
                SQLiteDatabase SQ = this.getWritableDatabase();
                ContentValues new_task = new ContentValues();
                new_task.put(ID_MODE, currMode.getId());
                new_task.put(ID_SPECIAL, currSpecialGroup.getId());
                new_task.put(ISRING, currSpecialGroupMode.getRing());
                new_task.put(ISMSG, currSpecialGroupMode.getMsg());
                new_task.put(AUTOMSG, currSpecialGroupMode.getAutoMsg());
                SQ.insert(SPECIAL_MODE_TABLE, null, new_task);
            }
        }finally{
        }
    }

    public SpecialGroup getSpecialGroupModeByIds(Mode currMode, SpecialGroup currSpecialGroup) {
        // if exist create new(!) SpecialGroup with this default name and defaul id and all the parameters
        // else return new SpecialGroup()
        Cursor cursor = null;
        modeActivated = new Mode();
        try {
            String[] tableColumns = new String[] {
                    COLUMN_ID, ID_MODE, ID_SPECIAL, ISRING, ISMSG, AUTOMSG
            };
            String whereClause =  ID_MODE + " = ?" + " AND " + ID_SPECIAL + " = ?" ;
            String[] whereArgs = new String[] {
                    Integer.toString(currMode.getId()), Integer.toString(currSpecialGroup.getId())
            };
            cursor = db.query(SPECIAL_MODE_TABLE, tableColumns, whereClause, whereArgs,
                    null, null, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                SpecialGroup sp =new SpecialGroup();
                sp.setRing(cursor.getInt(IS_RING_INDEX ) != 0);
                sp.setMsg(cursor.getInt(IS_MSG_INDEX ) != 0);
                sp.setAutoMsg(cursor.getString(AUTOMSG_SP_INDEX ));
                return sp;
            }
        }finally{
        }
        return new SpecialGroup();
    }

    public void removeMode(int modeId) {
        if(modeId != -1){
            db.execSQL("DELETE FROM " + MODE_TABLE + " WHERE " + COLUMN_ID + "= '" + modeId + "'");
            db.execSQL("DELETE FROM " + SPECIAL_MODE_TABLE + " WHERE " + ID_SPECIAL + "= '" + modeId + "'");
        }
    }

    public void removeSpecialGroup(int groupId) {
        if(groupId != -1){
            db.execSQL("DELETE FROM " + SPECIAL_GROUP_TABLE + " WHERE " + COLUMN_ID + "= '" + groupId + "'");
            db.execSQL("DELETE FROM " + SPECIAL_MODE_TABLE + " WHERE " + ID_SPECIAL + "= '" + groupId + "'");
        }
    }

    public List<SpecialGroup> getSpecialGroupsForMode(Mode mode) {
        // get all the specialGroups by mode id
        Cursor cursor = null;
        try {
            String[] tableColumns = new String[] {
                    COLUMN_ID, ID_MODE, ID_SPECIAL, ISRING, ISMSG, AUTOMSG
            };
            String whereClause =  ID_MODE + " = ?" ;
            String[] whereArgs = new String[] {Integer.toString(mode.getId())};
            cursor = db.query(SPECIAL_MODE_TABLE, tableColumns, whereClause, whereArgs,
                    null, null, null);
            if(cursor.getCount() > 0) {
//                cursor.moveToFirst();
                List<SpecialGroup> spArr = new ArrayList<>();
                while (cursor.moveToNext()) {
                    SpecialGroup sp = new SpecialGroup();
                    sp.setId(cursor.getInt(ID_SPECIAL_INDEX ));
                    sp.setRing(cursor.getInt(IS_RING_INDEX ) != 0);
                    sp.setMsg(cursor.getInt(IS_MSG_INDEX ) != 0);
                    sp.setAutoMsg(cursor.getString(AUTOMSG_SP_INDEX ));
                    // get the contact and name from SPECIAL_GROUP
                    sp.setContactsIds(getContactOfSpecialGroupsById(sp.getId()));
                    spArr.add(sp);
                }
                return spArr;
            }
        }finally{
        }
        return new ArrayList<>();

    }

    private List<Long> getContactOfSpecialGroupsById(int id) {
        Cursor cursor = null;
        try {
            String[] tableColumns = new String[] {
                    COLUMN_ID, NAME, CONTACT};
            String whereClause =  COLUMN_ID + " = ?" ;
            String[] whereArgs = new String[] {Integer.toString(id)};
            cursor = db.query(SPECIAL_GROUP_TABLE, tableColumns, whereClause, whereArgs,
                    null, null, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                String name = cursor.getString(NAME_INDEX );
                List<Long> contacts = convertStringToListOfLong(cursor.getString(CONTACT_INDEX));
                return contacts;
            }
        }finally{
        }
        return new ArrayList<>();

    }
}