package org.launchcode.WhatsSup.controllers;

import org.launchcode.WhatsSup.data.AddIngredientRepository;
import org.launchcode.WhatsSup.data.AddRecipeRepository;
import org.launchcode.WhatsSup.data.TagRepository;
import org.launchcode.WhatsSup.data.UserRepository;
import org.launchcode.WhatsSup.models.*;
import org.launchcode.WhatsSup.models.dto.FeaturedIngredientTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.launchcode.WhatsSup.models.Recipe;
import org.launchcode.WhatsSup.models.User;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("recipe")
public class AddRecipeController { //refactor to just be RecipeController

    @Autowired
    private AddRecipeRepository addRecipeRepository; //refactor to just RecipeRepository

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AuthenticationController authenticationController;

    @Autowired
    public AddIngredientRepository addIngredientRepository;

    @GetMapping("/add")
    public String displayAddRecipeForm(Model model, HttpSession session){
//        User currentUser = authenticationController.getUserFromSession(session);
//        String currentUsername=currentUser.getUsername();
//        model.addAttribute("currentUsername", currentUsername);
        model.addAttribute("title", "Add Recipe");
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute(new Recipe());

        return"/recipe/add";
    }

    @PostMapping("/add")
    public String processAddRecipeForm(@ModelAttribute @Valid
                                       @RequestParam(required = false) Integer userId,
                                       Recipe newRecipe,
                                       Errors errors){
        if(errors.hasErrors()){
            return "/recipe/add";
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            newRecipe.setUser(user);
        }

        addRecipeRepository.save(newRecipe);
        return "redirect:/recipe/add"; //refactor to return "view-my-recipes" when complete
    }

    @GetMapping("/view/{recipeId}")
    public String displayRecipe(Model model, @PathVariable int recipeId){

        Optional optRecipe = addRecipeRepository.findById(recipeId);
        if (optRecipe.isPresent()){
            Recipe recipe = (Recipe) optRecipe.get();
            model.addAttribute("title", "View Recipe");
            model.addAttribute("recipe", recipe);

        }

        return"/recipe/view"; //optional, follow model from jobs with jobID as req param.
    }

    @PostMapping("view/{recipeId}")
    public String processAddIngredientForm(@ModelAttribute Ingredient newIngredient, Model model){
        addIngredientRepository.save(newIngredient);
        return "redirect:/recipe/view/{recipeId}";
    }


//    @GetMapping("add-tag")
//    public String displayAddTagForm(@RequestParam Integer featuredRecipeId, Model model){
//        //TODO is Recipe or AddRecipe what should be optional<>?
//        //TODO RequestParam may need altered
//        Optional<Recipe> result = addRecipeRepository.findById(featuredRecipeId);
//        Recipe featuredIngredient = result.get();
//        model.addAttribute("title", "Add Tag to: " + featuredIngredient.getFeaturedIngredient());
//        model.addAttribute("tags", tagRepository.findAll());
//        FeaturedIngredientTagDTO featuredIngredientTag = new FeaturedIngredientTagDTO();
//        featuredIngredientTag.setFeaturedIngredient(featuredIngredient);
//        model.addAttribute("featuredRecipeTag", featuredIngredientTag);
//        return "recipe/add-tag.html";
//    }
//
//    @PostMapping("add-tag")
//    public String processAddTagForm(@ModelAttribute @Valid FeaturedIngredientTagDTO featuredIngredientTag,
//                                    Errors errors,
//                                    Model model){
//
//        if (!errors.hasErrors()) {
//            Recipe featuredIngredient = featuredIngredientTag.getFeaturedIngredient();
//            Tag tag = featuredIngredientTag.getTag();
//            if (!featuredIngredient.getTags().contains(tag)){
//                featuredIngredient.addTag(tag);
//                addRecipeRepository.save(featuredIngredient);
//            }
//            return "redirect:detail?featuredIngredientId=" + featuredIngredient.getId();
//        }
//
//        return "redirect:add-tag";
//    }

}
