package me.study.springbatch.part3;

import me.study.springbatch.TestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@ContextConfiguration(classes = {SavePersonConfiguration.class, TestConfiguration.class})
class SavePersonConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PersonRepository personRepository;

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }

    @Test
    void test_allow_duplicate() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allow_duplicate", "false")
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        int writeCount = jobExecution.getStepExecutions()
                .stream()
                .mapToInt(StepExecution::getWriteCount)
                .sum();
        assertThat(writeCount).isEqualTo(personRepository.count())
                .isEqualTo(3);
    }

    @Test
    void test_not_allow_duplicate() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allow_duplicate", "true")
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        int writeCount = jobExecution.getStepExecutions()
                .stream()
                .mapToInt(StepExecution::getWriteCount)
                .sum();
        assertThat(writeCount).isEqualTo(personRepository.count())
                .isEqualTo(100);
    }

    @Test
    void test_step() {
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("savePersonStep");

        // then
        int writeCount = jobExecution.getStepExecutions()
                .stream()
                .mapToInt(StepExecution::getWriteCount)
                .sum();
        assertThat(writeCount).isEqualTo(personRepository.count())
                .isEqualTo(3);
    }
}
