package com.screeninteraction.android.alphaprogressexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ProgressView extends LinearLayout {
	
	/**
	 * Bitmap for the alpha mask defining the transparent area
	 */
	private Bitmap mAlphaMaskBitmap;
	/**
	 * The bitmap to draw the result on
	 */
	private Bitmap mCanvasBitmap;
	/**
	 * The rotating view
	 */
	private ImageView mRotatingImageView;
	/**
	 * Paint used when drawing
	 */
	private Paint mPaint;
	/**
	 * Canvas to draw result on
	 */
	private Canvas mCanvas;
	
	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public ProgressView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		
		mRotatingImageView = new ImageView(getContext());
		mRotatingImageView.setImageResource(R.drawable.loading_bkg);
		mRotatingImageView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		addView(mRotatingImageView);
		
		mAlphaMaskBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.loading_mask)).getBitmap();
		
		mPaint = new Paint();

		// Mode.DST_OUT = Only the part of the destination image that doesn't overlap the source image will be drawn. 
		// This is what makes the area transparent.
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT)); 
		mPaint.setFilterBitmap(false);
		
		
		// Setup canvas to draw result on.
		mCanvasBitmap = Bitmap.createBitmap(mAlphaMaskBitmap.getWidth(), mAlphaMaskBitmap.getHeight(), Bitmap.Config.ARGB_8888);
	    mCanvas = new Canvas(mCanvasBitmap);
		
		setWillNotDraw(false);
		
		mRotatingImageView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.loading));
	}
	
	@Override
	public void draw(Canvas canvas) {
		
		// Clear canvas by drawing a transparent layer
		// Mode.CLEAR = Nothing will be drawn in the combined image.
		mCanvas.drawColor(0, PorterDuff.Mode.CLEAR); 
		
		// Draw on the resulting canvas 
		super.draw(mCanvas);
		
		// Draw alpha mask on alpha canvas
		mCanvas.drawBitmap(mAlphaMaskBitmap, getPaddingLeft(), getPaddingTop(), mPaint);
		
		// Draw rotating image on view canvas
		canvas.drawBitmap(mCanvasBitmap, 0, 0, null);

	}

}
