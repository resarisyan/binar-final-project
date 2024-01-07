package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.ChatOpenAiResponse;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import static java.util.Arrays.asList;
import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;

@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {
    @Value("${openai.api-key}")
    private String apiKey;

    @Override
    public ChatOpenAiResponse chat(String message) {
        List<ChatMessage> messages = asList(
                systemMessage( "You are a assistant programmer professional of a company named 'Byte Academy'"),
                userMessage(message)
        );
        String reply = aiChatModel(apiKey).
                generate(messages).content().text();

        return ChatOpenAiResponse.builder()
                .reply(reply)
                .build();
    }

    private static OpenAiChatModel aiChatModel(String apiKey) {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .build();
    }
}
