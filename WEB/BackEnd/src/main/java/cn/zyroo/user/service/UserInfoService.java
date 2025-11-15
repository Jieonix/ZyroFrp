package cn.zyroo.user.service;

import cn.zyroo.user.model.Users;
import cn.zyroo.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {

  @Autowired
  private UsersRepository usersRepository;

  // 根据邮箱查询用户
  public Users getUserByEmail(String Email) {
    return usersRepository.findByEmail(Email);
  }

  public List<Users> getAll() {
    return usersRepository.findAll();
  }

}
