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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.models.Book;
import com.hackerrank.models.Library;
import com.hackerrank.repositories.BooksRepository;
import com.hackerrank.repositories.LibrariesRepository;
import java.util.Set;
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
public class LibrariesController {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private LibrariesRepository librariesRepository;

    @RequestMapping(
        value = "/libraries",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity index() throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ObjectMapper().writeValueAsString(librariesRepository.findAll()));
    }

    @RequestMapping(
        value = "/libraries/{id}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity show(@PathVariable("id") String id) {
        Library library = librariesRepository.findById(Long.parseLong(id)).orElse(null);

        if (library == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(library);
    }

    @RequestMapping(
        value = "/libraries",
        method = RequestMethod.POST,
        consumes = "application/json"
    )
    public ResponseEntity create(@RequestBody Library library) {
        librariesRepository.save(library);

        return ResponseEntity.status(HttpStatus.CREATED).body(library);
    }

    @RequestMapping(
        value = "/libraries/{id}",
        method = RequestMethod.DELETE
    )
    public ResponseEntity destroy(@PathVariable("id") Long id) {
        Library library = librariesRepository.findById(id).orElse(null);

        if (library == null) {
            return ResponseEntity.status(400).build();
        }

        Set<Book> books = library.getBooks();

        librariesRepository.delete(library);

        books.stream().map(book -> {
            book.setLibrary(null);

            return book;
        })
        .forEachOrdered(book -> booksRepository.save(book));

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
