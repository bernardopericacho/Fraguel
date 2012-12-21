package fraguel.android.notifications;

import java.util.ArrayList;

import android.content.DialogInterface;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.states.MainMenuState;
import fraguel.android.utils.SavePointTemplate;

public class NewRouteTaggingButton implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		MainMenuState state = (MainMenuState) FRAGUEL.getInstance()
				.getCurrentState();
		state.setRouteName(state.getGeoTaggingForm().getRouteName());
		state.setGeoTaggingPoints(new ArrayList<PointOI>());

		SavePointTemplate capturer = state.getCoordinatesCapturer();

		if (capturer == null) {
			capturer = new SavePointTemplate(FRAGUEL.getInstance()
					.getApplicationContext());
			state.setCoordinatesCapturer(capturer);
		}
		FRAGUEL.getInstance().createCustomDialog(
				state.getRouteName() + ": captura de coordenadas", capturer,
				new CaptureCoordinatesButton(), "Capturar",
				new EndCaptureButton(), "Finalizar", null);
		dialog.dismiss();
	}

}
