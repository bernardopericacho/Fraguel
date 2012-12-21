package fraguel.android.notifications;

import android.content.DialogInterface;
import fraguel.android.FRAGUEL;
import fraguel.android.states.MapState;

public class StopRouteNotification implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		MapState.getInstance().setContextMenuDisplayed(false);
		MapState.getInstance().getGPS().stopRoute();
		FRAGUEL.getInstance().stopTalking();
	}

}
