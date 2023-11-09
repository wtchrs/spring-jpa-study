package com.example.repository.jdbctemplate;

import com.example.domain.Item;
import com.example.repository.ItemRepository;
import com.example.repository.ItemSearchCond;
import com.example.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Using following classes:
 * {@link NamedParameterJdbcTemplate}
 * {@link SqlParameterSource}
 *  - {@link BeanPropertySqlParameterSource}
 *  - {@link MapSqlParameterSource}
 * {@link Map}
 * {@link BeanPropertyRowMapper}
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    private final NamedParameterJdbcTemplate template;

    private final RowMapper<Item> itemRowMapper = BeanPropertyRowMapper.newInstance(Item.class);

    public JdbcTemplateItemRepositoryV2(DataSource ds) {
        this.template = new NamedParameterJdbcTemplate(ds);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item (item_name, price, quantity) values (:itemName, :price, :quantity)";

        SqlParameterSource params = new BeanPropertySqlParameterSource(item);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, params, keyHolder);

        Long key = keyHolder.getKey().longValue();
        item.setId(key);

        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);
        template.update(sql, params);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id=:id";

        Map<String, Object> params = Map.of("id", id);

        try {
            Item findItem = template.queryForObject(sql, params, itemRowMapper);
            return Optional.of(findItem);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        String sql = "select id, item_name, price, quantity from item";

        SqlParameterSource params = new BeanPropertySqlParameterSource(cond);

        if (!ObjectUtils.isEmpty(itemName) || !ObjectUtils.isEmpty(maxPrice)) {
            sql += " where ";
        }

        boolean hasWhereClause = false;
        if (!ObjectUtils.isEmpty(itemName)) {
            sql = sql + "item_name like concat('%', :itemName, '%')";
            hasWhereClause = true;
        }
        if (!ObjectUtils.isEmpty(maxPrice)) {
            if (hasWhereClause) {
                sql += " and ";
            }
            sql += "price <= :maxPrice";
        }

        log.info("sql = {}", sql);

        return template.query(sql, params, itemRowMapper);
    }

}
