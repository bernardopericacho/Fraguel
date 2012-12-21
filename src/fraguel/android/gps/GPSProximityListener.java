package fraguel.android.gps;

import android.location.Location;
import fraguel.android.FRAGUEL;
import fraguel.android.PointOI;
import fraguel.android.Route;
import fraguel.android.states.MapState;
import fraguel.android.states.PointInfoState;

public class GPSProximityListener extends GPSProximity {

	public GPSProximityListener() {

		super();
	}

	@Override
	public synchronized void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		altitude = location.getAltitude();
		distance = Float.MAX_VALUE;

		// hay un punto siendo visitado y en el radio
		if (MapState.getInstance().getGPS().isDialogDisplayed()) {
			PointInfoState state = (PointInfoState) FRAGUEL.getInstance()
					.getCurrentState();
			Location.distanceBetween(latitude, longitude,
					state.getPointOI().coords[0], state.getPointOI().coords[1],
					results);
			// si está fuera de rango cargamos el mapa
			float offset = Math.abs(lastFix - results[0]);
			if (offset <= 10.0f) {
				lastFix = results[0];
				hasBeenVisited = true;
			} else
				hasBeenVisited = false;
			if ((results[0] > proximityAlertDistance + proximityAlertError)
					&& hasBeenVisited) {
				FRAGUEL.getInstance().changeState(MapState.STATE_ID);
				MapState.getInstance().loadData(currentRoute, currentPoint);
				MapState.getInstance().getGPS().setDialogDisplayed(false);
			}
		} else if (!MapState.getInstance().getGPS().isDialogDisplayed()
				&& FRAGUEL.getInstance().getCurrentState().getId() == MapState.STATE_ID) {
			// si no hay ningún punto siendo visualizado , cogemos el de menos
			// distancia, siempre dentro del rango
			for (Route r : FRAGUEL.getInstance().routes) {
				for (PointOI p : r.pointsOI) {

					Location.distanceBetween(latitude, longitude, p.coords[0],
							p.coords[1], results);

					if (results[0] <= proximityAlertDistance) {
						if (results[0] < distance) {
							currentRoute = r;
							currentPoint = p;
							distance = results[0];
						}
					}
				}
			}
			if (currentRoute != null && currentPoint != null
					&& !MapState.getInstance().getGPS().isDialogDisplayed()) {
				super.mediaNotification();
				FRAGUEL.getInstance().changeState(PointInfoState.STATE_ID);
				FRAGUEL.getInstance().getCurrentState()
						.loadData(currentRoute, currentPoint);
				MapState.getInstance().getGPS().setDialogDisplayed(true);
				lastFix = results[0];
			}
		}

	}

}
