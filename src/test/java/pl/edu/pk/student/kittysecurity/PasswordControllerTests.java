package pl.edu.pk.student.kittysecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.edu.pk.student.kittysecurity.controller.PasswordController;
import pl.edu.pk.student.kittysecurity.dto.password.*;
import pl.edu.pk.student.kittysecurity.services.PasswordService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PasswordControllerTests {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private PasswordController passwordController;

    private final String token = "Bearer test.jwt.token";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(passwordController).build();
    }

    @Test
    public void shouldAddPasswordSuccessfully() throws Exception {
        CreatePasswordRequestDto requestDto = CreatePasswordRequestDto.builder()
                .url("https://example.com")
                .login("user@example.com")
                .serviceName("ExampleService")
                .password("encrypted-password")
                .Iv("test-iv")
                .build();

        CreatePasswordResponseDto responseDto = CreatePasswordResponseDto.builder()
                .status("success")
                .id(1L)
                .serviceName("ExampleService")
                .url("https://example.com")
                .login("user@example.com")
                .encrypted("encrypted-password")
                .Iv("test-iv")
                .build();

        Mockito.when(passwordService.addPasswordByJwt(Mockito.eq(token), Mockito.any(CreatePasswordRequestDto.class)))
                .thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/password")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.serviceName", is("ExampleService")))
                .andExpect(jsonPath("$.url", is("https://example.com")))
                .andExpect(jsonPath("$.login", is("user@example.com")))
                .andExpect(jsonPath("$.encrypted", is("encrypted-password")))
                .andExpect(jsonPath("$.IV", is("test-iv")));
    }

    @Test
    public void shouldGetAllPasswords() throws Exception {
        PasswordEntryDto entry = PasswordEntryDto.builder()
                .entryId(1L)
                .serviceName("ExampleService")
                .url("https://example.com")
                .login("user@example.com")
                .passwordEncrypted("encrypted-password")
                .Iv("test-iv")
                .build();

        Mockito.when(passwordService.getAllPasswordsByJwt(Mockito.eq(token)))
                .thenReturn(ResponseEntity.ok(List.of(entry)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/passwords")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ExampleService")))
                .andExpect(jsonPath("$[0].url", is("https://example.com")))
                .andExpect(jsonPath("$[0].login", is("user@example.com")))
                .andExpect(jsonPath("$[0].encrypted", is("encrypted-password")))
                .andExpect(jsonPath("$[0].IV", is("test-iv")));
    }

    @Test
    public void shouldGetPasswordById() throws Exception {
        PasswordEntryDto entry = PasswordEntryDto.builder()
                .entryId(1L)
                .serviceName("ExampleService")
                .url("https://example.com")
                .login("user@example.com")
                .passwordEncrypted("encrypted-password")
                .Iv("test-iv")
                .build();

        Mockito.when(passwordService.getPasswordByIdAndJwt(Mockito.eq(token), Mockito.eq(1L)))
                .thenReturn(ResponseEntity.ok(entry));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/password/1")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ExampleService")))
                .andExpect(jsonPath("$.url", is("https://example.com")))
                .andExpect(jsonPath("$.login", is("user@example.com")))
                .andExpect(jsonPath("$.encrypted", is("encrypted-password")))
                .andExpect(jsonPath("$.IV", is("test-iv")));
    }
}
