package fraguel.android.states;

import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.R;
import fraguel.android.Route;
import fraguel.android.State;
import fraguel.android.ar.CamLayer;
import fraguel.android.ar.GLView;
import fraguel.android.gps.LatLon2UTM;

public class ARState extends State {

	public static final int STATE_ID = 5;
	private static final int ARSTATE_PLAY = 1;
	private static final int ARSTATE_STOP = 2;

	GLView glView;
	private CamLayer mPreview;

	private LatLon2UTM ll;
	private float lastX;
	private float lastZ;

	public ARState() {
		super();
		id = STATE_ID;
	}

	@Override
	public void load() {

		ll = new LatLon2UTM();

		FRAGUEL.getInstance().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// Crear vista OpenGL
		glView = new GLView(FRAGUEL.getInstance().getApplicationContext());
		mPreview = new CamLayer(glView.getContext(), 240, 160);
		mPreview.synchronCallback = glView;

		// Añadir a la actividad
		FRAGUEL.getInstance().setContentView(glView);
		FRAGUEL.getInstance().addContentView(
				mPreview,
				new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
		glView.setKeepScreenOn(true);
	}

	@Override
	public void unload() {
		glView.setKeepScreenOn(false);
		FRAGUEL.getInstance().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		FRAGUEL.getInstance().setContentView(FRAGUEL.getInstance().getView());
		super.unload();
	}

	@Override
	public boolean loadData(Route r, PointOI p) {
		super.loadData(r, p);
		return true;
	}

	@Override
	public void onRotationChanged(float[] values) {
		if (glView.scene.camera() == null)
			return;
		int d = 2;
		float delta = glView.scene.camera().rotation.x - (values[2] + 90);
		if (Math.abs(delta) > d)
			glView.scene.camera().rotation.x = values[2] + 90;
		delta = glView.scene.camera().rotation.y - (values[0] + 90);
		if (Math.abs(delta) > d)
			glView.scene.camera().rotation.y = values[0] + 90;
		delta = glView.scene.camera().rotation.z - values[1];
		if (Math.abs(delta) > d)
			glView.scene.camera().rotation.z = values[1];
	}

	@Override
	public void onLocationChanged(float[] values) {
		if (glView.scene.camera() == null)
			return;
		ll.setVariables(values[0], values[1]);
		float x = -(float) ll.getEasting();
		float z = -(float) ll.getNorthing(values[0]);

		float dx = Math.abs(lastX - x);
		float dz = Math.abs(lastZ - z);

		if (dx > 1)
			glView.scene.camera().position.x = x;
		glView.scene.camera().position.y = 1.6f;
		if (dz > 1)
			glView.scene.camera().position.z = z;

		lastX = x;
		lastZ = z;
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public Menu onCreateStateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, ARSTATE_PLAY, 0, "Reproducir información").setIcon(
				R.drawable.ic_menu_talkplay);
		menu.add(0, ARSTATE_STOP, 0, "Detener reproducción").setIcon(
				R.drawable.ic_menu_talkstop);
		return menu;
	}

	@Override
	public boolean onStateOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case ARSTATE_PLAY:
			FRAGUEL.getInstance().talk(point.textAr);
			return true;

		case ARSTATE_STOP:
			FRAGUEL.getInstance().stopTalking();
			return true;
		}
		return false;
	}

}
