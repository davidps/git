package config;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * defines the device configuration
 * 
 */
public class Configurator {

	private static DisplayMetrics displayMetrics;
	public static float screenWidthInInches;
	public static float screenHeightInInches;
	public static float screenWidthInPixels;
	public static float screenHeightInPixels;
	private static float screenDiagonalInInches;
	public static float dpi; // dots per inch
	private static Rect rect;

	public static void setDisplayMetrics(Display display) {
		displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		dpi = displayMetrics.xdpi;
		calcScreenSize();
		rect = new Rect(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels);
	}

	/**
	 * estimate the screen size (diagonal)
	 */
	private static void calcScreenSize() {
		screenWidthInPixels = displayMetrics.widthPixels;
		screenHeightInPixels = displayMetrics.heightPixels;
		screenWidthInInches = displayMetrics.widthPixels / displayMetrics.xdpi;
		screenHeightInInches = displayMetrics.heightPixels / displayMetrics.ydpi;
		double density = displayMetrics.density * 160;
		double x = Math.pow(displayMetrics.widthPixels / density, 2);
		double y = Math.pow(displayMetrics.heightPixels / density, 2);
		screenDiagonalInInches = (float) Math.sqrt(x + y);
	}

	public static Rect getRect() {
		return rect;
	}
	
}
