package com.dao;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Student implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String classRoom;
	private Long marks;
	private String gender;
}
