# Assignment: URL Shortener

## Description

In this assignment we ask you to implement URL shortener. You might be familiar with seeing shortened URLs like bit.ly or t.co on our Twitter or Facebook feeds. These are examples of shortened URLs, which are a short alias or pointer to a longer page link.
As an example here is shortened URL https://rb.gy/a3woum that will forward you to a very long Google URL with search results on how to iron a shirt.

## Mandatory Requirements

- Design and implement an API for short URL creation
- Implement forwarding of short URLs to the original ones
- There should be some form of persistent storage
- The application should be distributed as one or more Docker images

## Additional Requirements

- Design and implement an API for gathering different statistics

## Assessment

Treat this as a real project. It should be readable, maintainable, and extensible where appropriate.

The implementation should be in Java.

If you will transfer it to another team - it should be clear how to work with it and what is going on.

You should send us a link to a Git repository that we will be able to clone.



###
TO RUN TEST

- /tools/db/up.sh
- ./mvnw test


TO RUN APP
- ./prepare.sh