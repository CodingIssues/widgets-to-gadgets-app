package com.example.test;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import com.example.test.config.GadgetImportConfig;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {

		final ApplicationContext ctx = SpringApplication.run(TestApplication.class, args);


		final JobLauncher jobLauncher = ctx.getBean("jobLauncher", JobLauncher.class);
		final Job job = ctx.getBean(GadgetImportConfig.JOB_NAME, Job.class);
		
		OffsetDateTime startDateTime = OffsetDateTime.parse("0001-01-01T00:00:00Z");
		OffsetDateTime endDateTime = OffsetDateTime.now(ZoneOffset.UTC);

		int exitCode = 0;
		try {

			JobParameters jobParameters =  new JobParametersBuilder()
			.addDate("lastModifiedStartDate", Date.from(startDateTime.atZoneSameInstant(ZoneId.systemDefault()).toInstant()))
			.addDate("lastModifiedEndDate", Date.from(endDateTime.atZoneSameInstant(ZoneId.systemDefault()).toInstant()))
			.toJobParameters();

			jobLauncher.run(job, jobParameters);
		} catch (final JobExecutionException e) {
			System.out.println("Job failed: " + e.getMessage());
			exitCode = 1;
		}
		
		System.out.println("Exiting application...");
		System.exit(exitCode);

	}

}
