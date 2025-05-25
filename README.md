# VIETJAY_AIRLINE

Online flight booking system of VIETJAY airline.

## Overview

VIETJAY_AIRLINE is a web application developed with Spring Boot, providing online flight booking features for customers. The system allows users to search flights, select ticket classes, choose seats, and complete the booking process.

## Main Features

### Flight Search
- Users can search flights based on departure location, destination, flight date, and number of passengers.
- The system displays a list of flights matching the search criteria.

### Flight Selection
- Users can select a flight and a suitable ticket class.
- The system checks seat availability and updates booking information.

### Seat Booking
- Users can select specific seats on the airplane.
- The system displays the seat map and the status of each seat.

### Passenger Information Management
- Users enter detailed passenger information.
- The system stores passenger information for the booking process.

### Payment
- Users can pay for their flight tickets.
- The system processes payments and updates booking status.

## Technologies Used

- **Backend**: Spring Boot, Spring Data JPA, Spring Security
- **Database**: MySQL
- **Build Tool**: Maven or Gradle

## Installation and Running

### System Requirements
- JDK 17 or higher
- Maven or Gradle
- Compatible database

### Installation Steps
1. Clone the repository
2. Run docker with the command `docker-compose up -d`
3. Run `mvn clean install`
4. Run `mvn spring-boot:run`

## Frontend (Mobile App - Android)

The mobile application for Android is developed separately and is located at:  
[https://github.com/VanLuanNguyen/UI-VJ-AIRLINE](https://github.com/VanLuanNguyen/UI-VJ-AIRLINE)

Please refer to the mobile app repository for installation, usage, and deployment instructions.

## Backend Repository

The backend repository is located at:  
[https://github.com/huynhsown/VJ-AIRLINE](https://github.com/huynhsown/VJ-AIRLINE)

## Contributing

All contributions are welcome. Please create issues or pull requests to contribute to this project.

## License

This project is licensed under the [MIT License](LICENSE).
