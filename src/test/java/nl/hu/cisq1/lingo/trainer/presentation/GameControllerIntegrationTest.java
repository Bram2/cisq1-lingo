package nl.hu.cisq1.lingo.trainer.presentation;

import com.jayway.jsonpath.JsonPath;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private int gameID;

    @BeforeEach
    void initializeGame() throws Exception {

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                .post("/game")
        ).andReturn();

        gameID = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");
    }

    @Test
    @DisplayName("Start a game")
    void startGame() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .post("/game");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.lastHint").isArray())
                .andExpect(jsonPath("$.feedback").isArray())
                .andExpect(jsonPath("$.gameState").value("PLAYING"))
                .andExpect(jsonPath("$.roundNumber").value(1));
    }

    @Test
    @DisplayName("Make a guess")
    void guess() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/{id}/guess", gameID)
                .content("WOORD");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastHint").isArray())
                .andExpect(jsonPath("$.feedback").isArray())
                .andExpect(jsonPath("$.roundNumber").value(1));
    }


    @Test
    @DisplayName("Get a game with a valid ID")
    void getGame() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get("/game/{id}", gameID);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gameID));
    }

    @Test
    @DisplayName("Get a game with an invalid ID")
    void getGameInvalidID() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get("/game/9999", gameID);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }


}
