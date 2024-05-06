package io.github.alfaio.afconfig.server.dal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import io.github.alfaio.afconfig.server.model.Config;

import java.util.List;


/**
 * @author LimMF
 * @since 2024/5/5
 **/
@Repository
@Mapper
public interface ConfigMapper {

    @Insert("insert into config(app, env, ns, pkey, pval) values(#{app}, #{env}, #{ns}, #{pkey}, #{pval})")
    int insert(Config config);

    @Update("update config set pval = #{pval} where app = #{app} and env = #{env} and ns = #{ns} and pkey = #{pkey}")
    int update(Config config);

    @Select("select * from config where app = #{app} and env = #{env} and ns = #{ns}")
    List<Config> listByAppAndEnvAndNs(String app, String env, String ns);

    @Select("select * from config where app = #{app} and env = #{env} and ns = #{ns} and pkey = #{pkey}")
    Config select(String app, String env, String ns, String pkey);
}
