package com.geulkkoli.domain.admin.service;

import com.geulkkoli.domain.admin.AccountLock;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.post.service.PostFindService;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.posthashtag.service.PostHashTagService;
import com.geulkkoli.domain.topic.Topic;
import com.geulkkoli.domain.topic.TopicRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.domain.admin.AccountLockRepository;
import com.geulkkoli.domain.admin.ReportRepository;
import com.geulkkoli.web.admin.DailyTopicDTO;
import com.geulkkoli.web.admin.ReportDTO;
import com.geulkkoli.web.blog.dto.ArticleEditRequestDTO;
import com.geulkkoli.web.blog.dto.WriteRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl {

    private final UserRepository userRepository;

    private final PostFindService postFindService;
    private final PostService postService;

    private final PostRepository postRepository;

    private final ReportRepository reportRepository;

    private final AccountLockRepository accountLockRepository;

    private final PostHashTagService postHashTagService;

    private final TopicRepository topicRepository;

    public AccountLock lockUser(Long userId, String reason, Long lockDate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        LocalDateTime lockUntil = LocalDateTime.now().plusDays(lockDate);
        return accountLockRepository.save(user.lock(reason, lockUntil));
    }

    //신고받은 게시글 조회
    public List<ReportDTO> findAllReportedPost() {
        List<Post> allPost = reportRepository.findDistinctByReportedPost();
        List<ReportDTO> reportDtoList = new ArrayList<>();

        for (Post post : allPost) {
            reportDtoList.add(ReportDTO.toDto(post, reportRepository.countByReportedPost(post)));
        }

        return reportDtoList;
    }

    //게시글 작성자 조회
    public User findUserByPostId(Long postId) {
        Post post = postFindService.findById(postId);
        return post.getUser();
    }

    public void deletePost(Long postId) {
        Post post = postFindService.findById(postId);
        Post deletePost = post.getUser().deletePost(post);
        postService.deletePost(deletePost.getPostId(), deletePost.getUser().getUserId());
    }

    public Post saveNotice(WriteRequestDTO writeRequestDto, User user) {
        Post save = postRepository.save(user.writePost(writeRequestDto));
        log.info("addDto.getTagListString() = " + writeRequestDto.getHashTagString());
        postHashTagService.addHashTagsToPost(save, writeRequestDto);
        return save;
    }

    public Post updateNotice(Long postId, ArticleEditRequestDTO updateParam) {
        Post post = postFindService.findById(postId);
        postService.updatePost(post, updateParam);
        return postHashTagService.editHashTagsToPost(post, updateParam);
    }

    public List<DailyTopicDTO> findWeeklyTopic() {
        List<Topic> topics = topicRepository.findTopicByUpComingDateBetween(LocalDate.now(), LocalDate.now().plusDays(29), Sort.by(Sort.Direction.ASC, "upComingDate"));

        if (topics.size() < 30) {
            topics = fillTopic(topics);
        }

        return topics.stream()
                .limit(10)
                .map(a -> DailyTopicDTO.builder()
                        .topic(a.getTopicName())
                        .date(a.getUpComingDate().toString())
                        .build())
                .collect(Collectors.toList());
    }


    public List<Topic> fillTopic(List<Topic> topics) {
        for (int i = 0; i < 30; i++) {
            if (topics.size() == i || !topics.get(i).getUpComingDate().isEqual(LocalDate.now().plusDays(i))) {
                Topic topic = topicRepository.findTopicByUseDateBefore(LocalDate.now().minusDays(30));
                if (!topics.contains(topic)) {
                    topics.add(i, topic);
                    topics.get(i).settingUpComingDate(LocalDate.now().plusDays(i));
                } else {
                    i--;
                }
            }
        }
        topics.sort(Comparator.comparing(Topic::getUpComingDate));
        return topics;
    }

    public Topic updateTopic(DailyTopicDTO topic) {
        Topic findTopic = topicRepository.findTopicByUpComingDate(LocalDate.parse(topic.getDate()));
        log.info("findTopic = " + findTopic);
        findTopic.settingUpComingDate(LocalDate.of(2000, 1, 1));

        Topic updateTopic = topicRepository.findTopicByTopicName(topic.getTopic());

        if (Objects.isNull(updateTopic)) {
            updateTopic = Topic.builder().topicName(topic.getTopic()).build();
        }

        updateTopic.settingUpComingDate(LocalDate.parse(topic.getDate()));
        return topicRepository.save(updateTopic);
    }
}
