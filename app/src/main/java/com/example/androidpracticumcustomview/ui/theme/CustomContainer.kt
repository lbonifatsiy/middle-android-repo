package com.example.androidpracticumcustomview.ui.theme

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Задание:
 * Реализуйте необходимые компоненты;
 * Создайте проверку что дочерних элементов не более 2-х;
 * Предусмотрите обработку ошибок рендера дочерних элементов.
 * Задание по желанию:
 * Предусмотрите параметризацию длительности анимации.
 */

class CustomContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    init {
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount > 2) {
            throw IllegalStateException(EXCEPTION_MESSAGE)
        }

        var maxChildWidth = 0
        var totalHeight = 0
        var maxWidth = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
                maxChildWidth = maxOf(maxChildWidth, child.measuredWidth)
                totalHeight += child.measuredHeight
                maxWidth = maxOf(maxWidth, child.measuredWidth)
            }
        }

        maxWidth += paddingLeft + paddingRight
        totalHeight += paddingTop + paddingBottom

        setMeasuredDimension(
            resolveSize(maxWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount > 2) {
            throw IllegalStateException(EXCEPTION_MESSAGE)
        }

        val parentWidth = right - left
        val parentHeight = bottom - top
        val parentCenterY = parentHeight / 2f

        // First child
        if (childCount >= 1) {
            val firstChild = getChildAt(0)
            if (firstChild.visibility != GONE) {
                val childWidth = firstChild.measuredWidth
                val childHeight = firstChild.measuredHeight

                val left = paddingLeft + (parentWidth - paddingLeft - paddingRight - childWidth) / 2
                val top = paddingLeft

                firstChild.layout(left, top, left + childWidth, top + childHeight)

                if (firstChild.tag != TAG_VALUE) {
                    firstChild.animateChildFromCenter(parentCenterY)
                    firstChild.tag = TAG_VALUE
                }
            }
        }

        // Second child
        if (childCount == 2) {
            val secondChild = getChildAt(1)
            if (secondChild.visibility != GONE) {
                val childWidth = secondChild.measuredWidth
                val childHeight = secondChild.measuredHeight

                val left = paddingLeft + (parentWidth - paddingLeft - paddingRight - childWidth) / 2
                val top = parentHeight - paddingBottom - childHeight

                secondChild.layout(left, top, left + childWidth, top + childHeight)

                if (secondChild.tag != TAG_VALUE) {
                    secondChild.animateChildFromCenter(parentCenterY)
                    secondChild.tag = TAG_VALUE
                }
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(layoutParams: LayoutParams?): Boolean {
        return layoutParams is MarginLayoutParams
    }

    override fun addView(child: View) {
        if (childCount >= 2) {
            throw IllegalStateException(EXCEPTION_MESSAGE)
        }
        child.tag = null
        super.addView(child)
    }

    private fun View.animateChildFromCenter(startY: Float) {
        translationY = startY - height / 2f - top
        alpha = 0f

        val animY = ObjectAnimator.ofFloat(this, "translationY", 0f)
            .apply { duration = TRANSLATION_ANIM_DURATION_MILLIS }
        val animAlpha = ObjectAnimator.ofFloat(this, "alpha", 1f)
            .apply { duration = ALPHA_ANIM_DURATION_MILLIS }
        animY.start()
        animAlpha.start()
    }

    private companion object {
        const val ALPHA_ANIM_DURATION_MILLIS = 2000L
        const val TRANSLATION_ANIM_DURATION_MILLIS = 5000L
        const val EXCEPTION_MESSAGE = "CustomContainer can only contain two child elements!"
        const val TAG_VALUE = "animated"
    }
}