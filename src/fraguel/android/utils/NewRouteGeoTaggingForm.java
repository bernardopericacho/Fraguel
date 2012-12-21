package fraguel.android.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewRouteGeoTaggingForm extends LinearLayout {

	private TextView routeName;

	public NewRouteGeoTaggingForm(Context context) {
		super(context);
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);

		TextView nameLabel = new TextView(context);
		nameLabel.setText("Nombre de la ruta:");
		nameLabel.setPadding(5, 5, 5, 5);
		this.addView(nameLabel);
		routeName = new EditText(context);
		routeName.setPadding(5, 0, 5, 0);
		routeName.setText("nombre1");
		this.addView(routeName);
	}

	public String getRouteName() {
		return routeName.getText().toString();
	}

}
