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
package com.hackerrank.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hackerrank.models.Book;
import com.hackerrank.models.Library;

/**
 * @author Abhimanyu Singh
 * @author abhimanyusingh@hackerrank.com
 */
public class BookDao {
    private String isbn;
    private String name;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("publication_year")
    private Integer publicationYear;

    @JsonProperty("selling_price")
    private Float sellingPrice;

    @JsonProperty("library_id")
    private Long libraryId;

    public BookDao() {

    }

    public BookDao(Book book) {
        this.isbn = book.getIsbn();
        this.name = book.getName();
        this.authorName = book.getAuthorName();
        this.publicationYear = book.getPublicationYear();
        this.sellingPrice = book.getSellingPrice();

        Library library = book.getLibrary();

        if (library != null) {
            this.libraryId = library.getId();
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }
}
