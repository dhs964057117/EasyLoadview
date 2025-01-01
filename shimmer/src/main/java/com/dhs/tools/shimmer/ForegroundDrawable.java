/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.dhs.tools.shimmer;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ForegroundDrawable extends Drawable {

    private final Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Rect mDrawRect = new Rect();
    private RectF mRectF = null;
    private int foregroundColor = 0x2E000000;
    private int radius = 20;

    /**
     * The shape of the shimmer's highlight. By default LINEAR is used.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Shape.ROUND, Shape.OVAL})
    public @interface Shape {
        /**
         * Linear gives a ray reflection effect.
         */
        int ROUND = 0;
        /**
         * Radial gives a spotlight effect.
         */
        int OVAL = 1;
    }

    @Shape
    private int shape = Shape.ROUND;

    public ForegroundDrawable() {
        rectPaint.setAntiAlias(true);
    }

    public void setAttr(@NonNull TypedArray a) {
        if (a.hasValue(R.styleable.ShimmerView_shimmer_foreground_color)) {
            foregroundColor = a.getColor(R.styleable.ShimmerView_shimmer_foreground_color, foregroundColor);
        }
        if (a.hasValue(R.styleable.ShimmerView_shimmer_foreground_corner)) {
            radius = a.getInt(R.styleable.ShimmerView_shimmer_foreground_color, radius);
        }
        if (a.hasValue(R.styleable.ShimmerView_shimmer_foreground_shape)) {
            shape = a.getInt(R.styleable.ShimmerView_shimmer_foreground_color, Shape.ROUND);
        }
        initRectPaint();
    }

    private void initRectPaint() {
        rectPaint.setColor(foregroundColor);
    }

    @Override
    public void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        if (mRectF == null) {
            mDrawRect.set(bounds);
            mRectF = new RectF(mDrawRect);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (shape == Shape.OVAL) {
            canvas.drawArc(mRectF, 0, 360, true, rectPaint);
        } else {
            canvas.drawRoundRect(mRectF, radius, radius, rectPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // No-op, modify the Shimmer object you pass in instead
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        // No-op, modify the Shimmer object you pass in instead
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
