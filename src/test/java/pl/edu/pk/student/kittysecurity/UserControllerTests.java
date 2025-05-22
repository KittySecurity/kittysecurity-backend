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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pk.student.kittysecurity.controller.UserController;
import pl.edu.pk.student.kittysecurity.dto.auth.*;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.user.DeleteUserRequestDto;
import pl.edu.pk.student.kittysecurity.dto.user.UserResponseDto;
import pl.edu.pk.student.kittysecurity.dto.user.UserUpdateRequestDto;
import pl.edu.pk.student.kittysecurity.dto.user.UserUpdateResponseDto;
import pl.edu.pk.student.kittysecurity.services.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTests {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    public void shouldReturnUserDataWhenTokenIsValid() throws Exception {
        String token = "Bearer valid.jwt.token";

        UserResponseDto userResponse = UserResponseDto.builder()
                .id(1)
                .username("exampleusername")
                .email("exampleuser@examplemail.com")
                .createdAt(1625247600000L)
                .updatedAt(1625347600000L)
                .build();

        Mockito.when(userService.getUserDataByJwt(Mockito.eq(token)))
                .thenReturn(ResponseEntity.ok(userResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("exampleusername")))
                .andExpect(jsonPath("$.email", is("exampleuser@examplemail.com")))
                .andExpect(jsonPath("$.created_at", is(1625247600000L)))
                .andExpect(jsonPath("$.updated_at", is(1625347600000L)));
    }

    @Test
    public void shouldUpdateUserDataWhenTokenIsValid() throws Exception {
        String token = "Bearer valid.jwt.token";

        UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                .displayName("newusername")
                .email("newemail@examplemail.com")
                .build();

        UserUpdateResponseDto updateResponse = UserUpdateResponseDto.builder()
                .status("OK")
                .displayName("newusername")
                .email("newemail@examplemail.com")
                .updatedAt(1625447600000L)
                .build();

        Mockito.when(userService.updateUserByJwt(Mockito.eq(token), Mockito.any(UserUpdateRequestDto.class)))
                .thenReturn(ResponseEntity.ok(updateResponse));

        String content = objectWriter.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("OK")))
                .andExpect(jsonPath("$.username", is("newusername")))
                .andExpect(jsonPath("$.email", is("newemail@examplemail.com")))
                .andExpect(jsonPath("$.updated_at", is(1625447600000L)));
    }

    @Test
    public void shouldDeleteUserWhenTokenIsValid() throws Exception {
        String token = "Bearer valid.jwt.token";

        DeleteUserRequestDto deleteRequest = DeleteUserRequestDto.builder()
                .masterHash("Password123")
                .build();

        StatusResponseDto deleteResponse = StatusResponseDto.builder()
                .status("OK")
                .build();

        Mockito.when(userService.deleteUserByJwt(Mockito.eq(token), Mockito.any(DeleteUserRequestDto.class)))
                .thenReturn(ResponseEntity.ok(deleteResponse));

        String content = objectWriter.writeValueAsString(deleteRequest);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("OK")));
    }



}
