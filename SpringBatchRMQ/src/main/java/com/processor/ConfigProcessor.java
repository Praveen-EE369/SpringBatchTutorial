package com.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dao.Student;

@Configuration
public class ConfigProcessor {
	
	  @Bean
	  public ItemProcessor<Student, Student> processor1() {
	    return student -> student;
	  }

}
