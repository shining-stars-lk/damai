package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.composite.CompositeContainer;
import com.example.core.RedisKeyEnum;
import com.example.core.StringUtil;
import com.example.dto.UserAuthenticationDto;
import com.example.dto.UserExistDto;
import com.example.dto.UserGetAndTicketUserListDto;
import com.example.dto.UserIdDto;
import com.example.dto.UserLoginDto;
import com.example.dto.UserMobileDto;
import com.example.dto.UserRegisterDto;
import com.example.dto.UserUpdateDto;
import com.example.dto.UserUpdateEmailDto;
import com.example.dto.UserUpdateMobileDto;
import com.example.dto.UserUpdatePasswordDto;
import com.example.entity.TicketUser;
import com.example.entity.User;
import com.example.entity.UserEmail;
import com.example.entity.UserMobile;
import com.example.enums.BaseCode;
import com.example.enums.BusinessStatus;
import com.example.enums.CompositeCheckType;
import com.example.exception.CookFrameException;
import com.example.jwt.TokenUtil;
import com.example.mapper.TicketUserMapper;
import com.example.mapper.UserEmailMapper;
import com.example.mapper.UserMapper;
import com.example.mapper.UserMobileMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.servicelock.LockType;
import com.example.servicelock.annotion.ServiceLock;
import com.example.util.RBloomFilterUtil;
import com.example.vo.TicketUserVo;
import com.example.vo.UserGetAndTicketUserListVo;
import com.example.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.core.DistributedLockConstants.REGISTER_USER_LOCK;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    private static final String TOKEN_SECRET = "CSYZWECHAT";
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserMobileMapper userMobileMapper;
    
    @Autowired
    private UserEmailMapper userEmailMapper;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private TicketUserMapper ticketUserMapper;
    
    @Autowired
    private RBloomFilterUtil rBloomFilterUtil;
    
    @Autowired
    private CompositeContainer compositeContainer;
    
    @Value("${token.expire.time:86400000}")
    private Long tokenExpireTime;
    
    @Transactional(rollbackFor = Exception.class)
    @ServiceLock(lockType= LockType.Write,name = REGISTER_USER_LOCK,keys = {"#userRegisterDto.mobile"})
    public void register(UserRegisterDto userRegisterDto) {
        compositeContainer.execute(CompositeCheckType.USER_REGISTER_CHECK.getValue(),userRegisterDto);
        //用户表添加
        User user = new User();
        BeanUtils.copyProperties(userRegisterDto,user);
        user.setId(uidGenerator.getUID());
        userMapper.insert(user);
        //用户手机表添加
        UserMobile userMobile = new UserMobile();
        userMobile.setId(uidGenerator.getUID());
        userMobile.setUserId(user.getId());
        userMobile.setMobile(userRegisterDto.getMobile());
        userMobileMapper.insert(userMobile);
        rBloomFilterUtil.add(userMobile.getMobile());
    }
    
    @ServiceLock(lockType= LockType.Read,name = REGISTER_USER_LOCK,keys = {"#mobile"})
    public void exist(UserExistDto userExistDto){
        doExist(userExistDto.getMobile());
    }
    
    public void doExist(String mobile){
        boolean contains = rBloomFilterUtil.contains(mobile);
        if (contains) {
            LambdaQueryWrapper<UserMobile> queryWrapper = Wrappers.lambdaQuery(UserMobile.class)
                    .eq(UserMobile::getMobile, mobile);
            UserMobile userMobile = userMobileMapper.selectOne(queryWrapper);
            if (Objects.nonNull(userMobile)) {
                throw new CookFrameException(BaseCode.USER_EXIST);
            }
        }
    }
    
    public String login(UserLoginDto userLoginDto) {
        String mobile = userLoginDto.getMobile();
        String email = userLoginDto.getEmail();
        if (StringUtil.isEmpty(mobile) && StringUtil.isEmpty(email)) {
            throw new CookFrameException(BaseCode.USER_MOBILE_AND_EMAIL_NOT_EXIST);
        }
        Long userId;
        if (StringUtil.isNotEmpty(mobile)) {
            LambdaQueryWrapper<UserMobile> queryWrapper = Wrappers.lambdaQuery(UserMobile.class)
                    .eq(UserMobile::getMobile, mobile);
            UserMobile userMobile = userMobileMapper.selectOne(queryWrapper);
            if (Objects.isNull(userMobile)) {
                throw new CookFrameException(BaseCode.USER_MOBILE_EMPTY);
            }
            userId = userMobile.getUserId();
        }else {
            LambdaQueryWrapper<UserEmail> queryWrapper = Wrappers.lambdaQuery(UserEmail.class)
                    .eq(UserEmail::getEmail, email);
            UserEmail userEmail = userEmailMapper.selectOne(queryWrapper);
            if (Objects.isNull(userEmail)) {
                throw new CookFrameException(BaseCode.USER_EMAIL_NOT_EXIST);
            }
            userId = userEmail.getUserId();
        }
        
        Boolean loginResult = redisCache.hasKey(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,userId));
        if (loginResult) {
            throw new CookFrameException(BaseCode.USER_LOG_IN);
        }
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()),user,tokenExpireTime + 1000,TimeUnit.MILLISECONDS);
        return createToken(user.getId());
    }
    
    public String createToken(Long userId){
        Map<String,Object> map = new HashMap<>(4);
        map.put("userId",userId);
        return TokenUtil.createToken(String.valueOf(uidGenerator.getUID()), JSON.toJSONString(map),tokenExpireTime,TOKEN_SECRET);
    }
    
    public void logout(UserIdDto userIdDto) {
        User user = userMapper.selectById(userIdDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        redisCache.del(RedisKeyWrap.createRedisKey(RedisKeyEnum.USER_ID,user.getId()));
    }
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateDto userUpdateDto){
        User user = userMapper.selectById(userUpdateDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        User updateUser = new User();
        BeanUtil.copyProperties(userUpdateDto,updateUser);
        userMapper.updateById(updateUser);
    }
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto){
        User user = userMapper.selectById(userUpdatePasswordDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        User updateUser = new User();
        BeanUtil.copyProperties(userUpdatePasswordDto,updateUser);
        userMapper.updateById(updateUser);
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(UserUpdateEmailDto userUpdateEmailDto){
        User user = userMapper.selectById(userUpdateEmailDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        User updateUser = new User();
        BeanUtil.copyProperties(userUpdateEmailDto,updateUser);
        updateUser.setEmailStatus(BusinessStatus.YES.getCode());
        userMapper.updateById(updateUser);
        
        String oldEmail = user.getEmail();
        LambdaQueryWrapper<UserEmail> userEmailLambdaQueryWrapper = Wrappers.lambdaQuery(UserEmail.class)
                .eq(UserEmail::getEmail, userUpdateEmailDto.getEmail());
        UserEmail userEmail = userEmailMapper.selectOne(userEmailLambdaQueryWrapper);
        if (Objects.isNull(userEmail)) {
            userEmail = new UserEmail();
            userEmail.setId(uidGenerator.getUID());
            userEmail.setUserId(user.getId());
            userEmail.setEmail(userUpdateEmailDto.getEmail());
            userEmailMapper.insert(userEmail);
        }else {
            LambdaUpdateWrapper<UserEmail> userEmailLambdaUpdateWrapper = Wrappers.lambdaUpdate(UserEmail.class)
                    .eq(UserEmail::getEmail, oldEmail);
            UserEmail updateUserEmail = new UserEmail();
            updateUserEmail.setEmail(userUpdateEmailDto.getEmail());
            userEmailMapper.update(updateUserEmail,userEmailLambdaUpdateWrapper);
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void updateMobile(UserUpdateMobileDto UserUpdateMobileDto){
        User user = userMapper.selectById(UserUpdateMobileDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        String oldMobile = user.getMobile();
        User updateUser = new User();
        BeanUtil.copyProperties(UserUpdateMobileDto,updateUser);
        userMapper.updateById(updateUser);
        LambdaQueryWrapper<UserMobile> userMobileLambdaQueryWrapper = Wrappers.lambdaQuery(UserMobile.class)
                .eq(UserMobile::getMobile, UserUpdateMobileDto.getMobile());
        UserMobile userMobile = userMobileMapper.selectOne(userMobileLambdaQueryWrapper);
        if (Objects.isNull(userMobile)) {
            userMobile = new UserMobile();
            userMobile.setId(uidGenerator.getUID());
            userMobile.setUserId(user.getId());
            userMobile.setMobile(UserUpdateMobileDto.getMobile());
            userMobileMapper.insert(userMobile);
        }else {
            LambdaUpdateWrapper<UserMobile> userMobileLambdaUpdateWrapper = Wrappers.lambdaUpdate(UserMobile.class)
                    .eq(UserMobile::getMobile, oldMobile);
            UserMobile updateUserMobile = new UserMobile();
            updateUserMobile.setMobile(UserUpdateMobileDto.getMobile());
            userMobileMapper.update(updateUserMobile,userMobileLambdaUpdateWrapper);
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void authentication(UserAuthenticationDto userAuthenticationDto){
        User user = userMapper.selectById(userAuthenticationDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        if (Objects.equals(user.getRelAuthenticationStatus(), BusinessStatus.YES.getCode())) {
            throw new CookFrameException(BaseCode.USER_AUTHENTICATION);
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setRelName(userAuthenticationDto.getRelName());
        updateUser.setIdNumber(userAuthenticationDto.getIdNumber());
        updateUser.setRelAuthenticationStatus(BusinessStatus.YES.getCode());
        userMapper.updateById(updateUser);
    }
    
    public UserVo getByMobile(UserMobileDto userMobileDto) {
        LambdaQueryWrapper<UserMobile> queryWrapper = Wrappers.lambdaQuery(UserMobile.class)
                .eq(UserMobile::getMobile, userMobileDto.getMobile());
        UserMobile userMobile = userMobileMapper.selectOne(queryWrapper);
        if (Objects.isNull(userMobile)) {
            throw new CookFrameException(BaseCode.USER_MOBILE_EMPTY);
        }
        User user = userMapper.selectById(userMobile.getUserId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user,userVo);
        userVo.setMobile(userMobile.getMobile());
        return userVo;
    }
    
    public UserVo getById(UserIdDto userIdDto) {
        User user = userMapper.selectById(userIdDto.getId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user,userVo);
        return userVo;
    }
    
    public UserGetAndTicketUserListVo getUserAndTicketUserList(final UserGetAndTicketUserListDto userGetAndTicketUserListDto) {
        UserIdDto userIdDto = new UserIdDto();
        userIdDto.setId(userGetAndTicketUserListDto.getUserId());
        UserVo userVo = getById(userIdDto);
        
        LambdaQueryWrapper<TicketUser> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUser.class)
                .in(TicketUser::getId, userGetAndTicketUserListDto.getTicketUserIdList());
        List<TicketUser> ticketUserList = ticketUserMapper.selectList(ticketUserLambdaQueryWrapper);
        List<TicketUserVo> ticketUserVoList = BeanUtil.copyToList(ticketUserList, TicketUserVo.class);
        
        UserGetAndTicketUserListVo userGetAndTicketUserListVo = new UserGetAndTicketUserListVo();
        userGetAndTicketUserListVo.setUserVo(userVo);
        userGetAndTicketUserListVo.setTicketUserVoList(ticketUserVoList);
        return userGetAndTicketUserListVo;
    }
}
