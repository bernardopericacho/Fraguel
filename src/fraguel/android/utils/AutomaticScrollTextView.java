package fraguel.android.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

public class AutomaticScrollTextView extends TextView {

	public AutomaticScrollTextView(Context context) {
		super(context);
		this.setLines(1);
		this.setMaxLines(1);
		this.setSingleLine();
		this.setSingleLine(true);
		this.setHorizontallyScrolling(true);

		this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		this.setMarqueeRepeatLimit(-1);
		this.setFocusable(true);
		this.setSelected(true);
	}

}
