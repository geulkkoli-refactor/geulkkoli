package com.geulkkoli.web.channels;

import com.geulkkoli.domain.favorites.service.FavoriteService;
import com.geulkkoli.domain.follow.service.FollowFindService;
import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.service.HashTagFindService;
import com.geulkkoli.domain.post.SearchType;
import com.geulkkoli.domain.post.service.PostFindService;
import com.geulkkoli.domain.posthashtag.service.PostHahTagFindService;
import com.geulkkoli.domain.user.service.UserFindService;
import com.geulkkoli.web.blog.dto.ArticlePagingRequestDTO;
import com.geulkkoli.web.blog.dto.PagingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RequestMapping("/channels")
@Controller
public class ChannelsController {
    private final PostFindService postFindService;

    private final PostHahTagFindService postHashTagFindService;
    private final HashTagFindService hashTagFindService;

    public ChannelsController(PostFindService postFindService, PostHahTagFindService postHashTagFindService, HashTagFindService hashTagFindService) {
        this.postFindService = postFindService;
        this.postHashTagFindService = postHashTagFindService;
        this.hashTagFindService = hashTagFindService;
    }

    @GetMapping("")
    public ModelAndView channels(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(defaultValue = "") String searchType,
                                 @RequestParam(defaultValue = "") String searchWords) {
        log.info("searchType: {}, searchWords: {}", searchType, searchWords);
        ModelAndView mv = new ModelAndView("channels/channels");
        //searchType이 해시태그 일때
        if (SearchType.HASH_TAG.getType().equals(searchType)) {
            List<HashTag> hashTag = hashTagFindService.findHashTag(searchWords);
            Page<ArticlePagingRequestDTO> postRequestListDTOS = postHashTagFindService.searchPostsListByHashTag(pageable, hashTag);
            PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(postRequestListDTOS);
            mv.addObject("page", pagingDTO);
            searchDefault(mv, searchType, searchWords);
            return mv;
        }

        Page<ArticlePagingRequestDTO> postRequestListDTOS = postFindService.searchPostsList(pageable, searchType, searchWords);
        PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(postRequestListDTOS);
        mv.addObject("page", pagingDTO);
        searchDefault(mv, searchType, searchWords);
        return mv;
    }
    private static void searchDefault(ModelAndView model, String searchType, String searchWords) {
        model.addObject("searchType", searchType);
        model.addObject("searchWords", searchWords);
    }
}
