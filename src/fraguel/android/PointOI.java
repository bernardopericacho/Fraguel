package fraguel.android;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Pair;

public class PointOI implements Serializable {
	private static final long serialVersionUID = 8799656478674716638L;
	public int id;
	public float[] coords = { 0.0f, 0.0f };
	public String title;
	public String icon;
	public ArrayList<Pair<String, String>> images;
	public String pointdescription;
	public String image;
	public String video;
	public String textAr;
	public String[] urlfilesAr;
	public float[] arCoords = { 0.0f, 0.0f, 0.0f };

	public PointOI() {
		id = 0;
		title = "";
		pointdescription = "";
		textAr = "";
		urlfilesAr = null;
	}

	public void setImages(ArrayList<Pair<String, String>> imgs) {

		this.images = imgs;

	}
}
