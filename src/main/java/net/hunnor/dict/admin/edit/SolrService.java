package net.hunnor.dict.admin.edit;

/**
 * Service for saving and deleting entries in Solr.
 */
public interface SolrService {

  void save(String language, String id, Integer entryId, String xml);

  void delete(String language, String id, Integer entryId);

}
