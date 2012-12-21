package fraguel.android.notifications;

import fraguel.android.FRAGUEL;
import fraguel.android.resources.ResourceManager;
import fraguel.android.states.MainMenuState;
import fraguel.android.utils.NewRouteForm;
import android.content.DialogInterface;

public class NewRouteNotification implements DialogInterface.OnClickListener {
	@Override
	public void onClick(DialogInterface dialog, int which) {
		MainMenuState state = (MainMenuState) (FRAGUEL.getInstance()
				.getCurrentState());
		NewRouteForm form = state.getBlankForm();
		ResourceManager.getInstance().createXMLTemplate(form.getFileName(),
				form.getRouteName(), -2, form.getNumPoints());
		dialog.dismiss();
	}

}
