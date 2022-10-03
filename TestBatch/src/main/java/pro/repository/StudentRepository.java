package pro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pro.dao.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
