package fraguel.android.resources;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fraguel.android.Route;

public class RouteHandler extends DefaultHandler {

	private boolean _in_nametag;
	private boolean _in_descriptiontag;
	private boolean _in_icontag;

	private Route _currentRoute;
	private Route _route;
	private StringBuffer buffer;

	public void endDocument() throws SAXException {
	}

	public void startDocument() throws SAXException {
		_in_nametag = false;
		_in_descriptiontag = false;
		_in_icontag = false;
		_currentRoute = new Route();
		_route = new Route();
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (_in_nametag) {
			buffer.append(ch, start, length);
			_currentRoute.name = buffer.toString();
		} else if (_in_descriptiontag) {
			buffer.append(ch, start, length);
			_currentRoute.description = buffer.toString();
		} else if (_in_icontag) {
			buffer.append(ch, start, length);
			_currentRoute.icon = buffer.toString();
		}
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		buffer = new StringBuffer();
		if (localName.equals("route")) {
			_currentRoute.id = Integer.parseInt(attributes.getValue("id"));
			_currentRoute.version = Float.parseFloat(attributes
					.getValue("version"));
		} else if (localName.equals("name")) {
			_in_nametag = true;
		} else if (localName.equals("description")) {
			_in_descriptiontag = true;
		} else if (localName.equals("icon")) {
			_in_icontag = true;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("route")) {
			_route = _currentRoute;
			_route.name = _route.name.replace("\\n", "\n");
			_route.description = _route.description.replace("\\n", "\n")
					.replace("\\t", "\t");
		} else if (localName.equals("name")) {
			_in_nametag = false;
		} else if (localName.equals("description")) {
			_in_descriptiontag = false;
		} else if (localName.equals("icon")) {
			_in_icontag = false;
		}
	}

	public Route getParsedData() {
		return _route;
	}
}