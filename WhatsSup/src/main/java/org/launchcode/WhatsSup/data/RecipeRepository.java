package org.launchcode.WhatsSup.data;

import org.launchcode.WhatsSup.models.Recipe;
import org.launchcode.WhatsSup.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
      List<Recipe> findByRecipeAuthor(String recipeAuthor);
      List<Recipe> findByIngredients(String ingredients);
      List<Recipe> findByUser(User user);
}