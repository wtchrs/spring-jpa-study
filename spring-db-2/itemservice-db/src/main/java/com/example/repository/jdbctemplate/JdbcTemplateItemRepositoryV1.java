package com.example.repository.jdbctemplate;

import com.example.domain.Item;
import com.example.repository.ItemRepository;
import com.example.repository.ItemSearchCond;
import com.example.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Using JdbcTemplate
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

    private final JdbcTemplate template;

    public JdbcTemplateItemRepositoryV1(DataSource ds) {
        this.template = new JdbcTemplate(ds);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item (item_name, price, quantity) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        // For inserts, it's preferable to use SimpleJdbcInsert instead of JdbcTemplate.
        template.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, item.getItemName());
            pstmt.setInt(2, item.getPrice());
            pstmt.setInt(3, item.getQuantity());
            return pstmt;
        }, keyHolder);

        Long key = keyHolder.getKey().longValue();
        item.setId(key);

        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=?, price=?, quantity=? where id=?";
        template.update(sql, updateParam.getItemName(), updateParam.getPrice(), updateParam.getQuantity(), itemId);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id=?";

        try {
            Item findItem = template.queryForObject(sql, JdbcTemplateItemRepositoryV1::itemRowMapper, id);
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

        List<Object> args = new ArrayList<>();

        if (!ObjectUtils.isEmpty(itemName) || !ObjectUtils.isEmpty(maxPrice)) {
            sql += " where ";
        }

        boolean hasWhereClause = false;
        if (!ObjectUtils.isEmpty(itemName)) {
            sql = sql + "item_name like concat('%', ?, '%')";
            args.add(itemName);
            hasWhereClause = true;
        }
        if (!ObjectUtils.isEmpty(maxPrice)) {
            if (hasWhereClause) {
                sql += " and ";
            }
            sql += "price <= ?";
            args.add(maxPrice);
        }

        log.info("sql = {}", sql);

        return template.query(sql, JdbcTemplateItemRepositoryV1::itemRowMapper, args.toArray());
    }

    private static Item itemRowMapper(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item(rs.getString("item_name"), rs.getInt("price"), rs.getInt("quantity"));
        item.setId(rs.getLong("id"));
        return item;
    }

}
