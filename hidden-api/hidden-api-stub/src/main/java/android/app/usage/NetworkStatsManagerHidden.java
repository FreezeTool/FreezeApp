package android.app.usage;

import com.john.hidden.api.Replace;

@Replace(NetworkStatsManager.class)
public class NetworkStatsManagerHidden {
    public static final int CALLBACK_LIMIT_REACHED = 0;
    public static final int CALLBACK_RELEASED = 1;
}
