package android.os;

import androidx.annotation.RequiresApi;

import com.john.hidden.api.Replace;

@Replace(UserHandle.class)
public class UserHandleHidden {
    public static UserHandle ALL;

    public static int USER_NULL;
    public static int USER_ALL;
    public static int USER_SYSTEM;

    @RequiresApi(Build.VERSION_CODES.N)
    public static UserHandle of(int userId) {
        throw new RuntimeException();
    }

    /**
     * @see #of(int)
     */
    public UserHandleHidden(int h) {
        throw new RuntimeException();
    }
}
