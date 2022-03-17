package org.shakespeareframework.reporting;

/**
 * A {@link Reporter} to be {@link org.shakespeareframework.Actor#informs informed} by
 * {@link org.shakespeareframework.Actor}s about their actions.
 */
public interface Reporter extends TaskReporter, QuestionReporter {
}
