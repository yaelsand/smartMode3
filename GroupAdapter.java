package yael.smartmode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onegravity.smartmode.contact.ContactDescription;
import com.onegravity.smartmode.contact.ContactSortOrder;
import com.onegravity.smartmode.core.ContactPickerActivity;
import com.onegravity.smartmode.picture.ContactPictureType;

import java.util.List;

import static android.R.id.list;

/**
 * Created by Yael on 30/08/2017.
 */

public class GroupAdapter extends ArrayAdapter {
    private List<SpecialGroup> groupsList;
    private static LayoutInflater inflater = null;
    private Context context;
    private Mode currMode;
    private static final int REQUEST_CONTACT = 0;


    public GroupAdapter(Context context, List<SpecialGroup> list, Mode mode) {
        super(context, 0, list);
        groupsList = list;
        inflater = LayoutInflater.from(context); //(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        currMode = mode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_groups, parent, false);
            // inflate custom layout called row
            holder = new ViewHolder();
            holder.name =(EditText) convertView.findViewById(R.id.editGroupName);
            holder.chooseContacts =(Button) convertView.findViewById(R.id.btnChooseContacts);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        final int pos = position;
        final SpecialGroup currSpecialGroup = groupsList.get(position);

        holder.name.setText(currSpecialGroup.getName());
        holder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                groupsList.get(pos).setName(s.toString());
            }
        });

        holder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditText editGroupName =(EditText) v.findViewById(R.id.editGroupName);
                final String groupName = editGroupName.getText().toString();
                // open a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(groupName)
                        .setMessage("Do you want to delete the group " + groupName + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Remove the item from list and DB
                                MainActivity.dbHelper.removeSpecialGroup(groupsList.get(pos).getId());
                                groupsList.remove(pos);
                                // Refresh the adapter
                                notifyDataSetChanged();
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

        holder.chooseContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactsIdsStr = MainActivity.dbHelper.convertListLongToString(currSpecialGroup.getContactsIds());

                Intent intent = new Intent(context, ContactPickerActivity.class)
                        .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE,
                                ContactPictureType.ROUND.name())

                        .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                                ContactDescription.ADDRESS.name())
                        .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                        .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT, 0)
                        .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, false)

                        .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                                ContactsContract.CommonDataKinds.Email.TYPE_WORK)

                        .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                                ContactSortOrder.AUTOMATIC.name())

                        .putExtra(ContactPickerActivity.CURRENT_SPECIAL_GROUP_ID, pos)
                        .putExtra(ContactPickerActivity.CURRENT_SPECIAL_GROUP_CONTACT, contactsIdsStr)
                        ;

                ((Activity) context).startActivityForResult(intent, REQUEST_CONTACT);
                }
        });
        return convertView;
    }

    static class ViewHolder {
        EditText name;
        Button chooseContacts;
    }
}
