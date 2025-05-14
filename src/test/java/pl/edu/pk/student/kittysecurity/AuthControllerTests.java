package pl.edu.pk.student.kittysecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pk.student.kittysecurity.controller.AuthController;
import pl.edu.pk.student.kittysecurity.dto.RegisterRequestDto;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.services.AuthService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthControllerTests {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final User USER_2 = new User(2,"user2@mail.com","user2","123");

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }


    @Test
    public void registerUserSuccess() throws Exception {
        User USER_1 = new User(1,"exampleuser@examplemail.com", "exampleusername", "password123");

        RegisterRequestDto request = RegisterRequestDto.builder()
                                                        .email("exampleuser@examplemail.com")
                                                        .password("examplepassword")
                                                        .username("exampleusername")
                                                        .build();

        Mockito.when(authService.register(Mockito.any(RegisterRequestDto.class))).thenReturn(USER_1);

        String content = objectWriter.writeValueAsString(request);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("exampleusername")));
    }

}
