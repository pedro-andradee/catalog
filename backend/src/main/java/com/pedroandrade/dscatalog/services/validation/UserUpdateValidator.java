package com.pedroandrade.dscatalog.services.validation;

import com.pedroandrade.dscatalog.dto.UserUpdateDTO;
import com.pedroandrade.dscatalog.entities.User;
import com.pedroandrade.dscatalog.repositories.UserRepository;
import com.pedroandrade.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {//Generics: 1º: tipo da annotation;
                                                                                                // 2º: tipo da classe que vai receber essa annotation.
    @Autowired
    private HttpServletRequest servletRequest;

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid ann) {
        //Aqui você pode colocar alguma lógica para rodar quando for inicializar o objeto
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
        //O HandlerMapping vai nos dar um Map(dicionario) com as variáveis presentes na URI
        var uriVars = (Map<String, String>) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
        User user = repository.findByEmail(dto.getEmail());
        if (user != null && userId != user.getId()) {
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
