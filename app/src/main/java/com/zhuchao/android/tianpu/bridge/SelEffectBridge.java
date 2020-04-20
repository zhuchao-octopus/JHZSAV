package com.zhuchao.android.tianpu.bridge;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.zhuchao.android.tianpu.BuildConfig;
import com.zhuchao.android.tianpu.utils.MyApplication;
import com.zhuchao.android.tianpu.views.MainUpView;

public class SelEffectBridge extends BaseEffectBridge {

	private static final int DEFAULT_TRAN_DUR_ANIM = 300;
	private boolean isInDraw = false;
	private boolean mIsHide = false;
	private boolean mAnimEnabled = true;
	private boolean isDrawUpRect = true;
	private MainUpView mMainUpView;
	private Drawable mDrawableUpRect;
	private AnimatorSet mCurrentAnimatorSet;
	private View mFocusView;
	private RectF mUpPaddingRect = new RectF(0, 0, 0, 0);
    //private float mScaleX,mScaleY;
	@Override
	public void onInitBridge(MainUpView view) {
		view.setVisibility(View.GONE);
	}

	@Override
	public void setMainUpView(MainUpView view) {
		mMainUpView = view;
	}

	@Override
	public MainUpView getMainUpView() {
		return mMainUpView;
	}

	@Override
	public void setUpRectResource(int resId) {
		try {
			mDrawableUpRect = MyApplication.res().getDrawable(resId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setUpRectDrawable(Drawable upRectDrawable) {
		this.mDrawableUpRect = upRectDrawable;
	}

	@Override
	public Drawable getUpRectDrawable() {
		return mDrawableUpRect;
	}

	@Override
	public void setDrawUpRectPadding(Rect rect) {
		mUpPaddingRect.set(rect);
	}

	@Override
	public void setDrawUpRectPadding(RectF rect) {
		mUpPaddingRect.set(rect);
	}

	@Override
	public RectF getDrawUpRect() {
		return mUpPaddingRect;
	}

	@Override
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {

	}

	@Override
	public void onFocusView(View focusView, float scaleX, float scaleY) {
		mFocusView = focusView;
		//mScaleX = scaleX;
		//mScaleY = scaleY;
		if (focusView != null) {
			runTranslateAnimation(focusView,scaleX,scaleY);
		}
	}

	/**
	 * 需要绘制的东西.
	 */
	@Override
	public boolean onDrawMainUpView(Canvas canvas) {
		canvas.save();
		// 绘制最上层的边框.
		onDrawUpRect(canvas);
		canvas.restore();
		return true;
	}

	/**
	 * 绘制最上层的移动边框.
	 */
	public void onDrawUpRect(Canvas canvas) {
		Drawable drawableUp = getUpRectDrawable();
		if (drawableUp != null) {
			RectF paddingRect = getDrawUpRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			// 边框的绘制.
			drawableUp.getPadding(padding);
			//
			int left = (int) Math.rint(paddingRect.left);
			int right = (int) Math.rint(paddingRect.right);
			int bottom = (int) Math.rint(paddingRect.bottom);
			int top = (int) Math.rint(paddingRect.top);
			//
			drawableUp.setBounds(-padding.left - (left), -padding.top - (top),
					width + padding.right + (right), height + padding.bottom + (bottom));
			drawableUp.draw(canvas);
		}
	}

	public Rect findLocationWithView(View view) {
		ViewGroup root = (ViewGroup) getMainUpView().getParent();
		Rect rect = new Rect();
		root.offsetDescendantRectToMyCoords(view, rect);
		return rect;
	}

	public void flyWhiteBorder(final View focusView, View moveView,float scaleX,float scaleY) {
		int newWidth = 0;
		int newHeight = 0;
		int oldWidth = 0;
		int oldHeight = 0;

		int newX = 0;
		int newY = 0;

		if (focusView != null) {
			// 有一点偏差,需要进行四舍五入.
			newWidth = (int) Math.rint(focusView.getMeasuredWidth()*scaleX);
			newHeight = (int) Math.rint(focusView.getMeasuredHeight()*scaleY);
			oldWidth = moveView.getMeasuredWidth();
			oldHeight = moveView.getMeasuredHeight();
			Rect fromRect = findLocationWithView(moveView);
			Rect toRect = findLocationWithView(focusView);
			int x = toRect.left - fromRect.left;
			int y = toRect.top - fromRect.top;
			newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
			newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
		}

		// 取消之前的动画.
		if (mCurrentAnimatorSet != null)
			mCurrentAnimatorSet.cancel();

		ObjectAnimator transAnimatorX
				= ObjectAnimator.ofFloat(moveView, "translationX", newX);
		ObjectAnimator transAnimatorY
				= ObjectAnimator.ofFloat(moveView, "translationY", newY);
		ObjectAnimator scaleXAnimator
				= ObjectAnimator.ofInt(new ScaleView(moveView), "width", oldWidth,
				newWidth);
		ObjectAnimator scaleYAnimator
				= ObjectAnimator.ofInt(new ScaleView(moveView), "height", oldHeight,
				newHeight);

		AnimatorSet mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
		mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
		mAnimatorSet.setDuration(DEFAULT_TRAN_DUR_ANIM);
		mAnimatorSet.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
				if (mIsHide) {
					getMainUpView().setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = true;
				getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
				// XF add（先锋TV开发(404780246)修复)
				// BUG:5.0系统边框错位.
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					int newWidth = (int) (focusView.getMeasuredWidth() * scaleX);
					int newHeight = (int) (focusView.getMeasuredHeight() *scaleY);
					getMainUpView().getLayoutParams().width = newWidth;
					getMainUpView().getLayoutParams().height = newHeight;
					getMainUpView().requestLayout();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
			}
		});
		mCurrentAnimatorSet = mAnimatorSet;
		mAnimatorSet.start();
	}

	public void runTranslateAnimation(View toView,float x,float y) {
		flyWhiteBorder(toView, getMainUpView(),x,y);
	}

	/**
	 * 隐藏移动的边框.
	 */
	public void setVisibleWidget(boolean isHide) {
		this.mIsHide = isHide;
		getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
	}

	/**
	 * 用於放大的view
	 */
	public class ScaleView {
		private View view;
		private int width;
		private int height;

		public ScaleView(View view) {
			this.view = view;
		}

		public int getWidth() {
			return view.getLayoutParams().width;
		}

		public void setWidth(int width) {
			this.width = width;
			view.getLayoutParams().width = width;
			view.requestLayout();
		}

		public int getHeight() {
			return view.getLayoutParams().height;
		}

		public void setHeight(int height) {
			this.height = height;
			view.getLayoutParams().height = height;
			view.requestLayout();
		}
	}
}
