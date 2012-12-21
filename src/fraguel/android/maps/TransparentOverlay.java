package fraguel.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class TransparentOverlay extends Overlay {

	Paint innerPaint, borderPaint;
	MapActivity mma = null;
	GeoPoint location = null;

	public TransparentOverlay(GeoPoint location, MapActivity mma) {
		super();
		this.location = location;
		this.mma = mma;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Point screenPoint = new Point();
		mapView.getProjection().toPixels(this.location, screenPoint);

		innerPaint = new Paint();
		innerPaint.setARGB(180, 50, 200, 50);

		borderPaint = new Paint();
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);

		RectF drawRect = new RectF();
		drawRect.set(0, 5, 300, 60);

		canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);

	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		return super.onTap(p, mapView);
	}

}
