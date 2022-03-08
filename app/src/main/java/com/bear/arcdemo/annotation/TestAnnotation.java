package com.bear.arcdemo.annotation;


import android.widget.TextView;

import com.bear.arcdemo.R;
import com.bear.processor.InjectView;

public class TestAnnotation {
    @InjectView(R.id.accessibility_action_clickable_span)
    private TextView mTextView;

    public void onCreate() {

    }

}
