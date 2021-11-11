package ir.code4life.willy.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import ir.code4life.willy.R;

public class LoginButton extends LinearLayoutCompat {
    public LoginButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context,R.layout.login_button,this);
    }

    @Override
    public void setOnClickListener(OnClickListener listener){
        super.setOnClickListener(listener);
    }
}
