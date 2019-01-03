package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by isabe on 5/13/2018.
 */

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
    private final ItemClickListener itemClickListener;
    private final GestureDetector gestureDetector;

    public static interface ItemClickListener{
        void onClick(View view, int position);
    }

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                return true;

            }

            @Override
            public void onLongPress(MotionEvent e) {
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if(child !=null && itemClickListener !=null && gestureDetector.onTouchEvent(e)){
            itemClickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
