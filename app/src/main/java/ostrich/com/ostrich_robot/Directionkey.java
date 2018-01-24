package ostrich.com.ostrich_robot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by NeoprintPC on 2017/12/20.
 */

public class Directionkey extends View {
    private Paint outerPaint,innerPaint;
    private int width,height;
    private int outerRadius;//外圆半径
    private int innerRadius;//内圆半径
    private int innerX,innerY;

    public Directionkey(Context context){
        super(context);
    }
    public Directionkey(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        outerPaint = new Paint();
        outerPaint.setColor(Color.GRAY);
        outerPaint.setAntiAlias(true);
        outerPaint.setStrokeWidth(4);
        innerPaint = new Paint();
        innerPaint.setColor(Color.RED);
        innerPaint.setAntiAlias(true);
        innerPaint.setStrokeWidth(4);
    }
    public Directionkey(Context context,@Nullable AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        outerRadius = Math.min(width,height)/3;
        canvas.drawCircle(width/2,height/2,outerRadius,outerPaint);
        innerRadius = Math.min(width,height)/9;
        Log.i("DirectionKey", "width=" + width + "...height=" + height + "...outerRadius=" + outerRadius + "...innerRadius=" + innerRadius);
        if(innerX==0){
            innerX = width/2;
        }
        if(innerY == 0){
            innerY = height/2;
        }
        Log.i("DirectionKey", "innerX=" + innerX + "...innerY=" + innerY);
        canvas.drawCircle(innerX,innerY,innerRadius,innerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                //计算内圆可以滚动的最大半径
                int radius = outerRadius;
                //内圆将要超出外圆左边界
                if(x < innerRadius){
                    //计算手指位置距离中心的点距离
                    int r = (int) Math.sqrt((x-width/2)*(x-width/2)+(y-height/2)*(y-height/2));
                    //计算调整后的坐标距离中心点的宽度
                    int w = (int)(Math.abs(x-width/2)*radius/r);
                    int h = (int)(Math.abs(y-height/2)*radius/r);
                    //换算调整后的坐标，使得两圆是内切关系
                    //x = innerRadius;
                    x = width/2-w;
                    if(y < height/2){
                        y = height/2 - h;
                    }else{
                        y = height/2 + h;
                    }
                }
                //内圆将要超出外圆右边界
                if(x > width - innerRadius){
                    int r = (int)(Math.sqrt((x-width/2)*(x-width/2)+(y-height/2)*(y-height/2)));
                    int w = (int)(Math.abs(x - width/2)*radius/r);
                    int h = (int)(Math.abs(y - height/2)*radius/r);
                    x = width/2 + w;
                    if(y < height/2){
                        y = height/2 - h;
                    }else {
                        y = height/2 + h;
                    }
                }
                //内圆将要超出外圆上边界
                if(y < innerRadius){
                    int r = (int)Math.sqrt((x-width/2)*(x-width/2)+(y-height/2)*(y-height/2));
                    int h = (int)(Math.abs(y-height/2)*radius/r);
                    int w = (int)(Math.abs(x-width/2)*radius/r);
                    y = height/2 - h;
                    if(x < width/2){
                        x = width/2 - w;
                    }else{
                        x = width/2 + w;
                    }
                }
                //内圆将要超出下边界界面
                if(y > height - innerRadius){
                    int r = (int)Math.sqrt((x-width/2)*(x-width/2)+(y-height/2)*(y-height/2));
                    int h = (int)(Math.abs(y - height/2)*radius/r);
                    int w = (int)(Math.abs(x-width/2)*radius/r);
                    y = height/2 + h;
                    if(x < width/2){
                        x = width/2 - w;
                    }else{
                        x = width/2 + w;
                    }
                }
                innerX = (int)x;
                innerY = (int)y;
                invalidate();//重绘
                break;
            case MotionEvent.ACTION_UP:
                Log.i("DirectionKey", "innerX=" + innerX + "...innerY=" + innerY);
                innerX = 0;
                innerY = 0;
                invalidate();
                break;
        }
        return true;
    }
}
