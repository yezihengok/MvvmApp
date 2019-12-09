package com.example.commlib.weight;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.commlib.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  @anthor yzh
 * 语音录制的动画效果
 *  基于 https://www.jianshu.com/p/6dd10a5adca8 修改
 */
public class LineWaveVoiceView extends View {
    private static final String DEFAULT_TEXT = " 请录音 ";
    private static final int LINE_WIDTH = 9;//默认矩形波纹的宽度
    private Paint paint = new Paint();
    private Runnable task;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private RectF rectRight = new RectF();//右边波纹矩形的数据，10个矩形复用一个rectF
    private RectF rectLeft = new RectF();//左边波纹矩形的数据
    private String text = DEFAULT_TEXT;
    private int updateSpeed;
    private int lineColor;
    private int textColor;
    private float lineWidth;
    private float textSize;


    public LineWaveVoiceView(Context context) {
        super(context);
    }

    public LineWaveVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineWaveVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, context);
        resetView(mWaveList, DEFAULT_WAVE_HEIGHT);
        task = new LineJitterTask();
    }

    private void initView(AttributeSet attrs, Context context) {
        //获取布局属性里的值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LineWaveVoiceView);
        lineColor = mTypedArray.getColor(R.styleable.LineWaveVoiceView_voiceLineColor, ContextCompat.getColor(context,R.color.ui_blue));
        lineWidth = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceLineWidth, LINE_WIDTH);
        textSize = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceTextSize, 42);
        textColor = mTypedArray.getColor(R.styleable.LineWaveVoiceView_voiceTextColor, ContextCompat.getColor(context,R.color.ui_gray));
        updateSpeed = mTypedArray.getColor(R.styleable.LineWaveVoiceView_updateSpeed, UPDATE_INTERVAL_TIME);
        mTypedArray.recycle();
    }


//    .获取该View的实际宽高的一半，然后设置矩形的四边，熟悉Android的view的绘制都知道，view的宽为right - left，
//    高度为bottom - top。所以让right比left多一个lineWidth即可让矩形的宽为lineWidth，
//    bottom比top多4lineWidth即可让高读为4lineWidth，并利用实际宽高的一半，把矩形绘制在view的中央。

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mWaveList==null){
            return;
        }
        //获取实际宽高的一半
        int widthCentre = getWidth() / 2;
        int heightCentre = getHeight() / 2;
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);
        canvas.drawText(text, widthCentre - textWidth / 2, heightCentre - (paint.ascent() + paint.descent()) / 2, paint);

        //设置颜色
        paint.setColor(lineColor);
        //填充内部
        paint.setStyle(Paint.Style.FILL);
        //设置抗锯齿
        paint.setAntiAlias(true);
        for (int i = 0; i < 9; i++) {
            rectRight.left = widthCentre + textWidth / 2 + (1 + 2 * i) * lineWidth;
            rectRight.top = heightCentre - lineWidth * mWaveList.get(i) / 2;
            rectRight.right = widthCentre + textWidth / 2 + (2 + 2 * i) * lineWidth;
            rectRight.bottom = heightCentre + lineWidth * mWaveList.get(i) / 2;

            //左边矩形
            rectLeft.left = widthCentre - textWidth / 2 - (2 + 2 * i) * lineWidth;
            rectLeft.top = heightCentre - mWaveList.get(i) * lineWidth / 2;
            rectLeft.right = widthCentre - textWidth / 2 - (1 + 2 * i) * lineWidth;
            rectLeft.bottom = heightCentre + mWaveList.get(i) * lineWidth / 2;

            canvas.drawRoundRect(rectRight, 6, 6, paint);
            canvas.drawRoundRect(rectLeft, 6, 6, paint);
        }
    }

    private static final int MIN_WAVE_HEIGHT = 2;//矩形线最小高
  //  private static final int MAX_WAVE_HEIGHT = 10;//矩形线最大高
    private static final int MAX_WAVE_HEIGHT = 3;//矩形线最大高
    private static final int[] DEFAULT_WAVE_HEIGHT = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    private static final int UPDATE_INTERVAL_TIME = 100;//100ms更新一次
    private LinkedList<Integer> mWaveList = new LinkedList<>();
    private float maxDb;

    private void resetView(List<Integer> list, int[] array) {
        list.clear();
        for (int anArray : array) {
            list.add(anArray);
        }
    }

    private synchronized void refreshElement() {
        Random random = new Random();
        maxDb = random.nextInt(5) + 1;
        int waveH = MIN_WAVE_HEIGHT + Math.round(maxDb * (MAX_WAVE_HEIGHT - MIN_WAVE_HEIGHT));
       // ALog.i("waveH===="+waveH);
        mWaveList.add(0, waveH);
        mWaveList.removeLast();
    }

    public boolean isStart = false;

    private class LineJitterTask implements Runnable {
        @Override
        public void run() {
            while (isStart) {
                refreshElement();
                try {
                    Thread.sleep(updateSpeed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }
    }

    public synchronized void startRecord() {
        isStart = true;
        executorService.execute(task);
    }

    public synchronized void stopRecord() {
        isStart = false;
        mWaveList.clear();
        resetView(mWaveList, DEFAULT_WAVE_HEIGHT);
        postInvalidate();
    }


    public synchronized void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public void setUpdateSpeed(int updateSpeed) {
        this.updateSpeed = updateSpeed;
    }
}