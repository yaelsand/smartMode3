package yael.smartmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.onegravity.smartmode.contact.Contact;
import com.onegravity.smartmode.core.ContactPickerActivity;
import com.onegravity.smartmode.group.Group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends BaseActivity {

    private List<SpecialGroup> allSpecialGroups;
    private ListView specialGroupsList;

    private static final String EXTRA_GROUPS = "EXTRA_GROUPS";
    private static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";

    private static final int REQUEST_CONTACT = 0;

    private List<Contact> mContacts;
    private List<Group> mGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        setTitle("Special Groups");

        // read parameters either from the Intent or from the Bundle
        if (savedInstanceState != null) {
            mGroups = (List<Group>) savedInstanceState.getSerializable(EXTRA_GROUPS);
            mContacts = (List<Contact>) savedInstanceState.getSerializable(EXTRA_CONTACTS);
        }
        else {
            Intent intent = getIntent();
            mGroups = (List<Group>) intent.getSerializableExtra(EXTRA_GROUPS);
            mContacts = (List<Contact>) intent.getSerializableExtra(EXTRA_CONTACTS);
        }
        try {
            allSpecialGroups = MainActivity.dbHelper.getAllSpecialGroup();
            specialGroupsList = (ListView) findViewById(R.id.groupsList);
            specialGroupsList.setAdapter(new GroupAdapter(GroupsActivity.this, allSpecialGroups, null));
        }
        catch (Exception ex)
        {
            System.out.println("EXC - YYYYYYYY");
            System.out.println(ex.getMessage());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putBoolean(EXTRA_DARK_THEME, mDarkTheme);
        if (mGroups != null) {
            outState.putSerializable(EXTRA_GROUPS, (Serializable) mGroups);
        }
        if (mContacts != null) {
            outState.putSerializable(EXTRA_CONTACTS, (Serializable) mContacts);
        }
    }

    private List<Long> getContactsIDs(List<Contact> contacts)
    {
        List<Long> ids = new ArrayList<>();
        for (Contact c : contacts)
        {
            ids.add(c.getId());
        }
        return ids;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK && data != null &&
                (data.hasExtra(ContactPickerActivity.RESULT_GROUP_DATA) ||
                        data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA))) {
            // we got a result from the contact picker --> show the picked contacts
            mGroups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
            mContacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
//            populateContactList(mGroups, mContacts);
            int pos = data.getIntExtra(ContactPickerActivity.CURRENT_SPECIAL_GROUP_ID, -1);
            if (pos != -1){
                allSpecialGroups.get(pos).setContactsIds(getContactsIDs(mContacts));
            }
        }
    }

//    private void populateContactList(List<Group> groups, List<Contact> contacts) {
//        // we got a result from the contact picker --> show the picked contacts
//        TextView contactsView = (TextView) findViewById(R.id.contacts);
//        SpannableStringBuilder result = new SpannableStringBuilder();
//
//        try {
//            if (groups != null && ! groups.isEmpty()) {
//                result.append("GROUPS\n");
//                for (Group group : groups) {
//                    populateContact(result, group, "");
//                    for (Contact contact : group.getContactsIds()) {
//                        populateContact(result, contact, "    ");
//                    }
//                }
//            }
//            if (contacts != null && ! contacts.isEmpty()) {
//                result.append("CONTACTS\n");
//                for (Contact contact : contacts) {
//                    populateContact(result, contact, "");
//                }
//            }
//        }
//        catch (Exception e) {
//            result.append(e.getMessage());
//        }
//
//        contactsView.setText(result);
//    }
//
//    private void populateContact(SpannableStringBuilder result, ContactElement element, String prefix) {
//        //int start = result.length();
//        String displayName = element.getDisplayName();
//        result.append(prefix);
//        result.append(displayName + "\n");
//        //result.setSpan(new BulletSpan(15), start, result.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.contact_picker_demo, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        //int textId = mDarkTheme ? R.string.light_theme : R.string.dark_theme;
        //menu.findItem(R.id.action_theme).setTitle(textId);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            //mDarkTheme = ! mDarkTheme;
            Intent intent = new Intent(this, this.getClass());
                    //.putExtra(EXTRA_DARK_THEME, mDarkTheme);
            if (mGroups != null) {
                intent.putExtra(EXTRA_GROUPS, (Serializable) mGroups);
            }
            if (mContacts != null) {
                intent.putExtra(EXTRA_CONTACTS, (Serializable) mContacts);
            }
            startActivity(intent);
            finish();
            return true;
        }

        return false;
    }

    /**
     * add new default special group, with new line
     * @param view
     */
    public void onAddGroup(View view) {
        SpecialGroup specialGroup = new SpecialGroup();
        allSpecialGroups.add(specialGroup);
        specialGroupsList.setAdapter(new GroupAdapter(GroupsActivity.this, allSpecialGroups, null));
    }


    public void saveGroups (View view) {
        // update the groups in the DB
        MainActivity.dbHelper.updateAllSpecialGroup(allSpecialGroups);
        finish();
    }

    public void cancelGroups (View view) {
        finish();
    }

}
