package fraguel.android.notifications;

import android.content.DialogInterface;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.states.MapState;

public class RouteSelectionNotification implements
		DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		MapState.getInstance().setContextRoute(
				FRAGUEL.getInstance().routes.get(which));

		final CharSequence[] options = new CharSequence[MapState.getInstance()
				.getContextRoute().pointsOI.size() + 1];

		options[0] = "Desde el principio";
		int i = 1;
		for (PointOI p : MapState.getInstance().getContextRoute().pointsOI) {
			options[i] = p.title;
			i++;
		}
		dialog.dismiss();
		FRAGUEL.getInstance().createDialog("Elegir punto de comienzo", options,
				new PointSelectionNotification(), new BackKeyNotification());

	}

}
