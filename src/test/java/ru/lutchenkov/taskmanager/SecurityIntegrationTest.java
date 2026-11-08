package ru.lutchenkov.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.lutchenkov.taskmanager.model.Role;
import ru.lutchenkov.taskmanager.model.Task;
import ru.lutchenkov.taskmanager.model.User;
import ru.lutchenkov.taskmanager.repository.TaskRepository;
import ru.lutchenkov.taskmanager.repository.UserRepository;
import ru.lutchenkov.taskmanager.security.JwtService;
import ru.lutchenkov.taskmanager.service.TaskService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecurityIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerShouldReturn201AndDuplicateEmailShouldReturn409() throws Exception {
        String registerJson = """
                {
                  "email": "test@mail.ru",
                  "password": "password123",
                  "confirmPassword": "password123"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isConflict());
    }

    @Test
    void loginWithWrongPasswordShouldReturn401() throws Exception {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setPassword(passwordEncoder.encode("correctPassword"));
        user.setRole(Role.USER);
        userRepository.save(user);

        String badLoginJson = """
                {
                  "email": "user@mail.ru",
                  "password": "wrongPassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badLoginJson))
                .andExpect(status().isUnauthorized());

        String goodLoginJson = """
                {
                  "email": "user@mail.ru",
                  "password": "correctPassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodLoginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void getTasksWithoutTokenShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteTaskShouldReturn403ForUserAnd204ForAdmin() throws Exception {
        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Description");
        Task savedTask = taskService.createTask(task);

        User regularUser = new User();
        regularUser.setEmail("user@mail.ru");
        regularUser.setPassword(passwordEncoder.encode("password123"));
        regularUser.setRole(Role.USER);

        User adminUser = new User();
        adminUser.setEmail("admin@mail.ru");
        adminUser.setPassword(passwordEncoder.encode("password123"));
        adminUser.setRole(Role.ADMIN);

        String userToken = jwtService.generateToken(regularUser);
        String adminToken = jwtService.generateToken(adminUser);

        mockMvc.perform(delete("/api/tasks/" + savedTask.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/tasks/" + savedTask.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void requestWithTamperedTokenShouldReturn401() throws Exception {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setPassword("hashed");
        user.setRole(Role.USER);

        String validToken = jwtService.generateToken(user);
        String tamperedToken = validToken.substring(0, validToken.length() - 2) + "ab";

        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + tamperedToken))
                .andExpect(status().isUnauthorized());
    }
}