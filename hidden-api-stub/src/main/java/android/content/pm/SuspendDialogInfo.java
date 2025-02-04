package android.content.pm;

public final class SuspendDialogInfo {

    SuspendDialogInfo(Builder b) {

    }

    public static final class Builder {
        public Builder setIcon(int resId) {

            return this;
        }

        public Builder setTitle(int resId) {

            return this;
        }

        public Builder setTitle(String title) {

            return this;
        }


        public Builder setMessage(String message) {

            return this;
        }


        public Builder setMessage(int resId) {

            return this;
        }


        public Builder setNeutralButtonText(int resId) {

            return this;
        }

        public Builder setNeutralButtonText(String neutralButtonText) {

            return this;
        }

        public Builder setNeutralButtonAction(int buttonAction) {

            return this;
        }

        public SuspendDialogInfo build() {
            return new SuspendDialogInfo(this);
        }
    }
}