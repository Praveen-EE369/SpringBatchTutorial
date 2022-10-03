package pro.readers;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import pro.dao.Book;
import pro.dao.Person;
import pro.dao.Student;
import pro.dto.DiffCsv;
import pro.operations.BookRowMapper;
import pro.operations.StudentRowMapper;

@Configuration
public class ConfigReader {

	@Autowired
	private DataSource dataSource;

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
		// lineTokenizer.setIncludedFields(new int[] { 0, 1, 2, 3 });

		BeanWrapperFieldSetMapper<Student> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Student>();
		beanWrapperFieldSetMapper.setTargetType(Student.class);
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return defaultLineMapper;
	}

	@Bean
	public FlatFileItemReader<DiffCsv> currentReader() throws MalformedURLException {
		FlatFileItemReader<DiffCsv> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new ClassPathResource("input/currentNew.csv"));
		flatFileItemReader.setLineMapper(new DefaultLineMapper<DiffCsv>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "gender", "mark", "classRoom", "name", "id" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<DiffCsv>() {
					{
						setTargetType(DiffCsv.class);
					}
				});
			}
		});
		// flatFileItemReader.setLinesToSkip(1);
		log.trace("Read From current.csv file");
		return flatFileItemReader;
	}

	@Bean
	public JdbcCursorItemReader<Student> dbReader() {
		JdbcCursorItemReader<Student> jdbcCursorItemReader = new JdbcCursorItemReader<Student>();
		jdbcCursorItemReader.setDataSource(dataSource);
		jdbcCursorItemReader.setSql("SELECT ID, CLASS_ROOM, GENDER, MARKS, NAME FROM STUDENT");
		jdbcCursorItemReader.setRowMapper(new StudentRowMapper());
		log.trace("Reading From Database....");
		return jdbcCursorItemReader;
	}

	@Bean
	public JdbcCursorItemReader<Book> dbReaderForXML() {
		JdbcCursorItemReader<Book> jdbcCursorItemReader = new JdbcCursorItemReader<Book>();
		jdbcCursorItemReader.setDataSource(dataSource);
		jdbcCursorItemReader.setSql("SELECT ID, AUTHOR, DESCRIPTION, GENRE, PRICE, PUBLISH_DATE, TITLE FROM BOOK");
		jdbcCursorItemReader.setRowMapper(new BookRowMapper());
		log.trace("Reading From Database....");
		return jdbcCursorItemReader;
	}

	@Bean
	public JdbcPagingItemReader<Person> readerJdbc() {
		JdbcPagingItemReader<Person> reader = new JdbcPagingItemReader<Person>();
		MySqlPagingQueryProvider query = new MySqlPagingQueryProvider();
		query.setSelectClause("SELECT ID, FIRST_NAME,LAST_NAME, GENDER");
		query.setFromClause("FROM PERSON");
		Map<String, Order> sortConfiguration = new HashMap<>();
		sortConfiguration.put("first_name", Order.ASCENDING);
		query.setSortKeys(sortConfiguration);
		reader.setDataSource(dataSource);
		reader.setQueryProvider(query);
		reader.setRowMapper(new BeanPropertyRowMapper<>(Person.class));
		return reader;
	}

	public LineMapper<Person> lineMapperTXT() {
		DefaultLineMapper<Person> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(" ");
		lineTokenizer.setNames(new String[] { "id", "firstName", "lastName", "gender" });
		BeanWrapperFieldSetMapper<Person> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Person>();
		beanWrapperFieldSetMapper.setTargetType(Person.class);
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return defaultLineMapper;
	}

	@Bean
	public FlatFileItemReader<Person> txtReader() {
		FlatFileItemReader<Person> flatFileItemReader = new FlatFileItemReader<Person>();
		flatFileItemReader.setResource(new ClassPathResource("/input/person.txt"));
		flatFileItemReader.setLineMapper(lineMapperTXT());
		return flatFileItemReader;
	}

	@Bean
	public StaxEventItemReader<Book> readerXml() {
		StaxEventItemReader<Book> xmlFileReader = new StaxEventItemReader<Book>();

		xmlFileReader.setResource(new ClassPathResource("/input/books.xml"));
		xmlFileReader.setFragmentRootElementName("book");

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Book.class);
		xmlFileReader.setUnmarshaller(marshaller);

		return xmlFileReader;

	}

}
