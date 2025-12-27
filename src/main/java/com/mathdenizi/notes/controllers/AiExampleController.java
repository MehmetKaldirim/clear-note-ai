package com.mathdenizi.notes.controllers;

import com.mathdenizi.notes.models.examples.*;
import com.mathdenizi.notes.services.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mathdenizi
 * Date: 29.11.25
 */
@RestController
@RequestMapping("/api/ai")
public class AiExampleController {

    private final OpenAIService openAIService;

    public AiExampleController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }


    @PostMapping("/capitalWithInfo")
    public GetCapitalWithInfoResponse getCapitalWithInfo(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapitalWithInfo(getCapitalRequest);
    }

    @PostMapping("/capital")
    public GetCapitalResponse getCapital(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapital(getCapitalRequest);
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }
}