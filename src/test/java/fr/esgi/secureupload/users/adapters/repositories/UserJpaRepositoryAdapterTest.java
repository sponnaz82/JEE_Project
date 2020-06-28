package fr.esgi.secureupload.users.adapters.repositories;

import fr.esgi.secureupload.users.entities.User;

import org.junit.Before;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserJpaRepositoryAdapterTest {

    @InjectMocks
    private UserJpaRepositoryAdapter userJpaRepositoryAdapter;

    @Mock
    private UserJpaRepository jpaRepository;

    @Mock
    private User userEntity;

    @Mock
    private UserJpaEntity userJpaEntity;

    @Mock
    private Page<UserJpaEntity> userJpaEntitiesPage;

    @Mock
    private Page<Object> userEntitiesPage;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findById_ShouldReturnUserById() {
        when(jpaRepository.findById(anyString())).thenReturn(Optional.of(userJpaEntity));
        User user = this.userJpaRepositoryAdapter.findById("id").orElse(null);
        verify(this.jpaRepository).findById(eq("id"));
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user instanceof User);
    }

    @Test
    public void findByEmail_ShouldReturnUserByEmail (){
        when(jpaRepository.findByEmail(anyString())).thenReturn(Optional.of(userJpaEntity));
        User user = this.userJpaRepositoryAdapter.findByEmail("email").orElse(null);
        verify(this.jpaRepository).findByEmail(eq("email"));
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user instanceof User);
    }

    @Test
    public void findAllByPattern_ShouldReturnUsersPage (){
        when(userJpaEntitiesPage.map(any())).thenReturn(userEntitiesPage);
        when(jpaRepository.findAllByPattern(anyString(), any(PageRequest.class))).thenReturn(userJpaEntitiesPage);
        Page<User> users = userJpaRepositoryAdapter.findAllByPattern("pattern", PageRequest.of(0, 10, Sort.by("email")));
        verify(this.jpaRepository).findAllByPattern(eq("pattern"), eq(PageRequest.of(0, 10, Sort.by("email"))));
        Assertions.assertNotNull(users);
    }

    @Test
    public void findAll_ShouldReturnUsersPage (){
        when(userJpaEntitiesPage.map(any())).thenReturn(userEntitiesPage);
        when(jpaRepository.findAll(any(PageRequest.class))).thenReturn(userJpaEntitiesPage);
        Page<User> users = userJpaRepositoryAdapter.findAll(PageRequest.of(0, 10, Sort.by("email")));
        verify(this.jpaRepository).findAll(eq(PageRequest.of(0, 10, Sort.by("email"))));
        Assertions.assertNotNull(users);
    }

    @Test
    public void save_ShouldReturnSavedUser (){
        when(jpaRepository.save(any(UserJpaEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        User user = userJpaRepositoryAdapter.save(this.userEntity);
        verify(this.jpaRepository).save(any(UserJpaEntity.class));
        Assertions.assertNotNull(user);
    }

    @Test
    public void delete_ShouldCallDelete (){
        userJpaRepositoryAdapter.deleteById("ID");
        verify(this.jpaRepository).deleteById(eq("ID"));
    }
}
