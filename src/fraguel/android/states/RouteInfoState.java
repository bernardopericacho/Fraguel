package fraguel.android.states;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.R;
import fraguel.android.Route;
import fraguel.android.State;
import fraguel.android.resources.ResourceManager;
import fraguel.android.utils.TitleTextView;

public class RouteInfoState extends State {
	public static final int STATE_ID = 57;
	private TitleTextView title;
	private ImageView image;
	private TextView text;

	public RouteInfoState() {
		super();
		id = STATE_ID;
	}

	@Override
	public void load() {
		LinearLayout container = new LinearLayout(FRAGUEL.getInstance()
				.getApplicationContext());
		container.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		container.setOrientation(LinearLayout.VERTICAL);

		Display display = ((WindowManager) FRAGUEL.getInstance()
				.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int height = display.getHeight();

		int heightAvailable = height - 2 * TitleTextView.HEIGHT;

		heightAvailable = heightAvailable / 2;

		title = new TitleTextView(FRAGUEL.getInstance().getApplicationContext());
		title.setText("Aquí va el título de la ruta");
		container.addView(title);

		image = new ImageView(FRAGUEL.getInstance().getApplicationContext());
		image.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				heightAvailable));

		image.setPadding(10, 10, 10, 10);
		image.setAdjustViewBounds(true);

		container.addView(image);

		ScrollView sv = new ScrollView(FRAGUEL.getInstance()
				.getApplicationContext());
		sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				heightAvailable - 40));
		text = new TextView(FRAGUEL.getInstance().getApplicationContext());
		sv.addView(text);
		container.addView(sv);

		Button b = new Button(FRAGUEL.getInstance().getApplicationContext());
		b.setId(0);
		b.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		b.setOnClickListener((OnClickListener) FRAGUEL.getInstance());
		b.setText("Continuar");
		b.setGravity(Gravity.CENTER);

		container.addView(b);

		viewGroup = container;
		FRAGUEL.getInstance().addView(viewGroup);
		if (route != null && point != null)
			this.loadData(route, point);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case 0:
			FRAGUEL.getInstance().changeState(MapState.STATE_ID);
			break;
		default:
			break;

		}

	}

	@Override
	public void unload() {
		super.unload();
	}

	@Override
	public boolean loadData(Route r, PointOI p) {
		route = r;
		point = p;
		title.setText(r.name);
		text.setText(r.description);
		image.setImageDrawable(FRAGUEL.getInstance().getResources()
				.getDrawable(R.drawable.loading));
		FRAGUEL.getInstance().talk(
				r.name + " \n \n \n \n \n \n " + r.description);

		return true;
	}

	@Override
	public void imageLoaded(int index) {
		if (index == 0) {
			String path = ResourceManager.getInstance().getRootPath() + "/tmp/"
					+ "route" + Integer.toString(route.id) + "image" + ".png";
			Bitmap bmp = BitmapFactory.decodeFile(path);
			image.setImageBitmap(bmp);
			image.invalidate();
		}

	}

	@Override
	public Menu onCreateStateOptionsMenu(Menu menu) {
		return menu;
	}

	@Override
	public boolean onStateOptionsItemSelected(MenuItem item) {
		return false;
	}

}
