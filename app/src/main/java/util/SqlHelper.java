package util;

import com.li.gddisease.dto.DiseaseChosenDto;

public class SqlHelper {
    public static String disease_sql(DiseaseChosenDto dto)
    {
        StringBuilder sql = new StringBuilder("select * from Disease where 1 = 1");
        if (dto.getPlace() != null)
            sql.append(" and place = " + dto.getPlace());
        if (dto.getStatus() != 0)
            sql.append(" and status = " + dto.getStatus());
        if (dto.getType() != 0)
            sql.append(" and type = " + dto.getType());
        return sql.toString();
    }
}
