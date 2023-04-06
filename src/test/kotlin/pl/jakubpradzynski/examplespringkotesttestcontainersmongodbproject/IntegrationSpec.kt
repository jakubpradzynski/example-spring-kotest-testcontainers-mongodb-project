package pl.jakubpradzynski.examplespringkotesttestcontainersmongodbproject

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.test.TestCase
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
@Testcontainers
abstract class IntegrationSpec(body: ShouldSpec.() -> Unit = {}) : ShouldSpec(body) {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mongoOperations: MongoOperations

    override suspend fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        mongoOperations.collectionNames.forEach { mongoOperations.dropCollection(it) }
    }

    companion object {
        @Container
        @JvmField
        var container = MongoDBContainer(DockerImageName.parse("mongo:6"))

        init {
            container.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun mongoDbProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") { container.replicaSetUrl }
        }
    }
}