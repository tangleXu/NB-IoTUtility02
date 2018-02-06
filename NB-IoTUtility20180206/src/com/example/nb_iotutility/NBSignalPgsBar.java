package com.example.nb_iotutility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class NBSignalPgsBar extends View {
	private int mBarWidth,mBarHeight;
	private float mPercent;
	private String mCaption;
	
	public NBSignalPgsBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		mPercent = (float) 50.0;
	}
	
	public NBSignalPgsBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPercent = (float) 50.0;
		mCaption = Float.toString(mPercent);
	}
	
	public void setPercent(float pcnt)
	{
		
		mPercent = pcnt;
		//mCaption = Float.toString(mPercent);
		this.invalidate();
	}
	
	public void setText(String ss){
		mCaption = ss;
		this.invalidate();
	}
	
	//private Color mBgColor;
	
//	public NBSignalPgsBar(Context context) {
//		super(context);
		// TODO Auto-generated constructor stub
		//mBgColor.rgb(51, 51, 51);
//		mPercent = (float) 50.0;
		//mCaption=Float.toString(mPercent);
//	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//canvas.drawColor(Color.rgb(33, 169, 63));
		canvas.drawColor(Color.rgb(81, 81, 81));
		
		Paint paint = new Paint();
		if(mPercent>0 && mPercent<25)
			paint.setColor(Color.RED); 
		else if(mPercent>=25 && mPercent<=40)
			paint.setColor(Color.YELLOW); 
		else if(mPercent>40)
			paint.setColor(Color.rgb(33, 169, 63));
		else
			paint.setColor(Color.rgb(33, 169, 63));  
		
        paint.setStrokeJoin(Paint.Join.ROUND);  
        paint.setStrokeCap(Paint.Cap.ROUND); 
        paint.setStrokeWidth(3);
        
		canvas.drawRect(new RectF(0,0,(float) (mPercent*mBarWidth/100.0),mBarHeight), paint);
		String testString = mCaption;  
        Paint textPaint = new Paint();  
        textPaint.setColor(Color.rgb(222, 222, 222));
        textPaint.setStrokeWidth(3);  
        textPaint.setTextSize(20);
        
        //textPaint.setStyle(Paint.Style.FILL);  
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center  
        textPaint.setTextAlign(Paint.Align.CENTER);  
  
        Rect bounds = new Rect();  
        textPaint.getTextBounds(testString, 0, testString.length(), bounds);  
        FontMetricsInt fontMetrics = textPaint.getFontMetricsInt(); 

        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  
        
		canvas.drawText(testString,  mBarWidth/ 2 ,baseline, textPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mBarWidth = MeasureSpec.getSize(widthMeasureSpec);
		mBarHeight = MeasureSpec.getSize(heightMeasureSpec);
	}

}
