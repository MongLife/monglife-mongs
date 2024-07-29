package com.mongs.play.domain.payment.service;

import com.mongs.play.core.error.domain.PaymentErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.payment.entity.ExchangeItem;
import com.mongs.play.domain.payment.repository.ExchangeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeItemService {

    private final ExchangeItemRepository exchangeItemRepository;

    @Transactional(transactionManager = "paymentTransactionManager")
    public List<ExchangeItem> getExchangeItem() {
        return exchangeItemRepository.findAll();
    }

    @Transactional(transactionManager = "paymentTransactionManager")
    public ExchangeItem getExchangeItem(String exchangeItemId) throws NotFoundException {
        return exchangeItemRepository.findById(exchangeItemId)
                .orElseThrow(() -> new NotFoundException(PaymentErrorCode.NOT_FOUND_EXCHANGE_ITEM));
    }

    @Transactional(transactionManager = "paymentTransactionManager")
    public ExchangeItem addExchangeItem(ExchangeItem exchangeItem) {
        return exchangeItemRepository.save(exchangeItem);
    }
}
