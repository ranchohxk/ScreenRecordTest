package com.supper.lupingdashi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/3/21.
 */

@SuppressLint("AppCompatCustomView")
public class TextImageView extends ImageView {
    public TextImageView(Context context) {
        super(context);
    }

    public TextImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
