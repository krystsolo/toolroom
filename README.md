# toolroom - backend
Toolroom app - let your production company increase productivity through operations automatization made in toolroom

Technologies, frameworks and tools used to create application:
* JAVA 8 
* Spring 5, SpringBoot 2
* JPA, H2
* Maven
* JWT (JSON Web Token Authentication)
* Lombok, MapStruct

Will appear soon: 
* MySQL as production DB
* Docker 

Known problems which must and will be resolved later: 
* Add pagination and sorting support for client requests that return a collection of objects, because returning a list of all objects would be critical for quality and speed provided services. It would also allow client app to not have to deal with it.
* Add GET request params which would allow move logic of filtering data from client side to server (the best option seems to tranfer this logic straight to repositories) 
