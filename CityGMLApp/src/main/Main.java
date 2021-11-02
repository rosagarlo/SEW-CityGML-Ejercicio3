package main;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import console.Console;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("Introduce el nombre del archivo: ");
		String archivo_gml = Console.readString();
		parse(archivo_gml);
	}

	public static void parse(String archivo_gml) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			ClassLoader classloader = Main.class.getClassLoader();
			InputStream xmlData = classloader.getResourceAsStream(archivo_gml);

			// procesa el XML con seguridad, evita ataques como XML External
			// Entities (XXE)
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			// parsear el archivo XML
			Document doc = dbf.newDocumentBuilder().parse(xmlData);

			// normalizar el DOM (eliminar \r o \n)
			doc.getDocumentElement().normalize();

			// lista de las personas
			NodeList list = doc.getElementsByTagName("gml:LinearRing");

			for (int i = 0; i < list.getLength()-1; i++) {

				Node node = list.item(0);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) node;
					
					NodeList coordList = element
							.getElementsByTagName("gml:posList");
					String dimension = coordList.item(0).getAttributes()
							.getNamedItem("srsDimension").getTextContent();
					
					Console.print("Dimensiones de la parcela: " + dimension);
					
					String coord = element.getElementsByTagName("gml:posList").item(0)
							.getTextContent();
					
					Console.print("\nCoordenadas de la parcela: " + coord);
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

}
