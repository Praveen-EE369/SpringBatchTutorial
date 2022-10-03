package pro.dao;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "book")
@Entity
public class Book {

	@Id
	private String id;
	private String author;
	private String title;
	private String genre;
	private Double price;
	private Date publish_date;
	private String description;

}
