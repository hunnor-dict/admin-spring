package net.hunnor.dict.admin.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.hunnor.dict.admin.config.Language;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = {"classpath:sql/hunnor-v3-create.sql", "classpath:sql/hunnor-v3-insert-export.sql"})
@Sql(scripts = {"classpath:sql/ordbank-create.sql", "classpath:sql/ordbank-insert-export.sql"})
public class ExportServiceTest {

  @Autowired
  private ExportService exportService;

  @Autowired
  private ParserService parserService;

  private static Document documentHu = null;

  private static Document documentNb = null;

  /**
   * Export and parse the result.
   * @throws ExportException if export fails
   */
  @Before
  public void setUp() throws ExportException {
    if (documentHu == null) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      exportService.export(Language.HU, outputStream);
      byte[] bytes = outputStream.toByteArray();
      String content = new String(bytes, StandardCharsets.UTF_8);
      documentHu = parserService.parse(content);
    }
    if (documentNb == null) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      exportService.export(Language.NB, outputStream);
      byte[] bytes = outputStream.toByteArray();
      String content = new String(bytes, StandardCharsets.UTF_8);
      documentNb = parserService.parse(content);
    }
  }

  @Test
  public void exportDefaultLanguage() throws ExportException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    exportService.export(Language.DEFAULT, outputStream);
  }

  @Test
  public void testHuEntryCount() {

    assertNotNull(documentHu);

    Element hnDict = documentHu.getDocumentElement();
    assertEquals("hnDict", hnDict.getLocalName());

    NodeList entryGrpList = hnDict.getElementsByTagName("entryGrp");
    assertEquals(2, entryGrpList.getLength());

    Element entryGrpA = (Element) entryGrpList.item(0);
    assertEquals("A", entryGrpA.getAttribute("head"));

    NodeList entryListA = entryGrpA.getElementsByTagName("entry");
    assertEquals(1, entryListA.getLength());

    Element entryGrpT = (Element) entryGrpList.item(1);
    assertEquals("T", entryGrpT.getAttribute("head"));

    NodeList entryListT = entryGrpT.getElementsByTagName("entry");
    assertEquals(1, entryListT.getLength());

  }

  @Test
  public void testHuEntry1() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '1']")
        .evaluate(documentHu, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(1, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("alma", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("fn", pos.getTextContent());

  }

  @Test
  public void testHuEntry2() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '2']")
        .evaluate(documentHu, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(2, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("tejföl", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("fn", pos.getTextContent());

    form = (Element) formList.item(1);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("no", form.getAttribute("primary"));

    orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("tejfel", orth.getTextContent());

    posList = form.getElementsByTagName("pos");
    assertEquals(0, posList.getLength());

  }

  @Test
  public void testNbEntryCount() {

    assertNotNull(documentNb);

    Element hnDict = documentNb.getDocumentElement();
    assertEquals("hnDict", hnDict.getLocalName());

    NodeList entryGrpList = hnDict.getElementsByTagName("entryGrp");
    assertEquals(4, entryGrpList.getLength());

    Element entryGrpB = (Element) entryGrpList.item(0);
    assertEquals("B", entryGrpB.getAttribute("head"));

    NodeList entryListB = entryGrpB.getElementsByTagName("entry");
    assertEquals(2, entryListB.getLength());

    Element entryGrpF = (Element) entryGrpList.item(1);
    assertEquals("F", entryGrpF.getAttribute("head"));

    NodeList entryListF = entryGrpF.getElementsByTagName("entry");
    assertEquals(4, entryListF.getLength());

    Element entryGrpJ = (Element) entryGrpList.item(2);
    assertEquals("J", entryGrpJ.getAttribute("head"));

    NodeList entryListJ = entryGrpJ.getElementsByTagName("entry");
    assertEquals(1, entryListJ.getLength());

    Element entryGrpR = (Element) entryGrpList.item(3);
    assertEquals("R", entryGrpR.getAttribute("head"));

    NodeList entryListR = entryGrpR.getElementsByTagName("entry");
    assertEquals(2, entryListR.getLength());

  }

  @Test
  public void testNbEntry1() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '1']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(1, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("bil", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("subst", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(2, inflCodeList.getLength());
    Element inflCodeBob = (Element) inflCodeList.item(0);
    assertTrue(inflCodeBob.hasAttributes());
    assertEquals(1, inflCodeBob.getAttributes().getLength());
    assertEquals("bob", inflCodeBob.getAttribute("type"));
    assertEquals("m1", inflCodeBob.getTextContent());
    Element inflCodeSuff = (Element) inflCodeList.item(1);
    assertTrue(inflCodeSuff.hasAttributes());
    assertEquals(1, inflCodeSuff.getAttributes().getLength());
    assertEquals("suff", inflCodeSuff.getAttribute("type"));
    assertEquals("-en", inflCodeSuff.getTextContent());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(1, inflParList.getLength());

    Element inflPar = (Element) inflParList.item(0);
    NodeList inflSeqList = inflPar.getElementsByTagName("inflSeq");
    assertEquals(3, inflSeqList.getLength());

    Element inflSeq = (Element) inflSeqList.item(0);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-0", inflSeq.getAttribute("form"));
    assertEquals("bilen", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(1);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-1", inflSeq.getAttribute("form"));
    assertEquals("biler", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(2);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-2", inflSeq.getAttribute("form"));
    assertEquals("bilene", inflSeq.getTextContent());

  }

  @Test
  public void testNbEntry2() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '2']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(1, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("jente", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("subst", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(2, inflCodeList.getLength());
    Element inflCodeBob = (Element) inflCodeList.item(0);
    assertTrue(inflCodeBob.hasAttributes());
    assertEquals(1, inflCodeBob.getAttributes().getLength());
    assertEquals("bob", inflCodeBob.getAttribute("type"));
    assertEquals("f1:m1", inflCodeBob.getTextContent());
    Element inflCodeSuff = (Element) inflCodeList.item(1);
    assertTrue(inflCodeSuff.hasAttributes());
    assertEquals(1, inflCodeSuff.getAttributes().getLength());
    assertEquals("suff", inflCodeSuff.getAttribute("type"));
    assertEquals("-en/-a", inflCodeSuff.getTextContent());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(2, inflParList.getLength());

    Element inflPar = (Element) inflParList.item(0);
    NodeList inflSeqList = inflPar.getElementsByTagName("inflSeq");
    assertEquals(3, inflSeqList.getLength());

    Element inflSeq = (Element) inflSeqList.item(0);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-0", inflSeq.getAttribute("form"));
    assertEquals("jenten", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(1);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-1", inflSeq.getAttribute("form"));
    assertEquals("jenter", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(2);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-2", inflSeq.getAttribute("form"));
    assertEquals("jentene", inflSeq.getTextContent());

    inflPar = (Element) inflParList.item(1);
    inflSeqList = inflPar.getElementsByTagName("inflSeq");
    assertEquals(3, inflSeqList.getLength());

    inflSeq = (Element) inflSeqList.item(0);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("1-0", inflSeq.getAttribute("form"));
    assertEquals("jenta", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(1);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("1-1", inflSeq.getAttribute("form"));
    assertEquals("jenter", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(2);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("1-2", inflSeq.getAttribute("form"));
    assertEquals("jentene", inflSeq.getTextContent());

  }

  @Test
  public void testNbEntry3() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '3']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(2, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("1", orth.getAttribute("n"));
    assertEquals("ferge", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("subst", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(0, inflCodeList.getLength());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(0, inflParList.getLength());

    form = (Element) formList.item(1);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("no", form.getAttribute("primary"));

    orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("1", orth.getAttribute("n"));
    assertEquals("ferje", orth.getTextContent());

    posList = form.getElementsByTagName("pos");
    assertEquals(0, posList.getLength());

    inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(0, inflCodeList.getLength());

    inflParList = form.getElementsByTagName("inflPar");
    assertEquals(0, inflParList.getLength());

  }

  @Test
  public void testNbEntry5() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '5']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(2, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("2", orth.getAttribute("n"));
    assertEquals("ferge", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("verb", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(0, inflCodeList.getLength());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(1, inflParList.getLength());

    Element inflPar = (Element) inflParList.item(0);
    NodeList inflSeqList = inflPar.getElementsByTagName("inflSeq");
    assertEquals(2, inflSeqList.getLength());

    Element inflSeq = (Element) inflSeqList.item(0);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-0", inflSeq.getAttribute("form"));
    assertEquals("ferger", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(1);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-1", inflSeq.getAttribute("form"));
    assertEquals("ferga", inflSeq.getTextContent());

    form = (Element) formList.item(1);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("no", form.getAttribute("primary"));

    orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("2", orth.getAttribute("n"));
    assertEquals("ferje", orth.getTextContent());

    posList = form.getElementsByTagName("pos");
    assertEquals(0, posList.getLength());

    inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(0, inflCodeList.getLength());

    inflParList = form.getElementsByTagName("inflPar");
    assertEquals(1, inflParList.getLength());

    inflPar = (Element) inflParList.item(0);
    inflSeqList = inflPar.getElementsByTagName("inflSeq");
    assertEquals(2, inflSeqList.getLength());

    inflSeq = (Element) inflSeqList.item(0);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-0", inflSeq.getAttribute("form"));
    assertEquals("ferjer", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(1);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-1", inflSeq.getAttribute("form"));
    assertEquals("ferja", inflSeq.getTextContent());

  }

  @Test
  public void testNbEntry7() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '7']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(1, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("brannbil", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("subst", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(0, inflCodeList.getLength());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(0, inflParList.getLength());

  }

  @Test
  public void testNbEntry8() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '8']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(1, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("forsvarsdepartementet", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("subst", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(0, inflCodeList.getLength());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(0, inflParList.getLength());

  }

  @Test
  public void testNbEntry9() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '9']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(1, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("rask", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("adj", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(2, inflCodeList.getLength());
    Element inflCodeBob = (Element) inflCodeList.item(0);
    assertTrue(inflCodeBob.hasAttributes());
    assertEquals(1, inflCodeBob.getAttributes().getLength());
    assertEquals("bob", inflCodeBob.getAttribute("type"));
    assertEquals("a1", inflCodeBob.getTextContent());
    Element inflCodeSuff = (Element) inflCodeList.item(1);
    assertTrue(inflCodeSuff.hasAttributes());
    assertEquals(1, inflCodeSuff.getAttributes().getLength());
    assertEquals("suff", inflCodeSuff.getAttribute("type"));
    assertEquals("-t, -e", inflCodeSuff.getTextContent());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(1, inflParList.getLength());

    Element inflPar = (Element) inflParList.item(0);
    NodeList inflSeqList = inflPar.getElementsByTagName("inflSeq");
    assertEquals(3, inflSeqList.getLength());

    Element inflSeq = (Element) inflSeqList.item(0);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-0", inflSeq.getAttribute("form"));
    assertEquals("raskt", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(1);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-1", inflSeq.getAttribute("form"));
    assertEquals("raske", inflSeq.getTextContent());
    inflSeq = (Element) inflSeqList.item(2);
    assertTrue(inflSeq.hasAttributes());
    assertEquals(1, inflSeq.getAttributes().getLength());
    assertEquals("0-2", inflSeq.getAttribute("form"));
    assertEquals("raske", inflSeq.getTextContent());

  }

  @Test
  public void testNbEntry10() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '10']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(1, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("raskt", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("adv", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(0, inflCodeList.getLength());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(0, inflParList.getLength());

  }

  @Test
  public void testEntry11() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '11']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNotNull(entry);

    NodeList formList = (NodeList) xpath
        .compile("h:formGrp/h:form")
        .evaluate(entry, XPathConstants.NODESET);
    assertEquals(1, formList.getLength());

    Element form = (Element) formList.item(0);
    assertTrue(form.hasAttributes());
    assertEquals(1, form.getAttributes().getLength());
    assertEquals("yes", form.getAttribute("primary"));

    NodeList orthList = form.getElementsByTagName("orth");
    assertEquals(1, orthList.getLength());
    Element orth = (Element) orthList.item(0);
    assertTrue(orth.hasAttributes());
    assertEquals(1, orth.getAttributes().getLength());
    assertEquals("0", orth.getAttribute("n"));
    assertEquals("fort", orth.getTextContent());

    NodeList posList = form.getElementsByTagName("pos");
    assertEquals(1, posList.getLength());
    Element pos = (Element) posList.item(0);
    assertFalse(pos.hasAttributes());
    assertEquals("adv", pos.getTextContent());

    NodeList inflCodeList = form.getElementsByTagName("inflCode");
    assertEquals(0, inflCodeList.getLength());

    NodeList inflParList = form.getElementsByTagName("inflPar");
    assertEquals(0, inflParList.getLength());

  }

  @Test
  public void testNbEntry99() throws XPathExpressionException {

    XPath xpath = getXPath();

    Element entry = (Element) xpath
        .compile("/h:hnDict/h:entryGrp/h:entry[@id = '99']")
        .evaluate(documentNb, XPathConstants.NODE);

    assertNull(entry);

  }

  private XPath getXPath() {
    XPath xpath = XPathFactory.newInstance().newXPath();
    xpath.setNamespaceContext(new NamespaceContext() {

      @Override
      public String getNamespaceURI(String prefix) {
        if ("h".equals(prefix)) {
          return "http://dict.hunnor.net";
        }
        return null;
      }

      @Override
      public String getPrefix(String namespaceUri) {
        return null;
      }

      @Override
      public Iterator<String> getPrefixes(String namespaceUri) {
        return null;
      }

    });
    return xpath;
  }

}
