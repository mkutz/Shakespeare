/**
 * The reporting package contains classes and interfaces for reporting what an {@link
 * org.shakespeareframework.Actor} does:
 *
 * <ul>
 *   <li>{@link org.shakespeareframework.reporting.Reporter} is the main interface,
 *   <li>{@link org.shakespeareframework.reporting.LoggingReporter} is the interface for reporters
 *       that generate logs,
 *   <li>{@link org.shakespeareframework.reporting.Slf4jReporter} is its default implementation,
 *   <li>{@link org.shakespeareframework.reporting.FileReporter} can be extended to generate report
 *       files.
 * </ul>
 *
 * @see <a href="https://shakespeareframework.org/latest/manual/#_reporting">Manual on
 *     "Repoting"</a>
 */
package org.shakespeareframework.reporting;
