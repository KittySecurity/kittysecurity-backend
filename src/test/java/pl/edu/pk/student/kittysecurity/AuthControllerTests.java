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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pk.student.kittysecurity.controller.AuthController;
import pl.edu.pk.student.kittysecurity.dto.auth.*;
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

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }


    @Test
    public void shouldReturnUserDataWhenRegistered() throws Exception {
        RegisterRequestDto request = RegisterRequestDto.builder()
                                                        .email("exampleuser@examplemail.com")
                                                        .masterHash("Password123")
                                                        .displayName("exampleusername")
                                                        .build();

        RegisterResponseDto response = RegisterResponseDto.builder()
                                                        .email("exampleuser@examplemail.com")
                                                        .username("exampleusername")
                                                        .status("OK")
                                                        .build();

        Mockito.when(authService.register(Mockito.any(RegisterRequestDto.class))).thenReturn(ResponseEntity.ok().body(response));

        String content = objectWriter.writeValueAsString(request);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("exampleusername")))
                .andExpect(jsonPath("$.email", is("exampleuser@examplemail.com")))
                .andExpect(jsonPath("$.status", is("OK")));
    }

    @Test
    public void shouldReturnJwtResponseWhenLoginWithValidCredentials() throws Exception {

        JwtResponseDto response = JwtResponseDto.builder()
                .accessTokenExpiresIn(10000)
                .accessToken("12345")
                .accessTokenType("Bearer")
                .refreshToken("54321")
                .refreshTokenExpiresIn(10000000)
                .build();

        LoginRequestDto request = LoginRequestDto.builder()
                .email("testuser")
                .masterHash("testpassword")
                .build();

        Mockito.when(authService.verify(Mockito.any(LoginRequestDto.class))).thenReturn(ResponseEntity.ok().body(response));

        String content = objectWriter.writeValueAsString(request);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.access_token", is("12345")))
                .andExpect(jsonPath("$.access_token_type", is("Bearer")))
                .andExpect(jsonPath("$.refresh_token", is("54321")))
                .andExpect(jsonPath("$.refresh_token_expires_in", is(10000000)))
                .andExpect(jsonPath("$.access_token_expires_in", is(10000)));
    }

    @Test
    public void shouldRevokeTokenSuccessfully() throws Exception {
        RefreshTokenRequestDto request = RefreshTokenRequestDto.builder()
                .token("dummy-refresh-token")
                .build();

        Mockito.doNothing().when(authService).revokeToken(Mockito.anyString());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnJwtResponseWhenRefreshingToken() throws Exception {
        RefreshTokenRequestDto request = RefreshTokenRequestDto.builder()
                .token("dummy-refresh-token")
                .build();

        JwtResponseDto response = JwtResponseDto.builder()
                .accessToken("new-access-token")
                .accessTokenExpiresIn(10000)
                .accessTokenType("Bearer")
                .refreshToken("new-refresh-token")
                .refreshTokenExpiresIn(10000000)
                .build();

        Mockito.when(authService.refreshToken(Mockito.anyString()))
                .thenReturn(ResponseEntity.ok(response));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", is("new-access-token")))
                .andExpect(jsonPath("$.access_token_type", is("Bearer")))
                .andExpect(jsonPath("$.refresh_token", is("new-refresh-token")))
                .andExpect(jsonPath("$.refresh_token_expires_in", is(10000000)))
                .andExpect(jsonPath("$.access_token_expires_in", is(10000)));
    }
}
