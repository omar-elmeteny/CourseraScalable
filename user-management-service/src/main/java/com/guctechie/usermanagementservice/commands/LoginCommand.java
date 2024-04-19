package com.guctechie.usermanagementservice.commands;

import com.guctechie.messagequeue.services.Command;
import com.guctechie.usermanagementmodels.AuthRequestDTO;
import com.guctechie.usermanagementmodels.CommandNames;
import com.guctechie.usermanagementmodels.JwtResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class LoginCommand implements Command<AuthRequestDTO, JwtResponseDTO> {


    @Override()
    public String getCommandName() {
        return CommandNames.LOGIN_COMMAND;
    }

    @Override
    public JwtResponseDTO execute(AuthRequestDTO authRequestDTO) throws Exception {
        return new JwtResponseDTO("token for" + authRequestDTO.getUsername());
    }

    @Override
    public Class<AuthRequestDTO> getRequestType() {
        return AuthRequestDTO.class;
    }

    @Override
    public Class<JwtResponseDTO> getResponseType() {
        return JwtResponseDTO.class;
    }
}
