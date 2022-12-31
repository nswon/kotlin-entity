package kotlinentity.kotlinentity.entity

import org.jetbrains.annotations.NotNull
import javax.persistence.*

@Entity
@Table(name = "USER")
class User(
    name: String
) : PrimaryKeyEntity() {
    @field:NotNull
    @Column(name = "name")
    var name = name
        private set
}