package yael.smartmode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yael on 15/08/2017.
 */

public class ModeAdapter extends ArrayAdapter<Mode> {
    private final Context context;
    private final List<Mode> values;

    public ModeAdapter(Context context, List<Mode> values) {
        super(context, R.layout.row_mode, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_mode, parent, false);

        return rowView;
    }
}