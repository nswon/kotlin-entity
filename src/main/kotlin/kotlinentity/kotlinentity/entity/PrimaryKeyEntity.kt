package kotlinentity.kotlinentity.entity

import com.github.f4b6a3.ulid.UlidCreator
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.domain.Persistable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.GeneratedValue
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
2. open vs abstract
 */
@MappedSuperclass
open class PrimaryKeyEntity : Persistable<UUID> {
    /*
    pk를 Long이 아닌 UUID로 하는 이유?
    1. 분산 DB 환경일 경우 PK값이 중복되어 insert될 수 도 있음
    2. pk값을 예측하기가 너무 쉽다.
    2번 이유를 보면 실무에서는 Long을 거의 안쓰는 것 같다.
    왜냐하면 pk가 몇번인지에 따라 회사의 규모가 드러나 보이기 때문이다.

    UUID도 단점이 있다.
    1. Long타입이 데이터 크기가 더 작다.
    이건 당연하다.
    2. Long타입은 정렬 시 성능적인 이점이 있다.
    이것도 ULID라는 오픈소스 라이브러리로 해결할 수 있다. ULID는 UUID와 다르게 시간순으로 정렬할 수 있다고 한다.
    이제 Long타입보다 안좋은 점이 다 없어졌다. (굳이 말하자면 귀찮다는 것? 확실히 Long이 순식간에 구현할 수 있다)
    결론 : Long타입보다 안좋은 점은 없고, 더 안전한 방식을 안 쓸 이유는 없다!!
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private val id: UUID = UlidCreator.getMonotonicUlid().toUuid()

    //원래 Entity의 Property는 DB의 컬럼과 매핑되지만, 아래 어노테이션을 붙여주면 매핑되지 않는다.
    @Transient
    private var isNew = true

    //아래 두 메서드는 꼭 오버라이딩 해야 한다. 왜냐하면 save와 delete 내부로직에 쓰이기 때문이다.
    override fun getId(): UUID = id

    override fun isNew(): Boolean = isNew

    /*
    스포카에서는 jpa 생명주기 이벤트에 대한 콜백하는 어노테이션을 달았다.
    하지만 나의 생각은 다르다.
    왜냐하면 jpa 생명주기 이벤트에 대해서 계속 콜백으로 하게 되면 영속화할 때도, 조회할 때도, 삭제할 때도 아래 메서드를 실행이 되기 때문이다.

    아래 메서드를 정의하는 이유는 delete 메서드 내부 로직에서 isNew가 참이면 remove를 실행하지 않고 바로 return을 때려버리기 때문이다.
    그 말은 isNew가 true라면(새로운 엔티티라면) 삭제가 안된다는 것이다.
    그래서 영속화를 시켜준 후 isNew를 false로 바꿔주기 위해 아래 메서드를 정의한다.

    하지만 생명주기 이벤트마다 호출되면 그거대로 부담이 가기 때문에, 차라리 영속화가 된 후 직접 딱 한번 호출하는 것이 더 좋다고 판단했다.

    ----------

    라고 생각했지만, 따로 flush나 commit으로 엔티티가 DB에 반영된 후 바로 호출되는 어노테이션이 존재했었다.
    @PostPersist

    @PostLoad 는 영속성 컨텍스트에 조회된 직후 또는 refresh를 호출한 후에 호출한다고 한다.
    refresh 호출은 뭔말인지 아직 모르겠는데, 삭제를 위해서 엔티티를 찾을 때, 영속성 컨텍스트에서 가져와도 불러야 하기 때문에 붙이는 것 같다.
     */
    @PostLoad
    @PostPersist
    protected fun load() {
        isNew = false
    }
}