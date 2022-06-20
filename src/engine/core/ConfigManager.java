package engine.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import engine.utils.Functions;

public class ConfigManager {
	private Document doc;
	private String fileName;
	
	public ConfigManager(String name) {
		fileName = "/assets/config/"+name+".xml";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder docBuilder;
		InputStream file = ConfigManager.class.getResourceAsStream(fileName);
		try {
			docBuilder = dbf.newDocumentBuilder();
			doc = docBuilder.parse(file);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setValue(String path, String value) {
		if(doc != null) {
			String aux[] = path.split("\\.");
			Element e = getNode(doc.getDocumentElement(), aux);
			if(e != null) {
				e.setTextContent(value);
			}
			
		}
		save();
	}
	
	public void save() {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			URL url = ConfigManager.class.getResource(fileName);
			Result output;
			output = new StreamResult(new File(url.toURI()));
			Source input = new DOMSource(doc);	
			transformer.transform(input, output);
			System.out.println("teste");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getValue(String path) {
		if(doc != null) {
			String aux[] = path.split("\\.");
			return getNode(doc.getDocumentElement(), aux).getTextContent();
		}
		
		return "";
	}
	
	private Element getNode(Element element, String path[]) {
		Element r = null;
		if(path.length==0) {
			return element;
		}
		NodeList nList = element.getElementsByTagName(getName(path[0]));
		for (int i = 0; i < nList.getLength(); i++) {
			Element eElement = (Element) nList.item(i);
			HashMap<String, String> att = getAttributes(path[0]);
			if(att.size() > 0) {	
				NamedNodeMap l_att = eElement.getAttributes();
				Boolean pass = true;
				for(int j = 0; j < l_att.getLength(); j++) {
					if(att.containsKey(l_att.item(j).getNodeName())) {
						String v = att.get(l_att.item(j).getNodeName());
						if(!l_att.item(j).getNodeValue().equals(v)) {
							pass = false;
						}						
					}
				}
				if(pass) {
					r = getNode(eElement, Arrays.copyOfRange(path, 1, path.length));
				}
			} else {
				r = getNode(eElement, Arrays.copyOfRange(path, 1, path.length));
			}
		}
		return r;
	}
	
	private HashMap<String, String> getAttributes(String s) {
		HashMap<String, String> r = new HashMap<String, String>();
		String aux = "";
		Boolean give = false;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == ')') {
				give = false;
			}
			if(give) {
				aux += c;
			}
			if(c == '(') {
				give = true;
			}
		}
		
		String l[] = aux.split(",");
		for(int i = 0; i < l.length; i++) {
			String a = l[i];
			String b[] = a.split(":");
			if(b.length == 2) {
				r.put(b[0], b[1]);
			}
		}
		
		
		return r;
	}

	private String getName(String s) {
		return s.split("\\(")[0];
	}
}
