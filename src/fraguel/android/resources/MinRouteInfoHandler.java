package fraguel.android.resources;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fraguel.android.MinRouteInfo;

public class MinRouteInfoHandler extends DefaultHandler {

	private boolean _in_nametag;
	private boolean _in_urltag;
	private boolean _in_arfilestag;
	private MinRouteInfo _currentRoute;
	private String arfiles;
	private ArrayList<MinRouteInfo> _allroutes;
	private StringBuffer buffer;

	public void endDocument() throws SAXException {
	}

	public void startDocument() throws SAXException {
		_in_nametag = false;
		_in_urltag = false;
		_in_arfilestag = false;
		_allroutes = new ArrayList<MinRouteInfo>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		buffer = new StringBuffer();
		if (localName.equals("route")) {
			_currentRoute = new MinRouteInfo();
			_currentRoute.id = Integer.parseInt(attributes.getValue("id"));
			_currentRoute.version = Float.parseFloat(attributes
					.getValue("version"));
		} else if (localName.equals("name")) {
			_in_nametag = true;
		} else if (localName.equals("url")) {
			_in_urltag = true;
		} else if (localName.equals("arfiles")) {
			arfiles = "";
			_in_arfilestag = true;
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (_in_nametag) {
			buffer.append(ch, start, length);
			_currentRoute.name = buffer.toString();
		} else if (_in_urltag) {
			buffer.append(ch, start, length);
			_currentRoute.urlXml = buffer.toString();
		} else if (_in_arfilestag) {
			buffer.append(ch, start, length);
			arfiles = buffer.toString();
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("route")) {
			_currentRoute.name = _currentRoute.name.replace("\\n", "\n");
			_allroutes.add(_currentRoute);
		} else if (localName.equals("name")) {
			_in_nametag = false;
		} else if (localName.equals("url")) {
			_in_urltag = false;
		} else if (localName.equals("arfiles")) {
			_in_arfilestag = false;
			if (!arfiles.equals(""))
				_currentRoute.urlArFiles = this.arfiles.split(",");
			else
				_currentRoute.urlArFiles = null;
		}
	}

	public ArrayList<MinRouteInfo> getParsedData() {
		return _allroutes;
	}
}
