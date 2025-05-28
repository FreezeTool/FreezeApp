package android.content.pm;

import android.content.ComponentName;
import android.content.LocusId;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N_MR1)
public final class ShortcutQueryWrapper extends LauncherApps.ShortcutQuery {

    public ShortcutQueryWrapper(LauncherApps.ShortcutQuery query) {
        this();
        
    }

    public long getChangedSince() {
        throw new RuntimeException("STUB");
    }

    
    public String getPackage() {
        throw new RuntimeException("STUB");
    }

    
    public List<LocusId> getLocusIds() {
        throw new RuntimeException("STUB");
    }

    
    public List<String> getShortcutIds() {
        throw new RuntimeException("STUB");
    }

    
    public ComponentName getActivity() {
        throw new RuntimeException("STUB");
    }

    public int getQueryFlags() {
        throw new RuntimeException("STUB");
    }


    public ShortcutQueryWrapper() {

    }

}