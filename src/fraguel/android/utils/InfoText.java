package fraguel.android.utils;

import fraguel.android.FRAGUEL;
import fraguel.android.R;
import android.content.Context;
import android.widget.TextView;

public class InfoText extends TextView {

	public InfoText(Context context) {
		super(context);
		this.setTextAppearance(FRAGUEL.getInstance().getApplicationContext(),
				R.style.ScrollText);
	}

}
