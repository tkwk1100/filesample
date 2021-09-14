package com.filesample.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class FileVO {
    private String f_seq;
    private String f_name;
    private String f_uri;
    private Date f_reg_dt;
}
