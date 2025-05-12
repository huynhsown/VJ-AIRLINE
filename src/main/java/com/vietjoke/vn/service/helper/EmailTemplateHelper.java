package com.vietjoke.vn.service.helper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailTemplateHelper {

    public String getRegistrationTemplate(String fullName, String otp) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    %s
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>VietJoke Airline</h1>
                </div>
                <div class="content">
                    <h2>Xin chào %s,</h2>
                    <p>Cảm ơn bạn đã đăng ký tài khoản tại VietJoke Airline. Chúng tôi rất vui mừng được chào đón bạn!</p>
                    <p>Để hoàn tất quá trình đăng ký, vui lòng sử dụng mã OTP sau:</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <div class="otp-box">
                            <span class="otp-code">%s</span>
                        </div>
                    </div>
                    <p style="color: #666; font-size: 14px;">
                        Mã OTP này sẽ hết hạn sau 5 phút.<br>
                        Nếu bạn không thực hiện đăng ký tài khoản này, vui lòng bỏ qua email này.
                    </p>
                </div>
                <div class="footer">
                    <p>Email này được gửi tự động, vui lòng không trả lời.</p>
                    <p>&copy; 2024 VietJoke Airline. All rights reserved.</p>
                </div>
            </body>
            </html>
            """.formatted(getCommonStyles(), fullName, otp);
    }

    public String getBookingSuccessTemplate(String fullName, String bookingDetails, boolean isPaid) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    %s
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Đặt Vé Thành Công</h1>
                </div>
                <div class="content">
                    <h2>Xin chào %s,</h2>
                    <p>Cảm ơn bạn đã đặt vé với VietJoke Airline. Đặt vé của bạn đã được xác nhận!</p>
                    
                    <div class="booking-details">
                        %s
                    </div>

                    %s

                    <div class="important-note">
                        <h3>Lưu ý quan trọng:</h3>
                        <ul>
                            <li>Vui lòng đến sân bay ít nhất 2 giờ trước giờ khởi hành</li>
                            <li>Mang theo giấy tờ tùy thân hợp lệ khi làm thủ tục check-in</li>
                            <li>Kiểm tra kỹ thông tin hành lý xách tay và ký gửi</li>
                        </ul>
                    </div>

                    <div class="action-buttons">
                        <a href="%s" class="button">Xem Chi Tiết Đặt Vé</a>
                        <a href="%s" class="button secondary">Liên Hệ Hỗ Trợ</a>
                    </div>
                </div>
                <div class="footer">
                    <p>Email này được gửi tự động, vui lòng không trả lời.</p>
                    <p>&copy; 2024 VietJoke Airline. All rights reserved.</p>
                </div>
            </body>
            </html>
            """.formatted(
                getCommonStyles(),
                fullName,
                bookingDetails,
                isPaid ? getPaidBookingContent() : getUnpaidBookingContent(),
                "https://vietjoke.vn/booking-details",
                "https://vietjoke.vn/support"
            );
    }

    private String getPaidBookingContent() {
        return """
            <div class="payment-status success">
                <h3>Trạng thái thanh toán: <span class="status">Đã thanh toán</span></h3>
                <p>Bạn đã hoàn tất thanh toán cho đặt vé này. Vui lòng kiểm tra email để xem hóa đơn chi tiết.</p>
            </div>
            """;
    }

    private String getUnpaidBookingContent() {
        return """
            <div class="payment-status warning">
                <h3>Trạng thái thanh toán: <span class="status">Chưa thanh toán</span></h3>
                <p>Vui lòng hoàn tất thanh toán trong vòng 24 giờ để giữ chỗ. Sau thời gian này, đặt vé sẽ tự động hủy.</p>
                <div class="payment-options">
                    <h4>Các phương thức thanh toán:</h4>
                    <ul>
                        <li>Chuyển khoản ngân hàng</li>
                        <li>Thẻ tín dụng/ghi nợ</li>
                        <li>Ví điện tử (MoMo, ZaloPay, VNPay)</li>
                    </ul>
                </div>
            </div>
            """;
    }

    private String getCommonStyles() {
        return """
            body {
                font-family: Arial, sans-serif;
                line-height: 1.6;
                color: #333;
                max-width: 600px;
                margin: 0 auto;
                padding: 20px;
            }
            .header {
                background-color: #1a73e8;
                color: white;
                padding: 20px;
                text-align: center;
                border-radius: 5px 5px 0 0;
            }
            .content {
                padding: 20px;
                background-color: #f9f9f9;
                border: 1px solid #ddd;
                border-top: none;
                border-radius: 0 0 5px 5px;
            }
            .booking-details {
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                border: 1px solid #ddd;
                margin: 20px 0;
            }
            .payment-status {
                padding: 15px;
                border-radius: 8px;
                margin: 20px 0;
            }
            .payment-status.success {
                background-color: #e6f4ea;
                border: 1px solid #34a853;
            }
            .payment-status.warning {
                background-color: #fef7e0;
                border: 1px solid #fbbc04;
            }
            .status {
                font-weight: bold;
            }
            .payment-status.success .status {
                color: #34a853;
            }
            .payment-status.warning .status {
                color: #fbbc04;
            }
            .important-note {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin: 20px 0;
            }
            .important-note h3 {
                color: #1a73e8;
                margin-top: 0;
            }
            .important-note ul {
                margin: 10px 0;
                padding-left: 20px;
            }
            .action-buttons {
                text-align: center;
                margin: 30px 0;
            }
            .button {
                display: inline-block;
                padding: 12px 24px;
                background-color: #1a73e8;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                margin: 0 10px;
            }
            .button.secondary {
                background-color: #5f6368;
            }
            .footer {
                text-align: center;
                margin-top: 20px;
                padding-top: 20px;
                border-top: 1px solid #ddd;
                color: #666;
                font-size: 12px;
            }
            """;
    }
}