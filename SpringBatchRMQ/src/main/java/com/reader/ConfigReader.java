package com.reader;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.amqp.AmqpItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.Config.BatchConfig;
import com.dao.Student;

@Configuration
public class ConfigReader {

	@Autowired
	private BatchConfig batchConfig;
	
	Logger log = LoggerFactory.getLogger(ConfigReader.class);

	@Bean
	public FlatFileItemReader<Student> csvReader() throws MalformedURLException {
		FlatFileItemReader<Student> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new ClassPathResource("input/student.csv"));
		flatFileItemReader.setLineMapper(getLineMapper());
		flatFileItemReader.setLinesToSkip(1);
		log.trace("Read From student.csv file");
		return flatFileItemReader;
	}

	public LineMapper<Student> getLineMapper() {
		DefaultLineMapper<Student> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] { "id", "name", "classRoom", "marks", "gender" });
		BeanWrapperFieldSetMapper<Student> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Student>();
		beanWrapperFieldSetMapper.setTargetType(Student.class);
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return defaultLineMapper;
	}
	
	@Bean
	public ItemReader<Student> readRMQ(){
		return new AmqpItemReader<>(batchConfig.rabbitTemplateRead());
	}
}
