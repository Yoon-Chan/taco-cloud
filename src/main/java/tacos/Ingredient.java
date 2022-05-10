package tacos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@RequiredArgsConstructor
//개체가 인자없는 생성자를 가져야 하기 위한 애노테이션.
//access= ... 클래스 외부에서 사용하지 못하게함.
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//JPA 개체로 선언하기 위한 애노테이션.
@Entity
public class Ingredient {

    //Entity한 경우 id 속성에는 반드시 @id를 지정.
    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
