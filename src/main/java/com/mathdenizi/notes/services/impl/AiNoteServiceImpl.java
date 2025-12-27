package com.mathdenizi.notes.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathdenizi.notes.models.AiNoteCorrectionResponse;
import com.mathdenizi.notes.services.AiNoteService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiNoteServiceImpl implements AiNoteService {

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Value("classpath:templates/ai-prompts-note-correction.st")
    private Resource noteCorrectionPrompt;

    public AiNoteServiceImpl(ChatModel chatModel, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }

    @Override
    public AiNoteCorrectionResponse correctNote(String content) {
        try {
            System.out.println("=== Bu gelen Content ===" + content);

            // 1️⃣ Prompt template
            String promptText = new String(noteCorrectionPrompt.getInputStream().readAllBytes());
            System.out.println("=== Prompt Template ===");
            System.out.println(promptText);
            System.out.println("=======================");

            PromptTemplate promptTemplate = new PromptTemplate(promptText);
            Prompt prompt = promptTemplate.create(Map.of(
                    "content", content
            ));

            // 2️⃣ AI call
            ChatResponse response = chatModel.call(prompt);

            // 3️⃣ AI’dan dönen ham JSON/metin
            String jsonOutput = response.getResult().getOutput().getText();
            System.out.println("=== AI Raw Output ===");
            System.out.println(jsonOutput);
            System.out.println("====================");

            // 4️⃣ JSON -> Record parse
            AiNoteCorrectionResponse result = objectMapper.readValue(jsonOutput, AiNoteCorrectionResponse.class);
            System.out.println("=== Parsed AiNoteCorrectionResponse ===");
            System.out.println("Corrected Content: " + result.correctedContent());
            System.out.println("Mistakes: " + result.mistakes());
            System.out.println("======================================");

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to correct note using AI", e);
        }
    }
}
