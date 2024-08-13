package com.example.posapplicationapis.services.session;

import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.dto.menu.MenuDtoResponse;
import com.example.posapplicationapis.dto.session.SessionDtoRequest;
import com.example.posapplicationapis.dto.session.SessionDtoResponse;
import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.entities.Menu;
import com.example.posapplicationapis.entities.Session;
import com.example.posapplicationapis.repositories.CategoryRepository;
import com.example.posapplicationapis.repositories.MenuRepository;
import com.example.posapplicationapis.repositories.SessionRepository;
import com.example.posapplicationapis.repositories.UserRepository;
import com.example.posapplicationapis.services.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public SessionDtoResponse createSession(SessionDtoRequest sessionDtoRequest) {
        Session session = new Session();
        return getSessionDtoResponse(sessionDtoRequest, session);
    }

    @Override
    public SessionDtoResponse getSessionById(Long id) {
        Session session = sessionRepository.findById(id).orElseThrow();
        return mapToDto(session);
    }

    @Override
    public List<SessionDtoResponse> getAllSessions() {
        return sessionRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public SessionDtoResponse updateSession(Long id, SessionDtoRequest sessionDtoRequest) {
        Session session = sessionRepository.findById(id).orElseThrow();
        return getSessionDtoResponse(sessionDtoRequest, session);
    }

    private SessionDtoResponse getSessionDtoResponse(SessionDtoRequest sessionDtoRequest, Session session) {
        session.setUser(userRepository.findById(sessionDtoRequest.getUserId()).orElseThrow());
        session.setStartTime(sessionDtoRequest.getStartTime());
        session.setEndTime(sessionDtoRequest.getEndTime());
        session.setPassword(bCryptPasswordEncoder.encode(sessionDtoRequest.getPassword()));
        session.setSessionStatus(sessionDtoRequest.getSessionStatus());
        session.setMenu(menuRepository.findById(sessionDtoRequest.getMenuId()).orElseThrow());
        sessionRepository.save(session);
        return mapToDto(session);
    }

    @Override
    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        Session session = sessionRepository.findById(id).orElseThrow(()-> new RuntimeException("Session with id " + id + " not found"));
        session.setPassword(bCryptPasswordEncoder.encode(newPassword));
        sessionRepository.save(session);
    }

    private SessionDtoResponse mapToDto(Session session) {
        SessionDtoResponse sessionDto = new SessionDtoResponse();
        sessionDto.setId(session.getId());
        sessionDto.setUserId(session.getUser().getId());
        sessionDto.setStartTime(session.getStartTime());
        sessionDto.setEndTime(session.getEndTime());
        sessionDto.setPassword(session.getPassword());
        sessionDto.setSessionStatus(session.getSessionStatus());
        sessionDto.setMenuDto(mapToMenuDto(session.getMenu()));
        return sessionDto;
    }

    private MenuDtoResponse mapToMenuDto(Menu menu) {
        MenuDtoResponse menuDto = new MenuDtoResponse();
        menuDto.setId(menu.getId());
        menuDto.setName(menu.getName());
        menuDto.setRestrictedCategories(menu.getRestrictedCategories().stream()
                .map(this::mapToCategoryDto)
                .collect(Collectors.toList()));
        return menuDto;
    }

    private CategoryDtoResponse mapToCategoryDto(Category category) {
        CategoryDtoResponse categoryDto = new CategoryDtoResponse();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        // Map other fields if necessary
        return categoryDto;
    }
}
