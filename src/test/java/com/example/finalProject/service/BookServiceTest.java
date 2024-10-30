package com.example.finalProject.service;

import com.example.finalProject.model.*;
import com.example.finalProject.repository.BookCopyRepository;
import com.example.finalProject.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository mockRepository;

    @Mock
    private BookCopyRepository mockCopyRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getBooks_shouldReturnListOfBookReturnDtos() {
        List<Book> books = new ArrayList<>();

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("123456");
        book1.setGenre("Fiction");
        book1.setPublisher("Publisher 1");
        book1.setLanguage("English");
        book1.setTotal_copies(1);
        book1.setPage_count(100);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("123457");
        book2.setGenre("Fiction");
        book2.setPublisher("Publisher 2");
        book2.setLanguage("English");
        book2.setTotal_copies(1);
        book2.setPage_count(100);

        books.add(book1);
        books.add(book2);

        when(mockRepository.findAllByOrderByIdAsc()).thenReturn(books);

        List<BookReturnDto> result = bookService.getBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());
    }

    @Test
    void createBook_shouldSaveNewBookAndRequiedCopies() {
        BookCreateDto bookToCreate = new BookCreateDto();
        bookToCreate.setTitle("Book 1");
        bookToCreate.setAuthor("Author 1");
        bookToCreate.setIsbn("123456");
        bookToCreate.setGenre("Fiction");
        bookToCreate.setPublisher("Publisher 1");
        bookToCreate.setLanguage("English");
        bookToCreate.setTotal_copies(1);
        bookToCreate.setPage_count(100);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle(bookToCreate.getTitle());
        savedBook.setAuthor(bookToCreate.getAuthor());
        savedBook.setIsbn(bookToCreate.getIsbn());
        savedBook.setGenre(bookToCreate.getGenre());
        savedBook.setPublisher(bookToCreate.getPublisher());
        savedBook.setLanguage(bookToCreate.getLanguage());
        savedBook.setTotal_copies(bookToCreate.getTotal_copies());
        savedBook.setPage_count(bookToCreate.getPage_count());

        BookCopy savedCopy = new BookCopy();
        savedCopy.setId(1L);

        Mockito.when(mockRepository.save(any(Book.class))).thenReturn(savedBook);
        Mockito.when(mockCopyRepository.save(any(BookCopy.class))).thenReturn(savedCopy);

        this.bookService.createBook(bookToCreate);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.verify(this.mockRepository).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();
        // as the tested method is a void method,
        // we need to catch the Book passed to the save() in the createBook method in order to inspect it


        assertEquals("Book 1", capturedBook.getTitle());
        assertEquals("Author 1", capturedBook.getAuthor());
        assertEquals("123456", capturedBook.getIsbn());
        assertEquals("Fiction", capturedBook.getGenre());
        assertEquals("Publisher 1", capturedBook.getPublisher());
        assertEquals("English", capturedBook.getLanguage());
        assertEquals(1, capturedBook.getTotal_copies());
        assertEquals(100, capturedBook.getPage_count());
    }

    @Test
    void updateBook_shouldUpdateExistingBookDetails() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Book 1");
        existingBook.setAuthor("Author 1");
        existingBook.setIsbn("123456");
        existingBook.setGenre("Fiction");
        existingBook.setPublisher("Publisher 1");
        existingBook.setLanguage("English");
        existingBook.setTotal_copies(1);
        existingBook.setPage_count(100);

        when(mockRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        BookUpdateDto updateDto = new BookUpdateDto();
        updateDto.setId(1L);
        updateDto.setTitle("New Book Title");
        updateDto.setAuthor("Author 1");
        updateDto.setIsbn("123456");
        updateDto.setGenre("Fiction");
        updateDto.setPublisher("Publisher 1");
        updateDto.setLanguage("English");
        updateDto.setTotal_copies(1);
        updateDto.setPage_count(100);

        this.bookService.updateBook(updateDto);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.verify(mockRepository).save(bookCaptor.capture());
        Book updatedBook = bookCaptor.getValue();

        assertEquals("New Book Title", updatedBook.getTitle());
        assertEquals("Author 1", updatedBook.getAuthor());
        assertEquals("123456", updatedBook.getIsbn());
        assertEquals("Fiction", updatedBook.getGenre());
        assertEquals("Publisher 1", updatedBook.getPublisher());
        assertEquals("English", updatedBook.getLanguage());
        assertEquals(1, updatedBook.getTotal_copies());
        assertEquals(100, updatedBook.getPage_count());
    }

    @Test
    void deleteBookById_shouldRemoveBookFromRepository() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Book 1");
        existingBook.setAuthor("Author 1");
        existingBook.setIsbn("123456");
        existingBook.setGenre("Fiction");
        existingBook.setPublisher("Publisher 1");
        existingBook.setLanguage("English");
        existingBook.setTotal_copies(1);
        existingBook.setPage_count(100);

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        this.bookService.deleteBookById(1L);

        Mockito.verify(mockRepository, Mockito.times(1)).deleteById(1L);
        //check that the delete method was called exactly once on the repository
    }
}