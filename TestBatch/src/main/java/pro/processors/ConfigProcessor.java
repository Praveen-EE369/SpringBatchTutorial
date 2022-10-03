package pro.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pro.dao.Book;
import pro.dao.Person;
import pro.dao.Student;
import pro.dto.DiffCsv;

@Configuration
public class ConfigProcessor {
	Logger log = LoggerFactory.getLogger(ConfigProcessor.class);

	@Bean
	public ItemProcessor<Student, Student> processor() {
//		return student -> {
//			if (student.getMarks() < 65)
//				return student;
//			return null;
//		};
		log.trace("Processed entire Student Object No Conditions applied..");
		return student -> student;
	}

	@Bean
	public ItemProcessor<Student, Student> processor1() {
		log.trace("Transfering Data which are having marks less than 65");
		return student -> {
			if (student.getMarks() < 65)
				return student;
			return null;
		};
		// return student -> student;
	}

	@Bean
	public ItemProcessor<DiffCsv, DiffCsv> successProcessor() {
		return diffCsv -> {
			if (diffCsv.getMark().equalsIgnoreCase("mark"))
				return diffCsv;
			if (diffCsv.getId().equalsIgnoreCase("id"))
				return diffCsv;
			if (Integer.parseInt(diffCsv.getMark()) > 80)
				return diffCsv;
			return null;
		};
	}

	@Bean
	public ItemProcessor<DiffCsv, DiffCsv> rejectProcessor() {
		return diffCsv -> {
			if (diffCsv.getMark().equalsIgnoreCase("mark"))
				return diffCsv;
			if (diffCsv.getId().equalsIgnoreCase("id"))
				return diffCsv;
			if (Integer.parseInt(diffCsv.getMark()) < 80)
				return diffCsv;
			return null;
		};
	}

	@Bean
	public ItemProcessor<Person, Person> processor2() {
		// return person -> person;
		return person -> {
			if (person.getGender().equalsIgnoreCase("female"))
				return person;
			return null;
		};
	}

	@Bean
	public ItemProcessor<Book, Book> processor3() {
		return book -> book;
//		return book -> {
//			if (book.getGender().equalsIgnoreCase("female"))
//				return book;
//			return null;
//		};
	}
}
