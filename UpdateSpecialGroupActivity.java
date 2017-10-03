package yael.smartmode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import static yael.smartmode.R.string.allowCall;

public class UpdateSpecialGroupActivity extends AppCompatActivity {

    Mode currMode;
    SpecialGroup currSpecialGroup;
    SpecialGroup currSpecialGroupMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_special_group);

        // get extra
        currMode = (Mode) getIntent().getSerializableExtra("Mode");
        currSpecialGroup = (SpecialGroup) getIntent().getSerializableExtra("SpecialGroup");
        if (currMode.getName() != null && currSpecialGroup.getName() != null)
            setTitle("Group: " + currMode.getName() + " For Mode: " + currSpecialGroup.getName());

        // get specialGroupMode if not exist return new SpecialGroup()
        currSpecialGroupMode = MainActivity.dbHelper.getSpecialGroupModeByIds(currMode, currSpecialGroup);

        ToggleButton allowCall =(ToggleButton) findViewById(R.id.btnSGAllowCall);
        if(currSpecialGroupMode.getRing())
            allowCall.setChecked(true);
        else
            allowCall.setChecked(false);
        allowCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currSpecialGroupMode.getRing())
                    currSpecialGroupMode.setRing(false);
                else
                    currSpecialGroupMode.setRing(true);
            }
        });

//        ToggleButton allowMsg =(ToggleButton) findViewById(R.id.btnSGAllowMsg);
//        if(currSpecialGroupMode.getMsg())
//            allowMsg.setChecked(true);
//        else
//            allowMsg.setChecked(false);
//        allowMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currSpecialGroupMode.getMsg())
//                    currSpecialGroupMode.setMsg(false);
//                else
//                    currSpecialGroupMode.setMsg(true);
//            }
//        });

        EditText replaceAutoMsg =(EditText) findViewById(R.id.editSGAutoMsg);
        replaceAutoMsg.setText(currSpecialGroupMode.getAutoMsg());
        replaceAutoMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currSpecialGroupMode.setAutoMsg(s.toString());
            }
        });

//        ToggleButton autoMsgInCall =(ToggleButton) findViewById(R.id.btnSGAutoMsgIncomingCall);
//        if(currSpecialGroupMode.getAutoMsgSendInCall())
//            autoMsgInCall.setChecked(true);
//        else
//            autoMsgInCall.setChecked(false);
//        autoMsgInCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currSpecialGroupMode.getAutoMsgSendInCall())
//                    currSpecialGroupMode.setAutoMsgSendInCall(false);
//                else
//                    currSpecialGroupMode.setAutoMsgSendInCall(true);
//            }
//        });

//        ToggleButton autoMsgInMsg =(ToggleButton) findViewById(R.id.btnSGAutoMsgIncomingMsg);
//        if(currSpecialGroupMode.getAutoMsgSendInMsg())
//            autoMsgInMsg.setChecked(true);
//        else
//            autoMsgInMsg.setChecked(false);
//        autoMsgInMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currSpecialGroupMode.getAutoMsgSendInMsg())
//                    currSpecialGroupMode.setAutoMsgSendInMsg(false);
//                else
//                    currSpecialGroupMode.setAutoMsgSendInMsg(true);
//            }
//        });
    }

    public void saveMode(View view) {
        MainActivity.dbHelper.updateModeSpecialGroup(currMode, currSpecialGroup, currSpecialGroupMode);
        finish();
    }

    public void cancelMode(View view) {
        finish();
    }
}