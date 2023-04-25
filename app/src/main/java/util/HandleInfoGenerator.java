package util;

import com.google.android.material.color.HarmonizedColorAttributes;
import com.li.gddisease.AppDatabase;
import com.li.gddisease.entity.Disease;
import com.li.gddisease.entity.Handle;
import com.li.gddisease.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.net.ssl.SNIHostName;

public class HandleInfoGenerator {
    private AppDatabase db;
    private static Random random = new Random();
    private static Set<Integer> usedIds = new HashSet<Integer>();
    private static Set<Integer> diseasedIds = new HashSet<Integer>();
    private static Set<Integer> handleIds = new HashSet<Integer>();

    /*
    自动编号，无需生成id
     */

    public HandleInfoGenerator(AppDatabase mdb) {
        this.db = mdb;
    }

    @Deprecated
    private static int generateUniqueId() {
        int id = random.nextInt(1000000);
        while (handleIds.contains(id)) {
            id = random.nextInt(1000000);
        }
        handleIds.add(id);
        return id;
    }

    private static int generateRandomStatus() {
        int num = random.nextInt(2) + 1;
        return num;
    }

    //这个代码是有问题的，理论上不能出现重复元素,这里为了实现多对多，原则上是不应该出现重复元素的，可能是数据库设计的不合理
    public Handle generateInfo()
    {
        List<Disease> diseases = db.diseaseDao().getDiseaseAll();
        int diseaseId = diseases.get(random.nextInt(diseases.size())).getId();
        List<User> users = db.userDao().getALL();
        int userId = users.get(random.nextInt(users.size())).getId();
        Handle handle = new Handle();
        handle.setDiseaseId(diseaseId);
        handle.setUserId(userId);
        handle.setStatus(generateRandomStatus());
        return handle;

    }

    /*
    还是自动编号问题
     */
    @Deprecated
    public Handle generateHandleInfo()
    {
        List<User> users = db.userDao().getALL();
        List<Disease> diseases = db.diseaseDao().getDiseaseAll();
        int userId = users.get(random.nextInt(users.size())).getId();
        int diseaseId = diseases.get(random.nextInt(diseases.size())).getId();
        while (usedIds.contains(userId)) {
            userId = users.get(random.nextInt(users.size())).getId();
        }
        usedIds.add(userId);
        while (diseasedIds.contains(diseaseId)) {
            diseaseId = diseases.get(random.nextInt(diseases.size())).getId();
        }
        diseasedIds.add(diseaseId);
        Handle handle = new Handle();
        handle.setUserId(userId);
        handle.setDiseaseId(diseaseId);
        int id = generateUniqueId();
        handle.setId(id);
        int status = generateRandomStatus();
        handle.setStatus(status);
        return handle;
    }

}
