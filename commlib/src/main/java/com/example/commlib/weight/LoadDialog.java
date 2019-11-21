package com.example.commlib.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.blankj.ALog;
import com.example.commlib.R;


/**
 * @Author: yzh
 * @CreateDate: 2019/11/1 11:39
 */
public class LoadDialog extends Dialog {

    private TextView tvMessage;
    private String msg = "";
    private Boolean cancelAble = true;
    private JumpingBeans jump;
    private int cancelTime;//避免异常情况下Dialog
    public LoadDialog(Context context, String msg, Boolean cancelAble) {
        super(context, R.style.comm_load_dialog);
        this.msg = msg;
        this.cancelAble = cancelAble;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_load_dialogs);
        tvMessage = this.findViewById(R.id.tvLoadDialog_Message);

        if (TextUtils.isEmpty(msg)) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(msg);
            if (msg.length() > 3) {
                //if(msg.equals(CommentUtils.getString("R.string.content_loading"))){
                 jump = JumpingBeans.with(tvMessage)
                        .makeTextJump(msg.length() - 3, msg.length())
                        .setIsWave(true)
                        .setLoopDuration(1500)
                        .build();
                //}
            }
        }

    }

    @Override
    public void dismiss() {
        if(jump!=null){
            jump.stopJumping();
        }
        cancelTime=0;
        super.dismiss();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        ALog.v("cancelTime "+cancelTime);
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                cancelTime++;
                if (cancelTime>9){
                    return super.onTouchEvent(event);
                }
                break;
        }


        if (!cancelAble){
            return false;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!cancelAble) return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}
