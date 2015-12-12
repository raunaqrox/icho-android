package in.icho.utils;

import java.lang.reflect.Method;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import in.icho.IchoApplication;

public class Utilities {
	/*
	 * Utility tool. display metrics, Locale. keyboard status. phone
	 * orientation.
	 */

	private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<String, Typeface>();
	public static float density = 1;
	public static Point displaySize = new Point();
	public static int statusBarHeight = 0;
	private static Boolean isTablet = null;
	private static Toast toast;

	static {
		density = IchoApplication.applicationContext.getResources()
				.getDisplayMetrics().density;
		checkDisplaySize();
	}

	public static Typeface getTypeface(String assetPath) {
		synchronized (typefaceCache) {
			if (!typefaceCache.containsKey(assetPath)) {
				try {
					Typeface t = Typeface.createFromAsset(
							IchoApplication.applicationContext.getAssets(),
							assetPath);
					typefaceCache.put(assetPath, t);
				} catch (Exception e) {
					return null;
				}
			}
			return typefaceCache.get(assetPath);
		}
	}

	@SuppressLint("NewApi")
	public static Point getRealScreenSize() {
		Point size = new Point();
		try {
			WindowManager windowManager = (WindowManager) IchoApplication.applicationContext
					.getSystemService(Context.WINDOW_SERVICE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				windowManager.getDefaultDisplay().getRealSize(size);
			} else {
				try {
					Method mGetRawW = Display.class.getMethod("getRawWidth");
					Method mGetRawH = Display.class.getMethod("getRawHeight");
					size.set((Integer) mGetRawW.invoke(windowManager
							.getDefaultDisplay()), (Integer) mGetRawH
							.invoke(windowManager.getDefaultDisplay()));
				} catch (Exception e) {
					size.set(windowManager.getDefaultDisplay().getWidth(),
							windowManager.getDefaultDisplay().getHeight());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static boolean haveInternet(Context ctx) {

		NetworkInfo info = ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}

	public static int dp(float value) {
		return (int) Math.ceil(density * value);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void checkDisplaySize() {
		try {
			WindowManager manager = (WindowManager) IchoApplication.applicationContext
					.getSystemService(Context.WINDOW_SERVICE);
			if (manager != null) {
				Display display = manager.getDefaultDisplay();
				if (display != null) {
					if (android.os.Build.VERSION.SDK_INT < 13) {
						displaySize
								.set(display.getWidth(), display.getHeight());
					} else {
						display.getSize(displaySize);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public static boolean isTablet() {
		return false;
	}

	public static void showKeyboard(View view) {
		if (view == null) {
			return;
		}
		InputMethodManager inputManager = (InputMethodManager) view
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

		((InputMethodManager) view.getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
	}

	public static boolean isKeyboardShowed(View view) {
		if (view == null) {
			return false;
		}
		InputMethodManager inputManager = (InputMethodManager) view
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		return inputManager.isActive(view);
	}

	public static void hideKeyboard(View view) {
		if (view == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (!imm.isActive()) {
			return;
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showToast(String text) {
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(IchoApplication.applicationContext, text,
				Toast.LENGTH_SHORT);
		toast.show();
	}
}
