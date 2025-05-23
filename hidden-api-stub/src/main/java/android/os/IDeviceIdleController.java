package android.os;

import androidx.annotation.RequiresApi;

import java.util.List;

public interface IDeviceIdleController extends IInterface {

    void addPowerSaveTempWhitelistApp(String name, long duration, int userId, String reason);

    @RequiresApi(Build.VERSION_CODES.S)
    void addPowerSaveTempWhitelistApp(String name, long duration, int userId, int reasonCode, String reason);


    void addPowerSaveWhitelistApp(String name);

    int addPowerSaveWhitelistApps(List<String> packageNames);

    void removePowerSaveWhitelistApp(String name);

    /* Removes an app from the system whitelist. Calling restoreSystemPowerWhitelistApp will add
    the app back into the system whitelist */
    void removeSystemPowerWhitelistApp(String name);

    void restoreSystemPowerWhitelistApp(String name);

    String[] getRemovedSystemPowerWhitelistApps();

    String[] getSystemPowerWhitelistExceptIdle();

    String[] getSystemPowerWhitelist();

    String[] getUserPowerWhitelist();

    String[] getFullPowerWhitelistExceptIdle();

    String[] getFullPowerWhitelist();

    int[] getAppIdWhitelistExceptIdle();

    int[] getAppIdWhitelist();

    int[] getAppIdUserWhitelist();

    int[] getAppIdTempWhitelist();

    boolean isPowerSaveWhitelistExceptIdleApp(String name);

    boolean isPowerSaveWhitelistApp(String name);

    long addPowerSaveTempWhitelistAppForMms(String name, int userId, int reasonCode, String reason);

    long addPowerSaveTempWhitelistAppForSms(String name, int userId, int reasonCode, String reason);

    long whitelistAppTemporarily(String name, int userId, int reasonCode, String reason);

    void exitIdle(String reason);

    int setPreIdleTimeoutMode(int Mode);

    void resetPreIdleTimeoutMode();


    abstract class Stub extends Binder implements IDeviceIdleController {

        public static IDeviceIdleController asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }
}
