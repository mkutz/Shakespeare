package org.shakespeareframework.reporting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.*;
import org.shakespeareframework.testing.TestQuestionBuilder;
import org.shakespeareframework.testing.TestTaskBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.shakespeareframework.reporting.FileReporter.ReportType.*;

class FileReporterTest {

    Path reportsPath = Path.of("build", "reports", "shakespeare");

    @BeforeEach
    @AfterEach
    void cleanReportsDirectory() throws IOException {
        if (exists(reportsPath)) {
            walk(reportsPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
            delete(reportsPath);
        }
    }

    @Test
    @DisplayName("reports directory is not created by constructor")
    void test1() {
        new TestFileReporter(reportsPath);
        assertThat(reportsPath).doesNotExist();
    }

    @Test
    @DisplayName("write task file reports")
    void test2() {
        var testFileReporter = new TestFileReporter(reportsPath);
        var task = new TestTaskBuilder()
                .string("some task")
                .buildRetryable();
        var fiona = new Actor("Fiona");

        testFileReporter.start(fiona, task);
        testFileReporter.retry(fiona, task, new RuntimeException("Fail!"));
        testFileReporter.success(fiona, task);
        testFileReporter.failure(fiona, task, new RuntimeException("Fail!"));

        assertThat(reportsPath)
                .exists()
                .isDirectoryContaining("glob:build/reports/shakespeare/001-fiona-start-some_task.txt")
                .isDirectoryContaining("glob:build/reports/shakespeare/002-fiona-retry-some_task.txt")
                .isDirectoryContaining("glob:build/reports/shakespeare/003-fiona-success-some_task.txt")
                .isDirectoryContaining("glob:build/reports/shakespeare/004-fiona-failure-some_task.txt");
    }

    @Test
    @DisplayName("write question file reports")
    void test3() {
        var testFileReporter = new TestFileReporter(reportsPath);
        var question = new TestQuestionBuilder<String>()
                .string("some question")
                .answer(actor -> "answer")
                .buildRetryable();
        var fiona = new Actor("Fiona");

        testFileReporter.start(fiona, question);
        testFileReporter.retry(fiona, question, new RuntimeException("Fail!"));
        testFileReporter.retry(fiona, question, "unacceptable answer");
        testFileReporter.success(fiona, question, "answer");
        testFileReporter.failure(fiona, question, new RuntimeException("Fail!"));
        testFileReporter.failure(fiona, question, "unacceptable answer");

        assertThat(reportsPath)
                .exists()
                .isDirectoryContaining("glob:build/reports/shakespeare/001-fiona-start-some_question.txt")
                .isDirectoryContaining("glob:build/reports/shakespeare/002-fiona-retry-some_question.txt")
                .isDirectoryContaining("glob:build/reports/shakespeare/003-fiona-retry-some_question.txt")
                .isDirectoryContaining("glob:build/reports/shakespeare/004-fiona-success-some_question.txt")
                .isDirectoryContaining("glob:build/reports/shakespeare/005-fiona-failure-some_question.txt")
                .isDirectoryContaining("glob:build/reports/shakespeare/006-fiona-failure-some_question.txt");
    }

    private static class TestFileReporter extends FileReporter {

        protected TestFileReporter(Path reportsDirectory) {
            super(reportsDirectory);
        }

        @Override
        public void start(Actor actor, Task task) {
            writeReport(actor, START, task, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public void retry(Actor actor, RetryableTask task, Exception cause) {
            writeReport(actor, RETRY, task, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public void success(Actor actor, Task task) {
            writeReport(actor, SUCCESS, task, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public void failure(Actor actor, Task task, Exception cause) {
            writeReport(actor, FAILURE, task, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public void start(Actor actor, Question<?> question) {
            writeReport(actor, START, question, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public void retry(Actor actor, RetryableQuestion<?> question, Exception cause) {
            writeReport(actor, RETRY, question, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public <A> void retry(Actor actor, RetryableQuestion<A> question, A answer) {
            writeReport(actor, RETRY, question, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public <A> void success(Actor actor, Question<A> question, A answer) {
            writeReport(actor, SUCCESS, question, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public void failure(Actor actor, Question<?> question, Exception cause) {
            writeReport(actor, FAILURE, question, "txt", "some test content".getBytes(UTF_8));
        }

        @Override
        public <A> void failure(Actor actor, Question<A> question, A answer) {
            writeReport(actor, FAILURE, question, "txt", "some test content".getBytes(UTF_8));
        }
    }
}
