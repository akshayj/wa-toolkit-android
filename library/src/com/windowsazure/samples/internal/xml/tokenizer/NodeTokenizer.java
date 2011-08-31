package com.windowsazure.samples.internal.xml.tokenizer;

import com.windowsazure.samples.internal.util.Coalescer;
import com.windowsazure.samples.internal.xml.XmlAttribute;
import com.windowsazure.samples.internal.xml.XmlElement;
import com.windowsazure.samples.internal.xml.XmlNamespace;
import com.windowsazure.samples.internal.xml.XmlNode;


// XML recursive-descent parser
public class NodeTokenizer extends AbstractTokenizer {

	public NodeTokenizer(String input) {
		super(input);
	}
	
	public XmlNode getRootNode() {
		skipOverHeader();
		XmlNode node = getNode();
		return node;
	}
	
	public XmlAttribute getAttribute() {
		skipWhiteSpace();
		
		Token identifierToken = getIdentifier();
		if (identifierToken == null)
			return null;
		
		if (identifierToken.getRepresentation().toLowerCase().indexOf(NAMESPACE_IDENTIFIER) != -1) {
			pushBack(identifierToken);
			return null;
		}
		
		skipWhiteSpace();
		getChar('=');
		skipWhiteSpace();
		
		Token quotedStringToken = getQuotedString();
		
		String fullName = identifierToken.getRepresentation();
		int tagIndex = fullName.indexOf(':');
		String tag = (tagIndex != -1) ? fullName.substring(0, tagIndex) : null;
		String name = (tagIndex != -1) ? fullName.substring(tagIndex + 1, fullName.length()) : fullName;
		
		return (quotedStringToken != null) ? new XmlAttribute(tag, name, quotedStringToken.getRepresentation()) : null;
	}
	
	protected void addChildrenToNode(XmlNode node) {
		XmlNode childNode = getNode();
		while (childNode != null) {
			node.addChild(childNode);
			childNode = getNode();
		}
	}
	
	protected Token getIdentifier() {
		skipWhiteSpace();
		
		Token firstCharToken = getCharInSet(IDENTIFIER_FIRST_CHAR);
		if (firstCharToken == null)
			return null;
		
		while (position < endPosition && IDENTIFIER_CHAR.indexOf(input[position++]) != -1)
			;
		if (position < endPosition)
			return new Token("identifier", inputText.substring(firstCharToken.getPosition(), --position), firstCharToken.getPosition());
		else
			return null;
	}
	
	protected String getInnerText() {
		int p = position;
		while (position < endPosition && input[position++] != '<')
			;
		if (position < endPosition)
			return inputText.substring(p, --position);
		else
			return null;
	}
	
	protected XmlNamespace getNamespace() {
		skipWhiteSpace();
		
		Token identifierToken = getIdentifier();
		if (identifierToken == null)
			return null;
		
		if (identifierToken.getRepresentation().toLowerCase().indexOf(NAMESPACE_IDENTIFIER) == -1) {
			pushBack(identifierToken);
			return null;
		}
		
		skipWhiteSpace();
		getChar('=');
		skipWhiteSpace();
		
		Token quotedStringToken = getQuotedString();
		
		String fullName = identifierToken.getRepresentation();
		int tagIndex = fullName.indexOf(':');
		String tag = (tagIndex != -1) ? fullName.substring(tagIndex + 1, fullName.length()) : null;
		
		return (quotedStringToken != null) ? new XmlNamespace(tag, quotedStringToken.getRepresentation()) : null;
	}
	
	protected XmlNode getNode() {
		skipWhiteSpace();
		
		Token nodeBeginToken = getChar('<');
		if (nodeBeginToken == null)
			return null;
		
		Token identifierToken = getIdentifier();
		if (identifierToken == null) {	// We encountered "</..."
			pushBack(nodeBeginToken);
			return null;
		}
		
		String fullName = identifierToken.getRepresentation();
		int tagIndex = fullName.indexOf(':');
		String tag = (tagIndex != -1) ? fullName.substring(0, tagIndex) : null;
		String name = (tagIndex != -1) ? fullName.substring(tagIndex + 1, fullName.length()) : fullName;
		XmlNode node = new XmlNode(tag, name);
		
		XmlElement element = null;
		do {
			XmlNamespace namespace = getNamespace();
			if (namespace != null)
				node.addNamespace(namespace);
			
			XmlAttribute attribute = getAttribute();
			if (attribute != null)
				node.addAttribute(attribute);
			
			element = new Coalescer<XmlElement>().coalesce(namespace, attribute);
		} while (element != null);
		
		skipWhiteSpace();
		if (getLiteral("node-end", "/>") != null)
			return node;
		
		Token tagEndToken = getChar('>');
		assert(tagEndToken != null);
		
		skipWhiteSpace();
		Token childBeginToken = getChar('<');
		if (childBeginToken != null) {
			pushBack(childBeginToken);
			addChildrenToNode(node);
			skipToNodeEnd();
			return node;
		}
		
		String innerText = getInnerText();
		node.setInnerText(innerText);
		skipToNodeEnd();
		return node;
	}
	
	protected boolean skipOverHeader() {
		Token token = getLiteral("header-begin", HEADER_BEGIN);
		if (token == null)
			return false;
		
		skipTo(HEADER_END);
		return true;
	}
	
	protected void skipToNodeEnd() {
		skipTo('>');
		if (position < endPosition)
			++position;
	}
	
	private static final String HEADER_BEGIN = "<?";
	private static final String HEADER_END = "?>";
	private static final String IDENTIFIER_FIRST_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";
	private static final String IDENTIFIER_CHAR = IDENTIFIER_FIRST_CHAR + "0123456789-.:";
	private static final String NAMESPACE_IDENTIFIER = "xmlns";
}
