package yael.smartmode;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.onegravity.smartmode.core.ContactPickerActivity;
import com.onegravity.smartmode.picture.ContactPictureType;

import java.util.List;

import static com.onegravity.smartmode.core.ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE;

/**
 * Created by Yael on 27/08/2017.
 */

public class SpecialGroupAdapter extends ArrayAdapter {
    private List<SpecialGroup> specialGroupsList;
    private static LayoutInflater inflater = null;
    private Context context;
    private Mode currMode;


    public SpecialGroupAdapter(Context context, List<SpecialGroup> list, Mode currMode) {
        super(context, 0, list);
        specialGroupsList = list;
        this.context = context;
        this.currMode = currMode;
        inflater = LayoutInflater.from(context); //(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_special_group, parent, false);
            // inflate custom layout called row
            holder = new ViewHolder();
            holder.name =(TextView) convertView.findViewById(R.id.txtSpecialGroupName);
            holder.editGroupBtn =(Button) convertView.findViewById(R.id.btnEditGroup);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        final SpecialGroup specialGroup = specialGroupsList.get(position);
        holder.name.setText(specialGroup.getName());

        holder.editGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateSpecialGroupActivity.class)
                        .putExtra("Mode", currMode)
                        .putExtra("SpecialGroup", specialGroup);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        Button editGroupBtn;
    }
}
