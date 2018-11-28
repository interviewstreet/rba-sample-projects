## Problem Statement

Create a REST API to manage books and libraries.

**Models**

- The `book` has the following attributes:
    - `isbn`: The unique string identifier.
    - `name`: The name of the book.
    - `author_name`: The author name.
    - `publication_year`: The year of publication.
    - `selling_price`: The selling price.

- The `library` has the following attributes:
    - `id`: The autogenerated sequential unique integer identifier.
    - `name`: The name of the library.

**Routes**

- *Books Controller*
    - `POST '/books'`: Creates a book. The request body is a JSON:
        ```json
        {
          "isbn":,
          "name":,
          "author_name":,
          "publication_year":,
          "selling_price":,
          "library_id":
        }
        ```
        Here, `library_id` is optional. It implies that the book should be added to the specified library if not present.
    - `PUT '/books/{isbn}'`: Updates book attributes. Note that updating the library must remove the book from the existing library.
    - `GET '/books'`: Returns all the books. The response should be JSON array:
        ```json
        [
          {
            "isbn":,
            "name":,
            "author_name":,
            "publication_year":,
            "selling_price":,
            "library_id":
          }
        ]
        ```
    - `GET '/books/{isbn}'`: Returns a book. The response should be JSON:
        ```json
        {
          "isbn":,
          "name":,
          "author_name":,
          "publication_year":,
          "selling_price":,
          "library_id":
        }
        ```
    - `DELETE '/books/{isbn}'`: Deletes a book.

- *Libraries Controller*
    - `POST '/libraries'`: Creates a library. The request body is a JSON:
        ```json
        {
          "name":
        }
        ```
    - `GET '/libraries'`: Returns all the libraries. The response should be JSON array:
        ```json
        [
          {
            "id":,
            "name":,
            "books":
          }
        ]
        ```
        Here, `books` is JSON array describing all the books present in the library:
        ```json
        [
          {
            "isbn":,
            "name":,
            "author_name":,
            "publication_year":,
            "selling_price":
          }
        ]
        ```
    - `GET '/libraries/{id}'`: Returns a library. The response should be JSON:
        ```json
        {
          "id":,
          "name":,
          "books":
        }
        ```
        Here, `books` is JSON array describing all the books present in the library:
        ```json
        [
          {
            "isbn":,
            "name":,
            "author_name":,
            "publication_year":,
            "selling_price":
          }
        ]
        ```
    - `DELETE '/libraries/{id}'`: Deletes a library. Note that deleting a library must not destroy the books but should update the library attribute for each of the books present in the library.


## Uploading on HackerRank Platform

- **Project Requirements**

  We assume the project passes all the tests on local.
  - Define server `address` and `port` by creating `src/main/resources/application.properties`:
      ```text
      server.address=0.0.0.0
      server.port=8000
      ```
  - You should not upload a working project. Although the incomplete version of the project must not result in compile errors when running the tests, i.e., the JUnit XML reports should always be generated.
      - Complete project:
      ```text
      └── com
          └── hackerrank
              ├── Application.java
              ├── controllers
              │   ├── BooksController.java
              │   └── LibrariesController.java
              ├── dao
              │   └── BookDao.java
              ├── models
              │   ├── Book.java
              │   └── Library.java
              └── repositories
                  ├── BooksRepository.java
                  └── LibrariesRepository.java
      ```
      - Incomplete project:
      ```text
      └── com
          └── hackerrank
              ├── Application.java
              ├── controllers
              ├── models
              └── repositories
      ```
  - Optionally, you can create a test suite `TestSuite.java` to execute all the tests if more than one tests present, to get all the tests status in one XML report.
      ```text
      └── com
          └── hackerrank
              ├── TestSuite.java
              └── requests
                  ├── BooksAndLibrariesControllerTest.java
                  ├── BooksControllerTest.java
                  └── LibrariesControllerTest.java
      ```

- **Project Configuration**

  The project configurations are specified by the file `hackerrank.yml`. The following properties should be defined:
  - `version`: The version.
  - `configuration`: It defines how to configure the scoring and IDE:
      - `has_webserver`: Whether the app requires web server or not.
      - `scoring`:
          - `command`: The test command.
          - `files`: An array describing JUnit XML reports relative path.
          - `testcase_weights`: An optional hash describing the testcase weights. The sum of all the weights must be one.
      - `ide_config`:
          - `default_open_files`: An array describing the files to open on the IDE.
          - `project_menu`: It describes the required commands in the project menu:
              - `install`: Defines how dependencies are installed if required.
              - `run`: Defines how to run the project.
              - `test`: Defines how project tests are executed.

  Sample `hackerrank.yml` configuration:
  ```yml
  version: 1.0
  configuration:
      has_webserver: True
      scoring:
          command: "mvn clean test -Dtest=TestSuite"
          files:
              - "target/surefire-reports/TEST-com.hackerrank.TestSuite.xml"
          testcase_weights:
              "createLibrary": 0.07
              "getLibraryById": 0.07
              "getAllLibraries": 0.07
              "deleteLibrary": 0.07
              "getAllBooks": 0.07
              "updateBook": 0.07
              "createBook": 0.08
              "getBookByIsbn": 0.07
              "deleteBook": 0.07
              "createLibraryAndCreateBook": 0.12
              "createLibraryCreateBookAndDeleteBook": 0.12
              "createLibraryCreateBookAndDeleteLibrary": 0.12
      ide_config:
          default_open_files:
              - "src/test/java/com/hackerrank/requests/BooksControllerTest.java"
              - "src/test/java/com/hackerrank/requests/LibrariesControllerTest.java"
              - "src/test/java/com/hackerrank/requests/BooksAndLibrariesControllerTest.java"
          project_menu:
              install: "mvn clean install"
              run: "mvn spring-boot:run"
              test: "mvn test -Dtest=TestSuite"
  ```

- **Uploading Zip**

  The zip should exclude the project name directory:
  ```text
  .
  ├── hackerrank.yml
  ├── pom.xml
  └── src
      ├── main
      │   ├── java
      │   │   └── com
      │   │       └── hackerrank
      │   │           ├── Application.java
      │   │           ├── controllers
      │   │           ├── models
      │   │           └── repositories
      │   └── resources
      │       └── application.properties
      └── test
          └── java
              └── com
                  └── hackerrank
                      ├── TestSuite.java
                      └── requests
                          ├── BooksAndLibrariesControllerTest.java
                          ├── BooksControllerTest.java
                          └── LibrariesControllerTest.java
  ```