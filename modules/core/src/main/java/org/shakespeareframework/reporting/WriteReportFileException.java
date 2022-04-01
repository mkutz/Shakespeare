package org.shakespeareframework.reporting;

import java.io.IOException;

/**
 * A {@link WriteReportFileException} is thrown when the {@link FileReporter} fails create a report
 * file.
 */
public class WriteReportFileException extends RuntimeException {

  /**
   * @param message the message
   * @param cause the causing {@link IOException}
   */
  public WriteReportFileException(String message, IOException cause) {
    super(message, cause);
  }
}
