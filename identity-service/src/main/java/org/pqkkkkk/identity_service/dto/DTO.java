package org.pqkkkkk.identity_service.dto;

import java.util.Date;
import java.util.List;

import org.pqkkkkk.identity_service.entity.OrganizerInfo;
import org.pqkkkkk.identity_service.entity.Role;
import org.pqkkkkk.identity_service.entity.User;

public class DTO {
    public record RoleDTO(
        Long roleId,
        String name
    ){
        public static RoleDTO from(Role role){
            return new RoleDTO(role.getRoleId(), role.getName().name());
        }
    }
    public record OrganizerInfoDTO(
        Long organizerId,
        String organizerName,
        String description
    ){
        public static OrganizerInfoDTO from(OrganizerInfo organizerInfo){
            return new OrganizerInfoDTO(organizerInfo.getOrganizerInfoId(), organizerInfo.getName(), organizerInfo.getDescription());
        }
    }
    public record UserDTO(
        Long userId,
        String userName,
        String passWord,
        String email,
        String fullName,
        String phoneNumber,
        Date birthday,
        String gender,
        String avatarPath,
        List<RoleDTO> roles,
        OrganizerInfoDTO organizerInfo
    ){
        public static UserDTO from(User user){
            List<RoleDTO> roleDTOs = user.getRoles() != null ? user.getRoles().stream()
                .map(RoleDTO::from)
                .toList() : List.of();
                
            OrganizerInfoDTO organizerInfoDTO = user.getOrganizerInfo() != null ? OrganizerInfoDTO.from(user.getOrganizerInfo()) : null;
            return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getPassWord(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getBirthday(),
                user.getGender(),
                user.getAvatarPath(),
                roleDTOs,
                organizerInfoDTO
            );
        }
    }
}
