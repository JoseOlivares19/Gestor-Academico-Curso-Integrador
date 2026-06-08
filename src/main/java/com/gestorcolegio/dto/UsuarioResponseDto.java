package com.gestorcolegio.dto;

import lombok.Data;

@Data
public class UsuarioResponseDto {
    private Long id;
    private String username;
    private String email;
    private String rol;
}