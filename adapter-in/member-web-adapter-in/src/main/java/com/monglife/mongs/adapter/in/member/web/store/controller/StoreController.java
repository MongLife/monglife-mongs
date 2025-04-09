package com.monglife.mongs.adapter.in.member.web.store.controller;

import com.monglife.mongs.application.member.port.in.StoreUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreUseCase storeUseCase;
}
