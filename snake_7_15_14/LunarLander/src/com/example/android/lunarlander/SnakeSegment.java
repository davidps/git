package com.example.android.lunarlander;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;

public class SnakeSegment {

	private static Bitmap snakeUp;
	private static Bitmap snakeDown;
	private static Bitmap snakeLeft;
	private static Bitmap snakeRight;

	public SnakeSegment(Context context, Point loc) {
		snakeUp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.snake);
		snakeDown = rotateBitmap(snakeUp, 180.f);
		snakeLeft = rotateBitmap(snakeUp, -90.f);
		snakeRight = rotateBitmap(snakeUp, 90.f);
	}

	public static Bitmap rotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);

	}

	public void draw(Canvas canvas, float x, float y, float vx, float vy) {
		if (vx == 0) {
			if (vy >= 0) {
				canvas.drawBitmap(snakeDown, x, y, null);
			} else {
				canvas.drawBitmap(snakeUp, x, y, null);

			}
		} else if (vx >= 0) {
			canvas.drawBitmap(snakeRight, x, y, null);
		} else {
			canvas.drawBitmap(snakeLeft, x, y, null);

		}

	}

}