package com.vietjoke.vn.constant;

public final class PassengerConstants {

    public static final int MIN_PASSENGERS = 1;
    public static final int MAX_PASSENGERS = 9;

    public static final int MIN_ADULT_PASSENGERS = 1;
    public static final int MAX_ADULT_PASSENGERS = 9;

    public static final int MIN_CHILD_PASSENGERS = 0;
    public static final int MAX_CHILD_PASSENGERS = 8;

    // Số lượng tối thiểu cho trẻ sơ sinh
    public static final int MIN_INFANT_PASSENGERS = 0;

    // Tỷ lệ tối đa trẻ sơ sinh so với người lớn (ví dụ: 1 người lớn chỉ được mang 2 trẻ sơ sinh)
    public static final int MAX_INFANT_PER_ADULT = 1;

    // Constructor private để ngăn khởi tạo đối tượng
    private PassengerConstants() {
        throw new UnsupportedOperationException("This is a constant class and cannot be instantiated");
    }
}