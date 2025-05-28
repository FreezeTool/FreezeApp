package android.app.smartspace;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SmartspaceTargetEvent {

    /**
     * User interacted with the target.
     */
    public static final int EVENT_TARGET_INTERACTION = 1;

    /**
     * Smartspace target was brought into view.
     */
    public static final int EVENT_TARGET_SHOWN = 2;
    /**
     * Smartspace target went out of view.
     */
    public static final int EVENT_TARGET_HIDDEN = 3;
    /**
     * A dismiss action was issued by the user.
     */
    public static final int EVENT_TARGET_DISMISS = 4;
    /**
     * A block action was issued by the user.
     */
    public static final int EVENT_TARGET_BLOCK = 5;
    /**
     * The Ui surface came into view.
     */
    public static final int EVENT_UI_SURFACE_SHOWN = 6;
    /**
     * The Ui surface went out of view.
     */
    public static final int EVENT_UI_SURFACE_HIDDEN = 7;

    @Nullable
    private final SmartspaceTarget mSmartspaceTarget;

    @Nullable
    private final String mSmartspaceActionId;

    @EventType
    private final int mEventType;

    private SmartspaceTargetEvent(@Nullable SmartspaceTarget smartspaceTarget,
                                  @Nullable String smartspaceActionId,
                                  @EventType int eventType) {
        mSmartspaceTarget = smartspaceTarget;
        mSmartspaceActionId = smartspaceActionId;
        mEventType = eventType;
    }

    /**
     * Get the {@link SmartspaceTarget} associated with this event.
     */
    @Nullable
    public SmartspaceTarget getSmartspaceTarget() {
        return mSmartspaceTarget;
    }

    /**
     * Get the action id of the Smartspace Action associated with this event.
     */
    @Nullable
    public String getSmartspaceActionId() {
        return mSmartspaceActionId;
    }

    /**
     * Get the {@link EventType} of this event.
     */
    @NonNull
    @EventType
    public int getEventType() {
        return mEventType;
    }


    /**
     * @hide
     */
    @IntDef(value = {
            EVENT_TARGET_INTERACTION,
            EVENT_TARGET_SHOWN,
            EVENT_TARGET_HIDDEN,
            EVENT_TARGET_DISMISS,
            EVENT_TARGET_BLOCK,
            EVENT_UI_SURFACE_SHOWN,
            EVENT_UI_SURFACE_HIDDEN
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {
    }

    public static final class Builder {
        @EventType
        private final int mEventType;
        @Nullable
        private SmartspaceTarget mSmartspaceTarget;
        @Nullable
        private String mSmartspaceActionId;

        /**
         * A builder for {@link SmartspaceTargetEvent}.
         */
        public Builder(@EventType int eventType) {
            mEventType = eventType;
        }

        /**
         * Sets the SmartspaceTarget for this event.
         */
        @NonNull
        public Builder setSmartspaceTarget(@NonNull SmartspaceTarget smartspaceTarget) {
            mSmartspaceTarget = smartspaceTarget;
            return this;
        }

        /**
         * Sets the Smartspace action id for this event.
         */
        @NonNull
        public Builder setSmartspaceActionId(@NonNull String smartspaceActionId) {
            mSmartspaceActionId = smartspaceActionId;
            return this;
        }

        /**
         * Builds a new {@link SmartspaceTargetEvent} instance.
         */
        @NonNull
        public SmartspaceTargetEvent build() {
            return new SmartspaceTargetEvent(mSmartspaceTarget, mSmartspaceActionId, mEventType);
        }
    }
}