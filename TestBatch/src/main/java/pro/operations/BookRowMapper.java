package pro.operations;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import pro.dao.Book;

public class BookRowMapper implements RowMapper<Book> {

	@Override
	public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
		Book book = new Book();
		book.setId(rs.getString("ID"));
		book.setAuthor(rs.getString("AUTHOR"));
		book.setDescription(rs.getString("DESCRIPTION"));
		book.setGenre(rs.getString("GENRE"));
		book.setPrice(rs.getDouble("PRICE"));
		book.setPublish_date(rs.getDate("PUBLISH_DATE"));
		book.setTitle(rs.getString("TITLE"));
		return book;
	}

}
