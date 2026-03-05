package com.cyberlearn.plataforma.controller;

public class ResetSenhaDTO {
    public String token;      // Definir como public resolve o erro de visibilidade
    public String novaSenha;  // Definir como public permite o acesso no UserController
}