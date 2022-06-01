package com.atguigu.en;

import com.atguigu.entity.Dict;

public enum DictCode {
    HOUSETYPE("houseType"),FLOOR("floor"),BUILDSTRUCTURE("buildStructure"),
    DECORATION("decoration"),DIRECTION("direction"),HOUSEUSE("houseUse");

    private String message;

    DictCode(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
