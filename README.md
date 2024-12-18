# blog-jdbc-postgresql
Work with JDBC and PostgreSQL

Been awhile since I worked with JDBC.  Mostly I have been working with JPA repositories.
This has definitely been a learning experience.

One-to-Many and Many-to-One relationships are much harder to implement.
* Had to change POJOs to contain the row id instead of the Object
* Have to compare existing and updated lists and then create/update/delete entries

The jdbcTemplate returns the number of updated rows.  Took a bit of research to create a PreparedStatement that returned the list of generated keys.  
