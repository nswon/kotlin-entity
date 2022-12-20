package kotlinentity.kotlinentity.entity

import javax.persistence.*

@Entity
@Table(name = "USER")
class User(
//    private val name: String,
    name: String,
) : PrimaryKeyEntity() {
    @Column(nullable = false, unique = true)
    var name: String = name
        protected set

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "writer")
    protected val mutableBoards: MutableList<Board> = mutableListOf()
    val boards: List<Board> get() = mutableBoards.toList()
//    val boards = mutableListOf<Board>()

    fun writeBoard(board: Board) {
        mutableBoards.add(board)
    }
}