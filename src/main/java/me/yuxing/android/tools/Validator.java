package me.yuxing.android.tools;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

/**
 * Created by yuxing on 2014-10-10.
 */
public class Validator {
    private final Context mContext;

    public Validator(Context context) {
        mContext = context;
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
}
