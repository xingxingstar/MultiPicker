package com.example.zhuwojia.multipicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * author：shixinxin on 2017/3/13
 * version：v1.0
 */

public class MyWheelView extends LinearLayout {

    private LinearLayout ll_title, ll_wheel_view;
    private List<String> titleList = new ArrayList<>();
    private OnWheelViewListener onWheelViewListener;
    private OnClickListener onCancleClickListener, onCommitListener;


    public interface OnClickListener {
        void onClickListener();
    }

    public void setOnCommitListener(OnClickListener onCommitListener) {
        this.onCommitListener = onCommitListener;
    }


    public void setOnCancleClickListener(OnClickListener onCancleClickListener) {
        this.onCancleClickListener = onCancleClickListener;
    }

    public static class OnWheelViewListener {
        public void onSelected(int titleIndex, String titleItem, int selectedIndex, String item) {
        }
    }


    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    public MyWheelView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pop_house_type, this);
        TextView bnt_cancle = (TextView) findViewById(R.id.bnt_cancle);
        TextView bnt_ok = (TextView) findViewById(R.id.bnt_ok);
        ll_wheel_view = (LinearLayout) findViewById(R.id.ll_wheel_view);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);


        bnt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancleClickListener != null) {
                    onCancleClickListener.onClickListener();
                }
            }
        });

        bnt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommitListener != null) {
                    onCommitListener.onClickListener();
                }
            }
        });
    }

    public MyWheelView(final Context context) {
        super(context);
    }

    //设置上方的title的值
    public void setTitleLayout(Context context, List<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.weight = 1;
            ll.gravity = Gravity.CENTER;
            textView.setLayoutParams(ll);
            textView.setText(titles.get(i));
            textView.setTextColor(getResources().getColor(R.color.sx_black));
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
            ll_title.addView(textView);
        }
        titleList.addAll(titles);
    }

    public void setTitleLayout(Context context, String[] titles) {
        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ll.weight = 1;
            textView.setLayoutParams(ll);
            textView.setText(titles[i]);
            textView.setTextColor(getResources().getColor(R.color.sx_black));
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
            ll_title.addView(textView);
        }
        titleList.addAll(Arrays.asList(titles));
    }

    //    设置滑动的值
    public void setWheelTitle(final Context context, List<String> wheelTitles) {
        if (titleList.size() > 0) {
            for (int i = 0; i < titleList.size(); i++) {
                WheelView view = new WheelView(context);
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ll.weight = 1;
                ll.gravity = Gravity.CENTER;
                view.setLayoutParams(ll);
                view.setOffset(1);
                view.setItems(wheelTitles);
                final int finalI = i;
                view.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        if (onWheelViewListener != null) {
                            onWheelViewListener.onSelected(finalI, titleList.get(finalI), selectedIndex, item);
                        }
                    }
                });
                ll_wheel_view.addView(view);
            }
        } else {
            throw new IllegalArgumentException("setTitleLayout未定义");
        }
    }

    public void setWheelTitle(final Context context, String[] wheelTitles) {
        if (titleList.size() > 0) {
            for (int i = 0; i < titleList.size(); i++) {
                WheelView view = new WheelView(context);
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ll.weight = 1;
                ll.gravity = Gravity.CENTER;
                view.setLayoutParams(ll);
                view.setOffset(1);
                view.setItems(Arrays.asList(wheelTitles));
                final int finalI = i;
                view.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        if (onWheelViewListener != null) {
                            onWheelViewListener.onSelected(finalI, titleList.get(finalI), selectedIndex, item);
                        }
                    }
                });
                ll_wheel_view.addView(view);
            }
        } else {
            throw new IllegalArgumentException("setTitleLayout未定义");
        }
    }


    static class WheelView extends ScrollView {
        private Context context;
        private LinearLayout views;

        public final String TAG = WheelView.class.getSimpleName();
        public static final int OFF_SET_DEFAULT = 1;
        int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）
        List<String> items;
        int displayItemCount; // 每页显示的数量
        int selectedIndex = 1;
        int initialY;//初始Y值
        Runnable scrollerTask;
        int newCheck = 50;
        int itemHeight = 0;

        interface OnWheelViewListener {
            void onSelected(int selectedIndex, String item);
        }

        public WheelView(Context context) {
            super(context);
            init(context);
        }

        public WheelView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public WheelView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init(context);
        }

        private void init(Context context) {
            this.context = context;

//        scrollView = ((ScrollView)this.getParent());
//        Log.d(TAG, "scrollview: " + scrollView);
            Log.d(TAG, "parent: " + this.getParent());
//        this.setOrientation(VERTICAL);
            this.setVerticalScrollBarEnabled(false);//隐藏滑动条
            views = new LinearLayout(context);
            views.setOrientation(LinearLayout.VERTICAL);
            views.setGravity(Gravity.CENTER);
            this.addView(views);

            scrollerTask = new Runnable() {

                public void run() {

                    int newY = getScrollY();
                    if (initialY - newY == 0) { // stopped
                        final int remainder = initialY % itemHeight;
                        final int divided = initialY / itemHeight;
//                    Log.d(TAG, "initialY: " + initialY);
//                    Log.d(TAG, "remainder: " + remainder + ", divided: " + divided);
                        if (remainder == 0) {
                            selectedIndex = divided + offset;

                            onSeletedCallBack();
                        } else {
                            if (remainder > itemHeight / 2) {
                                WheelView.this.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                        selectedIndex = divided + offset + 1;
                                        onSeletedCallBack();
                                    }
                                });
                            } else {
                                WheelView.this.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        WheelView.this.smoothScrollTo(0, initialY - remainder);
                                        selectedIndex = divided + offset;
                                        onSeletedCallBack();
                                    }
                                });
                            }


                        }


                    } else {
                        initialY = getScrollY();
                        WheelView.this.postDelayed(scrollerTask, newCheck);
                    }
                }
            };


        }


        private List<String> getItems() {
            return items;
        }

        public void setItems(List<String> list) {
            if (null == items) {
                items = new ArrayList<String>();
            }
            items.clear();
            items.addAll(list);

            // 前面和后面补全
            for (int i = 0; i < offset; i++) {
                items.add(0, "");
                items.add("");
            }

            initData();

        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }


        public void startScrollerTask() {

            initialY = getScrollY();
            this.postDelayed(scrollerTask, newCheck);
        }

        private void initData() {
            displayItemCount = offset * 2 + 1;

            for (String item : items) {
                views.addView(createView(item));
            }

            refreshItemView(0);
        }


        private TextView createView(String item) {
            TextView tv = new TextView(context);
            tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setSingleLine(true);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
            tv.setText(item);
            tv.setGravity(Gravity.CENTER);
            int padding = dip2px(15);
            tv.setPadding(padding, padding, padding, padding);
            if (0 == itemHeight) {
                itemHeight = getViewMeasuredHeight(tv);
                Log.d(TAG, "itemHeight: " + itemHeight);
                views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
                this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
            }
            return tv;
        }


        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);

//        Log.d(TAG, "l: " + l + ", t: " + t + ", oldl: " + oldl + ", oldt: " + oldt);

//        try {
//            Field field = ScrollView.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            OverScroller mScroller = (OverScroller) field.get(this);
//
//
//            if(mScroller.isFinished()){
//                Log.d(TAG, "isFinished...");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


            refreshItemView(t);

            if (t > oldt) {
//            Log.d(TAG, "向下滚动");
                scrollDirection = SCROLL_DIRECTION_DOWN;
            } else {
//            Log.d(TAG, "向上滚动");
                scrollDirection = SCROLL_DIRECTION_UP;

            }


        }

        private void refreshItemView(int y) {
            int position = y / itemHeight + offset;
            int remainder = y % itemHeight;
            int divided = y / itemHeight;

            if (remainder == 0) {
                position = divided + offset;
            } else {
                if (remainder > itemHeight / 2) {
                    position = divided + offset + 1;
                }

//            if(remainder > itemHeight / 2){
//                if(scrollDirection == SCROLL_DIRECTION_DOWN){
//                    position = divided + offset;
//                    Log.d(TAG, ">down...position: " + position);
//                }else if(scrollDirection == SCROLL_DIRECTION_UP){
//                    position = divided + offset + 1;
//                    Log.d(TAG, ">up...position: " + position);
//                }
//            }else{
////                position = y / itemHeight + offset;
//                if(scrollDirection == SCROLL_DIRECTION_DOWN){
//                    position = divided + offset;
//                    Log.d(TAG, "<down...position: " + position);
//                }else if(scrollDirection == SCROLL_DIRECTION_UP){
//                    position = divided + offset + 1;
//                    Log.d(TAG, "<up...position: " + position);
//                }
//            }
//        }

//        if(scrollDirection == SCROLL_DIRECTION_DOWN){
//            position = divided + offset;
//        }else if(scrollDirection == SCROLL_DIRECTION_UP){
//            position = divided + offset + 1;
            }

            int childSize = views.getChildCount();
            for (int i = 0; i < childSize; i++) {
                TextView itemView = (TextView) views.getChildAt(i);
                if (null == itemView) {
                    return;
                }
                if (position == i) {
                    itemView.setTextColor(Color.parseColor("#0288ce"));
                } else {
                    itemView.setTextColor(Color.parseColor("#bbbbbb"));
                }
            }
        }

        /**
         * 获取选中区域的边界
         */
        int[] selectedAreaBorder;

        private int[] obtainSelectedAreaBorder() {
            if (null == selectedAreaBorder) {
                selectedAreaBorder = new int[2];
                selectedAreaBorder[0] = itemHeight * offset;
                selectedAreaBorder[1] = itemHeight * (offset + 1);
            }
            return selectedAreaBorder;
        }


        private int scrollDirection = -1;
        private static final int SCROLL_DIRECTION_UP = 0;
        private static final int SCROLL_DIRECTION_DOWN = 1;

        Paint paint;
        int viewWidth;

        @Override
        public void setBackgroundDrawable(Drawable background) {

            if (viewWidth == 0) {
                viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
                Log.d(TAG, "viewWidth: " + viewWidth);
            }

            if (null == paint) {
                paint = new Paint();
                paint.setColor(Color.parseColor("#83cde6"));
                paint.setStrokeWidth(dip2px(1f));
            }

            background = new Drawable() {
                @Override
                public void draw(Canvas canvas) {
                    canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6, obtainSelectedAreaBorder()[0], paint);
                    canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6, obtainSelectedAreaBorder()[1], paint);
                }

                @Override
                public void setAlpha(int alpha) {

                }

                @Override
                public void setColorFilter(ColorFilter cf) {

                }

                @Override
                public int getOpacity() {
                    return 0;
                }
            };


            super.setBackgroundDrawable(background);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
            viewWidth = w;
            setBackgroundDrawable(null);
        }

        /**
         * 选中回调
         */
        private void onSeletedCallBack() {
            if (null != onWheelViewListener) {
                onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
            }

        }

        public void setSeletion(int position) {
            final int p = position;
            selectedIndex = p + offset;
            this.post(new Runnable() {
                @Override
                public void run() {
                    WheelView.this.smoothScrollTo(0, p * itemHeight);
                }
            });

        }

        public String getSeletedItem() {
            return items.get(selectedIndex);
        }

        public int getSeletedIndex() {
            return selectedIndex - offset;
        }


        @Override
        public void fling(int velocityY) {
            super.fling(velocityY / 3);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {

                startScrollerTask();
            }
            return super.onTouchEvent(ev);
        }

        private OnWheelViewListener onWheelViewListener;

        public OnWheelViewListener getOnWheelViewListener() {
            return onWheelViewListener;
        }

        public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
            this.onWheelViewListener = onWheelViewListener;
        }

        private int dip2px(float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        private int getViewMeasuredHeight(View view) {
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
            view.measure(width, expandSpec);
            return view.getMeasuredHeight();
        }

    }
}

