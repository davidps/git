package com.example.android.lunarlander;

import config.Configurator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Snake {

	private SnakeSegment snakeSegment;
	// VX and VY are in pixels/ms
	private static final float VX = 0.08f;
	private static final float VY = 0.08f;
	private static Bitmap mSnakeBitMap;
	private static Rect mRect;
	private Canvas snakeCanvas;
	private float vx;
	private float vy;
	private float x;
	private float y;
	private long previousTime;

	public Snake(Context context, Rect rect) {

		Resources res = context.getResources();
		// cache handles to our key sprites & other drawables
		// mSegment = context.getResources().getDrawable(R.drawable.snake);

//		mRect = Configurator.getRect();
		mRect = rect;
		mSnakeBitMap = Bitmap.createBitmap(mRect.width(), mRect.height(),
				Config.ARGB_8888);

		mSnakeBitMap.eraseColor(Color.BLACK);
		x = 100f;
		y = 100f;
		snakeCanvas = new Canvas(mSnakeBitMap);
		snakeSegment = new SnakeSegment(context, new Point((int) x, (int) y));

		vx = 0;
		vy = -VY;
	}

	public static Bitmap RotateBitmap(Bitmap source, float angle) {
		Bitmap targetBitmap = Bitmap.createBitmap(source.getWidth(),
				source.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(targetBitmap);
		Matrix matrix = new Matrix();
		matrix.setRotate(angle, source.getWidth() / 2, source.getHeight() / 2);
		canvas.drawBitmap(source, matrix, new Paint());
		return targetBitmap;
	}

	public void turnLeft() {
		if (vy != 0f) {
			vx = VY;
			vy = 0f;
		} else {
			vy = -VX;
			vx = 0f;
		}
		RotateBitmap(mSnakeBitMap, -90.f);

	}

	public void turnRight() {
		if (vy != 0f) {
			vx = -VY;
			vy = 0f;
		} else {
			vy = VX;
			vx = 0f;
		}
		RotateBitmap(mSnakeBitMap, 90.f);
	}

	public void initPreviousTime() {
		previousTime = System.currentTimeMillis();
	}

	public void updatePosition() {

		long currentTime;
		currentTime = System.currentTimeMillis();
		long incTime = currentTime - previousTime;
		x += vx * incTime;
		y += vy * incTime;
		previousTime = currentTime;
		if (x >= Configurator.screenWidthInPixels / 2 || x <= 0) {
			vx = -vx;
		}
		if (y >= Configurator.screenHeightInPixels / 2 || y <= 0) {
			vy = -vy;
		}
	}

	public void draw(Canvas surfaceCanvas) {
		
		mSnakeBitMap.eraseColor(Color.BLACK);
		snakeSegment.draw(snakeCanvas, x, y, vx, vy);
		surfaceCanvas.drawBitmap(mSnakeBitMap, x, y, null);

	}
}
