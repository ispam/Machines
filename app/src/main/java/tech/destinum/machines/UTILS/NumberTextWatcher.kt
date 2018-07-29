package tech.destinum.machines.UTILS

import java.text.DecimalFormat
import java.text.ParseException

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class NumberTextWatcher(private val et: EditText)//        df = new DecimalFormat("#,##0.000");
//        df.setDecimalSeparatorAlwaysShown(true);
//        dfnd = new DecimalFormat("#,##0.000");
//        hasFractionalPart = false;
    : TextWatcher {

    private val df: DecimalFormat? = null
    private val dfnd: DecimalFormat? = null
    private val hasFractionalPart: Boolean = false

    override fun afterTextChanged(s: Editable) {
        //        et.removeTextChangedListener(this);
        //
        //        try {
        //            int inilen, endlen;
        //            inilen = et.getText().length();
        //
        //            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
        //            Number n = df.parse(v);
        //            int cp = et.getSelectionStart();
        //            if (hasFractionalPart) {
        //                et.setText(df.format(n));
        //            } else {
        //                et.setText(dfnd.format(n));
        //            }
        //            endlen = et.getText().length();
        //            int sel = (cp + (endlen - inilen));
        //            if (sel > 0 && sel <= et.getText().length()) {
        //                et.setSelection(sel);
        //            } else {
        //                // place cursor at the end?
        //                et.setSelection(et.getText().length() - 1);
        //            }
        //        } catch (NumberFormatException nfe) {
        //            // do nothing?
        //        } catch (ParseException e) {
        //            // do nothing?
        //        }
        //
        //        et.addTextChangedListener(this);
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
        //        {
        //            hasFractionalPart = true;
        //        } else {
        //            hasFractionalPart = false;
        //        }
        val working = s.toString()
        var isValid = true

        if (working.length <= 4 && before == 0) {
            if (working.length < 4 || working.length > 10) {
                isValid = false
            }
        }

        if (!isValid) {
            et.error = "Dinero debe tener punto décimal"
        } else {
            et.error = null
        }
    }

    companion object {

        private val TAG = "NumberTextWatcher"
    }

}

/*
    Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
        mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher=mPattern.matcher(dest);
        if(!matcher.matches())
            return "";
        return null;
    }*/