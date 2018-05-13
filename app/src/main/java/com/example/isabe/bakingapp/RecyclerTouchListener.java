package com.example.isabe.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.isabe.bakingapp.adapters.RecipeListAdapter;

/**
 * Created by isabe on 5/13/2018.
 */

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
    private final RecipeListAdapter.RecipeOnClickListener recipesOnClickListener;
    private GestureDetector gestureDetector;


    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecipeOnClickListener clickListener) {
        this.recipesOnClickListener = (RecipeListAdapter.RecipeOnClickListener) clickListener;
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
        if(child !=null && recipesOnClickListener !=null && gestureDetector.onTouchEvent(e)){
            recipesOnClickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface RecipeOnClickListener {
        void onClick(View view, int itemId);
    }


}
