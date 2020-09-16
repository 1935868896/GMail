package com.zc.gmail.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
@Data
public class RegisterVo {
    @NotEmpty(message = "用户名必须提交")
    @Length(min =6,max=18,message = "6-18")
    private String userName;
    @NotEmpty(message = "must ")
    @Length(min = 6,max=18)
    private String password;
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$",message = "手机号格式不正确")
    private String phone;
}
