package com.cncoding.teazer.customViews.proximanovaviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.text.emoji.widget.EmojiAppCompatTextView;
import android.util.AttributeSet;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class ProximaNovaSemiBoldTextView extends EmojiAppCompatTextView {
    public ProximaNovaSemiBoldTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public ProximaNovaSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        ProximaNovaSemiboldButton.initAttrs(context, attrs, this);
    }

    public ProximaNovaSemiBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
        ProximaNovaSemiboldButton.initAttrs(context, attrs, this);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_semibold.otf");
        setTypeface(customFont);
    }
}