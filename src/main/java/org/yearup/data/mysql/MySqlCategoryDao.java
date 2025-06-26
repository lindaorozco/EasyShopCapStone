package org.yearup.data.mysql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.configurations.DatabaseConfig;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    private BasicDataSource basicDataSource;

    @Autowired

    @Override
    public List<Category> getAllCategories() {
        // get all categories / create list
        String query = "SELECT * FROM categories";
        List<Category> categoryList = new ArrayList<>();

        try(
                Connection connection = basicDataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet resultSet = ps.executeQuery()
        ){
            while (resultSet.next()){
                categoryList.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    @Override
    public Category getById(int categoryId) {
        // create null in case there isn't a ID
        Category category = null;
        String query = "SELECT * FROM \n" +
                "WHERE category_id = ?";
        try(
                Connection connection = basicDataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
                ){
            // set the parameter indexes
            ps.setInt(1, categoryId );

            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    category = mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return category;
    }

    @Override
    public Category create(Category category) {
        String query = "INSERT INTO category (name, description) VALUES (?,?)";
        try (
                Connection connection = basicDataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = ps.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        category.setCategoryId(resultSet.getInt(1));
                        return category; // éxito
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // si algo falla
    }


    @Override
    public void update(int categoryId, Category category){
        // update category
        String query = "UPDATE FROM categories SET name = ?\n " +
                "WHERE category_id = ?;";

        try(
                Connection connection = basicDataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
                ){
                    ps.setInt(1, categoryId);
                    ps.setString(2, category.getName());
                    ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String query = "DELETE FROM categories WHERE category_id = ?";

        try(
                Connection connection = basicDataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
        ){
            ps.setInt(1,categoryId);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
