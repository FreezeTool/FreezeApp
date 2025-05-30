package android.os;

import android.system.Os;

import com.john.hidden.api.Replace;

@Replace(Process.class)
public class ProcessHidden {
    public static final int myPpid() {
        throw new RuntimeException();
    }
}
