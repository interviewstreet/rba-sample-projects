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
package com.hackerrank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hackerrank.dao.BookDao;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Abhimanyu Singh
 * @author abhimanyusingh@hackerrank.com
 */
@Entity
public class Book {
    @Id
    @Column(
        unique = true,
        nullable = false
    )
    private String isbn;

    private String name;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("publication_year")
    private Integer publicationYear;

    @JsonProperty("selling_price")
    private Float sellingPrice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;

    public Book() {

    }

    public Book(BookDao book) {
        this.isbn = book.getIsbn();
        this.name = book.getName();
        this.authorName = book.getAuthorName();
        this.publicationYear = book.getPublicationYear();
        this.sellingPrice = book.getSellingPrice();
        this.library = null;
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
        if (name != null) {
            this.name = name;
        }
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        if (authorName != null) {
            this.authorName = authorName;
        }
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        if (publicationYear != null) {
            this.publicationYear = publicationYear;
        }
    }

    public Float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Float sellingPrice) {
        if (sellingPrice != null) {
            this.sellingPrice = sellingPrice;
        }
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }
}
