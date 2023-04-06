package pl.jakubpradzynski.examplespringkotesttestcontainersmongodbproject

import io.kotest.matchers.shouldNotBe
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired

internal class ExampleTest(@Autowired private val exampleRepository: ExampleRepository) : IntegrationSpec({

    should("save example") {
        // GIVEN
        val example = Example(id = ObjectId.get(), name = "test")

        // WHEN
        val result = exampleRepository.save(example)

        // THEN
        result shouldNotBe null
    }

})