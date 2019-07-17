package com.oa.wanyu.myutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2019/6/5.
 */

public class MonthView extends View {
    private List<Integer> YDataList = new ArrayList<>();//y轴数据-==楼房销量
    private List<String> XDataList = new ArrayList<>();//x轴数据-==上半年月份或者下半年月份
    private List<Integer> lineDataList = new ArrayList<>();//折线上的数据
    private final String xyColor = "#cccccc";//x,y轴文字颜色
    private final String lineColor = "#466afa";//折线和折线上的数字颜色

    private Paint mXYPaint;//X，y轴数据画笔
    private Paint mLinePaint;//折线画笔
    private Paint mCirclePaint;//圆圈画笔
    private Paint mLineDataPaint;//折线上数据画笔

    private float YEndPoint, XEndPoint;//y轴终点坐标(y轴高度),z轴终点坐标(z轴高度)
    private float XScale;//x轴刻度
    private float YScale;//y轴刻度
    private float YEachScale;//y轴刻度
    private float mSmallCircleRadius;
    private Context mContext;
    private DisplayMetrics mDisplayMetrics;

    public MonthView(Context context) {
        super(context);
        this.mContext = context;
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void setData(List<Integer> ydataList, List<String> xdataList, List<Integer> lineDataList) {
        this.YDataList = ydataList;
        this.XDataList = xdataList;
        this.lineDataList = lineDataList;
        for (int i=0;i<ydataList.size();i++){
            Log.e("y轴数据",ydataList.get(i)+"");
        }
        for (int i=0;i<xdataList.size();i++){
            Log.e("x轴数据",xdataList.get(i)+"");
        }
        for (int i=0;i<lineDataList.size();i++){
            Log.e("折线数据",lineDataList.get(i)+"");
        }
        initPaint();
    }

    private void initPaint() {

        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mSmallCircleRadius = dip2px(2.5f);//圆圈半径

//        YEndPoint = getHeight();
//        XEndPoint = getWidth();
//
//        Log.e("YEndPoint=",YEndPoint+"");
//        Log.e("XEndPoint=",XEndPoint+"");
//        YScale = YEndPoint / 10.f;
//        YEachScale = YScale / 10;
//        XScale = XEndPoint / 6.0f;
//        Log.e("YScale=",YScale+"");
//        Log.e("YEachScale=",YEachScale+"");
//        Log.e("XScale=",XScale+"");

        mXYPaint = new Paint();
        mXYPaint.setColor(Color.parseColor(xyColor));
        mXYPaint.setAntiAlias(true);
        mXYPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mXYPaint.setTextSize(dip2px(10));
        mXYPaint.setStrokeWidth(dip2px(0.3f));
        mXYPaint.setTextAlign(Paint.Align.CENTER);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor(lineColor));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(dip2px(2.0f));

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor(lineColor));
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(dip2px(2.0f));

        mLineDataPaint = new Paint();
        mLineDataPaint.setColor(Color.parseColor(lineColor));
        mLineDataPaint.setAntiAlias(true);
        mLineDataPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLineDataPaint.setTextSize(dip2px(10));
        mLineDataPaint.setStrokeWidth(dip2px(0.3f));
        mLineDataPaint.setTextAlign(Paint.Align.CENTER);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        YEndPoint = getHeight();
        XEndPoint = getWidth();

        Log.e("YEndPoint=",YEndPoint+"");
        Log.e("XEndPoint=",XEndPoint+"");
        YScale = YEndPoint / 10.f;
        YEachScale = YScale / 10;
        XScale = XEndPoint / 6.0f;
        Log.e("YScale=",YScale+"");
        Log.e("YEachScale=",YEachScale+"");
        Log.e("XScale=",XScale+"");

        // y轴刻度

        for (int i = 0; i < YDataList.size(); i++) {

            try {
                canvas.drawText(YDataList.get(i) + "", dip2px(20), YEndPoint - (2 + i) * YScale - (mXYPaint.ascent() + mXYPaint.descent() / 2), mXYPaint);

            } catch (Exception e) {

            }
        }


        //x轴刻度
        for (int i = 0; i < XDataList.size(); i++) {
            canvas.drawText(XDataList.get(i), XScale + XScale * i, YEndPoint - YScale, mXYPaint);
        }


        for (int i = 0; i < lineDataList.size(); i++) {
            canvas.drawCircle(XScale + XScale * i, Ycode(lineDataList.get(i)), mSmallCircleRadius, mLineDataPaint);

            try {
                canvas.drawLine(XScale + XScale * i, Ycode(lineDataList.get(i)), XScale + XScale * (i + 1), Ycode(lineDataList.get(i + 1)), mLineDataPaint);

            } catch (Exception e) {

            }

        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue
     * @return
     */
    public float dip2px(float dpValue) {
        float scale = mDisplayMetrics.density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * 每个圆圈的纵坐标
     *
     * @param a 集合中获取的楼房数量的多少
     * @return
     */
    private float Ycode(int a) {

        float e = YEndPoint - a * YEachScale;
        return e;
    }
}
