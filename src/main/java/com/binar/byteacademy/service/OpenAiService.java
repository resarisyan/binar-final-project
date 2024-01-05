package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.ChatOpenAiResponse;

public interface OpenAiService {
    ChatOpenAiResponse chat(String message);
}
