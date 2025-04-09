package com.monglife.mongs.adapter.in.member.web.collection.controller;

import com.monglife.mongs.application.member.port.in.CollectionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionUseCase collectionUseCase;
}
