package net.hunnor.dict.admin.edit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SolrServiceImpl implements SolrService {

  private static final Logger logger = LoggerFactory.getLogger(SolrServiceImpl.class);

  private final HttpClient httpClient = HttpClient.newHttpClient();

  @Value("${net.hunnor.dict.admin.solr.url:${SOLR_URL:}}")
  private String solrUrl;

  @Override
  public void save(String language, String id, Integer entryId, String xml) {
    if (!StringUtils.hasText(solrUrl)) {
      logger.warn("Skipping Solr save because Solr URL is not configured");
      return;
    }

    String updateUrl = updateUrl(language);
    boolean matchesEntry = matchesEntry(id, entryId);
    logger.debug("Solr save requested: "
        + "language='{}', id='{}', entryId={}, matchesEntry={}, updateUrl='{}', xmlLength={}",
        language,
        id,
        entryId,
        matchesEntry,
        updateUrl,
        xml == null ? 0 : xml.length());
    if (matchesEntry) {
      logger.debug("Solr save will POST update document for id='{}'", id);
      postUpdate(updateUrl, xml);
    } else {
      logger.debug("Solr save will POST delete for id='{}' because entryId does not match", id);
      postDelete(updateUrl, id);
    }
    logger.debug("Solr save will POST commit to updateUrl='{}'", updateUrl);
    postCommit(updateUrl);
  }

  @Override
  public void delete(String language, String id, Integer entryId) {
    if (!StringUtils.hasText(solrUrl)) {
      logger.warn("Skipping Solr delete because Solr URL is not configured");
      return;
    }

    boolean matchesEntry = matchesEntry(id, entryId);
    logger.debug(
        "Solr delete requested: language='{}', id='{}', entryId={}, matchesEntry={}",
        language,
        id,
        entryId,
        matchesEntry);
    if (matchesEntry) {
      String updateUrl = updateUrl(language);
      logger.debug("Solr delete will POST delete+commit for id='{}' to updateUrl='{}'",
          id, updateUrl);
      postDelete(updateUrl, id);
      postCommit(updateUrl);
    } else {
      logger.debug("Solr delete skipped because id and entryId do not match: id='{}', entryId={}",
          id, entryId);
    }
  }

  private boolean matchesEntry(String id, Integer entryId) {
    return entryId != null && id != null && id.equals(String.valueOf(entryId));
  }

  private String updateUrl(String language) {
    String normalized = language == null ? "" : language.toLowerCase(Locale.ROOT);
    String core = "hu".equals(normalized) ? "hunnor.hu" : "hunnor.nb";
    String base = solrUrl.endsWith("/") ? solrUrl.substring(0, solrUrl.length() - 1) : solrUrl;
    return base + "/" + core + "/update";
  }

  private void postUpdate(String url, String xml) {
    postXml(url, xml);
  }

  private void postDelete(String url, String id) {
    String xml = "<delete><id>" + escapeXml(id) + "</id></delete>";
    postXml(url, xml);
  }

  private void postCommit(String url) {
    postXml(url, "<commit/>");
  }

  private void postXml(String url, String body) {
    logger.debug("Sending Solr request: url='{}', body={}", url, summarizeXml(body));
    HttpRequest request = HttpRequest.newBuilder(URI.create(url))
        .header("Content-Type", "text/xml; charset=UTF-8")
        .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
        .build();
    try {
      HttpResponse<String> response = httpClient.send(
          request, HttpResponse.BodyHandlers.ofString());
      logger.debug(
          "Received Solr response: url='{}', status={}, body={}",
          url,
          response.statusCode(),
          summarizeResponse(response.body()));
      if (response.statusCode() < 200 || response.statusCode() >= 300) {
        throw new IllegalStateException(
            "Solr request failed with status " + response.statusCode() + ": " + response.body());
      }
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Solr request failed", ex);
    } catch (IOException ex) {
      throw new IllegalStateException("Solr request failed", ex);
    }
  }

  private String escapeXml(String value) {
    if (value == null) {
      return "";
    }
    return value
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;");
  }

  private String summarizeXml(String value) {
    return summarize(value, 400);
  }

  private String summarizeResponse(String value) {
    return summarize(value, 800);
  }

  private String summarize(String value, int maxLength) {
    if (value == null) {
      return "<null>";
    }
    String normalized = value.replace("\n", " ").replace("\r", " ").trim();
    if (normalized.length() <= maxLength) {
      return normalized;
    }
    return normalized.substring(0, maxLength) + "...";
  }
}
