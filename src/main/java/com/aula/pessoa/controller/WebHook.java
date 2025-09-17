package com.aula.pessoa.controller;

import com.aula.pessoa.service.PessoaService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm")
@AllArgsConstructor
public class WebHook {

    private final PessoaService service;

    @GetMapping
    public ResponseEntity<String> confirmar(@Param("id") Long id){
        service.confirm(id);
        return ResponseEntity.ok("Usuario Verificado");
    }

}
