package util;




import com.li.gddisease.entity.Disease;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DiseaseInfoGenerator {
    private static final double NB_MIN_LAT = 29.68311; // 宁波市最南端纬度
    private static final double NB_MAX_LAT = 30.31542; // 宁波市最北端纬度
    private static final double NB_MIN_LON = 120.69329; // 宁波市最西端经度
    private static final double NB_MAX_LON = 122.19334; // 宁波市最东端经度
    private static final String[] AREA_NAMES = { "鄞州区", "海曙区", "镇海区", "江北区", "北仑区" };
    private static Random random = new Random();
    private static Set<Integer> usedIds = new HashSet<Integer>();
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public static Disease generateRandomDisease() {
//        int id = generateUniqueId();
        String area = generateRandomArea();
        double[] doubles = generateRandomLocation();
        int type = generateRandomType();
        Date date = generateRandomDate();
        String description = generateRandomDescription();
//        return new Disease(id, area, doubles[1], doubles[0], type, description, date);
        return new Disease(area, doubles[1], doubles[0], type, description, date);
    }

    /*
    更改了数据库id为自动编号，不再需要生成id了
     */
    @Deprecated
    private static int generateUniqueId() {
        int id = random.nextInt(1000000);
        while (usedIds.contains(id)) {
            id = random.nextInt(1000000);
        }
        usedIds.add(id);
        return id;
    }

    private static String generateRandomArea() {
        int index = random.nextInt(AREA_NAMES.length);
        return AREA_NAMES[index];
    }

/*    private static double generateRandomLatitude() {
        return LATITUDE_MIN + (LATITUDE_MAX - LATITUDE_MIN) * random.nextDouble();
    }

    private static double generateRandomLongitude() {
        return LONGITUDE_MIN + (LONGITUDE_MAX - LONGITUDE_MIN) * random.nextDouble();
    }*/

    private static int generateRandomType() {
        return random.nextInt(5) + 1;
    }

    private static Date generateRandomDate() {
        long offset = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2023-04-25 00:00:00").getTime();
        long diff = end - offset + 1;
        return new Date(offset + (long)(Math.random() * diff));
    }

    private static String generateRandomDescription() {
        return "病害描述" + random.nextInt(100);
    }
    public static double[] generateRandomLocation() {
        double lat = NB_MIN_LAT + (NB_MAX_LAT - NB_MIN_LAT) * random.nextDouble();
        double lon = NB_MIN_LON + (NB_MAX_LON - NB_MIN_LON) * random.nextDouble();
        return new double[] { lat, lon };
    }

}