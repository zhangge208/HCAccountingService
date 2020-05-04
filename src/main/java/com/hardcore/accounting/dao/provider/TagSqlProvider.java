package com.hardcore.accounting.dao.provider;

import com.hardcore.accounting.model.persistence.Tag;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

@Slf4j
public class TagSqlProvider {
    /**
     * Update tag item via specific tag.
     *
     * @param tag the tag item need to update.
     * @return the sql to execute.
     */

    public String updateTag(final Tag tag) {
        // Dynamic SQL
        return new SQL() {
            {
                UPDATE("hcas_tag");
                if (tag.getDescription() != null) {
                    SET("description = #{description}");
                }
                if (tag.getStatus() != null) {
                    SET("status = #{status}");
                }
                if (tag.getUserId() != null) {
                    SET("user_id = #{userId}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }

    /**
     * Get tag list by ids.
     * @param ids the specific id list
     * @return the sql to execute.
     */
    public String getTagListByIds(@Param("id") final List<Long> ids) {
        return new SQL() {
            {
                SELECT("id", "description", "user_id", "status");
                FROM("hcas_tag");
                WHERE(String.format("id in ('%s')", Joiner.on("','").skipNulls().join(ids)));
            }
        }.toString();
    }


}
