package pro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.dao.Student;
import pro.repository.StudentRepository;

@Service
public class StudentService {
	@Autowired
	private StudentRepository studentRepository;

	public Student getStudentById(Long id) throws Exception {
		return studentRepository.findById(id).orElseThrow(() -> new Exception("Student Id not found in database"));
	}
}
