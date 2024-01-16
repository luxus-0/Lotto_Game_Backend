package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.lotto.LottoApplication;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.numbersgenerator.WinningNumbersRepository;
import pl.lotto.domain.resultannouncer.ResultAnnouncerRepository;
import pl.lotto.domain.resultchecker.ResultCheckerRepository;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(classes = {LottoApplication.class, IntegrationConfiguration.class})
@ActiveProfiles("integration")
@AutoConfigureMockMvc
@Testcontainers
public class BaseIntegrationTest {

    public static final String WIRE_MOCK_HOST = "http://localhost";
    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo")).withExposedPorts(27017);

    @Autowired
    public MockMvc mockMvc;
    @Autowired
    public ObjectMapper objectMapper;
    @Autowired
    public AdjustableClock clock;
    @Autowired
    public ResultAnnouncerRepository resultAnnouncerRepository;
    @Autowired
    public ResultCheckerRepository resultCheckerRepository;

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.port", () -> wireMockServer.getPort());
        registry.add("spring.data.mongodb.host", () -> WIRE_MOCK_HOST);
    }
}