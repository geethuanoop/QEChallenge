package dataaccess;

import config.DBConnection;
import model.File;
import model.Hero;
import model.Voucher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Repository {

    private static final String SQL_GET_BY_ID = "SELECT * FROM working_class_heroes WHERE natid = ?";
    private static final String SQL_VOUCHER_GET_BY_NAME = "SELECT name, voucher_type FROM testdb.vouchers where working_class_hero_id=(select id from working_class_heroes where natid=?);";
    private static final String SQL_FILE_FETCH = "SELECT * FROM testdb.file ORDER BY(id) desc";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public Hero getUserById(String natid) {

        Hero hero = new Hero();
        try {
            connection = DBConnection.getInstance();

            preparedStatement = connection.prepareStatement(SQL_GET_BY_ID);
            preparedStatement.setString(1, natid);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                hero.setNatid(resultSet.getString("natid"));
                hero.setName(resultSet.getString("name"));
                hero.setGender(resultSet.getString("gender"));
                hero.setBirthDate(resultSet.getString("birth_date"));
                hero.setDeathDate(resultSet.getString("death_date"));
                hero.setSalary(resultSet.getDouble("salary"));
                hero.setTaxPaid(resultSet.getDouble("tax_paid"));
                hero.setBrowniePoints(resultSet.getDouble("brownie_points"));

                return hero;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hero;
    }

    public Voucher getVoucherByName(String natid) {

        Voucher voucher = new Voucher();
        try {
            connection = DBConnection.getInstance();

            preparedStatement = connection.prepareStatement(SQL_VOUCHER_GET_BY_NAME);
            preparedStatement.setString(1, natid);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                voucher.setVoucherName(resultSet.getString("name"));
                voucher.setVoucherType(resultSet.getString("voucher_type"));

                return voucher;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voucher;
    }

    public File getFile() {

        File file = new File();
        try {
            connection = DBConnection.getInstance();

            preparedStatement = connection.prepareStatement(SQL_FILE_FETCH);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                file.setFileStatus(resultSet.getString("file_status"));
                file.setFileType(resultSet.getString("file_type"));
                file.setFilePath(resultSet.getString("file_path"));
                file.setTotalCount(resultSet.getLong("total_count"));

                return file;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return file;
    }
}
