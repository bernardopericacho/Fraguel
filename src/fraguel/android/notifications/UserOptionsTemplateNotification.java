package fraguel.android.notifications;

import fraguel.android.FRAGUEL;
import fraguel.android.states.MainMenuState;
import fraguel.android.utils.NewRouteForm;
import android.content.DialogInterface;

public class UserOptionsTemplateNotification implements
		DialogInterface.OnClickListener {

	private MainMenuState state = (MainMenuState) (FRAGUEL.getInstance()
			.getCurrentState());
	private final CharSequence[] options = new CharSequence[state
			.getTempFiles().length + 1];

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		switch (arg1) {
		case 0:
			NewRouteForm form = new NewRouteForm(FRAGUEL.getInstance());
			state.setBlankForm(form);
			FRAGUEL.getInstance().createCustomDialog(
					"Nueva ruta: plantilla en blanco", form,
					new NewRouteNotification(), "Aceptar", null);
			break;
		case 1:
			options[0] = "Nueva ruta";
			int i = 1;
			for (String s : state.getTempFiles()) {
				options[i] = "Continuar con: " + s;
				i++;
			}
			FRAGUEL.getInstance().createDialog(
					"Nueva ruta: mediante captura de puntos", options,
					new GeoTaggingButton(), null);
			break;

		}

		arg0.dismiss();
	}

}
