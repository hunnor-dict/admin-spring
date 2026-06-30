package net.hunnor.dict.admin.edit;

/**
 * Service for exporting entries from the legacy format.
 */
public interface LegacyExportService {

  String exportEntryXml(String language, String id);

}
