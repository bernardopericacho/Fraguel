package fraguel.android.notifications;

import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.states.MainMenuState;
import fraguel.android.utils.SavePointTemplate;
import android.content.DialogInterface;
import android.widget.Toast;

public class NoExtraInfoButton implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		MainMenuState state = (MainMenuState) FRAGUEL.getInstance()
				.getCurrentState();
		PointOI point = state.getCurrentPoint();
		state.getGeoTaggingPoints().add(point);
		Toast.makeText(
				FRAGUEL.getInstance().getApplicationContext(),
				"Hay " + state.getGeoTaggingPoints().size()
						+ " puntos guardados", Toast.LENGTH_LONG).show();
		state.setCoordinatesCapturer(new SavePointTemplate(FRAGUEL
				.getInstance().getApplicationContext()));
		FRAGUEL.getInstance().createCustomDialog(
				state.getGeoTaggingForm().getRouteName()
						+ ": captura de coordenadas",
				state.getCoordinatesCapturer(), new CaptureCoordinatesButton(),
				"Capturar", new EndCaptureButton(), "Finalizar", null);

	}

}
