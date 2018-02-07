package com.cncoding.teazer.customViews.proximanovaviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.text.emoji.widget.EmojiAppCompatEditText;
import android.util.AttributeSet;

import static com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiboldButton.initAttrs;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class ProximaNovaRegularTextInputEditText extends EmojiAppCompatEditText {
    public ProximaNovaRegularTextInputEditText(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public ProximaNovaRegularTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        initAttrs(context, attrs, this);
    }

    public ProximaNovaRegularTextInputEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
        initAttrs(context, attrs, this);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_regular.otf");
        setTypeface(customFont);
    }
}