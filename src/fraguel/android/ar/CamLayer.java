package fraguel.android.ar;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This class handles the camera. In particular, the method setPreviewCallback
 * is used to receive camera images. The camera images are not processed in this
 * class but delivered to the GLLayer.
 * 
 * @author Niels
 * 
 */
public class CamLayer extends SurfaceView implements SurfaceHolder.Callback,
		PreviewCallback {
	int previewWidth = 533;
	int previewHeight = 480;

	public static CamLayer instance;
	Camera mCamera;
	Context context;
	boolean paused = true;

	public GLView synchronCallback;

	public CamLayer(Context context, int previewWidth, int previewHeight) {
		super(context);
		this.previewWidth = previewWidth;
		this.previewHeight = previewHeight;

		Log.i("CamLayer", "CamLayer(GLCamTest context)");
		this.context = context;
		instance = this;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		SurfaceHolder mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i("CamLayer", "surfaceDestroyed");
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		synchronized (this) {
			try {
				paused = true;
				if (mCamera != null) {
					mCamera.stopPreview();
					mCamera.release();
				}
			} catch (Exception e) {
				Log.e("CamLayer", e.getMessage());
			}
		}
	}

	public void pausePreview() {
		paused = true;
		mCamera.stopPreview();
	}

	public void restartPreview() {
		mCamera.startPreview();
		mCamera.setPreviewCallback(this);
		paused = false;
	}

	public void onPause() {
		try {
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
			}
		} catch (Exception e) {
			Log.e("CamLayer", e.getMessage());
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i("CamLayer", "surfaceChanged");

		synchronized (this) {
			paused = false;

			mCamera = Camera.open();

			Camera.Parameters p = mCamera.getParameters();
			p.setPreviewSize(previewWidth, previewHeight);
			p.setSceneMode(Camera.Parameters.SCENE_MODE_ACTION);
			p.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT);
			p.setAntibanding(Camera.Parameters.ANTIBANDING_60HZ);
			p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			mCamera.setParameters(p);

			p = mCamera.getParameters();
			Size size = p.getPictureSize();
			previewWidth = size.width;
			previewHeight = size.height;

			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				Log.e("Camera", "mCamera.setPreviewDisplay(holder);");
			}

			PixelFormat pf = new PixelFormat();
			PixelFormat.getPixelFormatInfo(p.getPreviewFormat(), pf);
			int bufSize = (previewWidth * previewHeight * pf.bitsPerPixel) / 8;

			// Must call this before calling addCallbackBuffer to get all the
			// reflection variables setup
			initForACB();

			// Add three buffers to the buffer queue. I re-queue them once they
			// are used in
			// onPreviewFrame, so we should not need many of them.
			byte[] buffer = new byte[bufSize];
			addCallbackBuffer(buffer);
			buffer = new byte[bufSize];
			addCallbackBuffer(buffer);
			buffer = new byte[bufSize];
			addCallbackBuffer(buffer);

			setPreviewCallbackWithBuffer();

			mCamera.startPreview();
		}
	}

	Date start;
	int fcount = 0;
	int frameId = 0;

	public void onPreviewFrame(byte[] arg0, Camera arg1) {
		if (paused)
			return;
		try {
			if (synchronCallback != null)
				synchronCallback.onPreviewFrame(arg0, arg1);
		} catch (Throwable t) {
			t.printStackTrace();
			Log.e("CamLayer", "onPreviewFrame Error " + t.getMessage());
		}

		addCallbackBuffer(arg0);
	}

	/**
	 * This method will list all methods of the android.hardware.Camera class,
	 * even the hidden ones. With the information it provides, you can use the
	 * same approach I took below to expose methods that were written but hidden
	 * in eclair
	 */
	@SuppressWarnings(value = { "unchecked", "unused" })
	private void listAllCameraMethods() {
		try {
			Class<android.hardware.Camera> c = (Class<Camera>) Class
					.forName("android.hardware.Camera");
			Method[] m = c.getMethods();
			for (int i = 0; i < m.length; i++)
				Log.i("AR", "  method:" + m[i].toString());
		} catch (Exception e) {
			Log.i("AR", e.toString());
		}
	}

	/**
	 * These variables are re-used over and over by addCallbackBuffer
	 */
	Method mAcb;
	Object[] mArglist;

	private void initForACB() {
		try {
			Class<?> mC = Class.forName("android.hardware.Camera");

			Class<?>[] mPartypes = new Class[1];
			mPartypes[0] = (new byte[1]).getClass(); // There is probably a
														// better way to do
														// this.
			mAcb = mC.getMethod("addCallbackBuffer", mPartypes);

			mArglist = new Object[1];
		} catch (Exception e) {
			Log.e("AR",
					"Problem setting up for addCallbackBuffer: " + e.toString());
		}
	}

	/**
	 * This method allows you to add a byte buffer to the queue of buffers to be
	 * used by preview. See:
	 * http://android.git.kernel.org/?p=platform/frameworks
	 * /base.git;a=blob;f=core/java/android/hardware/Camera.java;hb=9d
	 * b3d07b9620b4269ab33f78604a36327e536ce1
	 * 
	 * @param b
	 *            The buffer to register. Size should be width * height *
	 *            bitsPerPixel / 8.
	 */
	private void addCallbackBuffer(byte[] b) {
		// Check to be sure initForACB has been called to setup
		// mAcb and mArglist
		if (mArglist == null) {
			initForACB();
		}

		mArglist[0] = b;
		try {
			mAcb.invoke(mCamera, mArglist);
		} catch (Exception e) {
			Log.e("AR", "invoking addCallbackBuffer failed: " + e.toString());
		}
	}

	/**
	 * Use this method instead of setPreviewCallback if you want to use manually
	 * allocated buffers. Assumes that "this" implements Camera.PreviewCallback
	 */
	private void setPreviewCallbackWithBuffer() {
		try {
			Class<?> c = Class.forName("android.hardware.Camera");

			Method spcwb = null;
			// This way of finding our method is a bit inefficient, but I am a
			// reflection novice,
			// and didn't want to waste the time figuring out the right way to
			// do it.
			// since this method is only called once, this should not cause
			// performance issues
			Method[] m = c.getMethods();
			for (int i = 0; i < m.length; i++) {
				if (m[i].getName().compareTo("setPreviewCallbackWithBuffer") == 0) {
					spcwb = m[i];
					break;
				}
			}

			// If we were able to find the setPreviewCallbackWithBuffer method
			// of Camera,
			// we can now invoke it on our Camera instance, setting 'this' to be
			// the
			// callback handler
			if (spcwb != null) {
				Object[] arglist = new Object[1];
				arglist[0] = this;
				spcwb.invoke(mCamera, arglist);
				Log.i("AR", "setPreviewCallbackWithBuffer: Called method");
			} else {
				Log.i("AR", "setPreviewCallbackWithBuffer: Did not find method");
			}

		} catch (Exception e) {
			Log.i("AR", e.toString());
		}
	}
}
