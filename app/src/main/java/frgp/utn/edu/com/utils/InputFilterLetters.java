package frgp.utn.edu.com.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterLetters implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (!Character.isLetter(source.charAt(i)) && source.charAt(i) != ' ') {
                return "";
            }
        }
        return null;
    }
}

