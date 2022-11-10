package com.akhan.nomadsocialnetworkservice.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.akhan.nomadsocialnetworkservice.error.EntityNotFoundException;
import com.akhan.nomadsocialnetworkservice.error.UserAlreadyExistException;
import com.akhan.nomadsocialnetworkservice.model.User;
import com.akhan.nomadsocialnetworkservice.model.UserDto;
import com.akhan.nomadsocialnetworkservice.repository.UserRepository;

@Service
public class UserService implements IUserService{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptEncoder;

    @Override
    public User registerNewUserAccount(final UserDto account) {
        if (emailExists(account.getEmail())) {
            LOGGER.error("There is an account with that email address: {}", account.getEmail());
            throw new UserAlreadyExistException("There is an account with that email address: " + account.getEmail());
        }
        final User user = new User();

        user.setFirstName(account.getFirstName());
        user.setLastName(account.getLastName());
        user.setPassword(bCryptEncoder.encode(account.getPassword()));
        user.setEmail(account.getEmail());
        // user.setUsing2FA(account.isUsing2FA());
        // user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return userRepository.save(user);
    }

    public User getUserById(String id){
        Optional<User> _user = userRepository.findById(id);
        if(_user.isEmpty()){
            LOGGER.error("User with id \'{}\' does not exist", id);
            throw new EntityNotFoundException("User with id \'" + id + "\' does not exist");
        }
        return _user.get();
    }

    @Override
    public boolean emailExists(String email) {
        // return userRepository.findByEmail(email) != null;
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getUser(String verificationToken) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveRegisteredUser(User user) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteUser(User user) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByID(long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void changeUserPassword(User user, String password) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String password) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String validateVerificationToken(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User updateUser2FA(boolean use2fa) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getUsersFromSessionRegistry() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String isValidNewLocationToken(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addUserLocation(User user, String ip) {
        // TODO Auto-generated method stub
        
    }
}
