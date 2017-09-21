/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.calculator.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import com.duy.calculator.R;
import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.base.Evaluator;
import com.duy.calculator.utils.TextUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FormattedNumberEditText adds more advanced functionality to NumberEditText.
 * <p/>
 * Commas will appear as numbers are typed, exponents will be raised, and backspacing
 * on sin( and log( will remove the whole word. Because of the formatting, getText() will
 * no longer return the correct value. getCleanText() has been added instead.
 */
public class CalculatorEditText extends ResizingEditText {
    public static final String TAG = "CalculatorEditText";
    public static final String CURSOR = "✿";

    private final Set<TextWatcher> mTextWatchers = new HashSet<>();
    /**
     * enable text watcher
     */
    private final TextWatcher mCursorWatcher = new TextWatcher() {
        private CharSequence s;
        private int start;
        private int before;
        private int count;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.s = s;
            this.start = start;
            this.before = before;
            this.count = count;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private List<String> mKeywords;
    private boolean mIsInserting = false;

    public CalculatorEditText(Context context) {
        super(context);
        setUp(context, null);
    }


    public CalculatorEditText(Context context, AttributeSet attr) {
        super(context, attr);
        setUp(context, attr);
    }

    private void setUp(Context context, @Nullable AttributeSet attrs) {
        addTextChangedListener(mCursorWatcher);
        invalidateKeywords(context);
    }

    public void invalidateKeywords(Context context) {
        mKeywords = Arrays.asList(
                context.getString(R.string.fun_arcsin) + "(",
                context.getString(R.string.fun_arccos) + "(",
                context.getString(R.string.fun_arctan) + "(",
                context.getString(R.string.arcsin) + "(",
                context.getString(R.string.arccos) + "(",
                context.getString(R.string.arctan) + "(",
                context.getString(R.string.fun_sin) + "(",
                context.getString(R.string.fun_cos) + "(",
                context.getString(R.string.fun_tan) + "(",
                context.getString(R.string.sin) + "(",
                context.getString(R.string.cos) + "(",
                context.getString(R.string.tan) + "(",
                context.getString(R.string.fun_arccsc) + "(",
                context.getString(R.string.fun_arcsec) + "(",
                context.getString(R.string.fun_arccot) + "(",
                context.getString(R.string.fun_csc) + "(",
                context.getString(R.string.fun_sec) + "(",
                context.getString(R.string.fun_cot) + "(",
                context.getString(R.string.fun_log) + "(",
                context.getString(R.string.mod) + "(",
                context.getString(R.string.fun_ln) + "(",
                context.getString(R.string.op_cbrt) + "(",
                context.getString(R.string.tanh) + "(",
                context.getString(R.string.cosh) + "(",
                context.getString(R.string.sinh) + "(",
                context.getString(R.string.log2) + "(",
                context.getString(R.string.log10) + "(",
                context.getString(R.string.abs) + "(",
                context.getString(R.string.sgn) + "(",
                context.getString(R.string.floor) + "(",
                context.getString(R.string.ceil) + "(",
                context.getString(R.string.arctanh) + "(",
                context.getString(R.string.sum) + "(",
                context.getString(R.string.diff) + "(",
                context.getString(R.string.avg) + "(",
                context.getString(R.string.vari) + "(",
                context.getString(R.string.stdi) + "(",
                context.getString(R.string.mini) + "(",
                context.getString(R.string.maxi) + "(",
                context.getString(R.string.min) + "(",
                context.getString(R.string.max) + "(",
                context.getString(R.string.std) + "(",
                context.getString(R.string.mean) + "(",
                context.getString(R.string.sqrt_sym) + "(",
                context.getString(R.string.log2) + "(",
                context.getString(R.string.log10) + "(",
                context.getString(R.string.cot) + "(",
                context.getString(R.string.exp) + "(",
                context.getString(R.string.sign) + "(",
                context.getString(R.string.arg) + "(",
                context.getString(R.string.gcd_up) + "(",
                context.getString(R.string.log2) + "(",
                context.getString(R.string.ln) + "(",
                context.getString(R.string.ln) + "(",
                context.getString(R.string.log2) + "(",
                context.getString(R.string.arcsinh) + "(",
                context.getString(R.string.arccosh) + "(",
                context.getString(R.string.arctanh) + "(",
                context.getString(R.string.op_cbrt) + "(",
                context.getString(R.string.permutations) + "(",
                context.getString(R.string.binomial) + "(",
                context.getString(R.string.trunc) + "(",
                context.getString(R.string.max) + "(",
                context.getString(R.string.min) + "(",
                context.getString(R.string.mod) + "(",
                context.getString(R.string.gcd) + "(",
                context.getString(R.string.lcm) + "(",
                context.getString(R.string.sign) + "(",
                context.getString(R.string.rnd) + "(",
                context.getString(R.string.ans),
                context.getString(R.string.mtrue),
                context.getString(R.string.mfalse),
                context.getString(R.string.eq)
        );
    }

    private void generalReplacement() {
    }

    protected void onFormat(Editable s) {
        int select = getSelectionStart();
        setText(s);
        setSelection(select);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (watcher.equals(mCursorWatcher) || mTextWatchers == null) {
            super.addTextChangedListener(watcher);
        } else {
            mTextWatchers.add(watcher);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (getText() != null) {
            setSelection(getText().length());
        }
    }

    /**
     * return clean text , reformat text.
     *
     * @return - String
     */
    public String getCleanText() {
        return TextUtil.getCleanText(this);
    }

    public void insert(String delta) {
        String currentText = getText().toString();
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        String textBeforeInsertionHandle = currentText.substring(0, selectionStart);
        String textAfterInsertionHandle = currentText.substring(selectionEnd, currentText.length());

        // Add extra rules for decimal points and operators
        if (delta.length() == 1) {
            char text = delta.charAt(0);

            // don't allow two dots in the same number
            if (text == Constants.DECIMAL_POINT) {
                int p = selectionStart - 1;
                while (p >= 0 && Evaluator.isDigit(getText().charAt(p))) {
                    if (getText().charAt(p) == Constants.DECIMAL_POINT) {
                        return;
                    }
                    --p;
                }
                p = selectionStart;
                while (p < getText().length() && Evaluator.isDigit(getText().charAt(p))) {
                    if (getText().charAt(p) == Constants.DECIMAL_POINT) {
                        return;
                    }
                    ++p;
                }
            }

            char prevChar = selectionStart > 0 ? getText().charAt(selectionStart - 1) : '\0';

            // don't allow the first character to be an operator
            if (selectionStart == 0 && Evaluator.isOperator(text)
                    && text != Constants.MINUS_UNICODE) {
                return;
            }

            // don't allow multiple successive operators
            if (Evaluator.isOperator(text) &&
                    text != Constants.MINUS_UNICODE) {
                while (Evaluator.isOperator(prevChar)) {
                    if (selectionStart == 1) {
                        return;
                    }

                    --selectionStart;
                    prevChar = selectionStart > 0 ? getText().charAt(selectionStart - 1) : '\0';
                    textBeforeInsertionHandle = textBeforeInsertionHandle.substring(0, selectionStart);
                }
            }
            // don't allow two degree symbol
            Log.d(TAG, "insert: " + text + " " + (text == Constants.DEGREE_UNICODE));
            if (text == Constants.DEGREE_UNICODE
                    && Evaluator.isOperator(prevChar)) {
                return;
            }
        }
        mIsInserting = true;
        setText(textBeforeInsertionHandle + delta + textAfterInsertionHandle);
        setSelection(selectionStart + delta.length());
        mIsInserting = false;
    }

    public void clear() {
        setText(null);
    }

    public void next() {
        if (getSelectionStart() == getText().length()) {
            setSelection(0);
        } else {
            setSelection(getSelectionStart() + 1);
        }
    }


    public void backspace() {
        // Check and remove keywords
        String text = getText().toString();
        int selectionHandle = getSelectionStart();
        String textBeforeInsertionHandle = text.substring(0, selectionHandle);
        String textAfterInsertionHandle = text.substring(selectionHandle, text.length());

        for (String s : mKeywords) {
            if (textBeforeInsertionHandle.endsWith(s)) {
                int deletionLength = s.length();
                String newText = textBeforeInsertionHandle.substring(0, textBeforeInsertionHandle.length() - deletionLength) + textAfterInsertionHandle;
                setText(newText);
                setSelection(selectionHandle - deletionLength);
                return;
            }
        }

        // Override NumberEditText's method -- because commas might disappear, it complicates things
        if (selectionHandle != 0) {
            setText(textBeforeInsertionHandle.substring(0, textBeforeInsertionHandle.length()
                    - 1) + textAfterInsertionHandle);

            if (getText().length() == text.length() - 2) {
                // 2 characters were deleted (likely a comma and a number)
                selectionHandle -= 2;
            } else {
                --selectionHandle;
            }

            setSelection(selectionHandle);
        }
    }


}
