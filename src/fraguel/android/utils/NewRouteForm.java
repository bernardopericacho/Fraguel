package fraguel.android.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class NewRouteForm extends LinearLayout {

	private EditText fileName, routeName;
	private NumberPicker decenas, unidades;

	public NewRouteForm(Context context) {
		super(context);

		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		ScrollView sv = new ScrollView(context);
		sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		sv.setVerticalScrollBarEnabled(false);
		sv.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_INSET);

		LinearLayout container = new LinearLayout(context);
		container.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		container.setOrientation(LinearLayout.VERTICAL);

		TextView fileLabel = new TextView(context);
		fileLabel.setText("Nombre del fichero:");
		fileLabel.setPadding(5, 5, 5, 5);
		container.addView(fileLabel);
		fileName = new EditText(context);
		fileName.setPadding(5, 0, 5, 0);
		fileName.setText("ruta1");
		container.addView(fileName);

		TextView nameLabel = new TextView(context);
		nameLabel.setText("Nombre de la ruta:");
		nameLabel.setPadding(5, 5, 5, 5);
		container.addView(nameLabel);
		routeName = new EditText(context);
		routeName.setPadding(5, 0, 5, 0);
		routeName.setText("nombre1");
		container.addView(routeName);

		TextView pointLabel = new TextView(context);
		pointLabel.setText("Número de puntos de interés:");
		pointLabel.setPadding(5, 5, 5, 5);
		container.addView(pointLabel);

		LinearLayout numbers = new LinearLayout(context);
		numbers.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		numbers.setOrientation(LinearLayout.HORIZONTAL);

		decenas = new NumberPicker(context, null);
		decenas.setOrientation(LinearLayout.VERTICAL);
		numbers.addView(decenas);
		unidades = new NumberPicker(context, null);
		unidades.setOrientation(NumberPicker.VERTICAL);
		unidades.setValue(1);
		numbers.addView(unidades);

		numbers.setGravity(Gravity.CENTER_HORIZONTAL);

		container.addView(numbers);
		sv.addView(container);
		this.addView(sv);
	}

	public String getFileName() {
		String s = fileName.getText().toString();
		return s;
	}

	public String getRouteName() {
		String s = routeName.getText().toString();
		return s;
	}

	public int getDecenas() {
		return decenas.getValue();
	}

	public int getUnidades() {
		return unidades.getValue();
	}

	public int getNumPoints() {
		return 10 * decenas.getValue() + unidades.getValue();
	}

}
