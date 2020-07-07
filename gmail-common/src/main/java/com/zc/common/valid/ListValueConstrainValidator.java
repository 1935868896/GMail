package com.zc.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

public class ListValueConstrainValidator implements ConstraintValidator<ListValue,Integer> {
    HashSet<Integer> set=new HashSet<>();
    @Override
    public void initialize(ListValue constraintAnnotation) {

        int[] vals=constraintAnnotation.vals();
        for(int val : vals){
           set.add(val);
        }

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(value);
    }
}
