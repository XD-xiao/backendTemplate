package com.example.backendtemplate.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class AiController {

    private final ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content(); // 返回 AI 生成的文本内容
    }

}
