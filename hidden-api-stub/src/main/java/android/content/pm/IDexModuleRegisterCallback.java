package android.content.pm;

public interface IDexModuleRegisterCallback {
    void onDexModuleRegistered(String dexModulePath, boolean success, String message);
}