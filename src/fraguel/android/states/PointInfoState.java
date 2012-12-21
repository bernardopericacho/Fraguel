package fraguel.android.states;

import java.io.File;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.R;
import fraguel.android.Route;
import fraguel.android.State;
import fraguel.android.lists.InfoPointAdapter;
import fraguel.android.resources.ResourceManager;
import fraguel.android.threads.ImageDownloadingThread;
import fraguel.android.utils.TitleTextView;

public class PointInfoState extends State {
	public static final int STATE_ID = 20;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	private GridView gridView;
	private TitleTextView title;
	private ImageView image;
	private TextView text;
	private boolean talk = true;
	private static final int MENU_POINTINFO_STARTTALK = 0;
	private static final int MENU_POINTINFO_STOPTALK = 1;
	private static final int MENU_POINTINFO_MAINMENU = 2;
	private static final int MENU_POINTINFO_BACK = 3;

	public PointInfoState() {
		super();
		id = STATE_ID;
	}

	@Override
	public void load() {
		FRAGUEL.getInstance().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		LinearLayout container = new LinearLayout(FRAGUEL.getInstance()
				.getApplicationContext());
		container.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		container.setOrientation(LinearLayout.VERTICAL);

		Display display = ((WindowManager) FRAGUEL.getInstance()
				.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int height = display.getHeight();
		int width = display.getWidth();

		int heightAvailable = height - 2 * TitleTextView.HEIGHT;

		heightAvailable = heightAvailable / 2;

		title = new TitleTextView(FRAGUEL.getInstance().getApplicationContext());
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

		gridView = new GridView(FRAGUEL.getInstance().getApplicationContext());
		gridView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		gridView.setNumColumns(3);
		gridView.setColumnWidth(width / 3);
		gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridView.setAdapter(new InfoPointAdapter(FRAGUEL.getInstance()
				.getApplicationContext()));
		gridView.setScrollContainer(false);
		setGridViewListener();
		container.addView(gridView);

		viewGroup = container;
		FRAGUEL.getInstance().addView(viewGroup);

		if (route != null && point != null) {
			talk = false;
			this.loadData(route, point);
		}
		talk = true;
	}

	public void setGridViewListener() {

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {

				case 0:
					if (point.images != null && point.images.size() != 0) {
						FRAGUEL.getInstance().changeState(ImageState.STATE_ID);
						FRAGUEL.getInstance().getCurrentState()
								.loadData(route, point);
					} else
						Toast.makeText(
								FRAGUEL.getInstance().getApplicationContext(),
								"Este punto no tiene imágenes disponibles",
								Toast.LENGTH_SHORT).show();
					break;

				case 1:
					if (point.video != null && !point.video.equals(""))
						FRAGUEL.getInstance().startActivity(
								new Intent(Intent.ACTION_VIEW, Uri
										.parse(point.video)));
					else
						Toast.makeText(
								FRAGUEL.getInstance().getApplicationContext(),
								"Este punto no tiene video disponible",
								Toast.LENGTH_SHORT).show();
					break;

				case 2:

					FRAGUEL.getInstance().changeState(ARState.STATE_ID);
					FRAGUEL.getInstance().getCurrentState()
							.loadData(route, point);
					break;

				}
			}
		});

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void unload() {
		talk = true;
		FRAGUEL.getInstance().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		if (MapState.getInstance().getMapView() != null)
			MapState.getInstance().getGPS().setDialogDisplayed(false);
		super.unload();
	}

	@Override
	public boolean loadData(Route route, PointOI point) {
		super.loadData(route, point);
		String[] url = { point.icon };
		String path = ResourceManager.getInstance().getRootPath()
				+ "/tmp/route" + route.id + "/point" + point.id + "icon.png";
		File f = new File(path);
		if (f.exists()) {
			Bitmap bmp = BitmapFactory.decodeFile(path);
			image.setImageBitmap(bmp);
		} else {
			imageThread = new ImageDownloadingThread(url, "point" + point.id
					+ "icon", ResourceManager.getInstance().getRootPath()
					+ "/tmp/route" + route.id + "/");
			imageThread.start();
			image.setImageDrawable(FRAGUEL.getInstance().getResources()
					.getDrawable(R.drawable.loading));
		}
		String titleText;
		titleText = point.title + " (" + route.name + ")";
		title.setText(titleText);

		text.setText(point.pointdescription);
		if (talk) {
			FRAGUEL.getInstance().talk(
					point.title + " \n \n \n " + point.pointdescription);
			talk = false;
		}
		return true;

	}

	@Override
	public void imageLoaded(int index) {
		if (index == 0) {
			String path = ResourceManager.getInstance().getRootPath()
					+ "/tmp/route" + route.id + "/point" + point.id + "icon"
					+ ".png";
			Bitmap bmp = BitmapFactory.decodeFile(path);
			image.setImageBitmap(bmp);
			image.invalidate();
		}

	}

	@Override
	public Menu onCreateStateOptionsMenu(Menu menu) {
		menu.clear();

		menu.add(0, MENU_POINTINFO_STOPTALK, 0, "Detener audio").setIcon(
				R.drawable.ic_menu_talkstop);
		menu.add(0, MENU_POINTINFO_STARTTALK, 0, "Comenzar audio").setIcon(
				R.drawable.ic_menu_talkplay);

		menu.add(0, MENU_POINTINFO_MAINMENU, 0, "Menu principal").setIcon(
				R.drawable.ic_menu_home);
		menu.add(0, MENU_POINTINFO_BACK, 0, "Atrás").setIcon(
				R.drawable.ic_menu_back);

		return menu;
	}

	@Override
	public boolean onStateOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_POINTINFO_STOPTALK:
			FRAGUEL.getInstance().stopTalking();
			return true;
		case MENU_POINTINFO_STARTTALK:
			FRAGUEL.getInstance().talk(
					point.title + " \n \n \n " + point.pointdescription);
			return true;
		case MENU_POINTINFO_MAINMENU:
			FRAGUEL.getInstance().changeState(MainMenuState.STATE_ID);
			return true;
		case MENU_POINTINFO_BACK:
			FRAGUEL.getInstance().returnState();
			return true;
		}
		return false;
	}

}
