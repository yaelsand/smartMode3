package yael.smartmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;
import yael.smartmode.CursorAdapter;

/**
 * Created by Lea on 04/09/2017.
 */

public class AlarmEndReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.isEnd = true;
        Mode mode = (Mode) intent.getSerializableExtra("modeEnd");
        AudioManager am;
        am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(mode.getRing());
        CursorAdapter.ScreenBrightness(mode.getScreenLight(), context);


    }
}
