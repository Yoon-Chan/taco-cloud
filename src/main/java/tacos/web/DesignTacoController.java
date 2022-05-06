package tacos.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/*
*  /design 경로에 접속한다면 DesignTacoContoller의 showDesignForm() 메서드가 실행
*
* */

//SLF4J Logger를 생성.
@Slf4j
//DesignTacoControoler를 컨트롤러로 식별하게 된다.
@Controller
@RequestMapping("design")
public class DesignTacoController {

    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values();
        for(Type type: types){
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        model.addAttribute("taco", new Taco());

        //모델 데이터를 브라우저에 나타내는 데 사용될 뷰의 논리적인 이름.
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors){
        // 이 지점에서 타코 디자인(선택된 식자재 내역)을 저장한다.
        // 이 작업은 3장에서 할 것.
        if(errors.hasErrors()){
            return "design";
        }

        log.info("Processing design : "+ design);
        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type){
        return ingredients.stream()
                .filter(x-> x.getType().equals(type))
                .collect(Collectors.toList());

    }


}
