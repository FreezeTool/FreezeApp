package android.app;

import android.content.IIntentSender;
import android.content.IntentSender;

import com.john.hidden.api.Replace;

@Replace(PendingIntent.class)
public class PendingIntentHidden {

    public PendingIntentHidden(IIntentSender intentSender) {
    }
}
