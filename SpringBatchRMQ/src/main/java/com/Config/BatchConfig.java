package com.Config;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.amqp.AmqpItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dao.Student;
import com.reader.ConfigReader;
import com.writer.ConfigWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	public final static String queueName = "first-queue";
	
	@Autowired
	private org.springframework.amqp.rabbit.connection.ConnectionFactory rabbitConnectionFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private ConfigReader configReader;
	
	@Autowired
	private ConfigWriter configWriter;
	
	Logger log = LoggerFactory.getLogger(BatchConfig.class);

    @Bean
    Queue firstQueue() {
        return new Queue(queueName, true);
    }
	
	
	@Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
	
	
	@Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        rabbitTemplate.setRoutingKey(firstQueue().getName());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
	
//	@Bean
//	public AmqpAdmin amqpAdmin() {
//		return new RabbitAdmin(rabbitConnectionFactory);
//	}
	
	@Bean
	public RabbitTemplate rabbitTemplateRead() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
		rabbitTemplate.setDefaultReceiveQueue(queueName);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
	
	@Bean
	public Step firstStep() throws MalformedURLException {
		return stepBuilderFactory.get("First Step")
				.<Student, Student>chunk(3)
				.reader(configReader.csvReader())
                .writer(configWriter.amqpWriter())
                .build();
	}
	
	@Bean
	public Step secondStep() throws MalformedURLException {
		return stepBuilderFactory.get("Second Step")
				.<Student, Student>chunk(3)
				.reader(configReader.readRMQ())
                .writer(configWriter.fromRMQWriter())
                .build();
	}
	
	@Bean
    public Job job() throws MalformedURLException{
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(secondStep())
                .build();
    }
	


}
