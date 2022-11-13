package com.gilclei.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.gilclei.cursomc.domain.Cliente;
import com.gilclei.cursomc.domain.enums.TipoCliente;
import com.gilclei.cursomc.dto.ClienteDTO;
import com.gilclei.cursomc.dto.ClienteNewDTO;
import com.gilclei.cursomc.repositories.ClienteRepository;
import com.gilclei.cursomc.resources.exceptions.FieldMessage;
import com.gilclei.cursomc.services.validation.utils.BR;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private HttpServletRequest request;

	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();

		Cliente cliente = repository.findByEmail(objDto.getEmail());
		if (cliente != null && !cliente.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "Email já existente"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
