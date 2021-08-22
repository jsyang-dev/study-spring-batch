package me.study.springbatch.part4;

import me.study.springbatch.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@ContextConfiguration(classes = {UserConfiguration.class, TestConfiguration.class})
class UserConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    @Test
    void test() throws Exception {
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        int writeCount = jobExecution.getStepExecutions()
                .stream()
                .filter(x -> x.getStepName().equals("userLevelUpStep"))
                .mapToInt(StepExecution::getWriteCount)
                .sum();
        int size = userRepository.findAllByUpdatedDate(LocalDate.now()).size();

        assertThat(writeCount).isEqualTo(size).isEqualTo(300);
        assertThat(userRepository.count()).isEqualTo(400);
    }
}
