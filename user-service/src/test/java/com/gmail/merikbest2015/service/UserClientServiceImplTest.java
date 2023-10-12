package com.gmail.merikbest2015.service;


import com.gmail.merikbest2015.UserServiceTestHelper;
import com.gmail.merikbest2015.dto.HeaderResponse;
import com.gmail.merikbest2015.dto.response.user.UserChatResponse;
import com.gmail.merikbest2015.mapper.BasicMapper;
import com.gmail.merikbest2015.repository.BlockUserRepository;
import com.gmail.merikbest2015.repository.FollowerUserRepository;
import com.gmail.merikbest2015.repository.UserRepository;
import com.gmail.merikbest2015.repository.projection.MutedUserProjection;
import com.gmail.merikbest2015.repository.projection.UserChatProjection;
import com.gmail.merikbest2015.service.impl.UserClientServiceImpl;
import com.gmail.merikbest2015.service.util.UserServiceHelper;
import com.gmail.merikbest2015.util.AbstractAuthTest;
import com.gmail.merikbest2015.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserClientServiceImplTest extends AbstractAuthTest {

    @Autowired
    private UserClientServiceImpl userClientService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FollowerUserRepository followerUserRepository;

    @MockBean
    private BlockUserRepository blockUserRepository;

    @MockBean
    private BasicMapper basicMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserServiceHelper userServiceHelper;

    @Before
    public void setUp() {
        super.setUp();
        when(authenticationService.getAuthenticatedUserId()).thenReturn(TestConstants.USER_ID);
    }

    @Test
    public void getUserFollowersIds_ShouldReturnUserIds() {
        when(followerUserRepository.getUserFollowersIds(TestConstants.USER_ID)).thenReturn(ids);
        assertEquals(4, userClientService.getUserFollowersIds().size());
        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(followerUserRepository, times(1)).getUserFollowersIds(TestConstants.USER_ID);
    }

    @Test
    public void searchUsersByUsername_ShouldReturnUserChatResponse() {
        UserChatProjection userChatProjection = UserServiceTestHelper.createUserChatProjection();
        PageImpl<UserChatProjection> userChatProjections = new PageImpl<>(List.of(userChatProjection), pageable, 20);
        HeaderResponse<UserChatResponse> headerResponse = new HeaderResponse<>(
                List.of(new UserChatResponse(), new UserChatResponse()), new HttpHeaders());
        when(userRepository.searchUsersByUsername("test", pageable, UserChatProjection.class)).thenReturn(userChatProjections);
        when(basicMapper.getHeaderResponse(userChatProjections, UserChatResponse.class)).thenReturn(headerResponse);
        assertEquals(headerResponse, userClientService.searchUsersByUsername("test", pageable));
        verify(userRepository, times(1)).searchUsersByUsername("test", pageable, UserChatProjection.class);
        verify(basicMapper, times(1)).getHeaderResponse(userChatProjections, UserChatResponse.class);
    }

    @Test
    public void getSubscribersByUserId() {
        when(userRepository.getSubscribersByUserId(TestConstants.USER_ID)).thenReturn(ids);
        assertEquals(ids, userClientService.getSubscribersByUserId(TestConstants.USER_ID));
        verify(userRepository, times(1)).getSubscribersByUserId(TestConstants.USER_ID);
    }

    @Test
    public void isUserFollowByOtherUser() {
        when(userServiceHelper.isUserFollowByOtherUser(TestConstants.USER_ID)).thenReturn(true);
        assertTrue(userClientService.isUserFollowByOtherUser(TestConstants.USER_ID));
        verify(userServiceHelper, times(1)).isUserFollowByOtherUser(TestConstants.USER_ID);
    }

    @Test
    public void isUserHavePrivateProfile() {
        when(userServiceHelper.isUserHavePrivateProfile(TestConstants.USER_ID)).thenReturn(true);
        assertTrue(userClientService.isUserHavePrivateProfile(TestConstants.USER_ID));
        verify(userServiceHelper, times(1)).isUserHavePrivateProfile(TestConstants.USER_ID);
    }

    @Test
    public void isUserBlocked() {
        when(blockUserRepository.isUserBlocked(TestConstants.USER_ID, 1L)).thenReturn(true);
        assertTrue(userClientService.isUserBlocked(TestConstants.USER_ID, 1L));
        verify(blockUserRepository, times(1)).isUserBlocked(TestConstants.USER_ID, 1L);
    }

    @Test
    public void isUserBlockedByMyProfile() {
        when(userServiceHelper.isUserBlockedByMyProfile(TestConstants.USER_ID)).thenReturn(true);
        assertTrue(userClientService.isUserBlockedByMyProfile(TestConstants.USER_ID));
        verify(userServiceHelper, times(1)).isUserBlockedByMyProfile(TestConstants.USER_ID);
    }

    @Test
    public void isMyProfileBlockedByUser() {
        when(userServiceHelper.isMyProfileBlockedByUser(TestConstants.USER_ID)).thenReturn(true);
        assertTrue(userClientService.isMyProfileBlockedByUser(TestConstants.USER_ID));
        verify(userServiceHelper, times(1)).isMyProfileBlockedByUser(TestConstants.USER_ID);
    }

    @Test
    public void increaseNotificationsCount() {
        userClientService.increaseNotificationsCount(TestConstants.USER_ID);
        verify(userRepository, times(1)).increaseNotificationsCount(TestConstants.USER_ID);
    }

    @Test
    public void increaseMentionsCount() {
        userClientService.increaseMentionsCount(TestConstants.USER_ID);
        verify(userRepository, times(1)).increaseMentionsCount(TestConstants.USER_ID);
    }

    @Test
    public void updateLikeCount() {
        userClientService.updateLikeCount(true);
        verify(userRepository, times(1)).updateLikeCount(true, TestConstants.USER_ID);
    }

    @Test
    public void updateTweetCount() {
        userClientService.updateTweetCount(true);
        verify(userRepository, times(1)).updateTweetCount(true, TestConstants.USER_ID);
    }

    @Test
    public void updateMediaTweetCount() {
        userClientService.updateMediaTweetCount(true);
        verify(userRepository, times(1)).updateMediaTweetCount(true, TestConstants.USER_ID);
    }
}
