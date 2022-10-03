package pro.writers;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import pro.dao.Book;
import pro.dao.Person;
import pro.dao.Student;
import pro.dto.DiffCsv;
import pro.repository.StudentRepository;

@Configuration
public class ConfigWriter {
	Logger log = LoggerFactory.getLogger(ConfigWriter.class);
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private DataSource dataSource;

	@Bean
	public ItemWriter<Student> writer() {
		log.trace("Writing into Database...");
		return students -> studentRepository.saveAll(students);
	}

	@Bean
	public JdbcBatchItemWriter<Person> jdbcWriterTxT2DB() {
		JdbcBatchItemWriter<Person> batchItemWriter = new JdbcBatchItemWriter<Person>();
		batchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
		batchItemWriter.setSql(
				"INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME, GENDER) VALUES (:id, :firstName, :lastName, :gender)");
		batchItemWriter.setDataSource(dataSource);
		return batchItemWriter;

	}

	@Bean
	public FlatFileItemWriter<Student> fileItemWriter() {
		FlatFileItemWriter<Student> writer = new FlatFileItemWriter<Student>();
		writer.setResource(new FileSystemResource(
				System.getProperty("user.dir") + "/src/main/resources/output/studentFromDB.csv"));
		DelimitedLineAggregator<Student> delimitedLineAggregator = new DelimitedLineAggregator<Student>();
		delimitedLineAggregator.setDelimiter(",");

		BeanWrapperFieldExtractor<Student> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<Student>();
		beanWrapperFieldExtractor.setNames(new String[] { "id", "classRoom", "gender", "marks", "name" });
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

		writer.setLineAggregator(delimitedLineAggregator);
		log.trace("Writing into studentFromDB.csv from Database....");
		return writer;
	}

	@Bean
	public FlatFileItemWriter<DiffCsv> successCsvWriter() {
		FlatFileItemWriter<DiffCsv> writer = new FlatFileItemWriter<DiffCsv>();
		writer.setResource(
				new FileSystemResource(System.getProperty("user.dir") + "/src/main/resources/output/success.csv"));
		DelimitedLineAggregator<DiffCsv> delimitedLineAggregator = new DelimitedLineAggregator<DiffCsv>();
		delimitedLineAggregator.setDelimiter(",");

		BeanWrapperFieldExtractor<DiffCsv> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<DiffCsv>();
		beanWrapperFieldExtractor.setNames(new String[] { "gender", "mark", "classRoom", "name", "id" });
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

		writer.setLineAggregator(delimitedLineAggregator);
		log.trace("Writing into success.csv from current.csv....");
		return writer;
	}

	@Bean
	public FlatFileItemWriter<DiffCsv> rejectCsvWriter() {
		FlatFileItemWriter<DiffCsv> writer = new FlatFileItemWriter<DiffCsv>();
		writer.setResource(
				new FileSystemResource(System.getProperty("user.dir") + "/src/main/resources/output/reject.csv"));
		DelimitedLineAggregator<DiffCsv> delimitedLineAggregator = new DelimitedLineAggregator<DiffCsv>();
		delimitedLineAggregator.setDelimiter(",");

		BeanWrapperFieldExtractor<DiffCsv> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<DiffCsv>();
		beanWrapperFieldExtractor.setNames(new String[] { "gender", "mark", "classRoom", "name", "id" });
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

		writer.setLineAggregator(delimitedLineAggregator);
		log.trace("Writing into reject.csv from current.csv....");
		return writer;
	}

	@Bean
	public FlatFileItemWriter<Person> db2txtItemWriter() {
		FlatFileItemWriter<Person> writer = new FlatFileItemWriter<Person>();
		writer.setResource(
				new FileSystemResource(System.getProperty("user.dir") + "/src/main/resources/output/DB2person.txt"));
		writer.setShouldDeleteIfExists(true);

		DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<Person>();
		delimitedLineAggregator.setDelimiter(" ");

		BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<Person>();
		beanWrapperFieldExtractor.setNames(new String[] { "id", "firstName", "lastName", "gender" });
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
		writer.setLineAggregator(delimitedLineAggregator);
		log.trace("Writing into person.txt from Database....");
		return writer;
	}

	@Bean
	public ItemWriter<Book> xmlItemWriter() {
		StaxEventItemWriter<Book> xmlFileWriter = new StaxEventItemWriter<>();

		xmlFileWriter.setResource(
				new FileSystemResource(System.getProperty("user.dir") + "/src/main/resources/output/DB2Books.xml"));

		xmlFileWriter.setRootTagName("books");

		Jaxb2Marshaller bookMarshaller = new Jaxb2Marshaller();
		bookMarshaller.setClassesToBeBound(Book.class);
		xmlFileWriter.setMarshaller(bookMarshaller);

		return xmlFileWriter;
	}

	@Bean
	public JdbcBatchItemWriter<Book> xml2dbjdbcWriter() {
		JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<Book>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Book>());
		writer.setSql(
				"INSERT INTO BOOK (ID, AUTHOR, DESCRIPTION, GENRE, PRICE, PUBLISH_DATE, TITLE) VALUES (:id, :author, :title, :genre, :price, :publish_date, :description)");
		writer.setDataSource(dataSource);

		return writer;
	}

}
