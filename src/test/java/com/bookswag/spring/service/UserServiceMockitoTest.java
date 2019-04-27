package com.bookswag.spring.service;

import com.bookswag.spring.dao.UserDao;
import com.bookswag.spring.domain.Level;
import com.bookswag.spring.domain.User;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import static com.bookswag.spring.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.bookswag.spring.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceMockitoTest {
    private static final String TEST_EMAIL = "test_spring_of_toby@gmail.com";

    @Mock
    private UserDao userDao;
    @Mock
    private MailSender mailSender;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void mockUpgradeLevels() throws Exception {
        List<User> users = Lists.newArrayList(
                new User("test_1", "테스터1", "1234", TEST_EMAIL, Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("test_2", "테스터2", "1234", TEST_EMAIL, Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("test_3", "테스터3", "1234", TEST_EMAIL, Level.SILVER, MIN_LOGCOUNT_FOR_SILVER+10, MIN_RECOMMEND_FOR_GOLD-1),
                new User("test_4", "테스터4", "1234", TEST_EMAIL, Level.SILVER, MIN_LOGCOUNT_FOR_SILVER+10, MIN_RECOMMEND_FOR_GOLD),
                new User("test_5", "테스터5", "1234", TEST_EMAIL, Level.GOLD, MIN_LOGCOUNT_FOR_SILVER+50, MIN_RECOMMEND_FOR_GOLD+50)
        );


        when(userDao.getAll()).thenReturn(users);

        userServiceImpl.upgradeLevels();

        verify(userDao, times(2)).update(any(User.class));
        verify(userDao).update(users.get(1));
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        verify(userDao).update(users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArgument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(2)).send(mailMessageArgument.capture());
        List<SimpleMailMessage> messages = mailMessageArgument.getAllValues();
        assertThat(messages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(messages.get(1).getTo()[0], is(users.get(3).getEmail()));
    }
}
