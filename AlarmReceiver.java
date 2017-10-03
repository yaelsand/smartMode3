package yael.smartmode;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yael.smartmode.CursorAdapter;

import static android.icu.lang.UCharacter.JoiningGroup.PE;

/**
 * Created by Lea on 29/08/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    Mode mode;
    boolean alreadyMode = false;
    DBHelper dbHelper;
    boolean uniqeCaller = false;
    String number;

    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper = new DBHelper(context);
        dbHelper.createDB();
        mode = dbHelper.getActivatedMode();

        MainActivity.isEnd = false;
//        Mode mode = (Mode) intent.getSerializableExtra("mode");
        AudioManager am;
        am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(mode.getRing());
        CursorAdapter.ScreenBrightness(mode.getScreenLight(), context);


        if(!MainActivity.isEnd){
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            // check if there is specialGroup in DB, check if caller id in one of the group
            List<SpecialGroup> specialGroups = dbHelper.getSpecialGroupsForMode(mode);
            for(SpecialGroup sp : specialGroups) {
                for(Long id : getContactIDsLookupList(number, context)) {
                    if (sp.getContactsIds().contains(id)) {
                        // send sms by the parameters of sp

                        uniqeCaller = true;
                        if (sp.getAutoMsg() != "") {
                            sendSMS(number, sp.getAutoMsg(), context);
                        }
                        else if (mode.getAutoMsg() != "") {
                            sendSMS(number, mode.getAutoMsg(), context);
                        }
                        // change ring by the parameters of sp
                        if (sp.getRing()) {
                            AudioManager audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                            audiomanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                        else {
                            AudioManager audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                            audiomanager.setRingerMode(mode.getRing());
                        }
                    }
                }

            }
            if (!uniqeCaller) {
                //if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
                if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                    // Mode mode = (Mode) intent.getSerializableExtra("mode1");
                    if (mode.getAutoMsg() != "") {
                        sendSMS(number, mode.getAutoMsg(), context);
                    }
                }
            }
        }
    }



    public void sendSMS(String phoneNo, String msg, Context context) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(context.getApplicationContext(), "Message Sent: " + msg,
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context.getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    /**
     * Gets a list of contact ids that is pointed at the passed contact number
     * parameter
     *
     * @param contactNo
     *            contact number whose contact Id is requested (no special chars)
     * @param cxt
     *            application context
     * @return String representation of a list of contact ids pointing to the
     *         contact in this format 'ID1','ID2','34','65','12','17'...
     */
    private List<Long> getContactIDsLookupList(String contactNo, Context cxt) {

        String contactNumber = Uri.encode(contactNo);
        List<Long> contactIdList = new ArrayList<>();
        if (contactNumber != null) {
            Cursor contactLookupCursor = cxt.getContentResolver().query(
                    Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                            Uri.encode(contactNumber)),
                    new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID },
                    null, null, null);
            if (contactLookupCursor != null) {
                while (contactLookupCursor.moveToNext()) {
                    int phoneContactID = contactLookupCursor.getInt(contactLookupCursor
                            .getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                    if (phoneContactID > 0) {
                        contactIdList.add(new Long(phoneContactID));
                    }
                }
            }
            contactLookupCursor.close();
        }
        return contactIdList;
    }

}

