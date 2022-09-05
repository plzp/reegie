package com.pl.reegi.pojo.dto;

import com.pl.reegi.pojo.Setmeal;
import com.pl.reegi.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
