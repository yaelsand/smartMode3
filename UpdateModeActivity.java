package yael.smartmode;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import layout.RepeatFragment;
import layout.TimerFragment;

import static layout.RepeatFragment.ARG_PARAM1;
import static layout.RepeatFragment.ARG_PARAM2;
import static layout.RepeatFragment.ARG_PARAM3;
import static layout.RepeatFragment.ARG_PARAM4;
import static layout.RepeatFragment.ARG_PARAM5;
import static layout.RepeatFragment.ARG_PARAM6;
import static layout.RepeatFragment.ARG_PARAM7;
import static layout.RepeatFragment.ARG_PARAM8;
import static layout.RepeatFragment.ARG_PARAM9;
import static layout.TimerFragment.ARG_PARAM3T;
import static yael.smartmode.CursorAdapter.getHourOfDay;
import static yael.smartmode.CursorAdapter.getMinuteOfDay;
import static yael.smartmode.MainActivity.dbHelper;


/**
 * Created by Yael on 20/08/2017.
 */

public class UpdateModeActivity extends AppCompatActivity {

    private Mode currMode;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment repeatFragment;
    private Fragment timerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mode);
        currMode = (Mode) getIntent().getSerializableExtra("Mode");
        setTitle("Update Mode:  " + currMode.getName());

        EditText name = (EditText) findViewById(R.id.editModeName);
        name.setText(currMode.getName());
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currMode.setName(s.toString());
            }
        });

//        EditText symbol = (EditText) findViewById(R.id.editModeSymbol);
//        symbol.setText(currMode.getSymbol());


        // update activate button by isRepeat
        int containsM = 0;
        int containsT = 0;
        int containsW = 0;
        int containsTh = 0;
        int containsF = 0;
        int containsS = 0;
        int containsSun = 0;

        for (String item : currMode.getRepeatDays()) {
            if ("m".equalsIgnoreCase(item)) {
                containsM = 1;
            }
            if ("t".equalsIgnoreCase(item)) {
                containsT = 1;
            }
            if ("w".equalsIgnoreCase(item)) {
                containsW = 1;
            }
            if ("th".equalsIgnoreCase(item)) {
                containsTh = 1;
            }
            if ("f".equalsIgnoreCase(item)) {
                containsF = 1;
            }
            if ("s".equalsIgnoreCase(item)) {
                containsS = 1;
            }
            if ("sun".equalsIgnoreCase(item)) {
                containsSun = 1;
            }
        }

        repeatFragment = new RepeatFragment().newInstance(containsSun, containsM, containsT, containsW, containsTh, containsF, containsS, getHourOfDay(currMode.getRepeatStart()), getMinuteOfDay(currMode.getRepeatStart()), getHourOfDay(currMode.getRepeatEnd()), getMinuteOfDay(currMode.getRepeatEnd()));
        timerFragment = new TimerFragment().newInstance(getHourOfDay(currMode.getTimer()), getMinuteOfDay(currMode.getTimer()));
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if (currMode.getRepeat())
            transaction.add(R.id.repeatTimerContainer, repeatFragment);
            //transaction.replace(R.id.repeatTimerContainer, repeatFragment);
        else
            transaction.add(R.id.repeatTimerContainer, timerFragment);
        //   transaction.replace(R.id.repeatTimerContainer, timerFragment);
        transaction.commit();
        final TextView repeat = (TextView) findViewById(R.id.txtRepeat);
        final TextView timer = (TextView) findViewById(R.id.txtTimer);

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction t = transaction = fragmentManager.beginTransaction();
                currMode.setRepeat(true);
                t.replace(R.id.repeatTimerContainer, repeatFragment);
                t.commit();
                repeat.setBackgroundColor(Color.parseColor("#00BFFF"));//LightBlue
                timer.setBackgroundColor(Color.parseColor("#F5F5F5"));//WhiteSmoke
            }
        });
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction t = transaction = fragmentManager.beginTransaction();
                currMode.setRepeat(false);
                t.replace(R.id.repeatTimerContainer, timerFragment);
                t.commit();
                timer.setBackgroundColor(Color.parseColor("#00BFFF"));//LightBlue
                repeat.setBackgroundColor(Color.parseColor("#F5F5F5"));//WhiteSmoke


            }
        });

        ImageButton ring;
        ImageButton viber;
        ImageButton silent;

        switch (currMode.getRing()) {
            case AudioManager.RINGER_MODE_NORMAL:
                ring = (ImageButton) findViewById(R.id.btnRingLoud);
                ring.setBackgroundResource(R.drawable.circle_on);
                viber = (ImageButton) findViewById(R.id.btnRingVibration);
                viber.setBackgroundResource(R.drawable.circle_off);
                silent = (ImageButton) findViewById(R.id.btnRingSilent);
                silent.setBackgroundResource(R.drawable.circle_off);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                ring = (ImageButton) findViewById(R.id.btnRingLoud);
                ring.setBackgroundResource(R.drawable.circle_off);
                viber = (ImageButton) findViewById(R.id.btnRingVibration);
                viber.setBackgroundResource(R.drawable.circle_on);
                silent = (ImageButton) findViewById(R.id.btnRingSilent);
                silent.setBackgroundResource(R.drawable.circle_off);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                ring = (ImageButton) findViewById(R.id.btnRingLoud);
                ring.setBackgroundResource(R.drawable.circle_off);
                viber = (ImageButton) findViewById(R.id.btnRingVibration);
                viber.setBackgroundResource(R.drawable.circle_off);
                silent = (ImageButton) findViewById(R.id.btnRingSilent);
                silent.setBackgroundResource(R.drawable.circle_on);
                break;
        }

        final SeekBar screenLight = (SeekBar) findViewById(R.id.seekBarScreenLight);
        screenLight.setMax(255);
        screenLight.setKeyProgressIncrement(1);
        screenLight.setProgress(currMode.getScreenLight());
        screenLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currMode.setScreenLight(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

//        ToggleButton alarmBtn = (ToggleButton) findViewById(R.id.btnAlarm);
//        if(currMode.getAlarm())
//            alarmBtn.setChecked(true);
//        else
//            alarmBtn.setChecked(false);

        EditText autoMsg = (EditText) findViewById(R.id.editModeAutoMsg);
        autoMsg.setText(currMode.getAutoMsg());
        autoMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currMode.setAutoMsg(s.toString());
            }
        });

        //TODO future add autoMsg in incoming message + counting before send with cancel button.
//        ToggleButton autoMsgIncomingCallBtn = (ToggleButton) findViewById(R.id.btnAutoMsgIncomingCall);
//        if(currMode.getAutoMsgIncomingCall())
//            autoMsgIncomingCallBtn.setChecked(true);
//        else
//            autoMsgIncomingCallBtn.setChecked(false);
//
//        ToggleButton autoMsgIncomingMsgBtn = (ToggleButton) findViewById(R.id.btnAutoMsgIncomingMsg);
//        if(currMode.getAutoMsgIncomingMsg())
//            autoMsgIncomingMsgBtn.setChecked(true);
//        else
//            autoMsgIncomingMsgBtn.setChecked(false);

//        NumberPicker counterAutoMsg = (NumberPicker) findViewById((R.id.numPickerAutoMsg));
//        counterAutoMsg.setValue(currMode.getCounterAutoMsg());
//        counterAutoMsg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                currMode.setCounterAutoMsg(newVal);
//            }
//        });

        TextView specialGroups = (TextView) findViewById(R.id.txtExistGroups);
        specialGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateModeActivity.this, SpecialGroupsActivity.class);
                intent.putExtra("Mode", currMode);
                startActivity(intent);
            }
        });
    }

    public void ringLoud (View view) {
        currMode.setRing(AudioManager.RINGER_MODE_NORMAL);
        ImageButton ring = (ImageButton) findViewById(R.id.btnRingLoud);
        ring.setBackgroundResource(R.drawable.circle_on);
        ImageButton viber = (ImageButton) findViewById(R.id.btnRingVibration);
        viber.setBackgroundResource(R.drawable.circle_off);
        ImageButton silent = (ImageButton) findViewById(R.id.btnRingSilent);
        silent.setBackgroundResource(R.drawable.circle_off);
    }

    public void ringVibration (View view) {
        currMode.setRing(AudioManager.RINGER_MODE_VIBRATE);
        ImageButton ring = (ImageButton) findViewById(R.id.btnRingLoud);
        ring.setBackgroundResource(R.drawable.circle_off);
        ImageButton viber = (ImageButton) findViewById(R.id.btnRingVibration);
        viber.setBackgroundResource(R.drawable.circle_on);
        ImageButton silent = (ImageButton) findViewById(R.id.btnRingSilent);
        silent.setBackgroundResource(R.drawable.circle_off);    }

    public void ringSilent (View view) {
        currMode.setRing(AudioManager.RINGER_MODE_SILENT);
        ImageButton ring = (ImageButton) findViewById(R.id.btnRingLoud);
        ring.setBackgroundResource(R.drawable.circle_off);
        ImageButton viber = (ImageButton) findViewById(R.id.btnRingVibration);
        viber.setBackgroundResource(R.drawable.circle_off);
        ImageButton silent = (ImageButton) findViewById(R.id.btnRingSilent);
        silent.setBackgroundResource(R.drawable.circle_on);    }

    //TODO future add message ring control
    public void messageLoud (View view) {
        currMode.setMessages(RingEnum.RING);
    }
    public void messageVibration (View view) {
        currMode.setMessages(RingEnum.VIBRATION);
    }
    public void messageSilent (View view) {
        currMode.setMessages(RingEnum.SILENT);
    }

    //TODO future add notification ring control
    public void notificationLoud (View view) {
        currMode.setNotification(RingEnum.RING);
    }
    public void notificationVibration (View view) {
        currMode.setNotification(RingEnum.VIBRATION);
    }
    public void notificationSilent (View view) {
        currMode.setNotification(RingEnum.SILENT);
    }

    //TODO future add alarm ring control
    public void alarm (View view) {
        if (currMode.getAlarm())
            currMode.setAlarm(false);
        else
            currMode.setAlarm(true);
    }

    public void autoMsgIncomingCall(View view) {
        if (currMode.getAutoMsgIncomingCall())
            currMode.setAutoMsgIncomingCall(false);
        else
            currMode.setAutoMsgIncomingCall(true);
    }

    public void autoMsgIncomingMsg(View view) {
        if (currMode.getAutoMsgIncomingMsg())
            currMode.setAutoMsgIncomingMsg(false);
        else
            currMode.setAutoMsgIncomingMsg(true);
    }

    public void saveMode (View view) {
        // get the currentMode
        currMode.setRepeatDays(getRepeatsDay());
        if(currMode.getRepeat()){
            if(getRepeatStart() != null)
                currMode.setRepeatStart(getRepeatStart());
            if(getRepeatEnd() != null)
                currMode.setRepeatEnd(getRepeatEnd());
        }
        else{
            if(getTimer() != null)
                currMode.setTimer(getTimer());
        }
        dbHelper.updateMode(currMode);
        finish(); // return to main activity
    }

    public String getRepeatStart(){
        return  repeatFragment.getArguments().getString(ARG_PARAM1);
    }

    public String getRepeatEnd(){
        return  repeatFragment.getArguments().getString(ARG_PARAM2);
    }

    public String getTimer(){
        return  timerFragment.getArguments().getString(ARG_PARAM3T);
    }
    public String[] getRepeatsDay(){
        int i = 0;
        int length = repeatFragment.getArguments().getInt(ARG_PARAM3) + repeatFragment.getArguments().getInt(ARG_PARAM4) + repeatFragment.getArguments().getInt(ARG_PARAM5) + repeatFragment.getArguments().getInt(ARG_PARAM6) + repeatFragment.getArguments().getInt(ARG_PARAM7) + repeatFragment.getArguments().getInt(ARG_PARAM8) + repeatFragment.getArguments().getInt(ARG_PARAM9);
        String[] repeatDays = new String[length];
        if(repeatFragment.getArguments().getInt(ARG_PARAM3) == 1){
            repeatDays[i] = "sun";
            i++;
        }
        if(repeatFragment.getArguments().getInt(ARG_PARAM4) == 1){
            repeatDays[i] = "m";
            i++;
        }
        if(repeatFragment.getArguments().getInt(ARG_PARAM5) == 1){
            repeatDays[i] = "t";
            i++;
        }
        if(repeatFragment.getArguments().getInt(ARG_PARAM6) == 1){
            repeatDays[i] = "w";
            i++;
        }
        if(repeatFragment.getArguments().getInt(ARG_PARAM7) == 1){
            repeatDays[i] = "th";
            i++;
        }
        if(repeatFragment.getArguments().getInt(ARG_PARAM8) == 1){
            repeatDays[i] = "f";
            i++;
        }
        if(repeatFragment.getArguments().getInt(ARG_PARAM9) == 1){
            repeatDays[i] = "s";
            i++;
        }

        return  repeatDays;
    }
    public void cancelMode (View view) {
        finish(); // return to main activity
    }

}

