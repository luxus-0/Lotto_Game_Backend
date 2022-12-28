package pl.lotto.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.lotto.numberreceiver.dto.NumberReceiverDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class NumberReceiverIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    NumberReceiverIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    void shouldReturnStatus200WhenUserGetInputNumbers() throws Exception {
        //given
        MvcResult mvcResult = mockMvc.perform(
                get("/numbers"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        //when
        NumberReceiverDto numberReceiver = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NumberReceiverDto.class);

        //then
        assertThat(numberReceiver).isNotNull();
    }
}
