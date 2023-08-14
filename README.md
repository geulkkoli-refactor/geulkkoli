# 바라바라

## 프로젝트 개요

회원 가입제 블로깅 플랫폼 서비스 입니다. 현재 스프링 부트를 도커 컨테이너에 올린 후 깃헙 액션과 aws 라이트 세일을 이용하려 변경중입니다.

## 참여

- 김륜환


기술 스택 및 개발 도구
//프로젝트에 사용되는 개발 환경 및 도구 목록

<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white">


VCS
Git
Git hub
IntelliJ Git

Java 17
Spring boot 2.7.1
Spring Security 5.7

DB Access

JPA
querydsl

DB
H2 Database
MySql

Template Engine
thymeleaf

Email
JavaMailSender

Oauth2 Server
spring-boot-starter-oauth2-client

Web Language
Javascript
Jquery
프로젝트 아키텍처

디렉토리 구조

 ```
geulkkoli
        ├── GeulkkoliApplication.java
        ├── application
        │   ├── EmailDto.java
        │   ├── EmailService.java
        │   ├── comment
        │   │   └── AjaxBadResponseHandler.java
        │   ├── follow
        │   │   ├── FollowInfo.java
        │   │   └── FollowInfos.java
        │   ├── post
        │   │   └── WebConfig.java
        │   ├── security
        │   │   ├── AccountStatus.java
        │   │   ├── LockExpiredTimeException.java
        │   │   ├── LoginProcessException.java
        │   │   ├── Role.java
        │   │   ├── RoleEntity.java
        │   │   ├── RoleException.java
        │   │   ├── RoleRepository.java
        │   │   ├── config
        │   │   │   ├── CustomAuthenticationFilter.java
        │   │   │   └── SecurityConfig.java
        │   │   ├── handler
        │   │   │   ├── LoginFailureHandler.java
        │   │   │   └── LoginSuccessHandler.java
        │   │   └── util
        │   │       └── RoleNameAttributeConverter.java
        │   ├── social
        │   │   ├── GoogleOAuth2User.java
        │   │   ├── KakaoOAuth2User.java
        │   │   ├── NaverOAuth2User.java
        │   │   ├── OAuth2ProviderUser.java
        │   │   ├── service
        │   │   │   ├── AbstractOauth2UserService.java
        │   │   │   └── CustomOauth2UserService.java
        │   │   └── util
        │   │       ├── DelegateOAuth2RequestConverter.java
        │   │       ├── GoogleRequestConverter.java
        │   │       ├── KaKaoRequestConverter.java
        │   │       ├── NaverRequestConverter.java
        │   │       ├── ProviderUserRequest.java
        │   │       ├── SocialType.java
        │   │       ├── SocialTypeException.java
        │   │       ├── UserRequest.java
        │   │       └── UserRequestConverter.java
        │   └── user
        │       ├── CustomAuthenticationPrinciple.java
        │       ├── ProviderUser.java
        │       ├── UserModelDto.java
        │       └── service
        │           ├── PasswordService.java
        │           └── UserSecurityService.java
        ├── domain
        │   ├── admin
        │   │   ├── AccountLock.java
        │   │   ├── AccountLockRepository.java
        │   │   ├── Report.java
        │   │   ├── ReportRepository.java
        │   │   └── service
        │   │       └── AdminServiceImpl.java
        │   ├── comment
        │   │   ├── Comments.java
        │   │   ├── CommentsRepository.java
        │   │   └── service
        │   │       ├── CommentNotFoundException.java
        │   │       └── CommentsService.java
        │   ├── favorites
        │   │   ├── Favorites.java
        │   │   ├── FavoritesRepository.java
        │   │   └── service
        │   │       └── FavoriteService.java
        │   ├── follow
        │   │   ├── Follow.java
        │   │   ├── FollowRepository.java
        │   │   ├── FollowRepositoryCustom.java
        │   │   ├── FollowRepositoryImpl.java
        │   │   └── service
        │   │       ├── CanNotFollowException.java
        │   │       ├── FollowFindService.java
        │   │       ├── FollowNotFoundException.java
        │   │       └── FollowService.java
        │   ├── hashtag
        │   │   ├── HashTag.java
        │   │   ├── HashTagRepository.java
        │   │   ├── HashTagRepositoryCustom.java
        │   │   ├── HashTagRepositoryImpl.java
        │   │   ├── HashTagType.java
        │   │   ├── service
        │   │   │   ├── HashTagFindService.java
        │   │   │   └── HashTagService.java
        │   │   └── util
        │   │       ├── HashTagSign.java
        │   │       └── HashTagTypeConverter.java
        │   ├── post
        │   │   ├── AdminTagAccessDenied.java
        │   │   ├── ConfigDate.java
        │   │   ├── NotAuthorException.java
        │   │   ├── Post.java
        │   │   ├── PostNotExistException.java
        │   │   ├── PostRepository.java
        │   │   ├── PostRepositoryCustom.java
        │   │   ├── PostRepositoryImpl.java
        │   │   ├── SearchType.java
        │   │   └── service
        │   │       ├── PostFindService.java
        │   │       └── PostService.java
        │   ├── posthashtag
        │   │   ├── PostHashTag.java
        │   │   ├── PostHashTagRepository.java
        │   │   ├── PostHashTagRepositoryCustom.java
        │   │   ├── PostHashTagRepositoryImpl.java
        │   │   └── service
        │   │       ├── PostHahTagFindService.java
        │   │       └── PostHashTagService.java
        │   ├── social
        │   │   ├── NoSuchSocialInfoException.java
        │   │   └── service
        │   │       ├── SocialInfo.java
        │   │       ├── SocialInfoFindService.java
        │   │       ├── SocialInfoRepository.java
        │   │       └── SocialInfoService.java
        │   ├── topic
        │   │   ├── Topic.java
        │   │   ├── TopicRepository.java
        │   │   ├── service
        │   │   │   └── TopicService.java
        │   │   └── service 2
        │   └── user
        │       ├── ConfigDate.java
        │       ├── NoSuchCommnetException.java
        │       ├── NoSuchReportException.java
        │       ├── User.java
        │       ├── UserNotExistException.java
        │       ├── UserRepository.java
        │       └── service
        │           ├── UserFindService.java
        │           └── UserService.java
        ├── infrastructure
        │   └── QuerydslConfiguration.java
        └── web
            ├── account
            │   ├── AccountManagementController.java
            │   └── dto
            │       ├── CalendarDto.java
            │       ├── ConnectSocialInfo.java
            │       ├── ConnectedSocialInfos.java
            │       ├── GoogleConnectedSocialInfo.java
            │       ├── KaKaoConnectedSocialInfo.java
            │       ├── NaverConnectedSocialInfo.java
            │       └── edit
            │           ├── PasswordEditFormDto.java
            │           └── UserInfoEditFormDto.java
            ├── admin
            │   ├── AdminController.java
            │   ├── DailyTopicDTO.java
            │   ├── ReportDTO.java
            │   └── UserLockDTO.java
            ├── blog
            │   ├── BlogController.java
            │   └── dto
            │       ├── ArticleDTO.java
            │       ├── ArticleEditRequestDTO.java
            │       ├── ArticlePagingRequestDTO.java
            │       ├── PagingDTO.java
            │       ├── UserProfileDTO.java
            │       └── WriteRequestDTO.java
            ├── channels
            │   └── ChannelsController.java
            ├── comment
            │   ├── CommentController.java
            │   └── dto
            │       ├── CommentBodyDTO.java
            │       ├── CommentEditDTO.java
            │       └── CommentListDTO.java
            ├── favorite
            │   ├── FavoriteController.java
            │   └── FavoriteRequestDTO.java
            ├── feed
            │   └── FeedController.java
            ├── follow
            │   ├── FollowApiController.java
            │   └── dto
            │       ├── FollowResultDTO.java
            │       └── FollowsCountDTO.java
            ├── home
            │   ├── HomeController.java
            │   ├── ResponseMessage.java
            │   └── dto
            │       ├── EmailCheckForJoinDTO.java
            │       ├── EmailCheckResponseMessage.java
            │       ├── JoinDTO.java
            │       ├── LoginDTO.java
            │       └── find
            │           ├── FindEmailDTO.java
            │           ├── FindPasswordDTO.java
            │           └── FoundEmailDTO.java
            └── social
                ├── SocialController.java
                ├── SocialInfoDto.java
                ├── SocialSignUpDto.java
                └── util
                    └── SocialSignUpValueEncryptoDecryptor.java
```

데이터베이스 스키마
프로젝트의 데이터베이스 스키마 설계 또는 ER 다이어그램
![barabara.png](..%2F..%2F..%2Fbarabara.png)
프로젝트 데모 주소
https://geulkkoli.hop.sh/
