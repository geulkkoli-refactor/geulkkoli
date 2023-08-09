package com.geulkkoli.domain.user;

import com.geulkkoli.application.security.LockExpiredTimeException;
import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.security.RoleEntity;
import com.geulkkoli.domain.admin.AccountLock;
import com.geulkkoli.domain.admin.Report;
import com.geulkkoli.domain.follow.Follow;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.comment.Comments;
import com.geulkkoli.domain.favorites.Favorites;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.comment.dto.CommentEditDTO;
import com.geulkkoli.web.post.dto.PostAddDTO;
import com.geulkkoli.web.post.dto.PostEditRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "users", indexes = @Index(name = "idx_user_email_nick_name", columnList = "email,nick_name"))
public class User extends ConfigDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nick_name", nullable = false, unique = true)
    private String nickName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_No", nullable = false, unique = true)
    private String phoneNo;

    @Column(nullable = false)
    private String gender;

    /**
     * report의 필드 User reporter와 대응
     * casacde = CascadeType.AlL:: addReport할때 report.reporter()에 값을 넣으면 같이 영속성에 저장되게 하는 설정
     * report가 단일 소유가 아니기에 설정을 취소한다 추후에 post,like,comment일 때 적용하자
     */
    @OneToMany(mappedBy = "reporter")
    private Set<Report> reports = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "lockedUser")
    private Set<AccountLock> accountLocks = new LinkedHashSet<>();

    //게시글의 유저 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    //댓글의 유저 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    //좋아요의 유저 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Favorites> favorites = new LinkedHashSet<>();

    //팔로우의 유저 매핑
    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followees = new ArrayList<>();

    @Builder
    public User(String userName, String password, String nickName, String email, String phoneNo, String gender) {
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.gender = gender;
    }

    public String updatePassword(String password) {
        this.password = password;
        return this.password;
    }

    public String updateUserName(String userName) {
        this.userName = userName;
        return this.userName;
    }

    public String updateNickName(String nickName) {
        this.nickName = nickName;
        return this.nickName;
    }

    public String updatePhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
        return this.phoneNo;
    }

    public String updateGender(String gender) {
        this.gender = gender;
        return this.gender;
    }


    /**
     * 게시글 관련 CRUD
     */
    //유저가 쓴 게시글
    public Post writePost(PostAddDTO postAddDTO) {
        Post post = Post.builder()
                .title(postAddDTO.getTitle())
                .postBody(postAddDTO.getPostBody())
                .user(this)
                .nickName(postAddDTO.getNickName())
                .build();

        this.posts.add(post);
        return post;
    }

    public Post deletePost(Post post) {
        posts.remove(post);
        return post;
    }

    // 유저가 쓴 게시글 수정하기
    public Post editPost(Post post, PostEditRequestDTO postEditRequestDTO) {
        post.changePostBody(postEditRequestDTO.getPostBody());
        post.changeTitle(postEditRequestDTO.getTitle());
        post.changeNickName(postEditRequestDTO.getNickName());
        return post;
    }

    /**
     * 댓글 관련 CRUD
     */
    //유저가 쓴 댓글
    public Comments writeComment(CommentBodyDTO commentBody, Post post) {
        Comments comment = new Comments(this, post, commentBody.getCommentBody());
        post.getComments().add(comment);
        this.comments.add(comment);
        return comment;
    }

    // 유저가 쓴 댓글 수정하기
    public Comments editComment(Comments editTarget, CommentEditDTO editCommentBody) {
        Comments result = comments.stream().filter(comment -> comment.equals(editTarget)).findFirst().orElseThrow(() -> new NoSuchCommnetException("해당 댓글이 없습니다."));
        result.changeComments(editCommentBody.getCommentBody());
        return result;
    }

    //유저가 지운 댓글
    public Comments deleteComment(Comments deleteComment) {
        comments.remove(deleteComment);
        deleteComment.getPost().getComments().remove(deleteComment);
        return deleteComment;
    }

    /**
     * 좋아요 관련 CRUD
     */
    // 유저가 누른 좋아요
    public Favorites pressFavorite(Post post) {
        Favorites favorite = new Favorites(this, post);
        post.getFavorites().add(favorite);
        this.favorites.add(favorite);
        return favorite;
    }

    // 좋아요 취소하기
    public Favorites cancelFavorite(Favorites deleteFavorite) {
        favorites.remove(deleteFavorite);
        deleteFavorite.getPost().getFavorites().remove(deleteFavorite);
        return deleteFavorite;
    }


    /**
     * 신고 관련 CRUD
     */
    public Report writeReport(Post post, String reason) {
        Report report = Report.of(post, this, LocalDateTime.now(), reason);
        this.reports.add(report);
        return report;
    }

    public Report deleteReport(Report report) {
        Report report1 = reports.stream().filter(r -> r.equals(report)).findFirst().orElseThrow(() -> new NoSuchReportException("해당 신고가 없습니다."));
        reports.remove(report1);
        return report1;
    }

    /**
     * @param reason             잠김 이유가 들어옵니다.
     * @param lockExpirationDate 만료 날짜가 들어옵니다.
     */
    public AccountLock lock(String reason, LocalDateTime lockExpirationDate) {
        AccountLock accountLock = AccountLock.of(this, reason, lockExpirationDate);
        this.accountLocks.add(accountLock);
        return accountLock;
    }

    //계정이 잠금 여부를 확인하는 메서드 입니다.
    public Boolean isLock() {
        if (CollectionUtils.isEmpty(this.accountLocks)) {
            return false;
        }

        checkLockExpiredDate();

        return this.accountLocks.stream()
                .map(AccountLock::getLockExpirationDate)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new LockExpiredTimeException("계정 잠금 기간이 설정되지 않았습니다."))
                .isAfter(LocalDateTime.now());
    }

    private void checkLockExpiredDate() {
        for (AccountLock accountLock : this.accountLocks) {
            if (Objects.isNull(accountLock.getLockExpirationDate())) {
                throw new LockExpiredTimeException("계정 잠금 기간이 설정되지 않았습니다.");
            }
        }
    }


    public RoleEntity addRole(Role role) {
        RoleEntity roleEntity = RoleEntity.of(role, this);
        this.role = roleEntity;
        return roleEntity;
    }

    public Follow follow(User followee) {
        Follow follow = Follow.of(followee, this);
        this.followees.add(follow);
        return follow;
    }

    public Follow unfollow(Follow follow) {
        this.followees.remove(follow);
        return follow;
    }

    public RoleEntity getRole() {
        return role;
    }

    public String roleName() {
        return role.getRole().getRoleName();
    }

    public Boolean isAdmin() {
        return role.isAdmin();
    }


    public Boolean isUser() {
        return role.isUser();
    }

    public String authority() {
        return role.authority();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}





