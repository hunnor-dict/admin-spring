package net.hunnor.dict.admin.edit;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SaveResult {

  private static final DateTimeFormatter CLOCK_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

  private boolean success;

  private String addition;

  private final List<String> errors = new ArrayList<>();

  private final List<String> sql = new ArrayList<>();

  private final String clock = LocalTime.now().format(CLOCK_FORMAT);

  /**
   * Returns save status.
   *
   * @return true if save was successful
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * Sets save status.
   *
   * @param success true when save was successful
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }

  /**
   * Returns id of a newly inserted entry.
   *
   * @return inserted entry id or null
   */
  public String getAddition() {
    return addition;
  }

  /**
   * Sets id of a newly inserted entry.
   *
   * @param addition inserted entry id
   */
  public void setAddition(String addition) {
    this.addition = addition;
  }

  /**
   * Returns validation and processing errors.
   *
   * @return list of error messages
   */
  public List<String> getErrors() {
    return new ArrayList<>(errors);
  }

  /**
   * Adds one validation or processing error.
   *
   * @param error error message
   */
  public void addError(String error) {
    this.errors.add(error);
  }

  /**
   * Adds multiple validation or processing errors.
   *
   * @param newErrors error message list
   */
  public void addErrors(List<String> newErrors) {
    this.errors.addAll(newErrors);
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
