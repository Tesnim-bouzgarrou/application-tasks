# Application Tasks

This project implements a simple service that manages products for a specific store.
Currently the service only provides basic information about products.
Your task is to extend the service so that consumers of the service can also buy products.

The service uses the following technologies:

- Java 8
- Spring Boot (Spring WebMVC, Spring Data)
- JPA
- In-memory H2 database
- Gradle

You can start the service locally with Gradle:

```
./gradlew bootRun
```

The service listens on port 8080 for incoming requests.
It provides information about a few products that are defined in `src/main/resources/data.sql`.

## General information

- The service should provide RESTful resources for managing products and carts.
  The consumers of these resources are other services or mobile apps.
- Write a reasonable amount of tests for any new features.
  Using a test-driven approach might be a good idea, but it is up to you, how you test your code.
- You should commit changes while you are working on the tasks.
  Please include the complete commit history with your final result.
- If something is missing, ambiguous or contradictory just do "the right thing" from your point of view.

## Task 1: Manage products

- Add the ability to create and update products.
- Validate the product and enforce data integrity constraints:
  - SKU: required, not empty, unique
  - Name: required, not empty
  - EANs: At least one, non-empty item

## Task 2: Prices for products

- Extend the `Product` class with information about its price.
- Add validation for the new attribute: required, greater than 0
- Store the price in the database.

## Task 3: Manage carts

- Add the ability to manage carts.
  A user should be able to:
  - Create a new cart
  - Update their cart
  - Get their current cart
- Requirements for a cart:
  - Contains a non-empty list of items
  - Each item can have a specific quantity
  - Includes the total price of all items

## Task 4: Checkout carts

- Add the ability to checkout carts.
  A user should be able to mark their cart as checked out.
- Carts that have been checked out cannot be changed anymore.

## Bonus Task 1: Available products

- Not all products are available at any given time.
  Add the ability to flag a product as available / unavailable.
- Unavailable products cannot be added to a cart.

## Bonus Task 2: Cart reporting

- Write a simple UI that shows a list of all checked out carts.
- Show at least the following information:
  - URI of the Cart
  - Items
  - Total price
  - Date and time of the checkout
