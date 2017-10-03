package yael.smartmode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yael on 15/08/2017.
 */

public class SpecialGroup implements Serializable {
    private int id;
    private String name;
    private List<Long> contactsIds;
    private Boolean isRing;
    private Boolean isMsg;
    private String autoMsg;
    private Boolean isAutoMsgSendInCall;
    private Boolean isAutoMsgSendInMsg;

    public SpecialGroup(int id, String name, List<Long> contactsIds) {
        this.id = id;
        this.name = name;
        this.contactsIds = contactsIds;
        isRing = false;
        isMsg = false;
        autoMsg = "";
        isAutoMsgSendInCall = false;
        isAutoMsgSendInMsg = false;
    }

//    public SpecialGroup(int id, String name, List<Contact> contactsIds) {
//        this.id = id;
//        this.name = name;
//        this.contactsIds.addAll(contactsIds);
//        isRing = false;
//        isMsg = false;
//        autoMsg = "";
//        isAutoMsgSendInCall = false;
//        isAutoMsgSendInMsg = false;
//    }

    public SpecialGroup() {
        id = -1;
        name = "";
        contactsIds = new ArrayList<>();
        isRing = false;
        isMsg = false;
        autoMsg = "";
        isAutoMsgSendInCall = false;
        isAutoMsgSendInMsg = false;
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

    public List<Long> getContactsIds() {
        return contactsIds;
    }

    public void setContactsIds(List<Long> contactsIds) {
        this.contactsIds = contactsIds;
    }

    public Boolean getRing() {
        return isRing;
    }

    public void setRing(Boolean ring) {
        isRing = ring;
    }

    public Boolean getMsg() {
        return isMsg;
    }

    public void setMsg(Boolean msg) {
        isMsg = msg;
    }

    public String getAutoMsg() {
        return autoMsg;
    }

    public void setAutoMsg(String autoMsg) {
        this.autoMsg = autoMsg;
    }

    public Boolean getAutoMsgSendInCall() {
        return isAutoMsgSendInCall;
    }

    public void setAutoMsgSendInCall(Boolean autoMsgSendInCall) {
        isAutoMsgSendInCall = autoMsgSendInCall;
    }

    public Boolean getAutoMsgSendInMsg() {
        return isAutoMsgSendInMsg;
    }

    public void setAutoMsgSendInMsg(Boolean autoMsgSendInMsg) {
        isAutoMsgSendInMsg = autoMsgSendInMsg;
    }

}
