package tacos.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
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
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

    private TacoRepository tacoRepo;

    private UserRepository userRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository
    , TacoRepository tacoRepo, UserRepository userRepository){
        this.ingredientRepository = ingredientRepository;
        this.tacoRepo = tacoRepo;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showDesignForm(Model model, Principal principal){

        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(i-> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for(Type type: types){
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        //  model.addAttribute("taco", new Taco());
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        //모델 데이터를 브라우저에 나타내는 데 사용될 뷰의 논리적인 이름.
        return "design";
    }

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors
    , @ModelAttribute Order order){
        // 이 지점에서 타코 디자인(선택된 식자재 내역)을 저장한다.
        // 이 작업은 3장에서 할 것.
        if(errors.hasErrors()){
            return "design";
        }

        Taco saved = tacoRepo.save(design);
        order.addDesign(saved);
        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type){
        return ingredients.stream()
                .filter(x-> x.getType().equals(type))
                .collect(Collectors.toList());

    }


}
