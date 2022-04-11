/**
 * The testing package contains mere util classes that are useful for testing in more than one
 * module.
 *
 * <p>Currently there are
 *
 * <ul>
 *   <li>the {@link org.shakespeareframework.testing.TestTaskBuilder} to construct ad-hoc {@link
 *       org.shakespeareframework.Task} or {@link org.shakespeareframework.RetryableTask} objects,
 *       and
 *   <li>the {@link org.shakespeareframework.testing.TestQuestionBuilder} to do the same for {@link
 *       org.shakespeareframework.Question} and {@link org.shakespeareframework.RetryableQuestion}
 *       objects.
 * </ul>
 *
 * <p>There is no documentation on these outside of JavaDoc as these classes should not be used by
 * regular users of Shakespeare (creating Tasks and Questions), but might be useful if you want to
 * author your own module containing custom Abilities or your own Reporters.
 */
package org.shakespeareframework.testing;
