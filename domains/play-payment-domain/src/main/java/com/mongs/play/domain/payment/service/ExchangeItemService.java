package com.mongs.play.domain.payment.service;

import com.mongs.play.core.error.domain.PaymentErrorCode;
import com.mongs.play.core.exception.domain.NotFoundException;
import com.mongs.play.domain.payment.entity.ExchangeItem;
import com.mongs.play.domain.payment.repository.ExchangeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeItemService {

    private final ExchangeItemRepository exchangeItemRepository;

    public List<ExchangeItem> getExchangeItem() {
        return exchangeItemRepository.findAll();
    }

    public ExchangeItem getExchangeItem(String exchangeItemId) throws NotFoundException {
        return exchangeItemRepository.findById(exchangeItemId)
                .orElseThrow(() -> new NotFoundException(PaymentErrorCode.NOT_FOUND_EXCHANGE_ITEM));
    }

    public ExchangeItem addExchangeItem(ExchangeItem exchangeItem) {
        return exchangeItemRepository.save(exchangeItem);
    }
}
