package fraguel.android.states;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.R;
import fraguel.android.Route;
import fraguel.android.State;
import fraguel.android.utils.InfoText;
import fraguel.android.utils.TitleTextView;

public class InfoState extends State {

	public static final int STATE_ID = 6;
	public static final int INFOSTATE_STOP_RECORD = 1;
	public static final int INFOSTATE_REPEAT_RECORD = 2;

	private TitleTextView title;
	private InfoText text;
	private boolean talk = true;

	public InfoState() {
		super();
		id = STATE_ID;
	}

	@Override
	public void load() {
		LinearLayout ly = new LinearLayout(FRAGUEL.getInstance()
				.getApplicationContext());
		ly.setOrientation(LinearLayout.VERTICAL);
		viewGroup = ly;
		FRAGUEL.getInstance().addView(viewGroup);

		title = new TitleTextView(FRAGUEL.getInstance().getApplicationContext());

		viewGroup.addView(title);

		ScrollView container = new ScrollView(FRAGUEL.getInstance()
				.getApplicationContext());
		text = new InfoText(FRAGUEL.getInstance().getApplicationContext());
		text.setText("");
		if (route != null && point != null) {
			talk = false;
			this.loadData(route, point);
		}

		container.addView(text);

		viewGroup.addView(container);
		talk = true;

	}

	@Override
	public boolean loadData(Route r, PointOI p) {
		super.loadData(r, p);

		text.setText(point.pointdescription);
		title.setText(p.title + " - " + r.name);
		if (talk) {
			FRAGUEL.getInstance().talk(
					p.title + " \n \n \n " + p.pointdescription);
			talk = false;
		}
		return true;

	}

	@Override
	public void unload() {
		talk = true;
		super.unload();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public Menu onCreateStateOptionsMenu(Menu menu) {
		menu.clear();

		menu.add(0, INFOSTATE_STOP_RECORD, 0, R.string.infostate_menu_stop)
				.setIcon(R.drawable.ic_menu_talkstop);
		menu.add(0, INFOSTATE_REPEAT_RECORD, 0, R.string.infostate_menu_repeat)
				.setIcon(R.drawable.ic_menu_talkplay);

		return menu;

	}

	@Override
	public boolean onStateOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case INFOSTATE_STOP_RECORD:
			FRAGUEL.getInstance().stopTalking();
			return true;

		case INFOSTATE_REPEAT_RECORD:
			FRAGUEL.getInstance().talk(
					point.title + " /n /n /n " + point.pointdescription);
			return true;
		}
		return false;
	}

}
