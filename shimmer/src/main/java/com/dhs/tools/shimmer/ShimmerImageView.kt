package com.dhs.tools.shimmer

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View


/**
 * FileName: ShimmerTextView
 * Author: haosen
 * Date: 2024/12/31 0:50
 * Description:
 **/
class ShimmerImageView : androidx.appcompat.widget.AppCompatImageView {
    private val mContentPaint = Paint()
    private val mShimmerDrawable: ShimmerDrawable = ShimmerDrawable()
    private val mForegroundDrawable: ForegroundDrawable = ForegroundDrawable()

    /** Return whether the shimmer drawable is visible.  */
    var isShimmerVisible: Boolean = true
    var isLoading: Boolean = true
    private var mStoppedShimmerBecauseVisibility = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        setWillNotDraw(false)
        mShimmerDrawable.callback = this

        if (attrs == null) {
            setShimmer(Shimmer.AlphaHighlightBuilder().build())
            return
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.ShimmerView, 0, 0)
        try {
            val shimmerBuilder =
                if (a.hasValue(R.styleable.ShimmerView_shimmer_colored)
                    && a.getBoolean(R.styleable.ShimmerView_shimmer_colored, false)
                ) Shimmer.ColorHighlightBuilder()
                else Shimmer.AlphaHighlightBuilder()
            setShimmer(shimmerBuilder.consumeAttributes(a).build())
            setForeground(a)
        } finally {
            a.recycle()
        }
    }

    fun setShimmer(shimmer: Shimmer?): ShimmerImageView {
        mShimmerDrawable.setShimmer(shimmer)
        if (shimmer != null && shimmer.clipToChildren) {
            setLayerType(LAYER_TYPE_HARDWARE, mContentPaint)
        } else {
            setLayerType(LAYER_TYPE_NONE, null)
        }

        return this
    }

    private fun setForeground(a: TypedArray) {
        mForegroundDrawable.setAttr(a)
    }

    val shimmer: Shimmer?
        get() = mShimmerDrawable.shimmer

    /** Starts the shimmer animation.  */
    fun startShimmer() {
        if (isAttachedToWindow) {
            mShimmerDrawable.startShimmer()
        }
    }

    /** Stops the shimmer animation.  */
    fun stopShimmer() {
        mStoppedShimmerBecauseVisibility = false
        mShimmerDrawable.stopShimmer()
    }

    val isShimmerStarted: Boolean
        /** Return whether the shimmer animation has been started.  */
        get() = mShimmerDrawable.isShimmerStarted

    /**
     * Sets the ShimmerDrawable to be visible.
     *
     * @param startShimmer Whether to start the shimmer again.
     */
    fun showShimmer(startShimmer: Boolean) {
        isShimmerVisible = true
        if (startShimmer) {
            startShimmer()
        }
        invalidate()
    }

    /** Sets the ShimmerDrawable to be invisible, stopping it in the process.  */
    fun hideShimmer() {
        stopShimmer()
        isShimmerVisible = false
        invalidate()
    }

    fun showLoading() {
        showShimmer(true)
        isLoading = true
        invalidate()
    }

    fun hideLoading() {
        hideShimmer()
        isLoading = false
        invalidate()
    }

    val isShimmerRunning: Boolean
        get() = mShimmerDrawable.isShimmerRunning

    public override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = width
        val height = height
        if (isLoading) {
            mForegroundDrawable.setBounds(0, 0, width, height)
        }
        mShimmerDrawable.setBounds(0, 0, width, height)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        // View's constructor directly invokes this method, in which case no fields on
        // this class have been fully initialized yet.
        if (mShimmerDrawable == null) {
            return
        }
        if (visibility != VISIBLE) {
            // GONE or INVISIBLE
            if (isShimmerStarted) {
                stopShimmer()
                mStoppedShimmerBecauseVisibility = true
            }
        } else if (mStoppedShimmerBecauseVisibility) {
            mShimmerDrawable.maybeStartShimmer()
            mStoppedShimmerBecauseVisibility = false
        }
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mShimmerDrawable.maybeStartShimmer()
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopShimmer()
    }

    override fun onDraw(canvas: Canvas) {
        if (!isLoading) {
            Log.d("ShimmerImageView", "onDraw")
            super.onDraw(canvas)
        }
    }

    public override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isShimmerVisible) {
            if (isLoading) {
                mForegroundDrawable.draw(canvas)
            }
            Log.d("ShimmerImageView", "dispatchDraw")
            mShimmerDrawable.draw(canvas)
        }
    }

    //    override fun setImageResource(resId: Int) {
//        super.setImageResource(resId)
//        isShimmerVisible = resId == 0
//    }
//
//    override fun setImageURI(uri: Uri?) {
//        super.setImageURI(uri)
//        isShimmerVisible = uri == null
//    }
//
//    override fun setImageBitmap(bm: Bitmap?) {
//        super.setImageBitmap(bm)
//        isShimmerVisible = bm == null
//    }
//
//    override fun setImageDrawable(drawable: Drawable?) {
//        super.setImageDrawable(drawable)
//        isShimmerVisible = drawable == null
//    }
//
//    override fun setImageIcon(icon: Icon?) {
//        super.setImageIcon(icon)
//        isShimmerVisible = icon == null
//    }
//
    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === mShimmerDrawable
    }

    fun setStaticAnimationProgress(value: Float) {
        mShimmerDrawable.setStaticAnimationProgress(value)
    }

    fun clearStaticAnimationProgress() {
        mShimmerDrawable.clearStaticAnimationProgress()
    }
}