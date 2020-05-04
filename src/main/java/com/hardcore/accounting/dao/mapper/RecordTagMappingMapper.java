package com.hardcore.accounting.dao.mapper;

import com.hardcore.accounting.model.persistence.RecordTagMapping;
import com.hardcore.accounting.model.persistence.Tag;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RecordTagMappingMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO hcas_record_tag_mapping(record_id, tag_id, status) "
            + "VALUES (#{recordId}, #{tagId}, #{status})")
    int insertRecordTagMapping(RecordTagMapping recordTagMapping);

    @Insert({"<script>",
                "INSERT INTO hcas_record_tag_mapping(record_id, tag_id, status) VALUES ",
                "<foreach item='item' index='index' collection='recordTagMappings'",
                "open='(' separator = '),(' close=')'>",
                "#{item.recordId}, #{item.tagId}, #{item.status}",
                "</foreach>",
             "</script>"})
    int batchRecordTagMapping(@Param("recordTagMappings") List<RecordTagMapping> recordTagMappings);

    @Select("SELECT ht.id, ht.description, hrtm.record_id FROM hcas_tag ht "
            + "LEFT JOIN hcas_record_tag_mapping hrtm "
            + "ON hrtm.tag_id = ht.id "
            + "WHERE hrtm.record_id = #{recordId};")
    List<Tag> getTagListByRecordId(@Param("recordId") Long recordId);

    @Select("SELECT id, record_id, tag_id, status FROM hcas_record_tag_mapping"
            + "WHERE record_id = #{recordId};")
    @Results({
                 @Result(column = "id", property = "id"),
                 @Result(column = "record_id", property = "recordId"),
                 @Result(column = "tag_id", property = "tagId"),
                 @Result(column = "status", property = "status"),
             })
    List<RecordTagMapping> getRecordTagMappingListByRecordId(@Param("recordId") Long recordId);

    @Update("DELETE FROM hcas_record_tag_mapping WHERE record_id=#{recordId}")
    int deleteRecordTagMappingList(@Param("recordId") Long recordId);
}
