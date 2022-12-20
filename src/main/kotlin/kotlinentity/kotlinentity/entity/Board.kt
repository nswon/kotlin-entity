package kotlinentity.kotlinentity.entity

import javax.persistence.*

@Entity
@Table(name = "BOARD")
class Board(
    val title: String,
    val content: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private val writer:User? = null
}