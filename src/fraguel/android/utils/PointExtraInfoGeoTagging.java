package fraguel.android.utils;

import fraguel.android.FRAGUEL;
import fraguel.android.states.MainMenuState;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PointExtraInfoGeoTagging extends LinearLayout {
	private TextView pointName;

	public PointExtraInfoGeoTagging(Context context) {
		super(context);
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);

		TextView nameLabel = new TextView(context);
		nameLabel.setText("Nombre del punto capturado:");
		nameLabel.setPadding(5, 5, 5, 5);
		this.addView(nameLabel);
		pointName = new EditText(context);
		pointName.setPadding(5, 0, 5, 0);

		MainMenuState state = (MainMenuState) FRAGUEL.getInstance()
				.getCurrentState();

		pointName.setText("nombre" + state.getGeoTaggingPoints().size());
		this.addView(pointName);
	}

	public String getPointName() {
		return pointName.getText().toString();
	}
}
