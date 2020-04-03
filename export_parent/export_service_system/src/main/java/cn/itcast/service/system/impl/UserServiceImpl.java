package cn.itcast.service.system.impl;

import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Author han
 * @Date 2020/3/11 16:20
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


    @Override
    public PageInfo<User> findByPage(String companyId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userDao.findAll(companyId);
        return new PageInfo<>(list);
    }

    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    @Override
    public void save(User user) {
        user.setUserId(UUID.randomUUID().toString());
        if(user.getPassword() != null && user.getPassword() != ""){
          user.setPassword(new Md5Hash(user.getPassword(),user.getEmail()).toString());
        }
        userDao.add(user);
    }

    @Override
    public void update(User user) {
        userDao.upData(user);
    }

    @Override
    public boolean delete(String id) {
        Long count = userDao.findUserRoleByUserId(id);
        if (count == null || count == 0) {
            userDao.delete(id);
            return true;
        } else {
            System.out.println("删除失败");
            return false;
        }
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public void updataUserRole(String userId, String[] roleIds) {
        userDao.deleteUserRole(userId);
        if (roleIds != null && roleIds.length > 0) {
            for (String roleId : roleIds) {
                userDao.updataUserRole(userId, roleId);
            }
        }

    }

    @Override
    public User login(String email) {
        return userDao.login(email);
    }
}
