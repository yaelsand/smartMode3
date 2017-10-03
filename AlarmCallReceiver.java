//package yael.smartmode;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import java.util.Calendar;
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.telephony.SmsManager;
//import android.telephony.TelephonyManager;
//import android.widget.Toast;
//
///**
// * Created by Lea on 06/09/2017.
// */
//
//public class AlarmCallReceiver extends BroadcastReceiver {
//    Mode mode;
//    boolean alreadyMode = false;
//    DBHelper dbHelper;
//    //int i = 0;
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        //      Toast.makeText(context, "Call!!!!!!!!!!", Toast.LENGTH_LONG).show();
//
//        dbHelper = new DBHelper(context);
//        dbHelper.createDB();
//        mode = dbHelper.getActivatedMode();
//
//        Toast.makeText(context,mode.getAutoMsg() ,Toast.LENGTH_SHORT).show();
//        // int counter = i+1;
//        // intent.putExtra(Integer.toString(counter), mode);
//        //  intent.putExtra("mode1", mode);
//        //  Calendar startCal = (Calendar) intent.getSerializableExtra("startCal");
//        //    Calendar calendar = (Calendar) intent.getSerializableExtra("Calendar");
//        //if(Calendar.getInstance().before(calendar) && Calendar.getInstance().after(startCal) ){
//        if(!MainActivity.isEnd){
//            //   Toast.makeText(context,"after is end " +  mode.getAutoMsg(),Toast.LENGTH_SHORT).show();
//            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//            if(TelephonyManager.EXTRA_STATE_RINGING.equals(state)){
//                Toast.makeText(context,"Ringing State Number is -" + number  ,Toast.LENGTH_SHORT).show();
//                // Mode mode = (Mode) intent.getSerializableExtra("mode1");
//                if(mode.getAutoMsg() != "" )
//                {
//                    Toast.makeText(context, "Call!!!!!!!!!!", Toast.LENGTH_LONG).show();
//
//                    sendSMS(number,mode.getAutoMsg(),context);
//                }
//            }
//        }
//
//        //   }
//
//    }
//
//    public void sendSMS(String phoneNo, String msg, Context context) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//            Toast.makeText(context.getApplicationContext(), "Message Sent",
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Toast.makeText(context.getApplicationContext(),ex.getMessage().toString(),
//                    Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
//        }
//    }
//}
