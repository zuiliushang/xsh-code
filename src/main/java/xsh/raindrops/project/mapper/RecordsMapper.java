package xsh.raindrops.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;
import org.apache.kafka.common.record.Records;

public interface RecordsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table records
     *
     * @mbg.generated
     */
    @Delete({
        "delete from records",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table records
     *
     * @mbg.generated
     */
    @Insert({
        "insert into records (id, name, ",
        "records)",
        "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{records,jdbcType=INTEGER})"
    })
    int insert(Records record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table records
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, name, records",
        "from records",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="records", property="records", jdbcType=JdbcType.INTEGER)
    })
    Records selectByPrimaryKey(Integer id);
    
    @Select({
    	"select",
    	"id,name,records",
    	"from records",
    	"where name = #{name,jdbcType=VARCHAR}"
    })
    @Results({
    	@Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
    	@Result(column="name", property="name",jdbcType=JdbcType.VARCHAR),
    	@Result(column="records", property="records", jdbcType=JdbcType.INTEGER)
    })
    Records selectLargRecord(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table records
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, name, records",
        "from records"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="records", property="records", jdbcType=JdbcType.INTEGER)
    })
    List<Records> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table records
     *
     * @mbg.generated
     */
    @Update({
        "update records",
        "set name = #{name,jdbcType=VARCHAR},",
          "records = #{records,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Records record);
    
    
}