package android.os;

public class UserBatteryConsumer extends BatteryConsumer {
    static final int CONSUMER_TYPE_USER = 2;

    private static final int COLUMN_INDEX_USER_ID = BatteryConsumer.COLUMN_COUNT;

    static final int COLUMN_COUNT = BatteryConsumer.COLUMN_COUNT + 1;


    public int getUserId() {
        throw new RuntimeException("STUB");
    }
}