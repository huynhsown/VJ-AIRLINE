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
            .otp-box {
                background-color: #f8f9fa;
                border: 2px dashed #1a73e8;
                border-radius: 8px;
                padding: 20px;
                display: inline-block;
            }
            .otp-code {
                font-size: 32px;
                font-weight: bold;
                color: #1a73e8;
                letter-spacing: 5px;
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