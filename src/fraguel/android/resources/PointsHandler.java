package fraguel.android.resources;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Pair;

import fraguel.android.PointOI;

public class PointsHandler extends DefaultHandler {

	private boolean _in_titletag;
	private boolean _in_icontag;
	private boolean _in_pointdescriptiontag;
	private boolean _in_imagestag;
	private boolean _in_imagetag;
	private boolean _in_videotag;
	private boolean _in_artag;

	private ArrayList<PointOI> _points;
	private PointOI _currentPoint;
	private ArrayList<Pair<String, String>> _currentImgs;
	private String _imageName = "";
	private String _imageUrl = "";
	private StringBuffer buffer;
	private String arFiles;

	public void endDocument() throws SAXException {
	}

	public void startDocument() throws SAXException {
		_in_titletag = false;
		_in_icontag = false;
		_in_imagestag = false;
		_in_videotag = false;
		_in_artag = false;
		_in_imagetag = false;
		_in_pointdescriptiontag = false;
		_points = new ArrayList<PointOI>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		buffer = new StringBuffer();
		if (localName.equals("point")) {
			_currentPoint = new PointOI();
			arFiles = "";
			_currentPoint.id = Integer.parseInt(attributes.getValue("id"));
		} else if (localName.equals("coords")) {
			_currentPoint.coords[0] = Float
					.parseFloat(attributes.getValue("x"));
			_currentPoint.coords[1] = Float
					.parseFloat(attributes.getValue("y"));
		} else if (localName.equals("title")) {
			_in_titletag = true;
		} else if (localName.equals("pointicon")) {
			_in_icontag = true;
		} else if (localName.equals("pointdescription")) {
			_in_pointdescriptiontag = true;
		} else if (localName.equals("images")) {
			_currentImgs = new ArrayList<Pair<String, String>>();
		} else if (localName.equals("image")) {
			_in_imagetag = true;
			_imageUrl = attributes.getValue("url");
		} else if (localName.equals("video")) {
			_in_videotag = true;
		} else if (localName.equals("ar")) {
			_in_artag = true;
			_currentPoint.arCoords[0] = Float.parseFloat(attributes
					.getValue("lat"));
			_currentPoint.arCoords[1] = Float.parseFloat(attributes
					.getValue("long"));
			_currentPoint.arCoords[2] = Float.parseFloat(attributes
					.getValue("alt"));
			this.arFiles = attributes.getValue("file");
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (_in_titletag) {
			buffer.append(ch, start, length);
			_currentPoint.title = buffer.toString();
		} else if (_in_icontag) {
			buffer.append(ch, start, length);
			_currentPoint.icon = buffer.toString();
		} else if (_in_pointdescriptiontag) {
			buffer.append(ch, start, length);
			_currentPoint.pointdescription = buffer.toString();
		} else if (_in_imagestag) {
			buffer.append(ch, start, length);
		} else if (_in_imagetag) {
			buffer.append(ch, start, length);
			_imageName = buffer.toString();
		} else if (_in_videotag) {
			buffer.append(ch, start, length);
			_currentPoint.video = buffer.toString();
		} else if (_in_artag) {
			buffer.append(ch, start, length);
			_currentPoint.textAr = buffer.toString();
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("point")) {
			_currentPoint.title = _currentPoint.title.replace("\\n", "\n");
			_currentPoint.pointdescription = _currentPoint.pointdescription
					.replace("\\n", "\n").replace("\\t", "\t");
			_currentPoint.textAr = _currentPoint.textAr.replace("\\n", "\n")
					.replace("\\t", "\t");
			if (!this.arFiles.equals("")) {
				_currentPoint.urlfilesAr = arFiles.split(",");
			}
			_points.add(_currentPoint);
		} else if (localName.equals("title")) {
			_in_titletag = false;
		} else if (localName.equals("pointicon")) {
			_in_icontag = false;
		} else if (localName.equals("pointdescription")) {
			_in_pointdescriptiontag = false;
		} else if (localName.equals("images")) {
			_currentPoint.images = _currentImgs;
		} else if (localName.equals("image")) {
			_in_imagetag = false;
			_currentImgs.add(new Pair<String, String>(_imageName, _imageUrl));
		} else if (localName.equals("video")) {
			_in_videotag = false;
		} else if (localName.equals("ar")) {
			_in_artag = false;
		}
	}

	public ArrayList<PointOI> getParsedData() {
		return _points;
	}
}
