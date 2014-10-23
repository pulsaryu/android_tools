package me.yuxing.android.tools;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuxing on 2014-10-10.
 */
public class Validator {
    private final Context mContext;
    private List<Validate> mValidates = new ArrayList<Validate>();
    private String mError;

    public Validator(Context context) {
        mContext = context;
    }

    public static Validator init(Context context) {
        return new Validator(context);
    }

    public boolean requiredValidate(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            editText.setError(mContext.getString(R.string.error_cannot_empty));
            return false;
        }

        return true;
    }

    public boolean emailValidate(EditText editText) {
        String value = editText.getText().toString().trim();
        if (!value.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            editText.setError(mContext.getString(R.string.error_invalid_email));
            return false;
        }

        return true;
    }

    public boolean confirmValidate(EditText editText, EditText confirmEditText, String error) {
        String value = editText.getText().toString();
        String confirm = confirmEditText.getText().toString();
        if (!value.isEmpty() && !value.equals(confirm)) {
            editText.setError(error);
            return false;
        }

        return true;
    }

    public Validator required(String message) {
        mValidates.add(new RequiredValidate(message));
        return this;
    }

    public Validator email(String message) {
        mValidates.add(new EmailValidate(message));
        return this;
    }

    public Validator minString(String message, int min) {
        mValidates.add(new MinStringValidate(message, min));
        return this;
    }

    public Validator maxString(String message, int max) {
        mValidates.add(new MaxStringValidate(message, max));
        return this;
    }

    public Validator compare(String message, EditText compareText) {
        mValidates.add(new CompareValidate(message, compareText));
        return this;
    }

    public Validator from(EditText editText) {
        String value = editText.getText().toString().trim();
        for (Validate validate : mValidates) {
            if (!validate.fire(value)) {
                mError = validate.message();
                editText.setError(mError);
                break;
            }
        }

        return this;
    }

    public boolean success() {
        return mError == null;
    }

    private static abstract class Validate {

        private String mMessage;

        protected Validate(String message) {
            mMessage = message;
        }

        public abstract boolean fire(String value);
        public String message() {
            return mMessage;
        }
    }

    private static class RequiredValidate extends Validate {

        protected RequiredValidate(String message) {
            super(message);
        }

        @Override
        public boolean fire(String value) {
            if (TextUtils.isEmpty(value)) {
                return false;
            }

            return true;
        }
    }

    private static class EmailValidate extends Validate {

        protected EmailValidate(String message) {
            super(message);
        }

        @Override
        public boolean fire(String value) {
            return value.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(value).matches();
        }
    }

    private static class MinStringValidate extends Validate {

        private final int mMin;

        protected MinStringValidate(String message, int min) {
            super(message);
            mMin = min;
        }

        @Override
        public boolean fire(String value) {
            return value.length() >= mMin;
        }
    }

    private static class MaxStringValidate extends Validate {

        private final int mMax;

        protected MaxStringValidate(String message, int min) {
            super(message);
            mMax = min;
        }

        @Override
        public boolean fire(String value) {
            return value.length() <= mMax;
        }
    }

    private static class CompareValidate extends Validate {

        private final EditText mCompareText;

        protected CompareValidate(String message, EditText compareText) {
            super(message);
            mCompareText = compareText;
        }

        @Override
        public boolean fire(String value) {
            String compare = mCompareText.getText().toString().trim();
            return value.equals(compare);
        }
    }
}
