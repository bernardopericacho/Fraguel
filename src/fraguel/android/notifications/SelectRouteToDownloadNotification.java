package fraguel.android.notifications;

import android.content.DialogInterface;
import android.widget.Toast;
import fraguel.android.FRAGUEL;
import fraguel.android.MinRouteInfo;
import fraguel.android.resources.ResourceManager;
import fraguel.android.states.RouteManagerState;
import fraguel.android.threads.FileDownloadingThread;

public class SelectRouteToDownloadNotification implements
		DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface arg0, int which) {
		RouteManagerState state = (RouteManagerState) FRAGUEL.getInstance()
				.getCurrentState();

		MinRouteInfo routeToDownload = state.getAllRoutesAvailables()
				.get(which);
		String[] urls;
		String[] names;
		if (routeToDownload.urlArFiles != null) {
			urls = new String[routeToDownload.urlArFiles.length + 1];
			names = new String[urls.length];

			urls[0] = routeToDownload.urlXml;
			names[0] = "route" + routeToDownload.id + ".xml";

			int i = 1;
			String[] name;
			for (String s : routeToDownload.urlArFiles) {
				urls[i] = s;
				name = s.split("/");
				names[i] = name[name.length - 1];
				i++;
			}
		} else {
			urls = new String[1];
			names = new String[1];
			urls[0] = routeToDownload.urlXml;
			names[0] = "route" + routeToDownload.id + ".xml";

		}
		FileDownloadingThread t = new FileDownloadingThread(urls, names,
				ResourceManager.getInstance().getRootPath() + "/ar/");
		Toast.makeText(FRAGUEL.getInstance().getApplicationContext(),
				"Descargando ruta y archivos asociados. Espere por favor.",
				Toast.LENGTH_LONG).show();
		t.start();
	}

}
