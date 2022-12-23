package com.pedroandrade.dscatalog.services.validation;

import com.pedroandrade.dscatalog.dto.UserInsertDTO;
import com.pedroandrade.dscatalog.entities.User;
import com.pedroandrade.dscatalog.repositories.UserRepository;
import com.pedroandrade.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {//Generics: 1º: tipo da annotation;
                                                                                                //2º: tipo da classe que vai receber essa annotation.
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
        User user = repository.findByEmail(dto.getEmail());
        if(user != null) {
            list.add(new FieldMessage("email", "E-mail já está em uso"));
        }

        //Está inserindo na lista de erros do beans validation os erros encontrados nos testes acima
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
