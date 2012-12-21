package fraguel.android.notifications;

import android.content.DialogInterface;
import android.widget.Toast;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.states.MainMenuState;
import fraguel.android.utils.SavePointTemplate;

public class ExtraInfoPointButton implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		MainMenuState state = (MainMenuState) FRAGUEL.getInstance()
				.getCurrentState();
		PointOI point = state.getCurrentPoint();
		point.title = state.getExtraInfo().getPointName();
		state.getGeoTaggingPoints().add(point);
		Toast.makeText(
				FRAGUEL.getInstance().getApplicationContext(),
				"Hay " + state.getGeoTaggingPoints().size()
						+ " puntos guardados", Toast.LENGTH_LONG).show();
		arg0.dismiss();
		state.setCoordinatesCapturer(new SavePointTemplate(FRAGUEL
				.getInstance().getApplicationContext()));
		FRAGUEL.getInstance().createCustomDialog(
				state.getRouteName() + ": captura de coordenadas",
				state.getCoordinatesCapturer(), new CaptureCoordinatesButton(),
				"Capturar", new EndCaptureButton(), "Finalizar", null);
	}

}
