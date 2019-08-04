package com.leyou.common.exception;

public enum ExceptionEnum {
    // 商品管理模块异常
    BRAND_NOT_FOUND(404,"品牌不存在"),
    CATEGORY_NOT_FOUND(404,"商品分类不存在"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格不存在"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    INVALID_FILE_EEROR(500,"无效的文件类型"),
    SPEC_PARAM_NOT_FOUND(400,"商品规格参数不存在"),


    ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private int code;
    private String msg;
    ExceptionEnum(int errCode,String desc){
        this.code=errCode;
        this.msg=desc;
    }
}
