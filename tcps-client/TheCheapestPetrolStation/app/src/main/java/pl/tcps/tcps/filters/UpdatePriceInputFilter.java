package pl.tcps.tcps.filters;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdatePriceInputFilter implements InputFilter {

    private Pattern pattern;

    public UpdatePriceInputFilter() {
        pattern = Pattern.compile("[0-9](\\.[0-9]{0,2})?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher= pattern.matcher(createTextToMatch(source, start, end, dest, dstart, dend));
        if(!matcher.matches())
            return "";
        return null;
    }

    private String createTextToMatch(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceString = source.toString();
        String destString = dest.toString();
        return destString.substring(0, dstart) + sourceString.substring(start, end) + destString.substring(dend);
    }
}
