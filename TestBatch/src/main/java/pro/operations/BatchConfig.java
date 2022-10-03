package pro.operations;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pro.dao.Book;
import pro.dao.Person;
import pro.dao.Student;
import pro.dto.DiffCsv;
import pro.listener.ConfigListener;
import pro.processors.ConfigProcessor;
import pro.readers.ConfigReader;
import pro.writers.ConfigWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ConfigReader configReader;

	@Autowired
	private ConfigWriter configWriter;

	@Autowired
	private ConfigProcessor configProcessor;

	@Autowired
	private ConfigListener configListener;

	Logger log = LoggerFactory.getLogger(BatchConfig.class);

	@Bean
	public Step step1() throws MalformedURLException {
		log.trace("Performing Step 1");
		return stepBuilderFactory.get("Step 1").<Student, Student>chunk(2).reader(configReader.csvReader())
				.processor(configProcessor.processor()).writer(configWriter.writer()).build();
	}

	@Bean
	public Step step2() throws MalformedURLException {
		log.trace("Performing Step 2");
		return stepBuilderFactory.get("Step 2").<Student, Student>chunk(2).reader(configReader.dbReader())
				.processor(configProcessor.processor1()).writer(configWriter.fileItemWriter()).build();
	}

	@Bean
	public Step step3() throws MalformedURLException {
		log.trace("Performing Step 3");
		return stepBuilderFactory.get("Step 3").<Person, Person>chunk(2).reader(configReader.txtReader())
				.processor(configProcessor.processor2()).writer(configWriter.jdbcWriterTxT2DB()).build();
	}

	@Bean
	public Step step4() throws MalformedURLException {
		log.trace("Performing Step 4");
		return stepBuilderFactory.get("Step 4").<Person, Person>chunk(2).reader(configReader.readerJdbc())
				.processor(configProcessor.processor2()).writer(configWriter.db2txtItemWriter()).build();
	}

	@Bean
	public Step step5() throws MalformedURLException {
		log.trace("Performing Step 5");
		return stepBuilderFactory.get("Step 5").<Book, Book>chunk(2).reader(configReader.readerXml())
				.processor(configProcessor.processor3()).writer(configWriter.xml2dbjdbcWriter()).build();
	}

	@Bean
	public Step step6() throws MalformedURLException {
		log.trace("Performing Step 6");
		return stepBuilderFactory.get("Step 6").<Book, Book>chunk(2).reader(configReader.dbReaderForXML())
				.processor(configProcessor.processor3()).writer(configWriter.xmlItemWriter()).build();
	}

	@Bean
	public Step step7() throws MalformedURLException {
		log.trace("Performing Step 1 in Job 2");
		return stepBuilderFactory.get("Step 7").<DiffCsv, DiffCsv>chunk(2).reader(configReader.currentReader())
				.processor(configProcessor.successProcessor()).writer(configWriter.successCsvWriter()).build();
	}

	@Bean
	public Step step8() throws MalformedURLException {
		log.trace("Performing Step 2 in Job 2");
		return stepBuilderFactory.get("Step 8").<DiffCsv, DiffCsv>chunk(2).reader(configReader.currentReader())
				.processor(configProcessor.rejectProcessor()).writer(configWriter.rejectCsvWriter()).build();
	}

	@Bean
	public Job job() throws MalformedURLException {
		log.trace("Starting the Job....");
		return jobBuilderFactory.get("6 Jobs").incrementer(new RunIdIncrementer()).start(step1()).next(step2())
				.next(step3()).next(step4()).next(step5()).next(step6()).build();
	}

	@Bean
	public Job job1() throws MalformedURLException {
		log.trace("Starting the Job1....");
		return jobBuilderFactory.get("2nd Job").incrementer(new RunIdIncrementer()).start(step7()).next(step8())
				.listener(configListener).build();
	}

}
