package fraguel.android.utils;

import fraguel.android.FRAGUEL;
import fraguel.android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

public class TitleTextView extends AutomaticScrollTextView {
	public static final int HEIGHT = 30;

	public TitleTextView(Context context) {
		super(context);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		this.setTextAppearance(FRAGUEL.getInstance().getApplicationContext(),
				R.style.StateTitle);
		this.setBackgroundColor(Color.BLACK);
		this.setHeight(HEIGHT);
		this.setMinHeight(HEIGHT);
		this.setMaxHeight(HEIGHT);

	}

}
