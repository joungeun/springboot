package com.dsm.boot03.persistence;

import com.dsm.boot03.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long>, QuerydslPredicateExecutor<Board> {

    public List<Board> findBoardByTitle(String title);

    public Collection<Board> findByWriter(String writer);

    // 작성자 이름에 ~가 들어가있는 게시글을 검색하려고 함
    public Collection<Board> findByWriterContaining(String writer);

    public Collection<Board> findByTitleContainingOrContentContaining(String title, String content);

    public Collection<Board> findByTitleContainingAndBnoGreaterThan(String keyword, Long num);

    // bno 가 특정 숫자보다 큰 것을 (부등호 처리) 역순 처리
    public Collection<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno);

    public List<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno, Pageable paging);

    public Page<Board> findByBnoGreaterThan(Long bno, Pageable paging); //OrderBy 부분 없음, 정렬 조건 빠짐

    @Query("SELECT b FROM Board b WHERE b.title LIKE %?1% AND b.bno > 0 ORDER BY b.bno DESC")
    public List<Board> findByTitle(String title);

    // 필요 칼럼만 추출
    @Query("SELECT b.bno, b.title, b.writer, b.regdata"
            +" FROM Board b WHERE b.title LIKE %?1% AND b.bno > 0 ORDER BY b.bno DESC")
    public List<Object[]> findByTitle2(String title);

    // nativeQuery
    @Query(value="select writer from tbl_boards where title like CONCAT('%',?1,'%') and bno>0 order by bno desc", nativeQuery = true)
    List<Object> findByTitle3(String title);

    // @Param 사용해서 컨텐츠 검색
    @Query("SELECT c FROM Board c WHERE c.content LIKE %:content% AND c.bno > 0 ORDER BY c.bno DESC")
    public List<Board> findByContent(@Param("content")String content);

    // 엔티티 타입 자동 사용 #{#entityName}
    @Query("SELECT b FROM #{#entityName} b WHERE b.writer LIKE %?1% AND b.bno >0 ORDER BY b.bno DESC")
    List<Board> findByWriter2(String writer);

    @Query("SELECT b FROM Board b where b.bno >0 ORDER BY b.bno DESC")
    public List<Board> findBypage(Pageable pageable);

}
