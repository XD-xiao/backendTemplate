package com.example.backendtemplate.Config;

import com.example.backendtemplate.Tools.UserTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    @Bean
    public ChatMemory chatMemory(){
        return new InMemoryChatMemory();
    }


    @Bean
    public ChatClient chatClient(OllamaChatModel model , UserTools UserTools){

        return ChatClient
                .builder(model)
                .defaultSystem("你是硅基生命体,名叫 贾维斯.")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory()).build()
                )
                .defaultTools(UserTools)
                .build();


    }

}