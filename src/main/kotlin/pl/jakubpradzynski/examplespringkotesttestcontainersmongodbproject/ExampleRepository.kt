package pl.jakubpradzynski.examplespringkotesttestcontainersmongodbproject

import org.bson.types.ObjectId
import org.springframework.data.repository.Repository

data class Example(val id: ObjectId, val name: String)

internal interface ExampleRepository : Repository<Example, ObjectId> {
    fun save(example: Example): Example
}