package tacos.data;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository{
    private JdbcTemplate jdbc;

    public JdbcTacoRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }


    @Override
    public Taco save(Taco design) {
        //각 식자재를 저장하는 saveTacoInfo() 메서드를 호출.
        long tacoId = saveTacoInfo(design);

        design.setId(tacoId);
        for (Ingredient ingredient : design.getIngredients()){
            // 식자재 연관 정보를 저장하는 saveIngredientToTaco()
            saveIngredientToTaco(ingredient, tacoId);
        }
        return design;
    }

    private long saveTacoInfo(Taco taco){
        taco.setCareatedAt(new Date());
        PreparedStatementCreator psc =
                new PreparedStatementCreatorFactory(
                        "insert into Taco (name, createdAt) values (?,?)",
                        Types.VARCHAR, Types.TIMESTAMP
                ).newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                new Timestamp(taco.getCareatedAt().getTime())
                        )
                );

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(
            Ingredient ingredient, long tacoId){
        jdbc.update(
                "insert into Taco_Ingredients (taco, ingredient)"+
                        "values (?,?)",
                tacoId, ingredient.getId()
        );
    }

}
