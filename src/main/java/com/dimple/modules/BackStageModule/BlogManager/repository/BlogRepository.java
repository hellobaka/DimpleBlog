package com.dimple.modules.BackStageModule.BlogManager.repository;

import com.dimple.modules.BackStageModule.BlogManager.bean.Blog;
import com.dimple.modules.FrontDeskModule.domain.BlogDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : Dimple
 * @version : 1.0
 * @class : BlogRepository
 * @description :
 * @date : 12/26/18 19:09
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer>, JpaSpecificationExecutor<Blog> {

    @Query(value = "select * from blog where if(:title is not null,title like concat('%',:title,'%'),1=1 )" +
            "and if(:startTime is not null,update_time >= :startTime,1=1)" +
            "and if (:endTime is not null ,update_time <= :endTime,1=1)" +
            "and if(:status is not null ,status=:status,1=1) ", nativeQuery = true)
    List<Blog> findAll(@Param("title") String title, Date startTime, Date endTime, Integer status);

    @Query(value = "select (select count(*) from blog where status=1)as  publish," +
            "(select count(*) from blog where status=2)as drafts," +
            "(select count(*) from  blog where status=3) as dustbin", nativeQuery = true)
    Map<String, Integer> findAllBlogStatusCount();

    Integer countByStatus(Integer status);

    List<Blog> findAllByCategoryId(Integer id);

    List<Blog> findAllBySupportEquals(Boolean support);

    Blog findByBlogId(Integer id);

    @Query(value = "select * from blog order by update_time limit 0,4", nativeQuery = true)
    List<Blog> getNewestUpdateBlog();

    @Query(value = "select * from blog where blog_id > :id order by blog_id desc limit 0,1;", nativeQuery = true)
    Blog getNextBlog(@Param("id") Integer id);

    @Query(value = "select * from blog where blog_id < :id order by blog_id limit 0,1;", nativeQuery = true)
    Blog getPreviousBlog(@Param("id") Integer id);

    @Modifying
    @Query("update Blog set click=click+1 where blogId=:id")
    void incrementBlogCountById(@Param("id") Integer id);

    Blog getByBlogId(Integer id);

    /**
     * 获取随机的博客
     *
     * @param i 需要获取的博客的数量
     * @return
     */
    @Query(value = "select * from blog order by rand() limit :pageSize", nativeQuery = true)
    List<Blog> getRandomBlog(@Param("pageSize") int i);


    /**
     * 分页获取指定字段的数据
     *
     * @param pageable
     * @return
     */
    @Query(value = "select new com.dimple.modules.FrontDeskModule.domain.BlogDomain(b.blogId,b.categoryId,c.title as categoryTitle,b.title,b.summary,b.createTime,b.click,b.updateTime,b.headerUrl) from Blog as b,Category c where b.categoryId=c.categoryId ")
    Page<BlogDomain> getAllBlogVo(Pageable pageable);

    /**
     * 归档信息获取
     *
     * @return 年月以及这个年月下的博客数量
     */
    @Query(value = "select date_format(create_time, '%Y-%m') as date,count(*) as count from blog group by date_format(create_time,'%Y-%m')order by date_format(create_time, '%Y-%m') desc", nativeQuery = true)
    List<Map<String, Object>> getDateAndCount();

    @Query(value = "select blog_id,title,summary,create_time from blog where date_format(create_time, '%Y-%m') =:date", nativeQuery = true)
    List<Map<String, Object>> getAllByUpdateTime(@Param("date") String date);

    @Query(value = "select new com.dimple.modules.FrontDeskModule.domain.BlogDomain(b.blogId,b.categoryId,c.title as categoryTitle,b.title,b.summary,b.createTime,b.click,b.updateTime,b.headerUrl) from Blog as b,Category c where b.categoryId=c.categoryId and c.categoryId=:categoryId ")
    Page<BlogDomain> getAllBlogByCategoryId(Pageable pageable, @Param("categoryId") int categoryId);
}
