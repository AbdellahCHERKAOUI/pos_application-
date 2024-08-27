package com.example.posapplicationapis.services.session;

import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.dto.menu.MenuDtoRequest;
import com.example.posapplicationapis.dto.menu.MenuDtoResponse;
import com.example.posapplicationapis.dto.session.SessionDtoRequest;
import com.example.posapplicationapis.dto.session.SessionDtoResponse;
import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.entities.Menu;
import com.example.posapplicationapis.entities.Session;
import com.example.posapplicationapis.entities.User;
import com.example.posapplicationapis.repositories.CategoryRepository;
import com.example.posapplicationapis.repositories.MenuRepository;
import com.example.posapplicationapis.repositories.SessionRepository;
import com.example.posapplicationapis.repositories.UserRepository;
import com.example.posapplicationapis.services.menu.MenuServiceImpl;
import com.example.posapplicationapis.services.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MenuServiceImpl menuService;

    @Override
    public SessionDtoResponse createSession(SessionDtoRequest sessionDtoRequest) {
        Session session = new Session();
        session.setUser(userRepository.findById(sessionDtoRequest.getUserId()).orElseThrow());
        session.setStartTime(sessionDtoRequest.getStartTime());
        session.setEndTime(sessionDtoRequest.getEndTime());
        session.setSessionStatus(sessionDtoRequest.getSessionStatus());

        List<Category> categories = new ArrayList<>();
        for (Long categoryId : sessionDtoRequest.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
            categories.add(category);
        }

        Menu menu = Menu.builder()
                .name("--Menu--")
                .categories(categories)
                .build();
        menuRepository.save(menu);


        session.setMenu(menu);
        sessionRepository.save(session);
        return mapToDto(session);
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

        session.setUser(userRepository.findById(sessionDtoRequest.getUserId()).orElseThrow());
        session.setStartTime(sessionDtoRequest.getStartTime());
        session.setEndTime(sessionDtoRequest.getEndTime());
        session.setSessionStatus(sessionDtoRequest.getSessionStatus());

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
        sessionRepository.save(session);
    }

    @Override
    public SessionDtoResponse getSessionByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            return mapToDto(null);
        }

        Optional<Session> session = sessionRepository.findByUser(user.get());

        if (session.isEmpty()){
            return mapToDto(null);
        }

        if (session.isPresent()) {
            return mapToDto(session.get());
        }


        return mapToDto(null);
    }

    private SessionDtoResponse mapToDto(Session session) {
        SessionDtoResponse sessionDto = new SessionDtoResponse();
        if (session != null) {
            sessionDto.setId(session.getId());
            sessionDto.setUserId(session.getUser().getId());
            sessionDto.setStartTime(session.getStartTime());
            sessionDto.setEndTime(session.getEndTime());
            sessionDto.setSessionStatus(session.getSessionStatus());
            sessionDto.setMenuDto(mapToMenuDto(session.getMenu()));
            return sessionDto;
        }
        return null;
    }

    private MenuDtoResponse mapToMenuDto(Menu menu) {
        MenuDtoResponse menuDto = new MenuDtoResponse();
        menuDto.setId(menu.getId());
        menuDto.setName(menu.getName());

        List<Category> categories = menu.getCategories();
        List<CategoryDtoResponse> categoriesDto = new ArrayList<>();

        for (Category category: categories) {
            categoriesDto.add(mapToCategoryDto(category));
        }
        menuDto.setCategories(categoriesDto);
        return menuDto;
    }

    private CategoryDtoResponse mapToCategoryDto(Category category) {
        CategoryDtoResponse categoryDto = new CategoryDtoResponse();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setImageLink(category.getImage().getLink());
        return categoryDto;
    }
}
