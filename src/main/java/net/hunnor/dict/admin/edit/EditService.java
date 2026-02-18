package net.hunnor.dict.admin.edit;

import java.util.List;

public interface EditService {

  /**
   * Lists entries by language and filter options.
   *
   * @param language the language code
   * @param letter the first-letter filter
   * @param term the term prefix filter
   * @param translatedOnly true when only translated entries should be included
   * @return the matching entries
   */
  List<LegacyEntry> list(String language, String letter, String term, boolean translatedOnly);

  /**
   * Loads one entry by language and id.
   *
   * @param language the language code
   * @param id the entry id or N for a new entry
   * @param term optional initial term for a new entry
   * @return the loaded entry
   */
  LegacyEntry entry(String language, String id, String term);

  /**
   * Saves an entry and returns the save result.
   *
   * @param language the language code
   * @param id the entry id or N for a new entry
   * @param entry the entry group identifier
   * @param formsYaml forms in YAML format
   * @param pos part-of-speech code
   * @param translation translation XML content
   * @param status translation status value
   * @param editorId editor identifier used in log records
   * @return the save result with errors and executed SQL
   */
  SaveResult save(
      String language,
      String id,
      String entry,
      String formsYaml,
      String pos,
      String translation,
      String status,
      String editorId);

  /**
   * Deletes one entry by language and id.
   *
   * @param language the language code
   * @param id the entry identifier
   * @return the delete result
   */
  DeleteResult delete(String language, String id);

}
