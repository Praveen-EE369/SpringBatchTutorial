package pro.operations;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import pro.dao.Student;

public class StudentRowMapper implements RowMapper<Student> {

	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student();
		student.setId(rs.getLong("ID"));
		student.setClassRoom(rs.getString("CLASS_ROOM"));
		student.setGender(rs.getString("GENDER"));
		student.setMarks(rs.getLong("MARKS"));
		student.setName(rs.getString("NAME"));

		return student;
	}

}
