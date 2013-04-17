package com.xiao.policy.keyguardmodule;

import java.io.File;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ExtLockScreenLoader {
	private static final String TAG = "ExtLockScreenLoader";
	private static final boolean LOGE = true;
	private static final String DEFAULT_LOCKSCREEN_PKG = "com.orange.lockscreen";
	private static final String LOCKSCREEN_ENTRY = "android.lockscreen.ENTRY";

	private static Context sExtContext;
	private static Class<?> sLoadedClass;
	private static boolean classLoaded = false;
	
	/**
	 * Get lock screen from external APK, the APK must meet followed conditions:
	 * <li>
	 * package name must equal {@link DEFAULT_LOCKSCREEN_PKG}<li>extends
	 * {@link ExtLockScreen} <li>
	 * define meta-data with "android.lockscreen.VIEW_NAME" as key and root view
	 * name as value in manifest
	 * 
	 * @return lock screen view
	 */
	public static ExtLockScreen getLockScreen(Context context,
			KeyguardScreenCallback callback,
			KeyguardUpdateMonitor updateMonitor, Configuration configuration) {

		ExtLockScreen extLockScreen = null;
		
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(
					DEFAULT_LOCKSCREEN_PKG, PackageManager.GET_META_DATA);
			if (info != null && !classLoaded) {
				Bundle bundle = info.metaData;
				if (bundle != null) {
					String entry = bundle.getString(LOCKSCREEN_ENTRY);
					String srcPath = info.sourceDir;
					String dataDir = info.dataDir;
					loadLockScreenClass(context, DEFAULT_LOCKSCREEN_PKG, entry,
							srcPath, dataDir);
				}
			}
		} catch (Exception e) {
			if (LOGE) {
				Log.e(TAG, "Got an exception:", e);
			}
			classLoaded = false;
		}

		if (classLoaded) {
			extLockScreen = createExtLockScreen(sExtContext);
			if (extLockScreen != null) {
				extLockScreen.setCallbacks(callback, updateMonitor,
						configuration);
			}
		}
		return extLockScreen;
	}
	
	/**
	 * @param context
	 *            The parent Context, normally the Context need loading classes
	 *            from another APK.
	 * @param pkgName
	 *            The package to be loaded, e.g.
	 *            "com.orange.ost.widget.weather", it is the package specified
	 *            in the manefist.
	 * @param srcPath
	 *            The path to the apk, e.g. "/data/app/xxx.apk"
	 * @param dataDir
	 *            The Data directory for the application, e.g.
	 *            "/data/data/com.orange.test,com.orange.test"
	 * @return
	 */
	private static void loadLockScreenClass(Context context, String pkgName,
			String entry, String srcPath, String dataDir) {
		try {
			ClassLoader extClassLoader = getEncapsulatedClassLoader(srcPath,
					dataDir, context);
			sLoadedClass = extClassLoader.loadClass(entry);
			sExtContext = getWidgetPackageContext(context, pkgName);
			classLoaded = true;
		} catch (Exception e) {
			if (LOGE) {
				Log.e(TAG, "Got an exception:", e);
			}
			classLoaded = false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static ExtLockScreen createExtLockScreen(Context extContext) {
		ExtLockScreen lockScreen = null;
		if (sLoadedClass != null && extContext != null) {
			try {
				final Method method = sLoadedClass.getDeclaredMethod("getLockscreen",
						Context.class);
				lockScreen = (ExtLockScreen) method
						.invoke(null, extContext);
			} catch (Exception e) {
				if (LOGE) {
					Log.e(TAG, "Got an exception:", e);
				}
			}
		}
		return lockScreen;
	}

	private static Context getWidgetPackageContext(Context context,
			String pkgName) {
		Context newContext = null;
		try {
			newContext = context.createPackageContext(pkgName,
					Context.CONTEXT_INCLUDE_CODE
							+ Context.CONTEXT_IGNORE_SECURITY);
		} catch (Exception e) {
			if (LOGE) {
				Log.e(TAG, "Got an exception:", e);
			}
		}
		return newContext;
	}

	/**
	 * @param source_path
	 *            The path to the apk, e.g. "/data/app/xxx.apk"
	 * @param data_dir
	 *            The Data directory for the application, e.g.
	 *            "/data/data/com.orange.test,com.orange.test"
	 * @param context
	 *            The parent Context, normally the Context need loading classes
	 *            from another APK.
	 * 
	 * @return A class cloader for the Application, later could be used to load
	 *         Classes and resource from the APK; otherwise null.
	 */
	private static final String ANDORID_CLASSLOADER = "android.app.ApplicationLoaders";
	private static final String METHOD_GETDEFAULT = "getDefault";
	private static final String METHOD_GETCLASSLOADER = "getClassLoader";

	@SuppressWarnings("unchecked")
	private static ClassLoader getEncapsulatedClassLoader(String srcPath,
			String dataDir, Context context) {
		ClassLoader wrappedLoader = null;
		try {
			File file = new File(srcPath);
			Class loader = Class.forName(ANDORID_CLASSLOADER);
			Method getDefault = loader.getMethod(METHOD_GETDEFAULT,
					new Class[] {});
			Method getClassLoader = loader
					.getMethod(METHOD_GETCLASSLOADER, new Class[] {
							String.class, String.class, ClassLoader.class });
			Object appLoadersObj = getDefault.invoke(null);
			wrappedLoader = (ClassLoader) getClassLoader.invoke(appLoadersObj,
					new Object[] { file.getAbsolutePath(), dataDir,
							context.getClassLoader() });

		} catch (Exception e) {
			if (LOGE) {
				Log.e(TAG, "Got an exception:", e);
			}
		}
		return wrappedLoader;
	}

	public static abstract class ExtLockScreen extends FrameLayout implements
					KeyguardScreen, KeyguardUpdateMonitor.InfoCallback,
					KeyguardUpdateMonitor.SimStateCallback {
		
		private int mCreationOrientation;
		private int mKeyboardHidden;

		private KeyguardScreenCallback mKeyguardScreenCallback;
		private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
		private Configuration mConfiguration;

		public ExtLockScreen(Context context) {
			super(context);
			setFocusable(true);
			setFocusableInTouchMode(true);
			setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		}

		public void setCallbacks(KeyguardScreenCallback callback,
				KeyguardUpdateMonitor updateMonitor, Configuration configuration) {
			mKeyguardScreenCallback = callback;
			mKeyguardUpdateMonitor = updateMonitor;
			mConfiguration = configuration;
			if (mKeyguardUpdateMonitor != null) {
				mKeyguardUpdateMonitor.registerInfoCallback(this);
				mKeyguardUpdateMonitor.registerSimStateCallback(this);
			}
			if (mConfiguration != null) {
				mKeyboardHidden = mConfiguration.hardKeyboardHidden;
				mCreationOrientation = mConfiguration.orientation;
			}
		}

		protected final void goToUnlockScreen() {
			if (mKeyguardScreenCallback != null) {
				mKeyguardScreenCallback.goToUnlockScreen();
			}
		}

		protected final void pokeWakeLock() {
			if (mKeyguardScreenCallback != null) {
				mKeyguardScreenCallback.pokeWakelock();
			}
		}

	}
}
