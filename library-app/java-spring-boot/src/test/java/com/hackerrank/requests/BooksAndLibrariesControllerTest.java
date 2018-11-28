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
package com.hackerrank.requests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.Application;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Abhimanyu Singh
 * @author abhimanyusingh@hackerrank.com
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class BooksAndLibrariesControllerTest {
    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createLibraryAndCreateBook() throws Exception {
        mockMvc.perform(
            post("/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"library-1\"}")
        )
        .andExpect(status().is(201));

        String body = "{"
            + "\"isbn\": \"5589197551341\","
            + "\"name\": \"book-1\","
            + "\"author_name\": \"author-1\","
            + "\"publication_year\": 2018,"
            + "\"selling_price\": 250.00,"
            + "\"library_id\": 1"
            + "}";

        mockMvc.perform(
            post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(status().is(201));

        body = "{"
            + "\"isbn\": \"5589197551342\","
            + "\"name\": \"book-2\","
            + "\"author_name\": \"author-1, author-2\","
            + "\"publication_year\": 2018,"
            + "\"selling_price\": 350.00,"
            + "\"library_id\": 1"
            + "}";

        mockMvc.perform(
            post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(status().is(201));

        String data = mockMvc.perform(
            get("/libraries/1")
        )
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode dataJson = mapper.readTree(data);

        assertEquals(dataJson.get("id").asLong(), 1);
        assertEquals(dataJson.get("name").asText(), "library-1");

        JsonNode[] books = mapper.readValue(
            dataJson.get("books").traverse(),
            JsonNode[].class
        );

        String[] actualIsbn = Stream.of(books)
            .map(book -> book.get("isbn").asText())
            .sorted()
            .collect(toList())
            .toArray(new String[0]);

        String[] expectedIsbn = new String[] {"5589197551341", "5589197551342"};

        assertArrayEquals(expectedIsbn, actualIsbn);
    }

    @Test
    public void createLibraryCreateBookAndDeleteBook() throws Exception {
        mockMvc.perform(
            post("/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"library-1\"}")
        )
        .andExpect(status().is(201));

        String body = "{"
            + "\"isbn\": \"5589197551341\","
            + "\"name\": \"book-1\","
            + "\"author_name\": \"author-1\","
            + "\"publication_year\": 2018,"
            + "\"selling_price\": 250.00,"
            + "\"library_id\": 1"
            + "}";

        mockMvc.perform(
            post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(status().is(201));

        body = "{"
            + "\"isbn\": \"5589197551342\","
            + "\"name\": \"book-2\","
            + "\"author_name\": \"author-1, author-2\","
            + "\"publication_year\": 2018,"
            + "\"selling_price\": 350.00,"
            + "\"library_id\": 1"
            + "}";

        mockMvc.perform(
            post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(status().is(201));

        mockMvc.perform(
            delete("/books/5589197551341")
        )
        .andExpect(status().is(200));

        mockMvc.perform(
            get("/books/5589197551341")
        )
        .andExpect(status().is(404));

        String data = mockMvc.perform(
            get("/libraries/1")
        )
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode dataJson = mapper.readTree(data);

        assertEquals(dataJson.get("id").asLong(), 1);
        assertEquals(dataJson.get("name").asText(), "library-1");

        JsonNode[] books = mapper.readValue(
            dataJson.get("books").traverse(),
            JsonNode[].class
        );

        String[] actualIsbn = Stream.of(books)
            .map(book -> book.get("isbn").asText())
            .sorted()
            .collect(toList())
            .toArray(new String[0]);

        String[] expectedIsbn = new String[] {"5589197551342"};

        assertArrayEquals(expectedIsbn, actualIsbn);
    }

    @Test
    public void createLibraryCreateBookAndDeleteLibrary() throws Exception {
        mockMvc.perform(
            post("/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"library-1\"}")
        )
        .andExpect(status().is(201));

        mockMvc.perform(
            post("/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"library-2\"}")
        )
        .andExpect(status().is(201));

        String body = "{"
            + "\"isbn\": \"5589197551341\","
            + "\"name\": \"book-1\","
            + "\"author_name\": \"author-1\","
            + "\"publication_year\": 2018,"
            + "\"selling_price\": 250.00,"
            + "\"library_id\": 1"
            + "}";

        mockMvc.perform(
            post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(status().is(201));

        body = "{"
            + "\"isbn\": \"5589197551342\","
            + "\"name\": \"book-2\","
            + "\"author_name\": \"author-1, author-2\","
            + "\"publication_year\": 2018,"
            + "\"selling_price\": 350.00,"
            + "\"library_id\": 2"
            + "}";

        mockMvc.perform(
            post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(status().is(201));

        mockMvc.perform(
            delete("/libraries/1")
        )
        .andExpect(status().is(200));

        mockMvc.perform(
            get("/libraries/1")
        )
        .andExpect(status().is(404));

        String data = mockMvc.perform(
            get("/books/5589197551341")
        )
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode dataJson = mapper.readTree(data);

        assertEquals(dataJson.get("library_id").asLong(), 0);
    }
}
