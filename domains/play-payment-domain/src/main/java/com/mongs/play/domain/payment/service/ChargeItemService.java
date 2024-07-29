package com.mongs.play.domain.payment.service;

import com.mongs.play.core.error.domain.PaymentErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.payment.entity.ChargeItem;
import com.mongs.play.domain.payment.repository.ChargeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargeItemService {

    private final ChargeItemRepository chargeItemRepository;

    @Transactional(transactionManager = "paymentTransactionManager")
    public List<ChargeItem> getChargeItem() {
        return chargeItemRepository.findAll();
    }

    @Transactional(transactionManager = "paymentTransactionManager")
    public ChargeItem getChargeItem(String chargeItemId) throws NotFoundException {
        return chargeItemRepository.findById(chargeItemId)
                .orElseThrow(() -> new NotFoundException(PaymentErrorCode.NOT_FOUND_CHARGE_ITEM));
    }

    @Transactional(transactionManager = "paymentTransactionManager")
    public ChargeItem addChargeItem(ChargeItem chargeItem) {
        return chargeItemRepository.save(chargeItem);
    }
}
