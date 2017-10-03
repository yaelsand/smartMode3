package yael.smartmode;


import android.media.AudioManager;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class Mode implements Serializable {
    private int id;
    private String name;
    private String symbol;
    private Boolean isActivate;
    private Boolean isRepeat; //not Timer
    private String[] repeatDays;
    private String repeatStart;
    private String repeatEnd;
    private String timer;
    private int ring;
    private RingEnum notification;
    private RingEnum messages;
    private int screenLight;
    private Boolean alarm;
    private String autoMsg;
    private Boolean autoMsgIncomingCall;
    private Boolean autoMsgIncomingMsg;
    private int counterAutoMsg;
    private List<SpecialGroup> SpecialGroups;


    public Mode(){
        id = -1;
        this.name = "";
        symbol = "defaultSymbol";
        isActivate = false;
        isRepeat = true; //not Timer
        repeatDays = new String[]{};
        repeatStart = "12:00";
        repeatEnd = "15:00";
        timer = "1:30";
        ring = AudioManager.RINGER_MODE_NORMAL;;
        notification = RingEnum.VIBRATION;
        messages = RingEnum.VIBRATION;
        screenLight = 80;
        alarm = true;
        autoMsg = "";
        autoMsgIncomingCall = true;
        autoMsgIncomingMsg = true;
        counterAutoMsg = 5;
        SpecialGroups = new ArrayList<>();
    }

    public Mode(String name, int id){
        this.id = id;
        this.name = name;
        symbol = "defaultSymbol";
        isActivate = false;
        isRepeat = true; //not Timer
        repeatDays = new String[]{};
        repeatStart = "12:00";
        repeatEnd = "15:00";
        timer = "1:30";
        ring = AudioManager.RINGER_MODE_NORMAL;;
        notification = RingEnum.VIBRATION;
        messages = RingEnum.VIBRATION;
        screenLight = 80;
        alarm = true;
        autoMsg = "";
        autoMsgIncomingCall = true;
        autoMsgIncomingMsg = true;
        counterAutoMsg = 5;
        SpecialGroups = new ArrayList<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean getActivate() {
        return isActivate;
    }

    public void setActivate(Boolean activate) {
        isActivate = activate;
    }

    public Boolean getRepeat() {
        return isRepeat;
    }

    public void setRepeat(Boolean repeat) {
        isRepeat = repeat;
    }

    public String[] getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String[] repeatDays) {
        this.repeatDays = repeatDays;
    }

    public String getRepeatStart() {
        return repeatStart;
    }

    public void setRepeatStart(String repeatStart) {
        this.repeatStart = repeatStart;
    }

    public String getRepeatEnd() {
        return repeatEnd;
    }

    public void setRepeatEnd(String repeatEnd) {
        this.repeatEnd = repeatEnd;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public int getRing() {
        return ring;
    }

    public void setRing(int ring) {
        this.ring = ring;
    }

    public RingEnum getNotification() {
        return notification;
    }

    public void setNotification(RingEnum notification) {
        this.notification = notification;
    }

    public RingEnum getMessages() {
        return messages;
    }

    public void setMessages(RingEnum messages) {
        this.messages = messages;
    }

    public int getScreenLight() {
        return screenLight;
    }

    public void setScreenLight(int screenLight) {
        this.screenLight = screenLight;
    }

    public Boolean getAlarm() {
        return alarm;
    }

    public void setAlarm(Boolean alarm) {
        this.alarm = alarm;
    }

    public String getAutoMsg() {
        return autoMsg;
    }

    public void setAutoMsg(String autoMsg) {
        this.autoMsg = autoMsg;
    }

    public Boolean getAutoMsgIncomingCall() {
        return autoMsgIncomingCall;
    }

    public void setAutoMsgIncomingCall(Boolean autoMsgIncomingCall) {
        this.autoMsgIncomingCall = autoMsgIncomingCall;
    }

    public Boolean getAutoMsgIncomingMsg() {
        return autoMsgIncomingMsg;
    }

    public void setAutoMsgIncomingMsg(Boolean autoMsgIncomingMsg) {
        this.autoMsgIncomingMsg = autoMsgIncomingMsg;
    }

    public int getCounterAutoMsg() {
        return counterAutoMsg;
    }

    public void setCounterAutoMsg(int counterAutoMsg) {
        this.counterAutoMsg = counterAutoMsg;
    }

    public List<SpecialGroup> getSpecialGroups() {
        return SpecialGroups;
    }

    public void setSpecialGroups(List<SpecialGroup> specialGroups) {
        SpecialGroups = specialGroups;
    }
}
