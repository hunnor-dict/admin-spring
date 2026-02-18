package net.hunnor.dict.admin.edit;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DeleteResult {

  private static final DateTimeFormatter CLOCK_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

  private boolean deleted;

  private String error = "";

  private final List<String> sql = new ArrayList<>();

  private final String clock = LocalTime.now().format(CLOCK_FORMAT);

  /**
   * Returns deletion status.
   *
   * @return true if the entry was deleted
   */
  public boolean isDeleted() {
    return deleted;
  }

  /**
   * Sets deletion status.
   *
   * @param deleted true if the entry was deleted
   */
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * Returns the error message.
   *
   * @return error message text
   */
  public String getError() {
    return error;
  }

  /**
   * Sets the error message.
   *
   * @param error error message text
   */
  public void setError(String error) {
    this.error = error;
  }

  /**
   * Returns generated SQL statements.
   *
   * @return SQL statement list
   */
  public List<String> getSql() {
    return new ArrayList<>(sql);
  }

  /**
   * Adds one generated SQL statement.
   *
   * @param statement generated SQL statement
   */
  public void addSql(String statement) {
    this.sql.add(statement);
  }

  /**
   * Returns operation clock value.
   *
   * @return formatted time string
   */
  public String getClock() {
    return clock;
  }

}
