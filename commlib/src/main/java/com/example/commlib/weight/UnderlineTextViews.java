package com.example.commlib.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.commlib.R;
import com.example.commlib.utils.ChineseAndEnglish;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by yzh on 2021/1/11 17:12.
 */
//public class UnderlinedTextView extends AppCompatTextView {
//
//    private Rect lineBoundsRect;
//    private Paint underlinePaint;
//    float mStrokeWidth=4;
//    public UnderlinedTextView(Context context) {
//        this(context, null, 0);
//    }
//
//    public UnderlinedTextView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public UnderlinedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context, attrs, defStyleAttr);
//    }
//
//    private void init(Context context, AttributeSet attributeSet, int defStyle) {
//
//        float density = context.getResources().getDisplayMetrics().density;
//
//        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.UnderlinedTextView, defStyle, 0);
//        int mColor = typedArray.getColor(R.styleable.UnderlinedTextView_underlineColor, 0xFFFF0000);
//        mStrokeWidth = typedArray.getDimension(R.styleable.UnderlinedTextView_underlineWidth, density * 2);
//        typedArray.recycle();
//
//        lineBoundsRect = new Rect();
//        underlinePaint = new Paint();
//        underlinePaint.setStyle(Paint.Style.STROKE);
//        underlinePaint.setColor(mColor); //color of the underline
//        underlinePaint.setStrokeWidth(mStrokeWidth);
//    }
//
//    @ColorInt
//    public int getUnderLineColor() {
//        return underlinePaint.getColor();
//    }
//
//    public void setUnderLineColor(@ColorInt int mColor) {
//        underlinePaint.setColor(mColor);
//        invalidate();
//    }
//
//    public float getUnderlineWidth() {
//        return underlinePaint.getStrokeWidth();
//    }
//
//    public void setUnderlineWidth(float mStrokeWidth) {
//        underlinePaint.setStrokeWidth(mStrokeWidth);
//        invalidate();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        int count = getLineCount();
//
//        final Layout layout = getLayout();
//        float x_start, x_stop, x_diff;
//        int firstCharInLine, lastCharInLine;
//
//        for (int i = 0; i < count; i++) {
//            int baseline = getLineBounds(i, lineBoundsRect);
//            firstCharInLine = layout.getLineStart(i);
//            lastCharInLine = layout.getLineEnd(i);
//
//            x_start = layout.getPrimaryHorizontal(firstCharInLine);
//            x_diff = layout.getPrimaryHorizontal(firstCharInLine + 1) - x_start;
//            x_stop = layout.getPrimaryHorizontal(lastCharInLine - 1) + x_diff;
//
//            int lineSpace=6;
//            canvas.drawLine(x_start, baseline + mStrokeWidth+lineSpace, x_stop, baseline + mStrokeWidth+lineSpace, underlinePaint);
//        }
//
//        super.onDraw(canvas);
//    }
//}

/**
 * Created by yzh on 2021/1/11 17:12.
 */

public class UnderlineTextViews extends AppCompatTextView {
    private String TAG="UnderlineTextViews";
    private Rect mRect;
    private Paint mPaint;
    private int mColor;
    private float density;
    private float mStrokeWidth;
    private float mLineTopMargin = 0;


    private List<Integer[]> mList;

    /**
     * 添加多段索引
     * @param mList
     */
    public void setStartEnds(List<Integer[]> mList) {
        this.mList=mList;
        if(mList!=null&&!mList.isEmpty()){
            invalidate();
        }
    }

    /**
     * 添加下划线起止的起止 索引。 注意这里的 都包含   例如 3,5  35都是包含的
     * @param start
     * @param end
     */
    public void setStartEnd(int start, int end) {
        if (start < 0) {
            return;
        }
        if (start < end) {
            Integer[] data={start,end};
            mList=new ArrayList<>();
            mList.add(data);
            setStartEnds(mList);
        }
    }

    public UnderlineTextViews(Context context) {
        this(context, null, 0);
    }

    public UnderlineTextViews(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderlineTextViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取屏幕密度
        density = context.getResources().getDisplayMetrics().density;
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UnderlinedTextView, defStyleAttr, 0);
        mColor = array.getColor(R.styleable.UnderlinedTextView_underlineColor, 0xFFFF0000);
        mStrokeWidth = array.getDimension(R.styleable.UnderlinedTextView_underlineWidth, density * 2);
        mLineTopMargin = array.getDimension(R.styleable.UnderlinedTextView_underlineTopMargin, density * 2);
        //mLineTopMargin 设值较大的时候需要适当增加行点间距，否则会显示不下。
        setLineSpacing(mLineTopMargin, (float) 1.1);
        setPadding(getLeft(), getTop(), getRight(), getBottom());

        array.recycle();


        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrokeWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mList==null||mList.isEmpty()){
            super.onDraw(canvas);
            return;
        }
        //得到TextView显示有多少行
        int count = getLineCount();
        //得到TextView的布局
        final Layout layout = getLayout();
//    TextView layout属性：
//
//    TextView 的 layout里面包含各种获取字符位置、行数、列数等的方法
//    layout.getLineForOffset 获取该字符所在行数.
//    layout.getLineBounds获取该行的外包矩形（Rect) 这样 这个字符的顶部Y坐标就是rect的top 底部Y坐标就是rect的bottom
//    layout.getPrimaryHorizontal获取该字符左边的X坐标
//    layout.getSecondaryHorizontal获取该字符字符的右边X坐标

        for(Integer[] indexes:mList){
            if (indexes==null||indexes.length<1) {
                Log.e(TAG,"起止索引有误!");
                continue;
            }

            int start=indexes[0];
            int end=indexes[1];
            if (start < 0 || end == 0) {
                Log.e(TAG,"起止索引有误!");
                continue;
            }
            int lineStart = layout.getLineForOffset(start);
            int lineEnd = layout.getLineForOffset(end);
            //只存在单行
            if (lineStart == lineEnd) {
                //getLineBounds得到这一行的外包矩形,这个字符的顶部Y坐标就是rect的top 底部Y坐标就是rect的bottom
                int baseline = layout.getLineBounds(lineStart, mRect);
                // int yStart=mRect.bottom;//字符底部y坐标

                float xStart = layout.getPrimaryHorizontal(start);//开始字符左边x坐标
                float xDiff = layout.getPrimaryHorizontal(start + 1) - xStart;//末尾需要偏移一个字的x距离
                xDiff = getXDiff(start, end, xDiff);
                float xEnd = layout.getSecondaryHorizontal(end) + xDiff;//结束字符右边边x坐标
                float offsetY = baseline + mLineTopMargin + mStrokeWidth;
                canvas.drawLine(xStart, offsetY, xEnd, offsetY, mPaint);

            } else {
                //下划线跨行、有多行时
                for (int i = lineStart; i <= lineEnd; i++) {
                    int baseline = layout.getLineBounds(i, mRect);

                    int firstCharInLine = layout.getLineStart(i);
                    int lastCharInLine = layout.getLineEnd(i) - 1;

                    int startIndex = i == lineStart ? start : firstCharInLine;//开始的索引
                    int endIndex = i == lineEnd ? end : lastCharInLine;//结束的索引

                    //开始字符左边x坐标
                    float xStart = layout.getPrimaryHorizontal(startIndex);//第一行从指定start 后面行从每行开头开始

                    float xDiff = layout.getPrimaryHorizontal(startIndex + 1) - xStart;//截止末尾需要偏移一个字的x坐标距离


                    //tips ：xDiff 是按开始索引位置的 1个字符所占的x 来计算的。 因为可能出现中英文混合情况。而1个汉字的x坐标占位宽度 大概是1个英文字母的 1.8倍
                    //如果下划线的开头 是英文、结尾是中文 。实际偏移量x1.8   反之 除以1.8

                    //tips2：英文单词 一个单词 一行显示不下时。 会强行换行，而下划线是按原本占位画的，所以下划线会突出一点，末尾是空格的可以-1个占位 不添加偏移量（因为英文单词强制换行了）

                    //----------处理中英文混排调整xDiff 偏移量
                    xDiff = getXDiff(startIndex, endIndex, xDiff);

                    float xEnd = layout.getSecondaryHorizontal(endIndex) + xDiff;//最后行从指定截止，其余每行末尾截止
                    float offsetY = baseline + mLineTopMargin + mStrokeWidth;
                    canvas.drawLine(xStart, offsetY, xEnd, offsetY, mPaint);
                }
            }
        }


        super.onDraw(canvas);

    }

    /**
     * 处理中英文混排调整xDiff 偏移量
     *
     * @param startIndex
     * @param endIndex
     * @param xDiff
     * @return
     */
    private float getXDiff(int startIndex, int endIndex, float xDiff) {
        String content = getText().toString();
        if (ChineseAndEnglish.isChinese(content.charAt(startIndex + 1))) {
            if (ChineseAndEnglish.isEnglish(String.valueOf(content.charAt(endIndex)))) {
                Log.v("xDiff", "xDiff-->" + xDiff);
                xDiff /= 1.8f;
                Log.w("xDiff", "xDiff-->" + xDiff);
            }
        }
        if (ChineseAndEnglish.isEnglish(String.valueOf(content.charAt(startIndex + 1)))) {
            if (ChineseAndEnglish.isChinese(content.charAt(endIndex))) {
                Log.v("xDiff", "xDiff-->" + xDiff);
                xDiff *= 1.8f;
                Log.w("xDiff", "xDiff-->" + xDiff);
            }
        }

        if (Character.isWhitespace(content.charAt(endIndex))) {
            xDiff = 0;
            Log.w("xDiff", "末尾是空格不添加偏移量xDiff");
        }
        return xDiff;
    }

    public int getUnderLineColor() {
        return mColor;
    }

    public void setUnderLineColor(int mColor) {
        this.mColor = mColor;
        invalidate();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom + (int) mLineTopMargin + (int) mStrokeWidth);
    }

    public float getUnderlineWidth() {
        return mStrokeWidth;
    }

    public void setUnderlineWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        invalidate();
    }

}
