package tacos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;





import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.web.servlet.MockMvc;
import tacos.web.WebConfig;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;    //MockMvc를 주입

    @Test
    public void testHomePage() throws Exception{
        mockMvc.perform(get("/"))
                //응답이 http 200(OK) 상태가 되어야 한다.
                .andExpect(status().isOk())
                //뷰의 이름은 반드시 home이여야한다.
                .andExpect(view().name("home"))
                //브라우저에 보이는 뷰에는 반드시 "Welcome to..." 텍스트가 포함.
                .andExpect(content().string(
                        containsString("Welcome to...")
                ));

        //세가지중 하나라도 오류일시 테스트는 실패.
    }

}
