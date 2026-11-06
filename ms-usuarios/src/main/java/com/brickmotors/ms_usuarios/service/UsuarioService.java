package com.brickmotors.ms_usuarios.service;

import com.brickmotors.ms_usuarios.dto.UsuarioRequest;
import com.brickmotors.ms_usuarios.dto.UsuarioResponse;
import com.brickmotors.ms_usuarios.model.Usuario;
import com.brickmotors.ms_usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional 
public class UsuarioService {
    
}
