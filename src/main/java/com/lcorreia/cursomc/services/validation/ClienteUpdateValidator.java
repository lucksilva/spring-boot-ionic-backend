package com.lcorreia.cursomc.services.validation;

import com.lcorreia.cursomc.domain.Cliente;
import com.lcorreia.cursomc.domain.enums.TipoCliente;
import com.lcorreia.cursomc.dto.ClienteDTO;
import com.lcorreia.cursomc.dto.ClienteNewDTO;
import com.lcorreia.cursomc.repositories.ClienteRepository;
import com.lcorreia.cursomc.resources.exception.FieldMassage;
import com.lcorreia.cursomc.services.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ClienteRepository repo;

    @Override
    public void initialize(ClienteUpdate ann) {
    }
    @Override
    public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {

        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer urlId = Integer.parseInt(map.get("id"));

        List<FieldMassage> list = new ArrayList<>();

        Cliente aux = repo.findByEmail(objDto.getEmail());
        if (aux != null && !aux.getId().equals(urlId)){
            list.add(new FieldMassage("email", "Email já existente"));
        }

        for (FieldMassage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMassage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
