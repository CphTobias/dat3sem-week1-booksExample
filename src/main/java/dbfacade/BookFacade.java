package dbfacade;

import entity.Book;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class BookFacade {

	private static EntityManagerFactory emf;
	private static BookFacade instance;

	public BookFacade() {
	}

	public static BookFacade getBookFacade(EntityManagerFactory _emf) {
		if (instance == null) {
			emf = _emf;
			instance = new BookFacade();
		}
		return instance;
	}

	public Book createBook(String author) {
		Book book = new Book(author);
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(book);
			em.getTransaction().commit();
			return book;
		} finally {
			em.close();
		}
	}

	public Book findBookById(int id) {
		EntityManager em = emf.createEntityManager();
		try {
			Book book = em.find(Book.class, id);
			return book;
		} finally {
			em.close();
		}
	}

	public List<Book> getAllBooks() {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Book> query
				= em.createQuery("Select b from Book b", Book.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	//"Tests" from the document
	public static void main(String[] args) {
		EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("pu");
		BookFacade facade = BookFacade.getBookFacade(emf2);
		Book b1 = facade.createBook("Author 1");
		Book b2 = facade.createBook("Author 2");
		//Find book by ID
		System.out.println("Book1: " + facade.findBookById(b1.getId()).getAuthor());
		System.out.println("Book2: " + facade.findBookById(b2.getId()).getAuthor());
		//Find all books
		System.out.println("Number of books: " + facade.getAllBooks().size());

	}

}
