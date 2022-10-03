package in.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.dao.StudentDao;
import in.repository.StudentRespository;

@Service
public class StudentService {
	
	@Autowired
	private StudentRespository studentRespository;

	public StudentDao getStudentById(Long id) throws Exception {
		return studentRespository.findByStudentId(id);//.orElseThrow(() -> new Exception("Student Id not found!"));
	}
	
	public void addStudent(StudentDao student) {
		studentRespository.save(student);
	}
	
	public void updateStudent(StudentDao student) {
		studentRespository.save(student);
	}
	
	public void deleteStudentById(Long id) {
		studentRespository.deleteById(id);
	}

}
