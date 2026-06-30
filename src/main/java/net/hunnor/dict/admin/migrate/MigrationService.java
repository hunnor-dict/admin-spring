package net.hunnor.dict.admin.migrate;

/**
 * Service for migrating the dictionary database.
 */
public interface MigrationService {

  public void migrate() throws MigrationException;

}
