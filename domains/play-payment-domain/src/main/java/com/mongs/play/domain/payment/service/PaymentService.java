package com.mongs.play.domain.payment.service;

import com.mongs.play.core.error.domain.PaymentErrorCode;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.payment.entity.PaymentLog;
import com.mongs.play.domain.payment.enums.PaymentCode;
import com.mongs.play.domain.payment.repository.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentLogRepository paymentLogRepository;

    public PaymentLog addBuySlotLog(Long accountId, String deviceId) {

        String receipt = UUID.randomUUID().toString().replace("-", "");

        return paymentLogRepository.save(PaymentLog.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .receipt(receipt)
                .code(PaymentCode.BUY_SLOT)
                .build());
    }

    public PaymentLog addChargeStarPointLog(Long accountId, String deviceId, String receipt) throws InvalidException {

        // TODO(" 영수증 확인 로직 ")
        boolean isInvalid = true;

        if (!isInvalid) {
            throw new InvalidException(PaymentErrorCode.INVALID_RECEIPT);
        }

        return paymentLogRepository.save(PaymentLog.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .receipt(receipt)
                .code(PaymentCode.CHARGE_STAR_POINT)
                .build());
    }

    public PaymentLog addExchangePayPointLog(Long accountId, String deviceId) {

        String receipt = UUID.randomUUID().toString().replace("-", "");

        return paymentLogRepository.save(PaymentLog.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .receipt(receipt)
                .code(PaymentCode.EXCHANGE_PAY_POINT)
                .build());
    }

    public PaymentLog itemReward(Long paymentLogId) {

        PaymentLog paymentLog = paymentLogRepository.findById(paymentLogId)
                .orElseThrow(() -> new NotFoundException(PaymentErrorCode.NOT_FOUND_PAYMENT_LOG));

        return paymentLogRepository.save(paymentLog.toBuilder()
                .isReward(true)
                .build());
    }
}
