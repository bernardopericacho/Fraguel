package fraguel.android.notifications;

import android.content.DialogInterface;
import fraguel.android.FRAGUEL;
import fraguel.android.resources.ResourceManager;
import fraguel.android.states.MainMenuState;

public class ExportPointsButton implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		MainMenuState state = (MainMenuState) FRAGUEL.getInstance()
				.getCurrentState();
		switch (which) {
		case 0:
			ResourceManager.getInstance().toTempFile();
			break;
		case 1:
			ResourceManager.getInstance().createXMLFromPoints(
					state.getRouteName(), state.getRouteName(), -1,
					state.getGeoTaggingPoints());
			break;
		}
	}

}
