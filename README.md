# VIETJOKE_AIRLINE

Hệ thống đặt vé máy bay trực tuyến của hãng hàng không VIETJOKE.

## Tổng quan

VIETJOKE_AIRLINE là một ứng dụng web được phát triển bằng Spring Boot, cung cấp các chức năng đặt vé máy bay trực tuyến cho khách hàng. Hệ thống cho phép người dùng tìm kiếm chuyến bay, chọn hạng vé, đặt chỗ ngồi và hoàn tất quá trình đặt vé.

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
- **Công cụ xây dựng**: Maven hoặc Gradle

## Cài đặt và chạy

### Yêu cầu hệ thống
- JDK 17 trở lên
- Maven hoặc Gradle
- Database tương thích

### Các bước cài đặt
1. Clone repository
2. Truy cập thư mục docker
3. Chạy docker bằng lệnh `docker-compose up -d`
4. Chạy lệnh `mvn clean install`
5. Chạy lệnh `mvn spring-boot:run`

## Đóng góp

Mọi đóng góp đều được hoan nghênh. Vui lòng tạo issue hoặc pull request để đóng góp vào dự án.

## Giấy phép

Dự án này được phân phối dưới giấy phép [MIT](LICENSE). 
