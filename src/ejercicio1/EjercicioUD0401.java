package ejercicio1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class EjercicioUD0401 {

	private static Hashtable<String, String> cp = new Hashtable<String, String>();

	static void mostrarXML(Node nodrec) {
		NodeList listanodos = nodrec.getChildNodes();
		for (int i = 0; i < listanodos.getLength(); i++) {
			Node nodo = listanodos.item(i);
			Element elemento = (Element) nodo;
			mostrarNombreValor(elemento.getNodeName(), "", 0);
			NodeList listaPersonajes = elemento
					.getElementsByTagName("character"); /* A partir de un elemento, sacamos las etiquetas */
			for (int j = 0; j < listaPersonajes.getLength(); j++) {
				Element elemento2 = (Element) listaPersonajes.item(j);
				mostrarNombreValor(elemento2.getNodeName(), "", 1);
				mostrarNombreValor(elemento2.getElementsByTagName("id").item(0).getNodeName(),
						elemento2.getElementsByTagName("id").item(0).getTextContent(), 2);
				mostrarNombreValor(elemento2.getElementsByTagName("name").item(0).getNodeName(),
						elemento2.getElementsByTagName("name").item(0).getTextContent(), 2);
				NodeList listaTitulos = elemento2.getElementsByTagName("titles");
				for (int k = 0; k < listaTitulos.getLength(); k++) {
					Element elemento3 = (Element) listaTitulos.item(k);
					mostrarNombreValor(elemento3.getNodeName(), elemento3.getTextContent(), 3);
				}
			}
		}
	}

	static void mostrarXMLIterativo(Node nodrec) {

		NodeList listanodos = nodrec.getChildNodes();
		// NIVEL 1
		for (int i = 0; i < listanodos.getLength(); i++) {
			if (listanodos.item(i).getNodeType() == Node.TEXT_NODE && !(listanodos.item(i).getNodeValue().isEmpty())) {
				imprimeNodo(listanodos.item(i), 1);
			} else {
				imprimeNodo(listanodos.item(i), 0);
				NodeList listanodos2 = listanodos.item(i).getChildNodes();
				// NIVEL 2
				for (int j = 0; j < listanodos2.getLength(); j++) {
					if (listanodos2.item(j).getNodeType() == Node.TEXT_NODE
							&& !(listanodos2.item(j).getNodeValue().isEmpty())) {
						imprimeNodo(listanodos2.item(j), 2);
					} else {
						imprimeNodo(listanodos2.item(j), 1);
						NodeList listanodos3 = listanodos2.item(j).getChildNodes();
						// NIVEL 3
						for (int k = 0; k < listanodos3.getLength(); k++) {
							if (listanodos3.item(k).getNodeType() == Node.TEXT_NODE
									&& !(listanodos3.item(k).getNodeValue().isEmpty())) {
								imprimeNodo(listanodos3.item(k), 3);
							} else {
								imprimeNodo(listanodos3.item(k), 2);
								NodeList listanodos4 = listanodos3.item(k).getChildNodes();
								// NIVEL 4
								for (int l = 0; l < listanodos4.getLength(); l++) {
									if (listanodos4.item(l).getNodeType() == Node.TEXT_NODE
											&& !(listanodos4.item(l).getNodeValue().isEmpty())) {
										imprimeNodo(listanodos4.item(l), 4);

									} else {
										imprimeNodo(listanodos4.item(l), 3);
										imprimeNodo(listanodos4.item(l).getFirstChild(), 4);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	static void mostrarXMLRecursivo(Node nodo, int nivel) {
		if (nodo.getNodeType() == Node.TEXT_NODE) { // Ignoramos textos vacíos
			String text = nodo.getNodeValue();
			if (text.trim().length() == 0) {
				return;
			}
		}

		if (nodo.getNodeType() == Node.ELEMENT_NODE || nodo.getNodeType() == Node.TEXT_NODE)
			imprimeNodo(nodo, nivel);

		NodeList nodosHijos = nodo.getChildNodes(); // Muestra nodos hijos
		for (int i = 0; i < nodosHijos.getLength(); i++) {
			mostrarXMLRecursivo(nodosHijos.item(i), nivel + 1);
		}
	}

	static void mostrarNombreValor(String nombre, String valor, int nivel) {
		for (int i = 0; i < nivel; i++) {
			System.out.print("---");
		}
		System.out.println(nombre + ": " + valor);
	}

	static void imprimeNodo(Node nodo, int nivel) {
		switch (nodo.getNodeType()) { // Escribe información de nodo segúnn tipo
		case Node.ELEMENT_NODE: // Elemento
			mostrarNombreoValor(nodo.getNodeName(), nivel, true);
			break;
		case Node.TEXT_NODE: // Texto
			String text = nodo.getNodeValue();
			if (text.trim().length() == 0) {
				return;
			}
			mostrarNombreoValor(nodo.getNodeValue(), nivel, false);
			break;
		default: // Otro tipo de nodo, como Document, no hacemos nada
		}
	}

	static void mostrarNombreoValor(String nombre, int nivel, boolean nom) {
		if (nom) {
			System.out.println();
			for (int i = 0; i < nivel; i++) {
				System.out.print("---");
			}
			System.out.print(nombre + ": ");
		} else
			System.out.print(nombre);
	}

	static void anyadirPlayedBy(Node nodo, Document doc) {
		if (nodo.getNodeType() == Node.ELEMENT_NODE && nodo.getNodeName().equals("name")) {
			String character = nodo.getFirstChild().getNodeValue();
			if (character.contains("Stark")) {
				Element elem = doc.createElement("playedBy"); // creamos un hijo
				Text text = null;

				if (cp.containsKey(character)) {
					text = doc.createTextNode(cp.get(character)); // damos valor
					nodo.getParentNode().appendChild(elem); // pegamos el elemento hijo al nodo
					elem.appendChild(text); // pegamos el valor
				}
			}
		}

		NodeList nodosHijos = nodo.getChildNodes(); // Muestra nodos hijos
		for (int i = 0; i < nodosHijos.getLength(); i++) {
			anyadirPlayedBy(nodosHijos.item(i), doc);
		}
	}

	static void anyadirPersonaje(Document doc) {

		// CREAMOS EL PERSONAJE
		Element personaje = doc.createElement("character"); // creamos un hijo
		doc.getDocumentElement().appendChild(personaje);

		// CREAMOS ID
		Element elem = doc.createElement("id"); // creamos un hijo
		Text text = doc.createTextNode("583"); // damos valor
		elem.appendChild(text); // pegamos el valor
		personaje.appendChild(elem); // pegamos el elemento hijo

		// CREAMOS NOMBRE
		elem = doc.createElement("name");
		text = doc.createTextNode("Jon Snow");
		elem.appendChild(text);
		personaje.appendChild(elem);

		// CREAMOS NACIMIENTO
		elem = doc.createElement("born");
		text = doc.createTextNode("In 283 AC, at Winterfell");
		elem.appendChild(text);
		personaje.appendChild(elem);

		// CREAMOS SI VIVO
		elem = doc.createElement("alive");
		text = doc.createTextNode("FALSE");
		elem.appendChild(text);
		personaje.appendChild(elem);

		// CREAMOS TITULOS
		Element titulos = doc.createElement("titles");
		Element titulo = doc.createElement("title");
		text = doc.createTextNode("Lord Commander of the Night's Watch");
		titulo.appendChild(text);
		titulos.appendChild(titulo);
		titulo = doc.createElement("title");
		text = doc.createTextNode("King in the North");
		titulo.appendChild(text);
		titulos.appendChild(titulo);
		personaje.appendChild(titulos);

		// CREAMOS ALIAS
		Element aliases = doc.createElement("aliases");
		Element alias = doc.createElement("alias");
		text = doc.createTextNode("Lord Snow");
		alias.appendChild(text);
		aliases.appendChild(alias);
		alias = doc.createElement("alias");
		text = doc.createTextNode("Ned Stark's Bastard");
		alias.appendChild(text);
		aliases.appendChild(alias);
		alias = doc.createElement("alias");
		text = doc.createTextNode("The Snow of Winterfell");
		alias.appendChild(text);
		aliases.appendChild(alias);
		alias = doc.createElement("alias");
		text = doc.createTextNode("The Crow-Come-Over");
		alias.appendChild(text);
		aliases.appendChild(alias);
		alias = doc.createElement("alias");
		text = doc.createTextNode("The 998th Lord Commander of the Night's Watch");
		alias.appendChild(text);
		aliases.appendChild(alias);
		alias = doc.createElement("alias");
		text = doc.createTextNode("The Bastard of Winterfell");
		alias.appendChild(text);
		aliases.appendChild(alias);
		alias = doc.createElement("alias");
		text = doc.createTextNode("The Black Bastard of the Wall");
		alias.appendChild(text);
		aliases.appendChild(alias);
		alias = doc.createElement("alias");
		text = doc.createTextNode("Lord Crow");
		alias.appendChild(text);
		aliases.appendChild(alias);
		personaje.appendChild(aliases);

		// CREAMOS SI VIVO
		elem = doc.createElement("playedBy");
		text = doc.createTextNode("Kit Harington");
		elem.appendChild(text);
		personaje.appendChild(elem);

	}

	public static void main(String[] args) {

		String nomFich = "GOTini.xml";
		String eixidaFitxer = "GOTend.xml";

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document domDoc = db.parse(new File(nomFich));

			mostrarXML(domDoc);
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------\n");
			mostrarXMLIterativo(domDoc);

			System.out.println("\n\n--------------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------\n");
			mostrarXMLRecursivo(domDoc, 0);

			System.out.println("\n\n--------------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------\n");
			mostrarXMLRecursivo(domDoc, 0);

			cp.put("Arya Stark", "Alfie Allen");
			cp.put("Brandon Stark", "Isaac Hempstead-Wright");
			cp.put("Rickon Stark", "Art Parkinson");
			cp.put("Robb Stark", "Richard Madden");
			cp.put("Sansa Stark", "Sophie Turner");
			anyadirPlayedBy(domDoc, domDoc);
			anyadirPersonaje(domDoc);

			DOMSource domSource = new DOMSource(domDoc);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			BufferedWriter bw = new BufferedWriter(new FileWriter(eixidaFitxer));
			StreamResult sr = new StreamResult(bw);
			transformer.transform(domSource, sr);
			bw.close();

		} catch (ParserConfigurationException | SAXException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}