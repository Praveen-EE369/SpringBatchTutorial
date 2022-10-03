package in.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.dao.StudentDao;

@Repository
public interface StudentRespository extends JpaRepository<StudentDao, Long>{
	public StudentDao findByStudentId(long id);
}
