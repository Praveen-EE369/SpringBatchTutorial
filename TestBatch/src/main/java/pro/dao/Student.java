package pro.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {

	@Id
	private Long id;
	private String name;
	private String classRoom;
	private Long marks;
	private String gender;
}
