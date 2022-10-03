package pro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pro.dao.Book;

public interface BookRepository extends JpaRepository<Book, String> {

}
