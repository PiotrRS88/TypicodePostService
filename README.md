# TypicodePostService
Running backend and frontend application can be done by:<br/>
mvn spring-boot:run

Application starts in endpoint as below:
http://localhost:8080/

The TypicodePostService provides API functionalities based on the REST architecture. For this purpose, it uses the HTTP protocol with its methods: GET, POST, PUT, DELETE. <br/>
Endpoint to fetch posts from Typicode: <br/>
POST http://localhost:8080/fetchposts  <br/>
<br/>
Endpoint to update title and body of the post with id 1: <br/>
PUT http://localhost:8080/posts/1  <br/>
      Body params: <br/>
        { 
            "title": "New title",
            "body": "New body"
        } <br/>
        <br/>
Endpoint to delete post with id 1: <br/>
DELETE http://localhost:8080/posts/1  <br/>
<br/>
Endpoint to delete post with id 1: <br/>
DELETE http://localhost:8080/posts/1  <br/>
<br/>
Endpoint to get all posts: <br/>
GET http://localhost:8080/posts  <br/>
<br/>
Endpoint to get post by given title: <br/>
GET http://localhost:8080/posts?title=New&nbsp;title <br/>
