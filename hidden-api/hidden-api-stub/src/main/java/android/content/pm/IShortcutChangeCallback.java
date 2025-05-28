package android.content.pm;

import android.os.UserHandle;

import java.util.List;

public interface IShortcutChangeCallback {
    void onShortcutsAddedOrUpdated(String packageName, List<ShortcutInfo> shortcuts,
                                   UserHandle user);

    void onShortcutsRemoved(String packageName, List<ShortcutInfo> shortcuts,
                            UserHandle user);
}