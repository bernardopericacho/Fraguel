package fraguel.android.notifications;

import fraguel.android.FRAGUEL;
import android.content.DialogInterface;
import android.content.Intent;

public class GPSNotificationButton implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		FRAGUEL.getInstance().startActivity(gpsOptionsIntent);
	}

}
