package com.monglife.mongs.adapter.in.mong.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class InteractionController {
}
