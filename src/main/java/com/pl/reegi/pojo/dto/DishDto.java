package com.pl.reegi.pojo.dto;

import com.pl.reegi.pojo.Dish;
import com.pl.reegi.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
