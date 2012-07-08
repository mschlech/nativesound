package com.entscheidungsbaum.nativesound;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * com.entscheidungsbaum.nativesound
 * marcus
 * Author Marcus Schlechter
 * 7/5/12
 */
public class NotifyingToggleButton extends ToggleButton {

    public NotifyingToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NotifyingToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyingToggleButton(Context context) {
        super(context);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
