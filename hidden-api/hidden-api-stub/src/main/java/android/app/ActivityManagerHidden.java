package android.app;


import com.john.hidden.api.Replace;

@Replace(ActivityManager.class)
public class ActivityManagerHidden {

    public static int INTENT_SENDER_ACTIVITY;

    public static final class PendingIntentInfo {

        private final String mCreatorPackage;
        private final int mCreatorUid;
        private final boolean mImmutable;
        private final int mIntentSenderType;

        public PendingIntentInfo(String creatorPackage, int creatorUid, boolean immutable,
                                 int intentSenderType) {
            mCreatorPackage = creatorPackage;
            mCreatorUid = creatorUid;
            mImmutable = immutable;
            mIntentSenderType = intentSenderType;
        }

        public String getCreatorPackage() {
            return mCreatorPackage;
        }

        public int getCreatorUid() {
            return mCreatorUid;
        }

        public boolean isImmutable() {
            return mImmutable;
        }

        public int getIntentSenderType() {
            return mIntentSenderType;
        }


    }

    public static class TaskSnapshot {

    }
}
