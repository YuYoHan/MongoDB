package org.example.redis.service;

import lombok.RequiredArgsConstructor;
import org.example.redis.entity.BoardEntity;
import org.example.redis.repository.BoardRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    // @Cacheable 어노테이션을 붙이면 Cache Aside 전략으로 캐싱이 적용된다.
    @Cacheable(cacheNames = "getBoards", key = "'boards:page:' + #page + ':size:' + #size", cacheManager = "boardCacheManager")
    public List<BoardEntity> getBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);
        Page<BoardEntity> pageOfBoards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return pageOfBoards.getContent();
    }
}
