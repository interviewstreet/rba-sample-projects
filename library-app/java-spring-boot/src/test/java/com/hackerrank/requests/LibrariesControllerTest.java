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
public class LibrariesControllerTest {
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
    public void createLibrary() throws Exception {
        String data = mockMvc.perform(
            post("/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"library-1\"}")
        )
        .andExpect(status().is(201))
        .andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode dataJson = mapper.readTree(data);

        assertEquals(dataJson.get("id").asLong(), 1);
        assertEquals(dataJson.get("name").asText(), "library-1");

        data = mockMvc.perform(
            post("/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"library-2\"}")
        )
        .andExpect(status().is(201))
        .andReturn()
        .getResponse()
        .getContentAsString();

        dataJson = mapper.readTree(data);

        assertEquals(dataJson.get("id").asLong(), 2);
        assertEquals(dataJson.get("name").asText(), "library-2");
    }

    @Test
    public void getLibraryById() throws Exception {
        mockMvc.perform(
            post("/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"library-1\"}")
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
    }

    @Test
    public void getAllLibraries() throws Exception {
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

        String data = mockMvc.perform(
            get("/libraries")
        )
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode[] libraries = mapper.readValue(data, JsonNode[].class);

        assertEquals(libraries.length, 2);

        Long[] actualId = Stream.of(libraries)
            .map(book -> book.get("id").asLong())
            .sorted()
            .collect(toList())
            .toArray(new Long[0]);

        Long[] expectedId = new Long[] {1L, 2L};

        assertArrayEquals(expectedId, actualId);
    }

    @Test
    public void deleteLibrary() throws Exception {
        mockMvc.perform(
            post("/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"library-1\"}")
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
    }
}
