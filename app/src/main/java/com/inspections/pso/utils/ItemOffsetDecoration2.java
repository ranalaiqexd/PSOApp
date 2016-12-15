package com.inspections.pso.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mobiweb on 31/5/16.
 */
public class ItemOffsetDecoration2 extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public ItemOffsetDecoration2(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemOffsetDecoration2(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, 0);
    }
}
