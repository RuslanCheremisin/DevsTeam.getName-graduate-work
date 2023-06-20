package getName.devsTeam.graduateWork.service;

import getName.devsTeam.graduateWork.dto.RegisterReq;
import getName.devsTeam.graduateWork.dto.Role;

public interface AuthService {
    boolean login(String userName, String password);
    boolean register(RegisterReq registerReq, Role role);
}
