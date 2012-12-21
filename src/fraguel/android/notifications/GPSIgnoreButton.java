package fraguel.android.notifications;

import android.content.DialogInterface;
import fraguel.android.states.MapState;

public class GPSIgnoreButton implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		MapState.getInstance().getGPS().setDialogDisplayed(false);
		dialog.dismiss();
	}

}
