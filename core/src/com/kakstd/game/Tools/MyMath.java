package com.kakstd.game.Tools;

import com.badlogic.gdx.math.MathUtils;

public  class MyMath {
    public static float rounder05(float value){
        if (value >0) {
            if (value % MathUtils.round(value) < 0.5f){
                return value = MathUtils.round(value);
            }else{
                return value = MathUtils.round(value) + 0.5f;
            }
        }else {
            if (value % MathUtils.round(value) > -0.5f){
                return value = MathUtils.round(value);
            } else {
                return value = MathUtils.round(value) - 0.5f;
            }
        }
    }


}
