package fraguel.android;

import java.util.ArrayList;

public class Route {

	public int id;
	public float version;
	public String name;
	public String description;
	public ArrayList<PointOI> pointsOI;
	public String icon;

	public Route() {
		id = 0;
		name = "default";
		description = "none";
		version = 1.0f;
		pointsOI = new ArrayList<PointOI>();
	}

	public Route(int i, String n, String d) {
		id = i;
		name = n;
		description = d;
	}
}
