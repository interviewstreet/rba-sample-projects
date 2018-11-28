/*
 * Copyright 2018 HackerRank.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hackerrank.controllers;

import com.hackerrank.dao.BookDao;
import com.hackerrank.models.Book;
import com.hackerrank.models.Library;
import com.hackerrank.repositories.BooksRepository;
import com.hackerrank.repositories.LibrariesRepository;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Abhimanyu Singh
 * @author abhimanyusingh@hackerrank.com
 */
@RestController
public class BooksController {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private LibrariesRepository librariesRepository;

    @RequestMapping(
        value = "/books",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity index() {
        List<BookDao> books = booksRepository.findAll().stream().map(BookDao::new).collect(toList());
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @RequestMapping(
        value = "/books/{isbn}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity show(@PathVariable("isbn") String isbn) {
        Book book = booksRepository.findById(isbn).orElse(null);

        if (book == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new BookDao(book));
    }

    @RequestMapping(
        value = "/books",
        method = RequestMethod.POST,
        consumes = "application/json"
    )
    public ResponseEntity create(@RequestBody BookDao book) {
        if (booksRepository.existsById(book.getIsbn())) {
            return ResponseEntity.status(400).build();
        }

        Book newBook = new Book(book);
        Long libraryId = book.getLibraryId();

        if (libraryId != null) {
            Library library = librariesRepository.getOne(libraryId);

            if (library == null) {
                return ResponseEntity.status(400).build();
            }

            library.addBook(newBook);
            newBook.setLibrary(library);

            librariesRepository.save(library);
        }

        booksRepository.save(newBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BookDao(newBook));
    }

    @RequestMapping(
        value = "/books/{isbn}",
        method = RequestMethod.PUT,
        consumes = "application/json"
    )
    public ResponseEntity update(@PathVariable("isbn") String isbn, @RequestBody BookDao updatedBook) {
        Book book = booksRepository.findById(isbn).orElse(null);

        if (book == null) {
            return ResponseEntity.status(404).build();
        }

        book.setAuthorName(updatedBook.getAuthorName());
        book.setName(updatedBook.getName());
        book.setPublicationYear(updatedBook.getPublicationYear());
        book.setSellingPrice(updatedBook.getSellingPrice());

        Long library_id = updatedBook.getLibraryId();

        if (library_id != null) {
            Library library = librariesRepository.findById(library_id).orElse(null);

            if (library == null) {
                return ResponseEntity.status(400).build();
            }

            Library existingLibrary = book.getLibrary();

            if (existingLibrary != null) {
                existingLibrary.removeBook(book);

                librariesRepository.save(existingLibrary);
            }

            library.addBook(book);

            librariesRepository.save(library);

            book.setLibrary(library);
        }

        booksRepository.save(book);

        return ResponseEntity.status(HttpStatus.OK).body(new BookDao(book));
    }

    @RequestMapping(
        value = "/books/{isbn}",
        method = RequestMethod.DELETE
    )
    public ResponseEntity destroy(@PathVariable("isbn") String isbn) {
        Book book = booksRepository.findById(isbn).orElse(null);

        if (book == null) {
            return ResponseEntity.status(400).build();
        }

        Library library = book.getLibrary();

        if (library != null) {
            library.removeBook(book);

            librariesRepository.save(library);
        }

        booksRepository.delete(book);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
