package org.shakespeareframework.reporting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.SAME_THREAD)
class JsonReporterTest {

  @Test
  void test1() {
    var reporter = new JsonReporter();
  }
}
