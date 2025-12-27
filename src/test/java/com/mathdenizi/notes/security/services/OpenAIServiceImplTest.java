package com.mathdenizi.notes.security.services;

import com.mathdenizi.notes.services.impl.OpenAIServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by mathdenizi
 * Date: 29.11.25
 */
@SpringBootTest
class OpenAIServiceImplTest {

    @Autowired
    OpenAIServiceImpl openAIService;



    @Test
    void getAnswer() {
    String answer= openAIService.getAnswer("what is the capital city of turkey");

        System.out.println(answer);
    }


}