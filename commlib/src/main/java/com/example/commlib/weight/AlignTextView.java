package com.example.commlib.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Spannable;
import android.text.StaticLayout;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.commlib.R;


/**
 *
 * TextView在显示中文的时候标点符号不能显示在一行的行首和行尾，如果一个标点符号刚好在一行的行尾，该标点符号就会连同前一个字符跳到下一行显示  。导致文本左右不对齐差距较大。
 * Created by yzh on 2020/10/24 17:21.
 */
public class AlignTextView extends AppCompatTextView {


    //textview只有一行的时候是否需要实现左右对齐
    private boolean alignOnlyOneLine=false;

    public AlignTextView(Context context) {
        this(context, null);
    }

    public AlignTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlignTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlignTextView);
        alignOnlyOneLine = typedArray.getBoolean(R.styleable.AlignTextView_alignOnlyOneLine, false);
        typedArray.recycle();
        setTextColor(getCurrentTextColor());
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        getPaint().setColor(color);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence content = getText();
        if(content instanceof Spannable){

        }
//        if (!(content instanceof String)) {
//            super.onDraw(canvas);
//            return;
//        }
        String text = getText().toString();
        Layout layout = getLayout();

        for (int i = 0; i < layout.getLineCount(); ++i) {
            int lineBaseline = layout.getLineBaseline(i) + getPaddingTop();
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            if (alignOnlyOneLine && layout.getLineCount() == 1) {//只有一行
                String line = text.substring(lineStart, lineEnd);
                float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
                this.drawScaledText(canvas, line, lineBaseline, width);
            } else if (i == layout.getLineCount() - 1) {//最后一行
                canvas.drawText(text.substring(lineStart), getPaddingLeft(), lineBaseline, getPaint());
                break;
            } else {//中间行
                String line = text.substring(lineStart, lineEnd);
                float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
                this.drawScaledText(canvas, line, lineBaseline, width);
            }
        }
        //super.onDraw(canvas);
    }

    private void drawScaledText(Canvas canvas, String line, float baseLineY, float lineWidth) {
        if (line.length() < 1) {
            return;
        }
        float x = getPaddingLeft();
        boolean forceNextLine = line.charAt(line.length() - 1) == 10;
        int length = line.length() - 1;
        if (forceNextLine || length == 0) {
            canvas.drawText(line, x, baseLineY, getPaint());
            return;
        }

        float d = (getMeasuredWidth() - lineWidth - getPaddingLeft() - getPaddingRight()) / length;

        for (int i = 0; i < line.length(); ++i) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, this.getPaint());
            canvas.drawText(c, x, baseLineY, this.getPaint());
            x += cw + d;
        }
    }


}