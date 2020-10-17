package springbook.user.service;

import springbook.user.domain.User;

public final class TestUserService extends UserServiceImpl
{
    private String id;

    TestUserService(String id)
    {
        this.id = id;
    }

    @Override
    public void upgradeLevel(User user)
    {
        if (user.getId().equals(this.id))
        {
            throw new TestUserServiceException();
        }
        super.upgradeLevel(user);
    }
}