package com.mathdenizi.notes.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathdenizi.notes.models.examples.*;
import com.mathdenizi.notes.services.OpenAIService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by mahtweb
 */
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatModel chatModel;

    public OpenAIServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;

    @Value("classpath:templates/get-capital-with-info.st")
    private Resource getCapitalPromptWithInfo;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public GetCapitalWithInfoResponse getCapitalWithInfo(GetCapitalRequest getCapitalRequest) {
        BeanOutputConverter<GetCapitalWithInfoResponse> converter = new BeanOutputConverter<>(GetCapitalWithInfoResponse.class);
        String format = converter.getFormat();

        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry(),
                "format", format));
        ChatResponse response = chatModel.call(prompt);

        return converter.convert(response.getResult().getOutput().getText());
    }


    @Override
    public GetCapitalResponse getCapital(GetCapitalRequest getCapitalRequest) {

        BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
        String format = converter.getFormat();
        System.out.println("Format: \n " + format);

        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry(),
                "format", format));
        ChatResponse response = chatModel.call(prompt);

        // OpenAI yanıtını al
        String rawResponse = response.getResult().getOutput().getText();
        System.out.println("Raw OpenAI Response: " + rawResponse);

        // Yanıttaki Markdown formatlı ```json ve ``` karakterlerini temizle
        String cleanedResponse = rawResponse.replaceAll("```json", "").replaceAll("```", "").trim();
        System.out.println("Cleaned Response: " + cleanedResponse);


        return converter.convert(response.getResult().getOutput().getText());
    }



    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public String getAnswer(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(question);
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);

        return response.getResults().get(0).getOutput().getText();
    }
}