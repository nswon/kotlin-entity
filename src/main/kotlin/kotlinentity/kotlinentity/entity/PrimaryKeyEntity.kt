package kotlinentity.kotlinentity.entity

import com.github.f4b6a3.ulid.UlidCreator
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import java.io.Serializable
import java.util.Objects
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.PostLoad
import javax.persistence.PostPersist

/*
@MappedSuperclass 란?
여러(거의 모든) 클래스들이 공통적으로 가지고 있는 필드를 하나의 클래스에 담아서 상속할 때 사용하는 어노테이션
코드 재사용이 좋아진다.
상속이라고 하긴 하지만 진짜 상속은 아님. 그냥 필드만 재사용을 위해 모아놓았다고 생각하면 된다.
DB는 상속이라는 개념이 없다. 객체에서는 상속을 했지만 DB 컬럼을 보면 각각 필드가 다 있다.

궁금한 점
프로젝트를 하면서 거의 처음으로 추상클래스를 사용한 것 같다.
1. 그냥 클래스로 하는 것보다 좋은 이유는 뭘까

 */
@MappedSuperclass
abstract class PrimaryKeyEntity : Persistable<UUID> {
    @Id
    @Column(columnDefinition = "UUID")
    private val id: UUID = UlidCreator.getMonotonicUlid().toUuid()

    //TODO : Transient 찾아보기
    @Transient
    private var _isNew = true

    override fun getId(): UUID = id

    override fun isNew(): Boolean = _isNew

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is HibernateProxy && this::class != other::class) {
            return false
        }

        return id == getIdentifier(other)
    }

    private fun getIdentifier(obj: Any): Serializable {
        return if (obj is HibernateProxy) {
            obj.hibernateLazyInitializer.identifier
        } else {
            (obj as PrimaryKeyEntity).id
        }
    }

    override fun hashCode() = Objects.hashCode(id)

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }
}