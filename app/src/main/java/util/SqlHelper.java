package util;

import com.li.gddisease.dto.DiseaseChosenDto;

public class SqlHelper {
    public static String disease_sql(DiseaseChosenDto dto)
    {
        StringBuilder sql = new StringBuilder("select * from Disease where 1 = 1");
        //虽然这段写的很离谱，但是别动，数据库设计的不好，被迫的
        if (dto.getStatus() != 0)
        {
            if (dto.getStatus() == 3)
            {
                sql = new StringBuilder("select * from Disease where id not in " +
                        "(select disease_id from Handle, Disease where " +
                        "Disease.id = Handle.disease_id)");
            }
            else
            {
                sql = new StringBuilder("select * from Disease, Handle where " +
                        "Disease.id = Handle.disease_id and status = " + dto.getStatus());
            }
        }
        if (dto.getPlace() != null)
        {
            String s = (" and place = '" + dto.getPlace());
            sql.append(s + "'");
        }
        if (dto.getType() != 0)
            sql.append(" and type = " + dto.getType());
        return sql.toString();
    }
}
