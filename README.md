# VIETJOKE_AIRLINE

Hệ thống đặt vé máy bay trực tuyến của hãng hàng không VIETJOKE.

## Tổng quan

VIETJOKE_AIRLINE là một ứng dụng web được phát triển bằng Spring Boot, cung cấp các chức năng đặt vé máy bay trực tuyến cho khách hàng. Hệ thống cho phép người dùng tìm kiếm chuyến bay, chọn hạng vé, đặt chỗ ngồi và hoàn tất quá trình đặt vé.

## Cấu trúc dự án

Dự án được tổ chức theo mô hình phân lớp (layered architecture) với các thành phần chính sau:

### Controllers
- `FlightController`: Xử lý các yêu cầu liên quan đến chuyến bay như tìm kiếm, chọn chuyến bay.
- Các controller khác cho việc đặt vé, quản lý thông tin hành khách, thanh toán, v.v.

### Services
- `FlightService`: Cung cấp các chức năng nghiệp vụ liên quan đến chuyến bay.
- `SeatReservationService`: Quản lý việc đặt chỗ ngồi trên máy bay.
- `FareClassService`: Quản lý các hạng vé và giá vé.
- `BookingSessionService`: Quản lý phiên đặt vé của người dùng.
- Các service khác cho việc quản lý hãng hàng không, tuyến bay, máy bay, v.v.

### Repositories
- `FlightRepository`: Truy cập dữ liệu liên quan đến chuyến bay.
- `SeatReservationRepository`: Truy cập dữ liệu liên quan đến đặt chỗ ngồi.
- Các repository khác cho việc truy cập dữ liệu của các entity khác.

### Entities
- `FlightEntity`: Đại diện cho thông tin chuyến bay.
- `SeatReservationEntity`: Đại diện cho thông tin đặt chỗ ngồi.
- `FareClassEntity`: Đại diện cho thông tin hạng vé.
- `BookingSession`: Đại diện cho phiên đặt vé của người dùng.
- Các entity khác như `AircraftEntity`, `AircraftModelEntity`, `RouteEntity`, v.v.

### DTOs
- `SearchParamDTO`: Chứa thông tin tìm kiếm chuyến bay.
- `SelectFlightParamDTO`: Chứa thông tin chọn chuyến bay.
- `PassengersInfoParamDTO`: Chứa thông tin hành khách.
- `SeatResponseDTO`: Chứa thông tin phản hồi về chỗ ngồi.
- Các DTO khác cho việc truyền tải dữ liệu giữa các lớp.

### Converters
- `FlightConverter`: Chuyển đổi giữa `FlightEntity` và các DTO liên quan.
- `SeatConverter`: Chuyển đổi giữa `SeatReservationEntity` và các DTO liên quan.
- Các converter khác cho việc chuyển đổi dữ liệu.

## Các chức năng chính

### Tìm kiếm chuyến bay
- Người dùng có thể tìm kiếm chuyến bay dựa trên điểm đi, điểm đến, ngày bay, số lượng hành khách.
- Hệ thống hiển thị danh sách chuyến bay phù hợp với tiêu chí tìm kiếm.

### Chọn chuyến bay
- Người dùng có thể chọn chuyến bay và hạng vé phù hợp.
- Hệ thống kiểm tra tính khả dụng của ghế ngồi và cập nhật thông tin đặt vé.

### Đặt chỗ ngồi
- Người dùng có thể chọn chỗ ngồi cụ thể trên máy bay.
- Hệ thống hiển thị sơ đồ ghế ngồi và trạng thái của từng ghế.

### Quản lý thông tin hành khách
- Người dùng nhập thông tin chi tiết của hành khách.
- Hệ thống lưu trữ thông tin hành khách cho quá trình đặt vé.

### Thanh toán
- Người dùng có thể thanh toán vé máy bay thông qua các phương thức thanh toán khác nhau.
- Hệ thống xử lý thanh toán và cập nhật trạng thái đặt vé.

## Công nghệ sử dụng

- **Backend**: Spring Boot, Spring Data JPA, Spring Security
- **Database**: Có thể là MySQL, PostgreSQL hoặc H2 (tùy thuộc vào cấu hình)
- **Frontend**: Có thể là React, Angular hoặc Vue.js (không được hiển thị trong mã nguồn đã cung cấp)
- **Công cụ xây dựng**: Maven hoặc Gradle

## Cài đặt và chạy

### Yêu cầu hệ thống
- JDK 17 trở lên
- Maven hoặc Gradle
- Database tương thích

### Các bước cài đặt
1. Clone repository
2. Cấu hình database trong file `application.properties` hoặc `application.yml`
3. Chạy lệnh `mvn clean install` để build dự án
4. Chạy lệnh `mvn spring-boot:run` để khởi động ứng dụng

## Cấu trúc dữ liệu

### Chuyến bay (Flight)
- Mã chuyến bay
- Hãng hàng không
- Tuyến bay
- Thời gian khởi hành và hạ cánh
- Cổng và nhà ga
- Trạng thái chuyến bay
- Máy bay
- Danh sách đặt chỗ ngồi
- Danh sách hạng vé

### Đặt chỗ ngồi (SeatReservation)
- Số ghế
- Trạng thái ghế (còn trống, đã đặt, đã chọn)
- Hạng vé
- Chuyến bay

### Hạng vé (FareClass)
- Mã hạng vé
- Tên hạng vé
- Giá vé
- Số lượng ghế còn trống

### Phiên đặt vé (BookingSession)
- Mã phiên
- Thông tin tìm kiếm
- Chuyến bay đã chọn
- Thông tin hành khách
- Thời gian hết hạn

## Đóng góp

Mọi đóng góp đều được hoan nghênh. Vui lòng tạo issue hoặc pull request để đóng góp vào dự án.

## Giấy phép

Dự án này được phân phối dưới giấy phép [MIT](LICENSE). 