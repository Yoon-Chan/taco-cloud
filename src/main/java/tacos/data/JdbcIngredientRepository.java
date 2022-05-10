package tacos.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;


// 스프링 컴포턴트 검색에서 이 클래스를 자동으로 찾아서 스프링 애플리케이션 컨텍스트의 빈으로 생성.
@Repository
public class JdbcIngredientRepository implements IngredientRepository{
    private JdbcTemplate jdbc;

    // 빈이 생성되면 Autowired 애노테이션을 통해서 스프링 해당 빈을 JdbcTemplate에 주입(연결)한다.
    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbc){
        this.jdbc= jdbc;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        //query() 메서드는 두개의 인자를 받음, 1. SQL명령어, 2. 메서드
        // 쿼리에 생성된 결과세트 행 개수만큼 호출되며 행을 List에 저장 후 반환.
        return jdbc.query("select id, name, type from Ingredient", this::mapRowToIngredient);
    }

    @Override
    public Ingredient findById(String id) {
        //queryForObject() : query()와 비슷. 객체의 List를 반환하는 대신 하나의 객체만 반환.
        return jdbc.queryForObject("select id, name, type from Ingredient where id=?",
                this::mapRowToIngredient, id);
    }


    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum)
    throws SQLException {
        return new Ingredient(
                rs.getString("id"),
                rs.getString("name"),
                Ingredient.Type.valueOf(rs.getString("type"))
        );
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbc.update(
                "insert into Ingredient (id, name, type) values (?,?,?)"
                ,ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString()
        );
        return ingredient;
    }
}
