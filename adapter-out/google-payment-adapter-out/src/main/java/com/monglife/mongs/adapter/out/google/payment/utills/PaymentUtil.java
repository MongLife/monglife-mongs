package com.monglife.mongs.adapter.out.google.payment.utills;

public class PaymentUtil {

    /**
     * 인앱 상품 가격을 원화 단위로 변환
     * @param priceMicrosStr 마이크로 단위 인앱 상품 가격
     * @return 원화 단위로 변환한 상품 가격
     */
    public static Double priceMicrosToPrice(String priceMicrosStr) {

        String head = priceMicrosStr.substring(0, priceMicrosStr.length() - 6);
        String tail = priceMicrosStr.substring(priceMicrosStr.length() - 6);

        return Double.parseDouble(head + "." + tail);
    }
}
